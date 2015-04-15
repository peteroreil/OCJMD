/**
 * 
 */
package suncertify.dbtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
import suncertify.db.Subcontractor;
import static java.nio.file.StandardCopyOption.*;

/**
 * @author epetore
 *
 */
public class DataTest {

	private static Data data;
	private static File targetFile;
	private static File sourceFile;
	
	@BeforeClass
	public static void setUp() throws IOException {
		String testDB = System.getProperty("user.dir") + File.separator + "testdb";
		sourceFile = new File(System.getProperty("user.dir") + File.separator + "db-2x1.db");
		targetFile = new File(System.getProperty("user.dir") + File.separator + "testdb" + File.separator + "db-2x1.db");
		Files.copy(sourceFile.toPath(), targetFile.toPath(), REPLACE_EXISTING);		
		data = new Data(testDB);
	}
	
	@Test
	(expected = RecordNotFoundException.class)
	public void shouldNotThrowIndexOutOfBoundsException() {
		data.read(1);
	}
	
	@Test
	public void shouldReturnCorrectEntryOnReadFirstRecord() {
		String subName = "Buonarotti & Company";
		String subCity = "Smallville";
		int key = (subName + subCity).hashCode();
		String[] record = data.read(key);
		assertEquals("Buonarotti & Company", record[0]);
		assertEquals("Smallville", record[1]);
		assertEquals("Air Conditioning, Painting, Painting", record[2]);
		assertEquals("10", record[3]);
		assertEquals("$40.00", record[4]);
		assertEquals("", record[5]);
	}
	
	@Test
	public void shouldReturnCorrectEntryOnReadLastEntry() {
		String subName = "Moore Power Tool Ya";
		String subCity = "Lendmarch";
		int key = (subName + subCity).hashCode();
		
		String[] record = data.read(key);
		assertEquals("Moore Power Tool Ya", record[0]);
		assertEquals("Lendmarch", record[1]);
		assertEquals("Electrical, Heating, Glass", record[2]);
		assertEquals("7", record[3]);
		assertEquals("$95.00", record[4]);
		assertEquals("", record[5]);
	}
	
	@Test
	(expected = DuplicateKeyException.class) 
	public void shouldThrowExceptionInEventOfCreatinguplicateEntry() {
		String[] duplicateRecord = {"Buonarotti & Company", "Smallville", 
				"Air Conditioning, Painting, Painting", "10", 
				"$40.00", ""};
		data.create(duplicateRecord);
	} 
	
	
	@Test 
	public void shouldAddNewRecordToFileOnCreate() {
		
		String[] newRecord = {
				"test company",
				"test city",
				"test service",
				"1",
				"$20.00",
				""
		};
		
		List<Subcontractor> subs = data.getSubcontractors();
		int actualNumSubs = subs.size();
		int recNo = data.create(newRecord);
		subs = data.getSubcontractors();
		int expectedNumSubs = actualNumSubs + 1;
		actualNumSubs = subs.size();
		assertEquals(expectedNumSubs, actualNumSubs);
		
		String[] subScriber = data.read(recNo);
		for (int i = 0; i < newRecord.length; i++) {
			assertEquals(subScriber[i], newRecord[i]);
		}
	}
	
	
	@Test
	(expected = RecordNotFoundException.class)
	public void shouldUpdateExistingRecord() {
		
		String[] existingRecord = {
				"test company 2",
				"test city 2",
				"test service 2",
				"12",
				"$20.00",
				""
		};
		
		//assert read and create took place
		int recNo = data.create(existingRecord);		
		String[] record = data.read(recNo);		
		assertEquals(record[0], existingRecord[0]);
		
		List<Subcontractor> subs = data.getSubcontractors();
		int numSubsBefore = subs.size();
		
		String[] updatedRecord = existingRecord;
		updatedRecord[0] = "updated company";
		updatedRecord[1] = "updated location";
		
		data.update(recNo, updatedRecord);
		
		subs = data.getSubcontractors();
		int numSubsAfter = subs.size();
		
		//assert count of sub-contractors before and after update
		assertEquals(numSubsBefore, numSubsAfter);
		
		//read new record verify there.
		data.read((updatedRecord[0] + updatedRecord[1]).hashCode());
		
		//read old record number - should throw RecordNotFoundException
		data.read(recNo);
	}
	

}
