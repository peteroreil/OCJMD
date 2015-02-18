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
		subcontractor.setCustomerId("9999999");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowExceptionIfMoreThanEightDigits() {
		subcontractor.setCustomerId("100000000");
	}
	
	@Test 
	public void shouldSetForEightDigits() {
		String eightDigits = "10000000";
		subcontractor.setCustomerId(eightDigits);
		assertEquals(eightDigits, subcontractor.getCustomerId());
	}
	
	@Test 
	public void shouldSetForSevenZeroesAndOneDigit() {
		String eightDigits = "00000001";
		subcontractor.setCustomerId(eightDigits);
		assertEquals(eightDigits, subcontractor.getCustomerId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setCustIdShouldFailWithCharacters() {
		String characters = "0000ccsc";
		subcontractor.setCustomerId(characters);		
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
	
	@Test
	public void shouldReturnTrueForTwoEqualSubContractors() {
		Subcontractor otherSubContractor = new Subcontractor();
		String name = "name";
		String city = "city";
		subcontractor.setCityName(city);
		subcontractor.setName(name);
		otherSubContractor.setCityName(city);
		otherSubContractor.setName(name);
		assertTrue(subcontractor.equals(otherSubContractor));
	}
	
	@Test
	public void shouldReturnFalseForTwoEqualSubContractors() {
		Subcontractor otherSubContractor = new Subcontractor();
		String name = "name";
		String city = "city";
		subcontractor.setCityName(null);
		subcontractor.setName(name);
		otherSubContractor.setCityName(city);
		otherSubContractor.setName(name);
		assertFalse(subcontractor.equals(otherSubContractor));
	}

	@Test
	public void test() {
		System.out.println(System.getProperty("user.dir"));
	}
}
