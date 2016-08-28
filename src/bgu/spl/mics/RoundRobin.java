package bgu.spl.mics;
import java.util.ArrayList;


public class RoundRobin {
       private ArrayList<MicroService> myArray;
       private int requesterRunner;
       
       public RoundRobin() {
    	   this.myArray = new ArrayList<MicroService>();
    	   this.requesterRunner = -1;
       }
       
       public synchronized int getSize() {
    	   return this.myArray.size();
       }
       
       public synchronized MicroService get(int i) {
    	   return this.myArray.get(i);
       }
       
       public synchronized MicroService remove(int i) {
    	   return this.myArray.remove(i);
       }  
       
       public synchronized MicroService getNext() {
    	   this.requesterRunner = (this.requesterRunner+1)%(this.getSize());
    	   return this.myArray.get(this.requesterRunner);
       }
       
       public synchronized void addNext(MicroService m) {
    	   this.myArray.add(m);
       }
       
       public synchronized void printArray() {
    	   for(int i=0; i<this.getSize();i++) {
    		   if(this.getSize() == 1) {
    			   System.out.println("{" + this.myArray.get(i).getName() + "}");
    		   }
    		   if(i != this.getSize()-1) {
    			   System.out.print("{"+this.myArray.get(i).getName()+",");
    		   }
    		   else{
    			   System.out.print(this.myArray.get(i).getName()+"}");
    		   }
    	   }
       }
}



