/**
 * 
 */
package suncertify.db;

/**
 * @author epetore
 *
 */
public class DatabaseException extends RuntimeException {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseException(){}
	
	public DatabaseException(String message) {
		super(message);
	}
}
