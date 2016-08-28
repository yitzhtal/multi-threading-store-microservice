package bgu.spl.app;
import java.util.Timer;
import java.util.logging.*;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.MicroService;

public class TimeService extends MicroService {
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private int passedTime; //that's what we plan on sending as broadcast to everybody!
	private int speed; //how long does each tick takes? milliseconds.
    private int duration; //number of ticks before termination
    private CountDownLatch m_latchObject;
	private Timer t1;
	public TimeService(String name_,int speed_,int duration_,CountDownLatch m_latchObject_) {
		super(name_);
		this.speed = speed_;
		this.duration = duration_;
		this.passedTime = 0;
		this.m_latchObject = m_latchObject_;
		t1 = new Timer();
	}

	public int getPassedTime() {
		return this.passedTime;
	}
	
	protected void initialize() {	
		try {

			this.m_latchObject.await(); 
			//System.out.println(getName()+ " now starting sending ticks after everybody finished initialzing!");
			LOGGER.config(getName()+ " now starting sending ticks after everybody finished initializing!");
			class TimeServiceTask extends TimerTask {
				public void run() {
		        	passedTime+=1;
		            if(passedTime == duration + 1) {
		            	LOGGER.info("This was the last tick! We are done!");
		            	Store.getInstance().print();    	
		            	t1.cancel();
		            	this.cancel();
		            }
		            else  LOGGER.info("Tick: " + passedTime);
		        	sendBroadcast(new TickBroadcast(passedTime,duration));
		        
		            
		        }
		    }	
			t1.schedule(new TimeServiceTask(),this.speed,this.duration);
			terminate();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
