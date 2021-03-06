package suncertify.db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import java.io.FileNotFoundException;


/**
 * Database.java
 * This class implements the CRUD operations on the database
 * file. This class's methods are only intended to be called by the Data.java class
 * @author Peter O'Reilly
 * @version 1.0.0
 */
class Database {
	private static final String ENCODING = "US-ASCII";	
    private static final int MAGIC_COOKIE_BYTES = 4;
    private static final int RECORD_LENGTH_BYTES = 4;
    private static final int NUMBER_OF_FIELDS_BYTES = 2;      
    private static final int FIELD_NAME_BYTES_LENGTH = 2;
    private static final int FIELD_LENGTH_BYTES = 2;    
	private static String recordHolder = new String(new byte[Subcontractor.TOTAL_RECORD_LENGTH]);
	private static RandomAccessFile databaseFile = null;
	private static ReadWriteLock recordNumbersLock = new ReentrantReadWriteLock();
	private static Map<Integer, Long> recordLocations = new HashMap<Integer, Long>();


	/**
	 * Database constructor take a single argument the absolute path to the
	 * database file. Throws a database exception in the event of of file not 
	 * found.
	 * @param databaseFilePath - string absolute path to database file.
	 * @throws DatabaseException
	 */
	public Database(String databaseFilePath) {
		if (databaseFile == null) {
			String databasePath = databaseFilePath;
			String readWrite = "rw";
			try {
				databaseFile = new RandomAccessFile(databasePath, readWrite);				
				getSubcontractors(true);
			} catch (FileNotFoundException fnfe) {
				throw new DatabaseException("Database not found: "+ databasePath);
			}			
		}
	}

	/**
	 * Reads the specified record number and returns a String
	 * array containing the record values.
	 * @param recNo - the record number to read
	 * @return String array record read from database file
	 * @throws RecordNotFoundException
	 */
	String[] read(int recNo) throws RecordNotFoundException {
		Long recordLocation = null;
		
		recordLocation = this.getSubcontractor(recNo);			

		if (recordLocation == null) {
			throw new RecordNotFoundException("Record at index " + 
					recNo + " not found.");
		}
		
		Subcontractor subcontractor = this.retrieveSubcontrator(recordLocation);
		
		if(subcontractor == null) {
			throw new RecordNotFoundException("Record "+ recNo + "not found");
		}
		return subcontractor.toArray();
	}
	
	/**
	 * Writes a new record to databases.
	 * Requires a String[] of length 6 exactly. 
	 * @param data - the new record to create
	 * @return the newly created record, record number
	 * @throws DuplicateKeyException
	 * @throws IllegalArgumentException
	 */
	int create(String[] data) {
		validateData(data);
		return persistSubcontractor(data, null, true);
	}
	
	/**
	 * Updates an existing record. Requires a String[] of length
	 * 6 exactly.
	 * <p>
	 * <b>In event of an update of data[0] and/or data[1] the used recNo
	 * reference will no longer reference the updated record. A new recNo will have
	 * to be calculated after the update method is called</b>
	 * </p>
	 * 
	 * @param recNo 	the record number to update
	 * @param data 		the updated String data array
	 * @throws RecordNotFoundException
	 * @throws IllegalArgumentException
	 */
	void update(int recNo, String[] data) {
		validateData(data);		
		persistSubcontractor(data, recNo, true);
	}
	
	/**
	 * Deletes the supplied record number and updates 
	 * the database file that a record is no longer valid.
	 * @param recNo
	 * @throws RecordNotFoundException
	 */
	void delete(int recNo) {
		persistSubcontractor(null, recNo, false);		
	}
	
	/**
	 * Searches for all records that match or contain
	 * the subset of the criteria. Will return all records
	 * if any one of the search criteria is null valued.
	 * @param criteria - string array of search criteria, must be 
	 * an array of length 6.
	 * @return integer array containing record numbers of all 
	 * matching records
	 */
	int[] find(String[] criteria) {
		try {
			validateData(criteria);
		} catch (IllegalArgumentException | DatabaseException e) {
			return new int[]{};
		}
		
		List<Integer> matchingKeys = new ArrayList<Integer>();
		
		
		for (Subcontractor subcontractor : getSubcontractors()) {
			if (subcontractor.matches(criteria)) {
				matchingKeys.add(subcontractor.hashCode());
			}
		}
		
		int[] results = new int[matchingKeys.size()];
		
		for (int index = 0; index < matchingKeys.size(); index++) {
			results[index] = matchingKeys.get(index);
		}		
		
		return results;
	}

	/**
	 * Persists a record to the database file
	 * If an update an Integer recordNumber is required,
	 * if recordNumber is null it is considered a create. 
	 * 
	 * @param data Sub-contractor Data
	 * @param recordNumber Record to update or null
	 * @param isValidRecord
	 * @return the integer index of newly created/updated record
	 * @throws DuplicateKeyException
	 * @throws RecordNotFoundException
	 * @throws DatabaseException
	 */
	private int persistSubcontractor(String[] data, Integer recordNumber, boolean isValidRecord) {
		boolean createNewRecord = (recordNumber == null);
		
		int recordKey = (recordNumber == null) ?
				(data[0] + data[1]).hashCode()
				: recordNumber;
		
		recordNumbersLock.writeLock().lock();
		
		try {	
			Long dbOffset = recordLocations.get(recordKey);
			boolean isNewRecord = (dbOffset == null);
			
			if (createNewRecord) {	
				if (!isNewRecord) {
					throw new DuplicateKeyException("Key " + recordKey +
					"already exists");	
				}
				//create a new record location
				dbOffset = databaseFile.length();
				recordLocations.put(recordKey, dbOffset);
				
			} else {
				if (isNewRecord) {
					throw new RecordNotFoundException("No record found. Key: " +
							recordKey);
				}
				
				//verify composite key fields not updated
				if (isValidRecord) {
					int newKey = (data[0] + data[1]).hashCode();
					
					if(newKey != recordKey) {
						throw new DatabaseException("You cannont update Contractor Name "
								+ "and/or Location Field");
					}

				} else {
					// if record is marked for deletion
					recordLocations.remove(recordKey);
				}
				

			}
			
			final StringBuilder builder = new StringBuilder(recordHolder);
			
			class RecordWriter {
				int start = 0;
				void write(String data, int length) {					
					builder.replace(start, start + data.length(), data);
					start += length;
				}
			}				
			
			if (isValidRecord) {				
				RecordWriter writer = new RecordWriter();
				String validFlag = getValidRecordFlag(true);
				writer.write(validFlag, Subcontractor.VALID_RECORD_LENGTH);
				writer.write(data[0], Subcontractor.NAME_LENGTH);
				writer.write(data[1], Subcontractor.LOCATION_LENGTH);
				writer.write(data[2], Subcontractor.SPECIALITIES_LENGTH);
				writer.write(data[3], Subcontractor.SIZE_LENGTH);
				writer.write(data[4], Subcontractor.RATE_LENGTH);
				writer.write(data[5], Subcontractor.OWNER_LENGTH);	
			}
			
			synchronized(databaseFile) {
				databaseFile.seek(dbOffset);
				if (isValidRecord) {
					databaseFile.write(builder.toString().getBytes());
				} else {
					databaseFile.write(getValidRecordFlag(false).getBytes());
				}
			}
			
			
		} catch (IOException ioe) {
			throw new DatabaseException("Exception reading length of file");			
		} finally {
			recordNumbersLock.writeLock().unlock();
		}
		
		return recordKey;
	}
	

	/**
	 * Parses integer 0 or 1 to string. If argument is 
	 * true will return 0 parsed to string.
	 * @return a 0 or 1 valued string byte array
	 * @param isValid - boolean flag true or false
	 */
	private String getValidRecordFlag(boolean isValid) {
		int flag = 0;
		
		if (! isValid) {
			flag = 1;
		}
		
		byte validFlag = (byte)flag;
		byte[] validArray = new byte[1];
		validArray[0] = validFlag;
		return new String(validArray);		
	}
	
	/**
	 *Retrieves a list of Subcontractor Objects from the database file. If the 
	 *boolean isWriteLocked is true, it will store the record numbers and their 
	 *respective locations in the database file, in the recordLocations Map using the 
	 *record number as a key where the location in the file is the associated value.
	 * @return list of subcontractors from database file
	 * @param boolean isWriteLocked
	 * @throws DatabaseException 
	 */
	private List<Subcontractor> getSubcontractors(boolean isWriteLocked) {
		List<Subcontractor> subcontractors = new ArrayList<Subcontractor>();
		
		if(isWriteLocked) {
			recordNumbersLock.writeLock().lock();
		}
		
		try {
			long offSetInFile = calculateFileOffset();
			long dbLength = databaseFile.length();
			for (long locationInFile = offSetInFile; 
					locationInFile < dbLength;
					locationInFile += Subcontractor.TOTAL_RECORD_LENGTH) {
				
				Subcontractor subcontractor = retrieveSubcontrator(locationInFile);
				
				if (subcontractor != null) {
					subcontractors.add(subcontractor);
					if (isWriteLocked) {
						int key = subcontractor.hashCode();
						recordLocations.put(key, locationInFile);
					}
				}
			}
		} catch (IOException e) {
			throw new DatabaseException(e.getMessage());
		} finally {			
			if(isWriteLocked) {
				recordNumbersLock.writeLock().unlock();
			}
		}
		
		return subcontractors;
	}
	
	/**
	 * Returns a list of Subcontractor objects without an update
	 * to the record map
	 * @return List of Subcontractor objects
	 */
	private List<Subcontractor> getSubcontractors() {
		return getSubcontractors(false);
	}
	
	/**
	 * @return long the length in bytes from zero to where 
	 * the first sub-contractor record exists in the database file.
	 */
	private long calculateFileOffset() {
		long offset = Database.MAGIC_COOKIE_BYTES + 
				Database.NUMBER_OF_FIELDS_BYTES + 
				Database.RECORD_LENGTH_BYTES;		
		long commonFieldOffset = Database.FIELD_NAME_BYTES_LENGTH + 
				Database.FIELD_LENGTH_BYTES;			
		long nameOffset = commonFieldOffset + 
				Subcontractor.NAME_BYTES_LENGTH;
		long locationOffset = commonFieldOffset + 
				Subcontractor.LOCATION_BYTES_LENGTH;
		long specialityOffset = commonFieldOffset + 
				Subcontractor.SPECIALITIES_BYTES_LENGTH;
		long rateOffset = commonFieldOffset + 
				Subcontractor.RATE_BYTES_LENGTH;
		long sizeOffset = commonFieldOffset + 
				Subcontractor.SIZE_BYTES_LENGTH;
		long ownerOffset = commonFieldOffset + 
				Subcontractor.OWNER_BYTES_LENGTH;
		
		return (offset + nameOffset + locationOffset + 
				specialityOffset + rateOffset + sizeOffset + 
				ownerOffset);
	}

	/**
	 * @return Long location in file of subcontractor. Will return null if 
	 * no subcontractor is found.
	 * @param String key comprised of subcontractor name concatenated with city name.
	 * e.g. String key = subcontractor.name + subcontractor.cityName
	 *  
	 */
	private Long getSubcontractor(int recordNumber) {
		recordNumbersLock.readLock().lock();
		try {
			Long locationInFile = recordLocations.get(recordNumber);
			return locationInFile;
		} finally {
			recordNumbersLock.readLock().unlock();
		}
	}

	/**
	 * Reads the database file at the specifed offset
	 * locationInFile. Reads 183 bytes, one byte for valid record and 
	 * 182 for the record itself. Synchronizes on the databaseFile 
	 * RandomAccessFile.
	 * @param locationInFile - the offset to the location of record in file
	 * @return Subcontractor record read starting from the offset location.
	 * @throws DatabaseException 
	 */
	private Subcontractor retrieveSubcontrator(long locationInFile) {
		final byte[] isValidArray = new byte[Subcontractor.VALID_RECORD_LENGTH];
		final byte[] data = new byte[Subcontractor.RECORD_LENGTH];
		
		synchronized(databaseFile) {
			try {
				databaseFile.seek(locationInFile);
				databaseFile.readFully(isValidArray);
				databaseFile.readFully(data);
			} catch (IOException ioe) {
				throw new DatabaseException(ioe.getMessage());				
			}
		}
		
		class RecordReader{
			int offset = 0;
			String read(int length) {
			String str = null;
			try {
				str = new String(data, offset, length, Database.ENCODING);
			} catch (UnsupportedEncodingException usce) {
				throw new DatabaseException("Exception reading record: " + usce.getMessage());
			}
				offset += length;
				return str.trim();
			}
		}
		
		RecordReader reader = new RecordReader();
		int isValid = getValue(isValidArray);
		String name = reader.read(Subcontractor.NAME_LENGTH);
		String location = reader.read(Subcontractor.LOCATION_LENGTH);
		String specialities = reader.read(Subcontractor.SPECIALITIES_LENGTH);
		String size = reader.read(Subcontractor.SIZE_LENGTH);
		String rate = reader.read(Subcontractor.RATE_LENGTH);
		String owner = reader.read(Subcontractor.OWNER_LENGTH);
		
		//if record is marked as deleted return null.
		Subcontractor subcontractor = (isValid != 0) 
				? null 
				: new Subcontractor(name, location, rate, 
						specialities, size, owner);

		return subcontractor;
	}
	
    private int getValue(final byte [] byteArray) {
        int value = 0;
        final int byteArrayLength = byteArray.length;
 
        for (int i = 0; i < byteArrayLength; i++) {
            final int shift = (byteArrayLength - 1 - i) * 8;
            value += (byteArray[i] & 0x000000FF) << shift;
        }
 
        return value;
    }
    
	private void validateData(String[] data) {
		if (data == null) {
			throw new IllegalArgumentException("String[] data cannot be null");
		}
		
		if (data.length < (new Subcontractor()).toArray().length) {
			throw new DatabaseException("Insufficient fields passed: " + 
					Arrays.asList(data).toString());
		}		
	}

}
