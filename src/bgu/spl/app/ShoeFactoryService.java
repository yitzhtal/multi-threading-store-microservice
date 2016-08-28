package bgu.spl.app;
import java.util.concurrent.*;
import java.util.logging.*;
import bgu.spl.mics.MicroService;

public class ShoeFactoryService extends MicroService{
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private int currentTime;
    private int completed;
    ConcurrentLinkedQueue<ManufacturingOrderRequest<Receipt>> manufacturingRequests;
    private CountDownLatch m_latchObject;
    
	public ShoeFactoryService(String name,CountDownLatch m_latchObject_) {
		super(name);
		this.currentTime = 0;
		this.completed=-1;
		this.m_latchObject = m_latchObject_;
		this.manufacturingRequests = new ConcurrentLinkedQueue<ManufacturingOrderRequest<Receipt>> ();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class,tick -> {
			this.currentTime = tick.getCurrentTick();
         	 if(tick.getDuration()+1==this.currentTime) {
         	//	System.out.println(getName()+ " has terminated.");
         		LOGGER.config(getName()+ " has terminated.");
         		terminate();
         	 }
			if(manufacturingRequests.isEmpty()==false){
				completed++;
				if(completed==manufacturingRequests.peek().getAmountRequested()){
					String _shoeType = manufacturingRequests.peek().getShoeType();
					int _timeRequested = manufacturingRequests.peek().getTimeRequested();
					LOGGER.info(getName()+"completed manufacture of " +completed+" "+_shoeType);
					complete(manufacturingRequests.poll(),new Receipt(getName(),"store", _shoeType,false,currentTime,_timeRequested,completed));
					if(manufacturingRequests.isEmpty()==false) completed=0;
					else completed=-1;
				}
			}
		});

		subscribeRequest(ManufacturingOrderRequest.class,req -> {
			manufacturingRequests.add(req);
		});
		
	    this.m_latchObject.countDown();
	   // System.out.println(getName()+ " finished initializing!");
	    LOGGER.config(getName()+ " finished initializing.");
}

}
