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
	
	@Test
	public void shouldCreateAValidSubcontractor() {
		String specialities = "speciality";
		String name = "name";
		String city = "city";
		String hourlyRate = "123";
		String employeeCount = "1";
		String owner = "12345678";
		new Subcontractor(name, city, hourlyRate, specialities, employeeCount, owner);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldThrowExceptionIfNull() {
		subcontractor.setCustomerId(null);
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
		subcontractor.setEmployeeCount("-1");
	}
	
	@Test
	public void shouldAllowPositiveNumberofEmployees() {
		subcontractor.setEmployeeCount("0");
		assertEquals("0", subcontractor.getEmployeeCount());
	}
	
	@Test
	(expected=IllegalArgumentException.class)
	public void shouldNotAllowCharactersForEmployeeNumbers() {
		subcontractor.setEmployeeCount("abc");
	}
	
	@Test
	public void shouldReturnTrueForTwoEqualSubContractors() {
		String[] subC = {"name", "city", "specialities", "2", "", ""};
		Subcontractor sub = new Subcontractor(subC);
		Subcontractor otherSubContractor = new Subcontractor(subC);
		
		assertTrue(sub.equals(otherSubContractor));
	}
	
	@Test
	public void shouldReturnFalseForTwoEqualSubContractors() {		
		String[] subC = {"name", "city", "specialities", "2", "", ""};
		String[] otherSub = {"name", "othercity", "specialities", "2", "", ""};
		Subcontractor sub = new Subcontractor(subC);
		Subcontractor otherSubContractor = new Subcontractor(otherSub);
		assertFalse(sub.equals(otherSubContractor));
	}

	@Test
	(expected=IllegalArgumentException.class)
	public void shouldNotAllowAListOfZeroSpecialities() {
		String specialities = "";
		subcontractor.setSpecialities(specialities);
	}
	
	@Test
	(expected=IllegalArgumentException.class)
	public void shouldNotAllowNullSpecialities() {
		String specialities = null;
		subcontractor.setSpecialities(specialities);
	}
}
