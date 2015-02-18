package suncertify.db;

public class DatabaseConnectionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DatabaseConnectionException(String errMsg) {
		super(errMsg);
	}

}
