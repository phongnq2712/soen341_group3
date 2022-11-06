package test;

import junit.framework.Assert;
import org.junit.test;
import model.Quotations;

public class testing {
  

  
  @test
  public void testGetTheLowestQuotation()
  {
    int sum =0;
    Quotations q = new Quotations();
     Object[] data = new Object[3];
       data[0] ="Fish-5";
       data[1]= "Beef-7";
       data[2]= "Chicken-8";
     Quotations[] lowest = q.getTheLowestQuotation(data);
    for(Quotations quo: lowest) {
	            	if(quo: !null){
        				
        				sum += Integer.parseInt(q.getPrice()) * Integer.parseInt(q.getQty());
        				
        				}
      int expected = 4400;
      
      Assert.assertEquals(expected, sum);
  
  }
  
  
  
}
