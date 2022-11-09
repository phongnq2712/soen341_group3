package test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Assert;
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
		String name = "u1";
		String pwd = "123";
		boolean isLogin = false;

		int[] arrLogin = u.checkLogin(name, pwd);

		if (arrLogin.length > 0 && arrLogin[0] > 0 && arrLogin[1] > 0) {
			isLogin = true;
		}

		Assert.assertEquals(true, isLogin);

	}

}


