package bgu.spl.app;
import bgu.spl.mics.Broadcast;

public class NewDiscountBroadcast implements Broadcast {
          private String shoeType;
          private int amountSent;
          
          public NewDiscountBroadcast(String shoeType_,int amountSent_) {
        	  this.shoeType = shoeType_;
        	  this.amountSent = amountSent_;
          }
          
          public String getShoeType() {
        	  return this.shoeType;
          }
          
          public int getAmountSent() {
        	  return this.amountSent;
          }
}
