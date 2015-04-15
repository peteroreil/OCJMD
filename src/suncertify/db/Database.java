/**
 * 
 */
package suncertify.db;

import java.io.File;
import java.io.FileNotFoundException;
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


public class Database {
	private static final String DATABASE_FILENAME = "db-2x1.db";
	private static final String ENCODING = "US-ASCII";	
    private static final int MAGIC_COOKIE_BYTES = 4;
    private static final int RECORD_LENGTH_BYTES = 4;
    private static final int NUMBER_OF_FIELDS_BYTES = 2;      
    private static final int FIELD_NAME_BYTES_LENGTH = 2;
    private static final int FIELD_LENGTH_BYTES = 2;    
	private static String recordHolder = new String(new byte[Subcontractor.TOTAL_RECORD_LENGTH]);
	private static RandomAccessFile databaseFile = null;
	private static ReadWriteLock recordNumbersLock = new ReentrantReadWriteLock();
	public static Map<Integer, Long> recordLocations = new HashMap<Integer, Long>();


	/**
	 * @param String databaseFilePath absolute path to database file.
	 * @throws DatabaseException
	 */
	public Database(String databaseFilePath) {
		if (databaseFile == null) {
			String databasePath = databaseFilePath + File.separator + DATABASE_FILENAME;
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
	 * @param recNo
	 * @return String array record read from database file
	 * @throws RecordNotFoundException
	 */
	public String[] read(int recNo) {
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
	 * <p>
	 * Writes a new record to databases.
	 * Requires a String[] of length 6 exactly. 
	 * <p>
	 * @param data		the new record to create
	 * @return the newly created record, record number
	 * @throws DuplicateKeyException
	 * @throws IllegalArgumentException
	 */
	public int create(String[] data) {
		validateData(data);
		return persistSubcontractor(data, null);
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
	public void update(int recNo, String[] data) {
		validateData(data);		
		persistSubcontractor(data, recNo);
	}

	/**
	 * Creates or updates a record to the database file.
	 * If an update an Integer recordNumber is required,
	 * if recordNumber is null it is considered a create. 
	 * 
	 * @param String[] data Sub-contractor Data
	 * @param Integer recordNumber Record to update or null
	 * @return the integer index of newly created/updated record
	 * @throws DuplicateKeyException
	 * @throws RecordNotFoundException
	 */
	private int persistSubcontractor(String[] data, Integer recordNumber) {
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
				//update record location key in case of name or location change
				recordLocations.remove(recordKey);
				int newKey = (data[0] + data[1]).hashCode();
				recordLocations.put(newKey, dbOffset);				
			}
			
			final StringBuilder builder = new StringBuilder(recordHolder);

			class RecordWriter {
				int start = 0;
				void write(String data, int length) {					
					builder.replace(start, start + data.length(), data);
					start += length;
				}
			}
			
			RecordWriter writer = new RecordWriter();
			String validFlag = getRecordFlag(true);
			
			writer.write(validFlag, Subcontractor.VALID_RECORD_LENGTH);
			writer.write(data[0], Subcontractor.NAME_LENGTH);
			writer.write(data[1], Subcontractor.LOCATION_LENGTH);
			writer.write(data[2], Subcontractor.SPECIALITIES_LENGTH);
			writer.write(data[3], Subcontractor.SIZE_LENGTH);
			writer.write(data[4], Subcontractor.RATE_LENGTH);
			writer.write(data[5], Subcontractor.OWNER_LENGTH);	
			
			synchronized(databaseFile) {
				databaseFile.seek(dbOffset);
				databaseFile.write(builder.toString().getBytes());
			}
			
			
		} catch (IOException ioe) {
			throw new DatabaseException("Exception reading length of file");			
		} finally {
			recordNumbersLock.writeLock().unlock();
		}
		
		return recordKey;
	}

	/**
	 * returns a 0 or 1 valued string byte array
	 * @param b
	 */
	private String getRecordFlag(boolean isValid) {
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
	 * @param recNo
	 */
	public void delete(int recNo) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param criteria
	 * @return
	 */
	public int[] find(String[] criteria) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * @param recNo
	 */
	public void lock(int recNo) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param recNo
	 */
	public void unlock(int recNo) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param recNo
	 * @return
	 */
	public boolean isLocked(int recNo) {
		return false;
	}

	
	/**
	 * @return list of subcontractors from database file
	 * @param boolean isWriteLocked
	 * 
	 * if is writeLocked is true will obtain a write lock on the record
	 * numbers map and populate/update it.
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
	
	public List<Subcontractor> getSubcontractors() {
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
	 * @param locationInFile
	 * @return Subcontractor
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
