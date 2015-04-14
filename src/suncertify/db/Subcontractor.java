package suncertify.db;

import java.io.Serializable;

/**
 * Subcontractor class used as a transfer object
 * 
 * @author Peter O'Reilly
 * @version 1.0.0  
 */


public class Subcontractor implements Serializable{

	public static final int RECORD_LENGTH = 182;
    public static final int VALID_RECORD_LENGTH = 1; 
    public static final int TOTAL_RECORD_LENGTH = RECORD_LENGTH + VALID_RECORD_LENGTH;    
    public static final int NAME_BYTES_LENGTH = 4;
	public static final int NAME_LENGTH = 32;
	public static final int LOCATION_BYTES_LENGTH = 8;
	public static final int LOCATION_LENGTH = 64;
	public static final int SPECIALITIES_BYTES_LENGTH = 11;
	public static final int SPECIALITIES_LENGTH = 64;
	public static final int SIZE_BYTES_LENGTH = 4;
	public static final int SIZE_LENGTH = 6;
	public static final int RATE_BYTES_LENGTH = 4;
	public static final int RATE_LENGTH = 8;
	public static final int OWNER_BYTES_LENGTH = 5;
	public static final int OWNER_LENGTH = 8;
	
	private static final long serialVersionUID = 1L;
	private String specialities;
	private String name;
	private String cityName;
	private String hourlyRate;
	private String customerId;
	private String employeeCount;
	private Integer recordNumber;
	
	
	public Subcontractor() {}

	/**
	 * Creates an instance of the Subcontractor class with the specified parameters
	 * 
	 * @param subcontractorName The name of the sub-contractor
	 * @param cityName The name of the city the sub-contractor operates
	 * @param specialities A List of the types of work offered
	 * @param employeeCount The number of workers available when record is booked
	 */
	public Subcontractor(String subcontractorName, String cityName,
			String hourlyRate, String specialities, String employeeCount, String customerId) {
		this.setName(subcontractorName);
		this.setCityName(cityName); 
		this.setHourlyRate(hourlyRate);
		this.setSpecialities(specialities);
		this.setEmployeeCount(employeeCount);
		this.customerId = customerId;
	}

	/**
	 * @return the Subcontractor's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param subcontractorName sets the Subcontractor's name
	 */
	public void setName(String subcontractorName) {
		if(subcontractorName == null) {
			throw new IllegalArgumentException("You must provide a valid Subcontractor Name " + null);
		}
		this.name = subcontractorName;
	}

	/**
	 * @return the city name that the Subcontractor operates in
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName sets the city name that the Subcontractor operates in
	 */
	public void setCityName(String cityName) {
		if(cityName == null) {
			throw new IllegalArgumentException("You must provide a valid City Name "+ cityName);
		}
		this.cityName = cityName;
	}

	/**
	 * @return the hourly rate including the currency symbol
	 */
	public String getHourlyRate() {
		return hourlyRate;
	}

	/**
	 * @param hourlyRate sets the hourly rate of the Subcontractor
	 */
	public void setHourlyRate(String hourlyRate) {
		if(hourlyRate == null) {
			throw new IllegalArgumentException("You Must provide an hourly rate " + hourlyRate);
		}
		this.hourlyRate = hourlyRate;
	}

	/**
	 * @return a list of the specialties offered by a Subcontractor
	 */
	public String getSpecialities() {
		return specialities;
	}

	/**
	 * @param specialities sets the List of specialties
	 */
	public void setSpecialities(String specialities) {
		if(specialities == null || specialities.length() < 1) {
			throw new IllegalArgumentException("No Specialites in parameters setSpecialities");
		}
		this.specialities = specialities;
	}

	/**
	 * @return the number of employees currently available to the Subcontractor
	 */
	public String getEmployeeCount() {
		return employeeCount;		
	}

	/**
	 * @param employeeCount sets the number of employees available to the Subcontractor. 
	 * @throws IllegalArgumentException if the emplyee count is less than 0.
	 */
	public void setEmployeeCount(String employeeCount) {
		int count = 0;
		try {
			count = Integer.parseInt(employeeCount);
			if (count < 0) {
				throw new IllegalArgumentException("Employee count cannot be less than 0");
			}
			this.employeeCount = employeeCount;
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Employee count must be numeric integer of type String");
		}
	}

	/**
	 * @return the 8-digit customer ID who currently has booked this Subcontractor
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId sets the customer ID. Takes an 8-Digit String.
	 * @throws IllegalArgumentException if the param is less or greater than 8 digits
	 */
	public void setCustomerId(String customerId) throws IllegalArgumentException{
		try {
			Integer.parseInt(customerId);
			
			if (customerId.length() != 8) {
				throw new RuntimeException();
			}
			
			this.customerId = customerId;
			
		} catch (RuntimeException nfe) {
			String errMessage = "Customer ID must be 8 Digits. ID="+customerId;
			throw new IllegalArgumentException(errMessage); 			
		} 		
	}
	
	public Integer getRecordNumber() {
		return this.recordNumber;
	}
	
	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}
	
	/**
	 * Compares two Subcontractor objects on String <b>name</b> and String <b>cityName</b>
	 * @return <b>true</b> if both are equal
	 */
	@Override
	public boolean equals(Object subcontractor) {
		if(!(subcontractor instanceof Subcontractor)) {
			return false;
		}				
		Subcontractor otherSub = (Subcontractor) subcontractor;		
		String otherSubName = otherSub.getName();
		String otherSubCity = otherSub.getCityName();	
		
		boolean isNameEqual = (this.name == null) 
				? (otherSubName == null) 
				: this.name.equals(otherSubName);
				
		boolean isCityEqual = (this.cityName == null) 
				? (otherSubCity == null) 
				: this.cityName.equals(otherSubCity);	
				
		boolean areEqual = isNameEqual && isCityEqual;
		return areEqual;
	}
	
	
	@Override
	public int hashCode() {
		return (this.name+this.cityName).hashCode();
	}
	
	public String[] toArray() {
		return new String[] {
					this.name, 
					this.cityName,
					this.specialities, 
					this.employeeCount, 
					this.hourlyRate, 
					this.customerId
				};		
	}
	
}
