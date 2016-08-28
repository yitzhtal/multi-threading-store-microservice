package bgu.spl.mics.impl;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.app.NewDiscountBroadcast;
import bgu.spl.app.PurchaseOrderRequest;
import bgu.spl.app.Receipt;
import bgu.spl.app.SellingService;
import bgu.spl.app.WebsiteClientService;
import bgu.spl.mics.Message;
import bgu.spl.mics.RequestCompleted;

public class MessageBusImplTest {
	private CountDownLatch latch;
	private SellingService Tal;
	private SellingService Braberman;
	private SellingService Saak;
	private WebsiteClientService Nemesh;
	private WebsiteClientService Shapira;

	@Before
	public void setUp() throws Exception{
		Tal = new SellingService("Tal", latch);
		Braberman = new SellingService("Braberman", latch);
		Saak = new SellingService("Saak", latch);
		Shapira = new WebsiteClientService("Shapira", latch);
		Nemesh = new WebsiteClientService("Nemesh", latch);	
		MessageBusImpl.getInstance().register(Tal);
		MessageBusImpl.getInstance().register(Shapira);
	}

	@After
	public void tearDown(){
		MessageBusImpl.getInstance().unregister(Tal);
		MessageBusImpl.getInstance().unregister(Shapira);
		MessageBusImpl.getInstance().unregister(Braberman);
	}

	@Test
	public void testSubscribeRequest() throws InterruptedException{
		PurchaseOrderRequest purReq = new PurchaseOrderRequest("blundstone",false,"Shapira", 3);
		MessageBusImpl.getInstance().subscribeRequest(purReq.getClass(), Tal);
		MessageBusImpl.getInstance().sendRequest(purReq, Shapira);
		Message message = MessageBusImpl.getInstance().awaitMessage(Tal);
		assertEquals(purReq, message);
	}

	@Test
	public void testAwaitmessage() throws InterruptedException{
		PurchaseOrderRequest purReq = new PurchaseOrderRequest("blundstone",false,"Shapira", 3);
		MessageBusImpl.getInstance().subscribeRequest(purReq.getClass(), Braberman);
		MessageBusImpl.getInstance().sendRequest(purReq, Shapira);
		Message message = MessageBusImpl.getInstance().awaitMessage(Braberman);
		assertEquals(purReq, message);
	} 

	
	@Test
	public void testSubscribeBroadcast() throws InterruptedException{
		NewDiscountBroadcast disBro = new NewDiscountBroadcast("blundstone",1);
		MessageBusImpl.getInstance().subscribeBroadcast(disBro.getClass(), Tal);
		MessageBusImpl.getInstance().sendBroadcast(disBro);
		Message message = MessageBusImpl.getInstance().awaitMessage(Tal);
		assertEquals(disBro, message);
	}
	
	@Test
	public void testRegister() throws InterruptedException{
		PurchaseOrderRequest purReq = new PurchaseOrderRequest("blundstone",false,"Shapira", 3);
		SellingService theNewGuy = new SellingService("theNewGuy", latch);
		MessageBusImpl.getInstance().register(theNewGuy);
		MessageBusImpl.getInstance().subscribeRequest(purReq.getClass(), theNewGuy);
		MessageBusImpl.getInstance().sendRequest(purReq, Shapira);
		Message message = MessageBusImpl.getInstance().awaitMessage(theNewGuy);
		assertEquals(purReq, message);
	}

	@Test
	public void testSendBroadcast() throws InterruptedException{
		NewDiscountBroadcast broadcastThis = new NewDiscountBroadcast("Prince_Shoes",1);
		MessageBusImpl.getInstance().subscribeBroadcast(broadcastThis.getClass(), Nemesh);
		MessageBusImpl.getInstance().sendBroadcast(broadcastThis);
		Message message = MessageBusImpl.getInstance().awaitMessage(Nemesh);
		assertEquals(broadcastThis, message);	
	}

	@Test
	public void testSendRequest() throws InterruptedException{
		PurchaseOrderRequest mySpecialRequest = new PurchaseOrderRequest("Willson_Shoes",false,"Shapira2", 3);
		MessageBusImpl.getInstance().subscribeRequest(mySpecialRequest.getClass(), Saak);
		MessageBusImpl.getInstance().sendRequest(mySpecialRequest, Nemesh);
		Message message = MessageBusImpl.getInstance().awaitMessage(Saak);
		assertEquals(mySpecialRequest, message);	
	}
	
	@Test
	public void testComplete() throws InterruptedException{
		PurchaseOrderRequest purReq = new PurchaseOrderRequest("blundstone",false,"Shapira", 3);
		Receipt receipt = new Receipt("Tal", "Shapira", "kafkafim", false,7,3,1);
		MessageBusImpl.getInstance().subscribeRequest(purReq.getClass(),Tal);
		MessageBusImpl.getInstance().sendRequest(purReq, Shapira);
		MessageBusImpl.getInstance().complete(purReq, receipt);
		Message message = MessageBusImpl.getInstance().awaitMessage(Shapira);
		assertEquals(purReq,((RequestCompleted<?>)message).getCompletedRequest());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).getAmountSold(),receipt.getAmountSold());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).getCustomer(),receipt.getCustomer());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).wasDiscounted(),receipt.wasDiscounted());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).getIssuedTick(),receipt.getIssuedTick());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).getRequestTick(),receipt.getRequestTick());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).getShoeType(),receipt.getShoeType());
		assertEquals(( (Receipt) ((RequestCompleted<?>)message) .getResult()).getSeller(),receipt.getSeller());
	}
}










