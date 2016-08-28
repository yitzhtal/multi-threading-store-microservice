package bgu.spl.app;
import bgu.spl.mics.Request;

public class RestockRequest<Boolean> implements Request<Boolean> {
         private String shoeType;
         
         public RestockRequest(String shoeType_) {
        	 this.shoeType = shoeType_;
         }
         
         public String getType() {
        	 return this.shoeType;
         }
}
