package suncertify.db;

public class LocalDBConnector {
	public static DBMain getConnection(String dbFile) {
		return new Data(dbFile);
	}
}
