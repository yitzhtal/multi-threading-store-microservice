package bgu.spl.app;

public class ShoeStorageInfo {
        private String shoeType;
        private int amountOnStorage;
        private int discountedAmount;
        
        public ShoeStorageInfo(String shoeType_,int amountOnStorage_,int discountedAmount_) {
        	this.shoeType = shoeType_;
        	this.amountOnStorage = amountOnStorage_;
        	this.discountedAmount = discountedAmount_;
        }
        
        public synchronized String getShoeType() {
        	return this.shoeType;
        }
        
        public synchronized int getAmountOnStorage() {
        	return this.amountOnStorage;
        }
        
        public synchronized int getDiscountedAmount() {
        	return this.discountedAmount;
        }
        
        public synchronized void setShoeType(String n) {
        	this.shoeType = n;
        }
        
        public synchronized void addAmountOnStorage(int r) {
        	this.amountOnStorage += r;
        }
        
        public synchronized void decreaseAmountOnStorage() {
        	this.amountOnStorage--;
        }
        
        public synchronized void addDiscountedAmount(int p) {
        	this.discountedAmount += p;
        }
        
        public synchronized void decreaseDiscountedAmount() {
        	this.discountedAmount--;
        }
        
        public String toString() {
        	return("name: "+ this.shoeType + ", amount: " + this.amountOnStorage + ", discounted amount: " + this.discountedAmount+ ".");
        }
}
