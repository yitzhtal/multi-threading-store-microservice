package bgu.spl.mics.impl;
import bgu.spl.mics.Broadcast;
import java.util.logging.*;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;
import bgu.spl.mics.RequestCompleted;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import bgu.spl.mics.RoundRobin;

public class MessageBusImpl implements MessageBus {	
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServicesMap;
	private ConcurrentHashMap<Class<? extends Request<?>>,RoundRobin> requestsMap;
	private ConcurrentHashMap<Class<? extends Broadcast>,ArrayList<MicroService>> broadcastsMap;
	private ConcurrentHashMap<Message,MicroService> takingCareOf;
	private ConcurrentHashMap<MicroService,ArrayList<Class<? extends Message>>> unregisterMap;
	private static final MessageBusImpl MessageBusImplInstance = new MessageBusImpl();
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


	public MessageBusImpl() {
		this.microServicesMap = new ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>>();
		this.requestsMap = new ConcurrentHashMap<Class<? extends Request<?>>,RoundRobin>();
		this.broadcastsMap = new ConcurrentHashMap<Class<? extends Broadcast>,ArrayList<MicroService>>();
		this.takingCareOf = new ConcurrentHashMap<Message,MicroService>();
		this.unregisterMap = new ConcurrentHashMap<MicroService,ArrayList<Class<? extends Message>>>();
	}
	
	public static MessageBusImpl getInstance() {
		return MessageBusImplInstance;
	}
	
	public synchronized void subscribeRequest(Class<? extends Request> type, MicroService m) {
		if(this.requestsMap.containsKey(type)) {
			this.requestsMap.get(type).addNext(m);
		}
		else {
			this.requestsMap.put((Class<? extends Request<?>>) type,new RoundRobin());
			this.requestsMap.get(type).addNext(m);
		}
		if(this.unregisterMap.containsKey(m)) {
			this.unregisterMap.get(m).add(type);
		}
		else {
			this.unregisterMap.put(m,new ArrayList<Class<? extends Message>>());
			this.unregisterMap.get(m).add(type);
		}
	}

	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(this.broadcastsMap.containsKey(type)) 
			this.broadcastsMap.get(type).add(m);
		else {
			this.broadcastsMap.put(type, new ArrayList<MicroService>());
			this.broadcastsMap.get(type).add(m);
		}

		if(this.unregisterMap.containsKey(m)) {
			this.unregisterMap.get(m).add(type);
		}
		else {
			this.unregisterMap.put(m,new ArrayList<Class<? extends Message>>());
			this.unregisterMap.get(m).add(type);
		}
	}   

	public <T> void complete(Request<T> r, T result) {
		try {
			RequestCompleted<T> done = new RequestCompleted<T>(r,result);
			MicroService requester = this.takingCareOf.get(r);
			this.microServicesMap.get(requester).put(done);
			this.takingCareOf.remove(r);
		} catch (InterruptedException e) { }
	}

	public synchronized void sendBroadcast(Broadcast b) {
		for (Class<? extends Broadcast> i : broadcastsMap.keySet()) {
			if(i == b.getClass()) {
				for (int k = 0; k < this.broadcastsMap.get(i).size(); k++) {
					try {
						this.microServicesMap.get(this.broadcastsMap.get(i).get(k)).put(b);
					} catch (InterruptedException e) { }
				}
			}
		}		
	}

	public synchronized boolean sendRequest(Request<?> r, MicroService requester) {
		for (Class<? extends Request<?>> i : requestsMap.keySet()) {
			if(i == r.getClass()) {
				this.takingCareOf.put(r, requester);  //update the requesterMap so we can remember where to go back to.
				RoundRobin temp = this.requestsMap.get(i);			    
				MicroService takingCare = (MicroService)temp.getNext();
				this.microServicesMap.get(takingCare).add(r);
				return true;
			}
		}	    	
		return false;
	}

	public void register(MicroService m) {
		this.microServicesMap.put(m, new LinkedBlockingQueue<Message>());  	
	}

	public synchronized void unregister(MicroService m) {
		ArrayList<?> deleteMe = this.unregisterMap.get(m);
		if(deleteMe != null) {
			for (int k = 0; k < deleteMe.size(); k++) {
				if(deleteMe.get(k) instanceof Request) {
					RoundRobin temp = this.requestsMap.get(deleteMe.get(k).getClass());
					for(int i=0; i<temp.getSize(); i++) {
						if(temp.get(i) == m) {
							temp.remove(i);
						}
					}
				} else {       //It has to be an instance of Broadcast! We assume we have only Request/Broadcast
					ArrayList<?> temp = this.broadcastsMap.get(deleteMe.get(k).getClass());
					if(temp != null) {
						for(int i=0; i<temp.size(); i++) {
							if(temp.get(i) == m) {
								temp.remove(i);
							}
						}
					}
				}
			}	
		}
	}

	public Message awaitMessage(MicroService m) throws InterruptedException {
			return this.microServicesMap.get(m).take();
	}
}
