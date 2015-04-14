/**
 * 
 */
package suncertify.dbtest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
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
		String testDB = System.getProperty("user.dir") + File.separator + "testdb";
		sourceFile = new File(System.getProperty("user.dir") + File.separator + "db-2x1.db");
		targetFile = new File(System.getProperty("user.dir") + File.separator + "testdb" + File.separator + "db-2x1.db");
		Files.copy(sourceFile.toPath(), targetFile.toPath(), REPLACE_EXISTING);		
		data = new Data(testDB);
	}
	
	@Test
	public void shouldNotThrowIndexOutOfBoundsException() {
		data.read(1);
		data.read(29);
	}
	
	@Test
	(expected = RecordNotFoundException.class)
	public void shouldThrowRecordNotFoundIfIndexOutOfBoundsLower() {
		data.read(0);
	}
	
	@Test
	(expected = RecordNotFoundException.class)
	public void shouldThrowRecordNotFoundIfIndexOutOfBoundsUpper() {
		data.read(30);
	}
	
	@Test
	public void shouldReturnNonNullOnRead() {
		assertNotNull(data.read(1));
	}
	
	@Test
	public void shouldReturnCorrectEntryOnReadFirstRecord() {
		String[] record = data.read(1);
		assertEquals("Buonarotti & Company", record[0]);
		assertEquals("Smallville", record[1]);
		assertEquals("Air Conditioning, Painting, Painting", record[2]);
		assertEquals("10", record[3]);
		assertEquals("$40.00", record[4]);
		assertEquals("", record[5]);
	}
	
	@Test
	public void shouldReturnCorrectEntryOnReadLastEntry() {
		String[] record = data.read(29);
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
		
		data.create(newRecord);
	}

}
