package suncertify.dbtest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import suncertify.db.Subcontractor;

public class SubcontractorTest {

	private Subcontractor subcontractor;
	
	@Before
	public void setup() {
		subcontractor = new Subcontractor();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowExceptionIfLessThanEightDigits() {
		subcontractor.setCustomerId(9999999);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowExceptionIfMoreThanEightDigits() {
		subcontractor.setCustomerId(100000000);
	}
	
	@Test 
	public void shouldSetForEightDigits() {
		int eightDigits = 10000000;
		subcontractor.setCustomerId(eightDigits);
		assertEquals(eightDigits, subcontractor.getCustomerId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowNegativeNumberOfEmployees() {
		subcontractor.setEmployeeCount(-1);
	}
	
	@Test
	public void shouldAllowPositiveNumberofEmployees() {
		subcontractor.setEmployeeCount(0);
		assertEquals(0, subcontractor.getEmployeeCount());
	}

}
