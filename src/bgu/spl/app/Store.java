package bgu.spl.app;
import java.util.concurrent.*;
import java.util.logging.*;
public class Store {
	private ConcurrentHashMap<String,ShoeStorageInfo> myShoes;
    private CopyOnWriteArrayList<Receipt> myReceipts;
    private static final Store StoreInstance = new Store();
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	
    public Store() {
    	this.myShoes = new ConcurrentHashMap<String,ShoeStorageInfo>();
    	this.myReceipts = new CopyOnWriteArrayList<Receipt>();
    }
    
	public static Store getInstance() {
		return StoreInstance;
	}
	
	public void load(ShoeStorageInfo[] storage) {
		   for(int i=0; i<storage.length;i++) 
			    this.myShoes.put(storage[i].getShoeType(),storage[i]);
	}
	
	public synchronized BuyResult take(String shoeType, boolean onlyDiscount) {
		if(this.myShoes.containsKey(shoeType) == false || this.myShoes.get(shoeType).getAmountOnStorage() == 0) {
			return BuyResult.NOT_IN_STOCK;
		}
		else { 	//we have this type in stock
			ShoeStorageInfo temp = this.myShoes.get(shoeType);
			if(!onlyDiscount) { //he is not interested in discount -> wants regular price
				if(temp.getDiscountedAmount() > 0 ) { //there is a discount option, he will get it anyways, so lucky :)
					temp.decreaseDiscountedAmount();
					temp.decreaseAmountOnStorage();
					return BuyResult.DISCOUNTED_PRICE;
				}
				else { //there aren't any discounts options - he is gonna get the regular price
					temp.decreaseAmountOnStorage();
					return BuyResult.REGULAR_PRICE;
				
				}
			} else { //he is interested in discount
				if(temp.getDiscountedAmount() > 0) { //there is a discount option :)
					temp.decreaseDiscountedAmount();
					temp.decreaseAmountOnStorage();
					return BuyResult.DISCOUNTED_PRICE;
				}
				else { //there aren't any discounts options
					return BuyResult.NOT_ON_DISCOUNT;
	
				}
			}
		}	
	}

	public synchronized void add(String shoeType, int amount) {
		if(this.myShoes.containsKey(shoeType)) {
			this.myShoes.get(shoeType).addAmountOnStorage(amount);
		} else {
            this.myShoes.put(shoeType,new ShoeStorageInfo(shoeType,amount,0));
		}
	}
	
	public synchronized void addDiscount(String shoeType, int amount) {
		if(this.myShoes.containsKey(shoeType)) {
			int amountOnStorage = this.myShoes.get(shoeType).getAmountOnStorage();
			int discountOnStorage = this.myShoes.get(shoeType).getDiscountedAmount();
			if(amountOnStorage < discountOnStorage+amount)
		        	this.myShoes.get(shoeType).addDiscountedAmount(amountOnStorage-discountOnStorage);
			else{this.myShoes.get(shoeType).addDiscountedAmount(amount);}
				
		} 
		
	}
	
	public void file(Receipt receipt) {
			     this.myReceipts.add(receipt);
	}
	
	public void print() {
		System.out.println("------------------------------------------");
		System.out.println("------------------------------------------");
		System.out.println("Hi! these are my currents stocks which are available... get ready!");
		int i = 1;
		for (String thisOne : this.myShoes.keySet()) {
					System.out.println(i + ") " + myShoes.get(thisOne).toString());
					i++;
		}

		System.out.println("------------------------------------------");
		System.out.println("------------------------------------------");
		System.out.println("Receipts:");
		
		for(int i1=0; i1<this.myReceipts.size(); i1++) {
			Receipt thisOne = this.myReceipts.get(i1);
			System.out.println((i1+1) + ") "+ thisOne.toString());
		}
		System.out.println("------------------------------------------");
		System.out.println("------------------------------------------");
	}
}