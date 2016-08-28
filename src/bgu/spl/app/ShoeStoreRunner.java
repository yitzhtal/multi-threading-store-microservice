package bgu.spl.app;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.logging.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.util.logging.*;

import bgu.spl.app.JsonParser.Customer;
import bgu.spl.app.JsonParser.InitialStorage;
import bgu.spl.app.JsonParser.Services;
import bgu.spl.app.JsonParser.discountSchedule;
import bgu.spl.app.JsonParser.purchaseSchedule;

public class ShoeStoreRunner {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static void main(String[] args) throws IOException{
	    try {
	      StoreLogger.setup();
	    } catch (IOException e) {
	      e.printStackTrace();
	      throw new RuntimeException("Problems with creating the log files");
	    }
	   

		Gson gson = new Gson();
		try {
			System.out.println("--------------------------------------------------------------------------");
			System.out.println("Assignment2: by Tal Yitzhak & Yonathan Shapira, let the games begin... :)");
			System.out.println("--------------------------------------------------------------------------");
			String str = args[0];
			//String str = "sample.json";
			JsonParser data = gson.fromJson(new FileReader(str),JsonParser.class);
		
			int countMicroServices = data.getServices().getNumberOfServices();
			CountDownLatch latchObject = new CountDownLatch (countMicroServices);
		 
			//Building time service
			TimeService myTimeService = new TimeService("myTimeService",data.getServices().getTime().getSpeed(),data.getServices().getTime().getDuration(),latchObject);
			ManagementService myManager = new ManagementService("myManager",latchObject);

			//Building manager and adds his DiscountSchedules
			for(int i=0; i<data.getServices().getManager().getDiscountSchedule().size(); i++) {
				discountSchedule temp = data.getServices().getManager().getDiscountSchedule().get(i);
				myManager.addDiscountSchedule(new DiscountSchedule(temp.getShoeType(),temp.getTick(),temp.getAmount()));
			}			
			
			//Building the store
			List<InitialStorage> l = data.getInitialStorage();
			ShoeStorageInfo[] loadMe = new ShoeStorageInfo[l.size()];
			for(int i=0; i<l.size(); i++) 
				loadMe[i] = new ShoeStorageInfo(l.get(i).getShoeType(),l.get(i).getAmount(),0);
			Store.getInstance().load(loadMe);
			
			//Building the factories
			ArrayList<ShoeFactoryService> myFactories = new ArrayList<ShoeFactoryService>();
			for(int i=1; i<=data.getServices().getFactories(); i++) 
				myFactories.add(new ShoeFactoryService("Factory "+i,latchObject));
			
			//Building the sellers
			ArrayList<SellingService> mySellers = new ArrayList<SellingService>();
			for(int i=1; i<=data.getServices().getSellers(); i++) 
				mySellers.add(new SellingService("Seller "+i,latchObject));
			
			//Building the customers
			ArrayList<WebsiteClientService> myCustomers = new ArrayList<WebsiteClientService>();
			for(int i=0; i<data.getServices().getCustomers().size(); i++) {
				Customer temp = data.getServices().getCustomers().get(i);
				myCustomers.add(new WebsiteClientService(temp.getName(),latchObject));
				
				//adds his wish list
				for(int j=0; j<temp.getWishList().size(); j++) {
					myCustomers.get(i).addWishList(temp.getWishList().get(j));
				}
					
				//adds his purchaseSchedule list
				for(int k=0; k<temp.getPurchaseSchedule().size(); k++) {
					purchaseSchedule temp2 = temp.getPurchaseSchedule().get(k);
					myCustomers.get(i).addPurchaseSchedule(new PurchaseSchedule(temp2.getShoeType(),temp2.getTick()));
				}
			}		
			
			//building & start all threads
			new Thread(myTimeService).start();
			new Thread(myManager).start();
			
			for(int i=0; i<myFactories.size();i++) 
				new Thread(myFactories.get(i)).start();
			
			for(int i=0; i<mySellers.size(); i++) 
				new Thread(mySellers.get(i)).start();
			
			for(int i=0; i<myCustomers.size(); i++) 
				new Thread(myCustomers.get(i)).start();
			
		} catch(FileNotFoundException e) {LOGGER.severe("No Json file");}
	}
}
