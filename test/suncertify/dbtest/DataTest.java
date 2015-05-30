/**
 * 
 */
package suncertify.dbtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import suncertify.db.Data;
import suncertify.db.DuplicateKeyException;
import suncertify.db.RecordNotFoundException;
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
		String testDB = System.getProperty("user.dir") + File.separator + "testdb" + File.separator +"db-2x1.db";
		sourceFile = new File(System.getProperty("user.dir") + File.separator + "db-2x1.db");
		targetFile = new File(System.getProperty("user.dir") + File.separator + "testdb" + File.separator + "db-2x1.db");
		Files.copy(sourceFile.toPath(), targetFile.toPath(), REPLACE_EXISTING);		
		data = new Data(testDB);
	}
	
	private int getContractorsSize(boolean print) {
		
		int[] subs = data.find(new String[]{null,null,null,null,null,null});
		if (print) {
			System.out.println("\n\n\nPRINTING SUBS: "+subs.length);
			for (Integer sub : subs) {
				System.out.println(Arrays.asList(data.read(sub)).toString());
			}			
		}
		return subs.length;
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
		
		int subCount = getContractorsSize(false);
		int expectedNumSubs = subCount + 1;		
		int recNo = data.create(newRecord);		
		int actualNumSubs = getContractorsSize(false);
		
		assertEquals(expectedNumSubs, actualNumSubs);
		
		//assert all values of records are equal
		String[] subScriber = data.read(recNo);
		for (int i = 0; i < newRecord.length; i++) {
			assertEquals(subScriber[i], newRecord[i]);
		}
	}
	
	
	@Test
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

		int recCountBeforeUpdate = getContractorsSize(false);
		
		String[] updatedRecord = existingRecord;
		updatedRecord[2] = "updated service";
		
		data.update(recNo, updatedRecord);
		
		int recCountAfterUpdate = getContractorsSize(false);
		
		//assert count of sub-contractors before and after update
		assertEquals(recCountBeforeUpdate, recCountAfterUpdate);
		
		//read new record verify there.
		data.read((updatedRecord[0] + updatedRecord[1]).hashCode());
		String[] newRead = data.read(recNo);
		assertTrue(Arrays.equals(newRead, updatedRecord));
	}
	
	@Test
	(expected = RecordNotFoundException.class)
	public void shouldDeleteExistingRecord() {
		String[] record = {
				"test company delete",
				"test city delete",
				"test service delete",
				"12",
				"$20.00",
				""
		};
		int countBeforeCreate = getContractorsSize(false);		
		int recNo = data.create(record);
		String[] persistedRecord = data.read((record[0]+record[1]).hashCode());
		assertEquals(record[4], persistedRecord[4]);		
		int countAfterCreate = getContractorsSize(false);
		assertNotEquals(countBeforeCreate, countAfterCreate);
		data.delete(recNo);
		int countAfterDelete = getContractorsSize(false);
		assertEquals(countBeforeCreate, countAfterDelete);
		data.read(recNo);
	}
	
	@Test
	public void shouldMatchPattern()  {		
		String[] patternRecord = {
				"test company pattern",
				"test city pattern",
				"test service pattern",
				"12",
				"$20.00",
				""
		};
		data.create(patternRecord);
		int[] results = data.find(new String[]{"pattern"});
		assertNotNull(results);
	}
	

	@Test
	public void shouldReturnEmptyResultIfIncorrectParameters()  {
		int[] results = data.find(null);
		assertNotNull(results);		
	}
	
	@Test
	public void shouldReturnEmptyResultIfInsufficientFields()  {		
		String[] patternRecord = {
				"test company pattern",
				"test city pattern",
				"test service pattern",
				"12",
				""
		};

		int[] results = data.find(patternRecord);
		assertNotNull(results);
	}
	
	@Test
	public void shouldReturnRecordWhenPassedSingleMatchingField()  {
		String uniqueField = "auniquefield";
		String[] patternRecord = {
				"test company pattern test two",
				"test city pattern test two",
				uniqueField,
				"12",
				"$20.00",
				""
		};
		int record = data.create(patternRecord);

		int[] results = data.find(new String[]{"","",uniqueField,"","", ""});
		assertEquals(1, results.length);
		assertTrue(Arrays.equals(data.read(results[0]), data.read(record)));
	}
	
	@Test
	public void shouldReturnAllResultsIfFieldCriteriaIsNull()  {
		int[] results = data.find(new String[]{"","","","","", null});
		int countractorCount = getContractorsSize(false);
		assertEquals(countractorCount, results.length);
	}
	
	@Test
	public void shouldReturnNoneIfNoStringsPassed()  {
		int[] results = data.find(new String[]{"", "", "", "", "", ""});
		int expected = 0;
		int actual = results.length;
		assertEquals(expected, actual);
	}
	
	@Test
	public void secondDatabase()  {
		int count = getContractorsSize(false);
		Data data2 = new Data();
		
		String[] patternRecord = {
				"test company pattern data 2",
				"test city pattern data 2",
				"test service pattern",
				"12",
				"",
				""
		};
		data2.create(patternRecord);
		int countAfter = getContractorsSize(false);
		assertEquals(count + 1, countAfter);
	}
	
	@Test
	public void testThreadsLocking() throws InterruptedException, RecordNotFoundException {
		
		class Competitor extends Thread {
			private Data data;
			private int recNo;
			private String[] recordArray;
			
			public Competitor(Data data, int recNo, String[] recordArray) {
				this.data = data;
				this.recNo = recNo;
				this.recordArray = recordArray;
			}
			
			@Override
			public void run() {
				try {
					this.data.lock(this.recNo);
					this.data.update(this.recNo, this.recordArray);
					this.data.unlock(recNo);
				} catch (RecordNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
		
		int recKey = ("Philharmonic Remodeling"+"Hobbiton").hashCode();
		
		String [] testArray = new String[]{"Philharmonic Remodeling", 
				"Hobbiton", "test", "1", "test", "test"};
		
		String[] competitorThreadRecord = new String[]{"Philharmonic Remodeling", 
				"Hobbiton", "competitor", "2", "comp", "comp"};
		
		Data data2 = new Data();		
		Competitor competitor = new Competitor(data2, recKey, competitorThreadRecord);
		
		
		data.lock(recKey);
		competitor.start();
		Thread.sleep(3000);
		data.update(recKey, testArray);
		data.unlock(recKey);
		competitor.join();
		String[] arr = data.read(recKey);
		assertTrue(Arrays.equals(arr, competitorThreadRecord));		
	}

}
