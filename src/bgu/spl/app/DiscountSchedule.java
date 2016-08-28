package bgu.spl.app;

public class DiscountSchedule {
     private String shoeType;
     private int tick;
     private int amount;
     
     public DiscountSchedule(String shoeType_,int tick_,int amount_) {
    	 this.shoeType = shoeType_;
    	 this.tick = tick_;
    	 this.amount = amount_;
     }
     
     public int getTick() {
    	 return this.tick;
     }
     
     public int getAmount() {
    	 return this.amount;
     }
     
     public String getShoeType() {
    	 return this.shoeType;
     }
}
