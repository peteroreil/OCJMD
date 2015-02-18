package suncertify.db;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Subcontractor class used as a transfer object
 * 
 * @author epetore
 * @version 1.0.0 * 
 */


public class Subcontractor implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger("suncertify.db.Subcontractor");
	private List<String> specialities;
	private String name;
	private String cityName;
	private String hourlyRate;
	private String customerId;
	private int employeeCount;
	
	
	public Subcontractor() {
		log.finer("No Arg Constructor for Subcontractor");
	}

	/**
	 * Creates an instance of the Subcontractor class with the specified parameters
	 * 
	 * @param subcontractorName The name of the sub-contractor
	 * @param cityName The name of the city the sub-contractor operates
	 * @param specialities A List of the types of work offered
	 * @param employeeCount The number of workers available when record is booked
	 */
	public Subcontractor(String subcontractorName, String cityName,
			String hourlyRate, List<String> specialities, int employeeCount) {
		log.entering("Subcontractor", "Subcontractor", new Object[]{subcontractorName, 
				cityName, hourlyRate, specialities, employeeCount});
		this.name = subcontractorName;
		this.cityName = cityName;
		this.hourlyRate = hourlyRate;
		this.specialities = specialities;
		this.employeeCount = employeeCount;
		log.exiting("Subcontractor", "Subcontractor");
	}

	/**
	 * @return the Subcontractor's name
	 */
	public String getName() {
		log.entering("Subcontractor", "getSubcontractorName");
		log.exiting("Subcontractor", "getSubcontractorName", name);
		return name;
	}

	/**
	 * @param subcontractorName sets the Subcontractor's name
	 */
	public void setName(String subcontractorName) {
		log.entering("Subcontractor", "setSubcontractorName", subcontractorName);
		this.name = subcontractorName;
		log.exiting("Subcontractor", "setSubcontractorName");
	}

	/**
	 * @return the city name that the Subcontractor operates in
	 */
	public String getCityName() {
		log.entering("Subcontractor", "getCityName");
		log.exiting("Subcontractor", "getCityName", cityName);
		return cityName;
	}

	/**
	 * @param cityName sets the city name that the Subcontractor operates in
	 */
	public void setCityName(String cityName) {
		log.entering("Subcontractor", "setCityName", cityName);
		this.cityName = cityName;
		log.exiting("Subcontractor", "setCityName");
	}

	/**
	 * @return the hourly rate including the currency symbol
	 */
	public String getHourlyRate() {
		log.entering("Subcontractor", "getHourlyRate");
		log.exiting("Subcontractor", "getHourlyRate", hourlyRate);
		return hourlyRate;
	}

	/**
	 * @param hourlyRate sets the hourly rate of the Subcontractor
	 */
	public void setHourlyRate(String hourlyRate) {
		log.entering("Subcontractor", "setHourlyRate", hourlyRate);
		this.hourlyRate = hourlyRate;
		log.exiting("Subcontractor", "setHourlyRate");
	}

	/**
	 * @return a list of the specialties offered by a Subcontractor
	 */
	public List<String> getSpecialities() {
		log.entering("Subcontractor", "getSpecialities");
		log.exiting("Subcontractor", "getSpecialities", specialities);
		return specialities;
	}

	/**
	 * @param specialities sets the List of specialties
	 */
	public void setSpecialities(List<String> specialities) {
		log.entering("Subcontractor", "setSpecialities", specialities);
		this.specialities = specialities;
		log.exiting("Subcontractor", "setSpecialities");
	}

	/**
	 * @return the number of employees currently available to the Subcontractor
	 */
	public int getEmployeeCount() {
		log.entering("Subcontractor", "getEmployeeCount");
		log.exiting("Subcontractor", "getEmployeeCount", employeeCount);
		return employeeCount;		
	}

	/**
	 * @param employeeCount sets the number of employees available to the Subcontractor. 
	 * @throws IllegalArgumentException if the emplyee count is less than 0.
	 */
	public void setEmployeeCount(int employeeCount) {
		log.entering("Subcontractor", "setEmployeeCount", employeeCount);
		if (employeeCount < 0) {
			log.log(Level.SEVERE, "The employee count is incorrect: "+employeeCount);
			throw new IllegalArgumentException("Employee count cannot be less than 0");
		}
		this.employeeCount = employeeCount;
		log.exiting("Subcontractor", "setEmployeeCount");
	}

	/**
	 * @return the 8-digit customer ID who currently has booked this Subcontractor
	 */
	public String getCustomerId() {
		log.entering("Subcontractor", "getCustomerId");
		log.exiting("Subcontractor", "getCustomerId", customerId);
		return customerId;
	}

	/**
	 * @param customerId sets the customer ID. Takes an 8-Digit int.
	 * @throws IllegalArgumentException if the param is less or greater than 8 digits
	 */
	public void setCustomerId(String customerId) throws IllegalArgumentException{
		log.entering("Subcontractor", "setCustomerId", customerId);		
		try {
			Integer.parseInt(customerId);
			
			if (customerId.length() != 8) {
				throw new RuntimeException();
			}
			
			this.customerId = customerId;
			
		} catch (RuntimeException nfe) {
			String errMessage = "Customer ID must be 8 Digits. ID="+customerId;
			log.log(Level.SEVERE, errMessage);
			throw new IllegalArgumentException(errMessage); 
			
		} finally {
			log.exiting("Subcontractor", "setCustomerId");
		}		
	}
	
	/**
	 * Compares two Subcontractor objects on {@name} and {@cityName}
	 * @return true if both are equal
	 */
	@Override
	public boolean equals(Object subcontractor) {
		log.entering("Subcontractor", "equals", subcontractor);	
		if(!(subcontractor instanceof Subcontractor)) {
			return false;
		}				
		Subcontractor otherSub = (Subcontractor) subcontractor;		
		String otherSubName = otherSub.getName();
		String otherSubCity = otherSub.getCityName();		
		boolean isNameEqual = (this.name == null) ? (otherSubName == null) : this.name.equals(otherSubName);
		boolean isCityEqual = (this.cityName == null) ? (otherSubCity == null) : this.cityName.equals(otherSubCity);				
		boolean areEqual = isNameEqual && isCityEqual;
		log.exiting("Subcontractor", "equals", areEqual);	
		
		return areEqual;
	}
	
	
	@Override
	public int hashCode() {
		return (this.name+this.cityName).hashCode();
	}
	
}
