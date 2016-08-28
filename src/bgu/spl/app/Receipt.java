package bgu.spl.app;

public class Receipt {
      private String seller;
      private String customer;
      private String shoeType;
      private boolean discount;
      private int issuedTick;
      private int requestTick;
      private int amountSold;
      
      public Receipt(String seller,String customer, String shoeType,boolean discount,int issuedTick,int requestTick,int amountSold) {
    	  this.seller = seller;
    	  this.customer = customer;
    	  this.shoeType = shoeType;
    	  this.discount = discount;
    	  this.issuedTick = issuedTick;
    	  this.requestTick = requestTick;
    	  this.amountSold = amountSold;
      }
      
      public String getSeller() {
    	  return this.seller;
      }
      
      public String getCustomer() {
    	  return this.customer;
      }
      
      public String getShoeType() {
    	  return this.shoeType;
      }
      
      public boolean wasDiscounted() {
    	  return this.discount;
      }
      
      public int getIssuedTick() {
    	  return this.issuedTick;
      }
      
      public int getRequestTick() {
    	  return this.requestTick;
      }
      
      public int getAmountSold() {
    	  return this.amountSold;
      }
      
      public String toString() {
    	  return("seller: " + this.seller + ", customer: " + this.customer + ", shoe type: " + this.shoeType +
    			  ", discounted: " + this.discount+ ", issued tick: " + this.issuedTick + ", request tick: " + this.requestTick +
    			  ", amount sold: "+this.amountSold+".");
      }
}
