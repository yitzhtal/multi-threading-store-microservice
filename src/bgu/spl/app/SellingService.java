package bgu.spl.app;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import bgu.spl.mics.MicroService;

public class SellingService extends MicroService{
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private int currentTime;
    private CountDownLatch m_latchObject;
    
	public SellingService(String name,CountDownLatch m_latchObject_) {
		super(name);
        this.currentTime = 0;
        this.m_latchObject = m_latchObject_;
	}

	protected void initialize() {
             subscribeBroadcast(TickBroadcast.class,tick -> {
            	 this.currentTime = tick.getCurrentTick();
              	 if(tick.getDuration()+1==this.currentTime) {
              		//System.out.println(getName()+ " has terminated.");
              		LOGGER.config(getName()+ " has terminated.");
               		terminate();
               	 }
             });

             subscribeRequest(PurchaseOrderRequest.class,req -> {
            	 BuyResult thisResult = Store.getInstance().take(req.getShoeType(), req.wantsOnlyOnDiscount());

            	 switch (thisResult) {
            	 case NOT_IN_STOCK:
            		 if(req.wantsOnlyOnDiscount()) {
            			 complete(req,null);
            			 break;
            		 }
            		 sendRequest(new RestockRequest<Boolean>(req.getShoeType()), restock -> {
            			 if(restock.booleanValue() == false)
            				 complete(req,null);
            			 else {
            				 Receipt afterStock = new Receipt(getName(),req.getCustomer(),req.getShoeType(),false,currentTime,req.getRequestTick(),1);
            				 Store.getInstance().file(afterStock);
            			     complete(req,afterStock);
            			 }
                     });  
            		 break;
                     
            	 case NOT_ON_DISCOUNT:
            		 complete(req,null);
            		 break;
            		 
            	 case REGULAR_PRICE:
            		 Receipt nonDiscountedResult = new Receipt(getName(),req.getCustomer(),req.getShoeType(),false,currentTime,req.getRequestTick(),1);
            		 Store.getInstance().file(nonDiscountedResult);
            		 complete(req,nonDiscountedResult);
            		 break;
            		 
            	 case DISCOUNTED_PRICE:
            		 Receipt discountedResult = new Receipt(getName(),req.getCustomer(),req.getShoeType(),true,currentTime,req.getRequestTick(),1);
            		 Store.getInstance().file(discountedResult);
            		 complete(req,discountedResult);
            	 } 
             });
     	    this.m_latchObject.countDown();
     	   LOGGER.config(getName()+ " finished initalizing.");
    	   // System.out.println(getName()+ " finished initializing!");
	}

}
