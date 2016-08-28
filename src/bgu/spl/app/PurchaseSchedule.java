package bgu.spl.app;

public class PurchaseSchedule {
      private String shoeType;
      private int tick;
      
      public PurchaseSchedule(String shoeType_, int tick_) {
    	  this.shoeType = shoeType_;
    	  this.tick = tick_;
      }
      
      public String getShoeType() { 
    	  return this.shoeType;
      }
      
      public int getTick() { 
    	  return this.tick;
      }
}
