package suncertify.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Handles the locking and realeasing of record numbers.
 * This class's methods are only intended to be called by the Data.java class
 * 
 * @author Peter O'Reilly
 * @version 1.0.0
 */
class LockManager {
	
	private static Map<Integer, Data> lockedRecords
			= new HashMap<Integer, Data>();
	
	private static Lock lock = new ReentrantLock();
	private static Condition lockReleased = lock.newCondition();
	
	
	/**
	 * Locks a record. If record is not available waits until the 
	 * thread is notified. Guards against spurious wake ups.
	 * 
	 * @param recNo record number to lock
	 * @param data data Threads instance of Data
	 */
	void lock(int recNo, Data data) {
		lock.lock();

		try {
			while (lockedRecords.containsKey(recNo)) {
				lockReleased.await();
			}
			
			lockedRecords.put(recNo, data);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}		
	}
	
	/**
	 * Allows a thread to unlock a locked record and notifies all waiting 
	 * threads that a lock has been released. The unlock method requires the instance 
	 * of Data to successfully unlock the record to prevent a thread who does not own
	 * the lock from unlocking the record.
	 *  
	 * @param recNo
	 * @param data
	 * 
	 */
	void unlock(int recNo, Data data) {
		lock.lock();
		
		try {
			if (lockedRecords.get(recNo) == data) {
				lockedRecords.remove(recNo);
				lockReleased.signal();
			}
			
		} finally {
			lock.unlock();
		}
		
	}
	
	
	/**
	 * Determines if a record is currently locked. Returns true if the
	 * record is locked, false otherwise.
	 * @param recNo - the record number to check.
	 * @return boolean 	true if another thread has registered 
	 * a lock on the requested recNo
	 */
	boolean isLocked(int recNo) {
		return lockedRecords.containsKey(recNo);
	}


}
