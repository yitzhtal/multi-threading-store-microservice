package bgu.spl.app;
import bgu.spl.mics.Request;

public class PurchaseOrderRequest implements Request<Receipt> {
	private String shoeType;
	private boolean onlyDiscount;
    private String customer;
    private int requestTick;
    
	public PurchaseOrderRequest(String shoeType,boolean onlyDiscount,String customer,int requestTick) {
		this.shoeType = shoeType;
		this.onlyDiscount = onlyDiscount;
		this.requestTick = requestTick;
		this.customer = customer;
	}
	
	public String getShoeType() {
		return this.shoeType;
	}
	
	public boolean wantsOnlyOnDiscount() {
		return this.onlyDiscount;
	}
	
	public String getCustomer() {
		return this.customer;
	}
	
	public int getRequestTick() {
		return this.requestTick;
	}
}
