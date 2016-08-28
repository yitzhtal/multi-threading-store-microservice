package bgu.spl.app;

import bgu.spl.mics.Request;

public class ManufacturingOrderRequest<R> implements Request<R>{
            private String shoeType;
            private int amountRequested;
            private int timeRequested;
            
            ManufacturingOrderRequest(String shoeType, int amountRequested,int timeRequested) {
            	this.shoeType = shoeType;
            	this.amountRequested = amountRequested;
            	this.timeRequested = timeRequested;
            }
            
            public String getShoeType() {
            	return this.shoeType;
            }
            
            public int getAmountRequested() {
            	return this.amountRequested;
            }
            
            public int getTimeRequested() {
            	return this.timeRequested;
            }
}
