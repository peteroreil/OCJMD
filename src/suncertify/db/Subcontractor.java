package suncertify.db;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Subcontractor object used as a transfer object
 * 
 * @author epetore
 * @version 1.0.0 * 
 */


public class Subcontractor implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("suncertify.db.Subcontractor");
	private String subcontractorName;
	private String cityName;
	private List<String> specialities;
	private int employeeCount;
	private int customerId;
	
	public Subcontractor() {
		log.finer("No Arg Constructor for Subcontractor");
	}
}
