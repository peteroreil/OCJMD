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
	//private static String record = new String(new byte[Subcontractor.RECORD_LENGTH]);
	private static RandomAccessFile databaseFile = null;
	private static ReadWriteLock recordNumbersLock = new ReentrantReadWriteLock();
	public static List<String> recordKeys = new ArrayList<String>();
	public static Map<String, Long> recordLocations = new HashMap<String, Long>();


	/**
	 * @param databaseFilePath filepath to database file.
	 * Database constructor.
	 * Creates a new read write RandomAccess file.
	 * Populates what list?
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
	 * @return String array of data read from database file.
	 * @throws RecordNotFoundException
	 */
	public String[] read(int recNo) {
		int index = recNo - 1;
		Long recordLocation = null;
		
		try {
			recordLocation = this.getSubcontractor(index);			
		} catch (IndexOutOfBoundsException e){
			throw new RecordNotFoundException("record not found at index: " + 
					index + "\n" + e.getMessage());
		}
		
		Subcontractor subcontractor = this.retrieveSubcontrator(recordLocation);
		
		if(subcontractor == null) {
			throw new RecordNotFoundException("Record "+ recNo + "not found");
		}
		return subcontractor.toArray();
	}
	
	/**
	 * @param data
	 * @return
	 * @throws DuplicateKeyException
	 */
	public int create(String[] data) {
		validateData(data);
		return persistSubcontractor(data);
	}

	/**
	 * @param data
	 * @return
	 */
	private int persistSubcontractor(String[] data) {
		String newRecordKey = data[0] + data[1];
		
		recordNumbersLock.writeLock().lock();
		
		try {			
			for (String keys : recordKeys) {
				if (keys.equals(newRecordKey)) {
					throw new DuplicateKeyException("Key " + newRecordKey +
							"already exists");
				}
			}
			
			String lastRecordAddedKey = recordKeys.get(recordKeys.size()-1);
			recordKeys.add(newRecordKey);
			Long lastRecordAddedOffset = recordLocations.get(lastRecordAddedKey);
			Long newRecordOffset = lastRecordAddedOffset + Subcontractor.TOTAL_RECORD_LENGTH;
			recordLocations.put(newRecordKey, newRecordOffset);
			
			// now update the database file

			
		} finally {
			recordNumbersLock.writeLock().unlock();
		}
		
		return 0;
	}

	/**
	 * @param data
	 */
	private void validateData(String[] data) {
		if (data.length < (new Subcontractor()).toArray().length) {
			throw new DatabaseException("Insufficient fields passed: " + 
					Arrays.asList(data).toString());
		}
		
	}

	/**
	 * @param recNo
	 * @param data
	 */
	public void update(int recNo, String[] data) {
		// TODO Auto-generated method stub
		
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
	public List<Subcontractor> getSubcontractors(boolean isWriteLocked) {
		List<Subcontractor> subcontractors = new ArrayList<Subcontractor>();
		
		if(isWriteLocked) {
			recordNumbersLock.writeLock().lock();
		}
		
		try {
			long offSetInFile = calculateFileOffset();
			
			for (long locationInFile = offSetInFile; 
					locationInFile < databaseFile.length();
					locationInFile += Subcontractor.TOTAL_RECORD_LENGTH) {
				
				Subcontractor subcontractor = retrieveSubcontrator(locationInFile);

				if (subcontractor != null) {
					if (isWriteLocked) {
						String name = subcontractor.getName();
						String location = subcontractor.getCityName();
						String key = name + location;
						recordKeys.add(key);
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
	 * @return long the length in bytes from zero to where 
	 * the first subcontractor record exists in the db file.
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
	public Long getSubcontractor(int recordNumber) {
		recordNumbersLock.readLock().lock();
		try {
			String recordKey = recordKeys.get(recordNumber);
			Long locationInFile = recordLocations.get(recordKey);
			return locationInFile;
		} finally {
			recordNumbersLock.readLock().unlock();
		}
	}

	/**
	 * @param locationInFile
	 * @return Subcontractor
	 * @throws UnsupportedEncodingException 
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
				throw new DatabaseException("Exception readin record: " + usce.getMessage());
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
	
	
    private static int getValue(final byte [] byteArray) {
        int value = 0;
        final int byteArrayLength = byteArray.length;
 
        for (int i = 0; i < byteArrayLength; i++) {
            final int shift = (byteArrayLength - 1 - i) * 8;
            value += (byteArray[i] & 0x000000FF) << shift;
        }
 
        return value;
    }

}
