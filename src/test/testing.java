package test;

import junit.framework.Assert;
import org.junit.test;
import model.Quotations;

public class testing {
  

  
  @test
  public void testGetTheLowestQuotation( )
  {
    int sum =0;
    Quotations q = new Quotations();
     Object[] data = new Object[3];
       data[0] = new Object("Fish-5");
       data[1]= new Object("Beef-7");
       data[2]= new Object("Chicken-8");
     Quotations[] lowest = q.getTheLowestQuotation(data);
    for(Quotations q: lowest) {
	            	if(q != null) {
        				
        				sum += Integer.parseInt(q.getPrice()) * Integer.parseInt(q.getQty());
        				
        				}
      int expected = 4400;
      
      Assert.assertEquals(expected, sum);
  
  }
  
  
  
}
