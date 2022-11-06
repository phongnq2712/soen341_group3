package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import model.Quotations;
class testing {
	@Test
	  public void testGetTheLowestQuotation()
	  {
	    int sum =0;
	    Quotations q = new Quotations();
	     Object[] data = new Object[3];
	       data[0] ="Fish-1";
	       data[1]= "Beef-3";
	       data[2]= "Chicken-14";
	     Quotations[] lowest = q.getTheLowestQuotation(data);
	    for(Quotations quo: lowest) {
		            	if(quo != null){
	        				
	        				sum += Integer.parseInt(quo.getPrice()) * Integer.parseInt(quo.getQty());
	        				
	        				}
	      int expected = 8900;
	      
	      Assert.assertEquals(expected, sum);
	    }  
	  }
	

}

