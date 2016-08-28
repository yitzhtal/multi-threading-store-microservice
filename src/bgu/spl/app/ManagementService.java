package bgu.spl.app;
import bgu.spl.mics.MicroService;
import bgu.spl.app.StoreLogger;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.*;

public class ManagementService extends MicroService{
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private int currentTime;
	private ArrayList<DiscountSchedule> myDiscountedSales;
    private ConcurrentHashMap<String,Integer> orderedStatus;
    private ConcurrentHashMap<String,ConcurrentLinkedQueue<RestockRequest<Boolean>>> resRequests;
    private CountDownLatch m_latchObject;
    
	public ManagementService(String name,CountDownLatch m_latchObject_) {
		super(name);
		this.currentTime = 0;
        this.orderedStatus = new ConcurrentHashMap<String,Integer>();
        this.m_latchObject = m_latchObject_;
        this.myDiscountedSales = new ArrayList<DiscountSchedule>();
        this.resRequests = new ConcurrentHashMap<String,ConcurrentLinkedQueue<RestockRequest<Boolean>>>();
        
	}

	public void addDiscountSchedule(DiscountSchedule d1) {
		this.myDiscountedSales.add(d1);
	}
	@Override
	protected void initialize() {
		//Sorting the element by their tick time. O(nlogn) at the worst case.
		
		myDiscountedSales.sort((DiscountSchedule1,DiscountSchedule2) -> {
			return (DiscountSchedule1.getTick() - DiscountSchedule2.getTick());
		});
		
		subscribeBroadcast(TickBroadcast.class,tick -> {
			this.currentTime = tick.getCurrentTick();
         	 if(tick.getDuration()+1==this.currentTime) {
         		LOGGER.config(getName()+ " has terminated.");
          		//System.out.println(getName()+ " has terminated.");
         		terminate();
         	 }
	        for(int i=0; i<myDiscountedSales.size(); i++) {
	        	DiscountSchedule temp = myDiscountedSales.get(i);
	        	if(temp.getTick() > this.currentTime) break;
	        	if(temp.getTick() == this.currentTime) {
	        		LOGGER.info(temp.getAmount()+" "+temp.getShoeType()+"");
	        		sendBroadcast(new NewDiscountBroadcast(temp.getShoeType(),temp.getAmount()));
	        		Store.getInstance().addDiscount(temp.getShoeType(), temp.getAmount());
	        	}
	        }
		});
		
        
        //Now we should handle Restock Requests! Yey :)

		subscribeRequest(RestockRequest.class,req -> {
			String typeOfRequest = req.getType();
			boolean onOrder = this.orderedStatus.containsKey(typeOfRequest);
			if(onOrder==false) {
				this.orderedStatus.put(typeOfRequest,new Integer(0));
				this.resRequests.put(typeOfRequest,new ConcurrentLinkedQueue<RestockRequest<Boolean>>());
			}
			int checkMe = this.orderedStatus.get(typeOfRequest).intValue();
			if(checkMe > 0) {
				this.orderedStatus.put(typeOfRequest,new Integer(checkMe-1));
				this.resRequests.get(typeOfRequest).add(req);
			}
			else {

				ManufacturingOrderRequest<Receipt> reqSend = new ManufacturingOrderRequest<Receipt>(typeOfRequest,(currentTime%5)+1,this.currentTime);
				
				boolean result = sendRequest(reqSend,produceRequest -> { 
					synchronized(this.resRequests) {
						int i;
						for(i=1;i<=produceRequest.getAmountSold() && this.resRequests.get(produceRequest.getShoeType()).isEmpty()==false;i++){
							complete(this.resRequests.get(produceRequest.getShoeType()).poll(),true);
						}
						Store.getInstance().add(produceRequest.getShoeType(), produceRequest.getAmountSold()-i+1);
						int tempnum = this.orderedStatus.get(produceRequest.getShoeType());
						this.orderedStatus.put(produceRequest.getShoeType(),new Integer(tempnum-produceRequest.getAmountSold()-i+1));
						Store.getInstance().file(produceRequest);
					}
				});	

				if(result){
					this.orderedStatus.put(typeOfRequest,new Integer(currentTime%5));
					this.resRequests.get(typeOfRequest).add(req);
				} else {
					complete(req,false);
				}
			}
		});
	    this.m_latchObject.countDown();
	    LOGGER.config(getName()+ " finished intializing.");
	   // System.out.println(getName()+ " finished initializing!");
	}
}

