package bgu.spl.app;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
          private int currentTick;
	      private int duration;
	      
	      public TickBroadcast(int currentTick_,int duration_) {
	    	  this.currentTick = currentTick_;
	    	  this.duration = duration_;
	      }
	      
	      public int getCurrentTick() {
	    	  return this.currentTick;
	      }
	      
	      public void nextTick() {
	    	  this.currentTick++;
	      }
	      
          public int getDuration() {
        	  return this.duration;
          }
}
