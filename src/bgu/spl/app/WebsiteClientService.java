package bgu.spl.app;
import bgu.spl.mics.MicroService;
import java.util.logging.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class WebsiteClientService extends MicroService{
         	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
            private ArrayList<PurchaseSchedule> purchaseSchedule;
            private ArrayList<String> wishList;
            private ArrayList<PurchaseOrderRequest> waitingOrders;
            private int currentTime;
            private CountDownLatch m_latchObject;
            private boolean purchaseScheduleDone;
            private boolean wishlistDone;
            
            
	public WebsiteClientService(String name,CountDownLatch m_latchObject_) {
		super(name);
		this.currentTime = 0;
		this.wishList = new ArrayList<String>();
		this.purchaseSchedule = new ArrayList<PurchaseSchedule>();
		this.m_latchObject = m_latchObject_;
		 this.purchaseScheduleDone=false;
         this.wishlistDone=false;
         this.waitingOrders= new ArrayList<PurchaseOrderRequest>();
	}

	public void addPurchaseSchedule(PurchaseSchedule d1) {
		this.purchaseSchedule.add(d1);
	}
	
	public void addWishList(String d1) {
		this.wishList.add(d1);
	}
	
	@Override
	protected void initialize() {
		this.purchaseSchedule.sort((purchaseSchedule1,purchaseSchedule2) -> {
			return (purchaseSchedule1.getTick() - purchaseSchedule2.getTick());
		});
        subscribeBroadcast(TickBroadcast.class,tick -> {
          	 this.currentTime = tick.getCurrentTick();
          	 if(tick.getDuration()+1==this.currentTime) {
          		LOGGER.config(getName()+ " has terminated.");
          		terminate();
          	 }
          	 for(int i=0; i<purchaseSchedule.size(); i++) {
          		 PurchaseSchedule temp = purchaseSchedule.get(i);
          		 if(temp.getTick() > this.currentTime) break;
          		 if(temp.getTick() == this.currentTime) {
          			 if(i==purchaseSchedule.size()-1){
          				 purchaseScheduleDone=true;
          			 }
          			 PurchaseOrderRequest sendMe = new PurchaseOrderRequest(temp.getShoeType(),false,getName(),this.currentTime);
          			 waitingOrders.add(sendMe);
          			 sendRequest(sendMe,res -> {
          				 waitingOrders.remove(sendMe);
          				 LOGGER.info(this.getName()+" bought "+temp.getShoeType());

          				 if(this.wishList.contains(temp.getShoeType())) {
          					    this.wishList.remove(this.wishList.indexOf(temp.getShoeType()));
          					    if(this.wishList.isEmpty()) wishlistDone=true;
          					  
          					    }
          				  if(wishlistDone && purchaseScheduleDone && waitingOrders.isEmpty()){
    					    	LOGGER.config(getName()+ " has terminated.");
    			          		terminate();
          				 }
          				
          			 });
          		 }
          	 }     

           });
        
		subscribeBroadcast(NewDiscountBroadcast.class,brod -> {	
		        String shoeType = brod.getShoeType();
		        LOGGER.config("Subscribing "+ getName() + " to get discount shoes of type: " + shoeType);
		        if(this.wishList.contains(shoeType)) {
         			 PurchaseOrderRequest sendMe = new PurchaseOrderRequest(shoeType,true,getName(),this.currentTime);
         			 waitingOrders.add(sendMe);
         			 sendRequest(sendMe,res -> {
         				 waitingOrders.remove(sendMe);
         				 if(res != null) {
         					LOGGER.info(this.getName()+" bought "+res.getShoeType());
         					  this.wishList.remove(res.getShoeType());
        					    if(this.wishList.isEmpty()) wishlistDone=true;
        					    if(wishlistDone && purchaseScheduleDone && waitingOrders.isEmpty()){
        					    	LOGGER.config(getName()+ " has terminated.");
        			          		terminate();
        					    }
         				 }
         				 
         				      
         			 });
		        }
		});	
	    this.m_latchObject.countDown();
	   // System.out.println(getName()+ " has finished initializing!");
	    LOGGER.config(getName()+ " finished initializing.");
	}
}
