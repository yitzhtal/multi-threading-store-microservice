package bgu.spl.app;
import java.util.List;

public class JsonParser {
	private List<InitialStorage> initialStorage;
	private Services services;

	public Services getServices() {
		return services;
	}
	public List<InitialStorage> getInitialStorage() {
		return initialStorage;
	}

	public class InitialStorage {
		private String shoeType;
		private int amount;

		public String getShoeType() {
			return shoeType;
		}
		public int getAmount() {
			return this.amount;
		}
	}

	public class Services {
		private Time time;
		private Manager manager;
		private int factories;
		private int sellers;
		private List<Customer> customers;
		
		public Time getTime() {
			return time;
		}
		public Manager getManager() {
			return manager;
		}
		public int getFactories() {
			return factories;
		}
		public int getSellers() {
			return sellers;
		}
		
		public int getNumberOfServices() {
			int num =  1; //manager
            num += getFactories() + getSellers() + getCustomers().size();
            return num;
		}
		
		public List<Customer> getCustomers() {
			return customers;
		}
	}
	
	public class Time {
		private int speed;
		private int duration;
        
		public int getSpeed() {
			return this.speed;
		}
		
		public int getDuration() {
			return this.duration;
		}
	}
	
	public class discountSchedule {
		private String shoeType;
		private int amount;
		private int tick;

		public String getShoeType() {
			return shoeType;
		}
		public int getAmount() {
			return this.amount;
		}
		public int getTick() {
			return this.tick;
		}
	}
	public class Manager {
		private List<discountSchedule> discountSchedule;

		public List<discountSchedule> getDiscountSchedule() {
			return this.discountSchedule;
		}
	}

	public class purchaseSchedule {
		private String shoeType;
		private int tick;

		public String getShoeType() {
			return shoeType;
		}
		public int getTick() {
			return this.tick;
		}
	}
	
	public class Customer {
		private String name;
		private List<String> wishList;
		private List<purchaseSchedule> purchaseSchedule;
	
		public String getName() {
			return this.name;
		}

		public List<String> getWishList() {
			return this.wishList;
		}
		
		public List<purchaseSchedule> getPurchaseSchedule() {
			return this.purchaseSchedule;
		}
	}
}
