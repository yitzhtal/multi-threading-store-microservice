package bgu.spl.app;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import bgu.spl.app.Receipt;
import bgu.spl.app.ShoeStorageInfo;
import bgu.spl.app.Store;
import bgu.spl.app.BuyResult;


public class ShoeStoreTester {
	Store storeTest = Store.getInstance();

	@BeforeClass
	public static void initializeBeforeTest() throws Exception {
		ShoeStorageInfo[]  theStock = {
				new ShoeStorageInfo("Blundstone", 20, 4), 
				new ShoeStorageInfo("sandals", 22, 10),
				new ShoeStorageInfo("baby shoes", 7, 1),
				new ShoeStorageInfo("Nike", 3, 0),
				new ShoeStorageInfo("New-Balance", 0, 0)
		};
		Store.getInstance().load(theStock);
	}

	@Before
	public void setUp() throws Exception{
	}
	@After

	public void tearDown() throws Exception{
		ShoeStorageInfo[]  theStock = {};
		Store.getInstance().load(theStock);
	}

	@Test
	public void testInstance(){
		Store anotherStore = Store.getInstance();
		assertEquals(storeTest, anotherStore);
	}

	@Test
	public void testLoad(){
		System.out.println("Loading to Store");
		ShoeStorageInfo[] anotherStock = {
				new ShoeStorageInfo ("baby sandals", 3, 2),
				new ShoeStorageInfo("messi shoes", 8, 0),
				new ShoeStorageInfo("kafkafim", 9, 1),
				new ShoeStorageInfo("Nike", 20, 4), 
				new ShoeStorageInfo("sandals", 22, 10),
				new ShoeStorageInfo("baby shoes", 7, 1),
				new ShoeStorageInfo("Nike", 3, 0),
				new ShoeStorageInfo("New-Balance", 0, 0),
				new ShoeStorageInfo("pahit kola", 0, 0)
		};
		Store.getInstance().load(anotherStock);
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("baby sandals", true));
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("baby sandals", false));
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("kafkafim", false));
		assertEquals(BuyResult.REGULAR_PRICE, Store.getInstance().take("kafkafim", false));
		assertEquals(BuyResult.REGULAR_PRICE, Store.getInstance().take("messi shoes", false));
		assertEquals(BuyResult.NOT_ON_DISCOUNT, Store.getInstance().take("messi shoes", true));
	}

	@Test
	public void testTake(){
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("Blundstone", true));
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("Blundstone", false));
		assertEquals(BuyResult.NOT_ON_DISCOUNT, Store.getInstance().take("Nike", true));
		assertEquals(BuyResult.REGULAR_PRICE, Store.getInstance().take("Nike", false));
		assertEquals(BuyResult.NOT_IN_STOCK, Store.getInstance().take("New-Balance", true));
		assertEquals(BuyResult.NOT_IN_STOCK, Store.getInstance().take("New-Balance", false));
	}
	@Test
	public void testAdd(){
		Store.getInstance().add("ilan trening", 2);
		Store.getInstance().add("New-Balance", 4);
		assertEquals(BuyResult.REGULAR_PRICE, Store.getInstance().take("ilan trening", false));
		assertEquals(BuyResult.NOT_ON_DISCOUNT, Store.getInstance().take("ilan trening", true));
		assertEquals(BuyResult.REGULAR_PRICE, Store.getInstance().take("ilan trening", false));
		assertEquals(BuyResult.NOT_IN_STOCK, Store.getInstance().take("ilan trening", false));
		assertEquals(BuyResult.REGULAR_PRICE, Store.getInstance().take("New-Balance", false));
	}

	@Test
	public void testAddDiscount(){
		Store.getInstance().addDiscount("New-Balance", 2);
		Store.getInstance().addDiscount("pahit kola", 2);
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("New-Balance", true));
		assertEquals(BuyResult.DISCOUNTED_PRICE, Store.getInstance().take("New-Balance", false));
		assertEquals(BuyResult.NOT_ON_DISCOUNT, Store.getInstance().take("New-Balance", true));
		assertEquals(BuyResult.NOT_IN_STOCK, Store.getInstance().take("pahit kola", true));
	}
	@Test
	public void testFile(){
		ShoeStorageInfo[] cleaning = {};
		Store.getInstance().load(cleaning);
		Store.getInstance().print();
		Receipt receipt = new Receipt("Tal", "Shapira", "Blundstone", false, 4, 3, 2);
		Store.getInstance().file(receipt);
		Store.getInstance().print();
	}
}






