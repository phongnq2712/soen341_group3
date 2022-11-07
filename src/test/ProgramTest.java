package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import model.Quotations;
import model.User;

class ProgramTest {

	@Test
	public void testGetTheLowestQuotation() {

		int sum = 0;

		Quotations q = new Quotations();

		Object[] data = new Object[3];
		data[0] = "Fish-5";
		data[1] = "Beef-7";
		data[2] = "Chicken-8";
		Quotations[] lowest = q.getTheLowestQuotation(data);

		for (Quotations quo : lowest) {
			if (quo != null) {

				sum += Integer.parseInt(quo.getPrice()) * Integer.parseInt(quo.getQty());

			}

		}

		int expected = 10900;

		Assert.assertEquals(expected, sum);

	}
	
	@Test
	public void testCheckLogin() {
		User u = new User();
		String name = "2324";
		String pwd = "123";

		int[] arr = u.checkLogin(name, pwd);

		int[] e = new int[4];
		e[0] = 1; // user input
		e[1] = 2; // vendor input
		e[2] = 3; // admin input
		e[3] = 4; // invalid input

		if (arr[1] == 1)
			Assert.assertEquals(e[0], arr[1]);
		else if (arr[1] == 2)
			Assert.assertEquals(e[1], arr[1]);
		else if (arr[1] == 3)
			Assert.assertEquals(e[2], arr[1]);
		else
			Assert.assertEquals(e[4], arr[1]);
	}

}


