package com.company.propertyprice.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Geocode
 */
public class GeoCode implements java.io.Serializable {

	public static final double GEO_PERIMETER = 0.0005;
	// TODO: Use a broader perimeter to identify if addreses are in the same
	// region
	public static final double GEO_BROAD_PERIMETER = 0.005;

	public static final int GEO_CROSS_CHECK_CODE_FULL_CONSENSUS = 1;
	public static final int GEO_CROSS_CHECK_CODE_MYHOME_OSI_CONSENSUS = 2;
	public static final int GEO_CROSS_CHECK_CODE_OSI_CONSENSUS = 3;
	public static final int GEO_CROSS_CHECK_CODE_MYHOME_CONSENSUS = 4;
	public static final int GEO_CROSS_CHECK_CODE_MYHOME_OSI_CONSENSUS_2 = 5;
	public static final int GEO_CROSS_CHECK_CODE_MYHOME_RECOM = 6;
	public static final int GEO_CROSS_CHECK_CODE_OSI_RECOM = 7;
	public static final int GEO_CROSS_CHECK_CODE_GOOGLE_RECOM = 8;
	public static final int GEO_CROSS_CHECK_CODE_DEFAULT = 9;
	public static final int GEO_CROSS_CHECK_CODE_ERROR = -1;

	public static final String GEO_LOCATION_TYPE_ROOFTOP = "ROOFTOP";
	public static final String GEO_LOCATION_TYPE_RANGE_INTERPOLATED = "RANGE_INTERPOLATED";
	public static final String GEO_LOCATION_TYPE_GEOMETRIC_CENTER = "GEOMETRIC_CENTER";
	public static final String GEO_LOCATION_TYPE_APPROXIMATE = "APPROXIMATE";

	public static final String GEO_LOCATION_TYPE_CODE_ROOFTOP = "R";
	public static final String GEO_LOCATION_TYPE_CODE_RANGE_INTERPOLATED = "I";
	public static final String GEO_LOCATION_TYPE_CODE_GEOMETRIC_CENTER = "G";
	public static final String GEO_LOCATION_TYPE_CODE_APPROXIMATE = "A";

	public static final String GEO_STATUS_OK = "OK";
	public static final String GEO_STATUS_ZERO_RESULTS = "ZERO_RESULTS";
	public static final String GEO_STATUS_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";
	public static final String GEO_STATUS_REQUEST_DENIED = "REQUEST_DENIED";
	public static final String GEO_STATUS_INVALID_REQUEST = "INVALID_REQUEST";
	public static final String GEO_STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR";

	public static final String GEO_STATUS_CODE_OK = "O";
	public static final String GEO_STATUS_CODE_ZERO_RESULTS = "Z";
	public static final String GEO_STATUS_CODE_OVER_QUERY_LIMIT = "L";
	public static final String GEO_STATUS_CODE_REQUEST_DENIED = "D";
	public static final String GEO_STATUS_CODE_INVALID_REQUEST = "I";
	public static final String GEO_STATUS_CODE_UNKNOWN_ERROR = "U";

	/**
	 * street_address indicates a precise street address. route indicates a
	 * named route (such as "US 101"). intersection indicates a major
	 * intersection, usually of two major roads. political indicates a political
	 * entity. Usually, this type indicates a polygon of some civil
	 * administration. country indicates the national political entity, and is
	 * typically the highest order type returned by the Geocoder.
	 * administrative_area_level_1 indicates a first-order civil entity below
	 * the country level. Within the United States, these administrative levels
	 * are states. Not all nations exhibit these administrative levels.
	 * administrative_area_level_2 indicates a second-order civil entity below
	 * the country level. Within the United States, these administrative levels
	 * are counties. Not all nations exhibit these administrative levels.
	 * administrative_area_level_3 indicates a third-order civil entity below
	 * the country level. This type indicates a minor civil division. Not all
	 * nations exhibit these administrative levels. administrative_area_level_4
	 * indicates a fourth-order civil entity below the country level. This type
	 * indicates a minor civil division. Not all nations exhibit these
	 * administrative levels. administrative_area_level_5 indicates a
	 * fifth-order civil entity below the country level. This type indicates a
	 * minor civil division. Not all nations exhibit these administrative
	 * levels. colloquial_area indicates a commonly-used alternative name for
	 * the entity. locality indicates an incorporated city or town political
	 * entity. sublocality indicates a first-order civil entity below a
	 * locality. For some locations may receive one of the additional types:
	 * sublocality_level_1 to sublocality_level_5. Each sublocality level is a
	 * civil entity. Larger numbers indicate a smaller geographic area.
	 * neighborhood indicates a named neighborhood premise indicates a named
	 * location, usually a building or collection of buildings with a common
	 * name subpremise indicates a first-order entity below a named location,
	 * usually a singular building within a collection of buildings with a
	 * common name postal_code indicates a postal code as used to address postal
	 * mail within the country. natural_feature indicates a prominent natural
	 * feature. airport indicates an airport. park indicates a named park.
	 * point_of_interest indicates a named point of interest. Typically, these
	 * "POI"s are prominent local entities that don't easily fit in another
	 * category, such as "Empire State Building" or "Statue of Liberty."
	 */

	public static final String GEO_TYPE_STREET_ADDRESS = "street_address";
	public static final String GEO_TYPE_ROUTE = "route";
	public static final String GEO_TYPE_INTERSECTION = "intersection";
	public static final String GEO_TYPE_POLITICAL = "political";
	public static final String GEO_TYPE_COUNTRY = "country";
	public static final String GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";
	public static final String GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";
	public static final String GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_3 = "administrative_area_level_3";
	public static final String GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_4 = "administrative_area_level_4";
	public static final String GEO_TYPE_ADMINISTRATIVE_AREA_LEVEL_5 = "administrative_area_level_5";
	public static final String GEO_TYPE_COLLOQUIAL_AREA = "colloquial_area";
	public static final String GEO_TYPE_LOCALITY = "locality";
	public static final String GEO_TYPE_SUBLOCALITY = "sublocality";
	public static final String GEO_TYPE_SUBLOCALITY_LEVEL_1 = "sublocality_level_1";
	public static final String GEO_TYPE_SUBLOCALITY_LEVEL_2 = "sublocality_level_2";
	public static final String GEO_TYPE_SUBLOCALITY_LEVEL_3 = "sublocality_level_3";
	public static final String GEO_TYPE_SUBLOCALITY_LEVEL_4 = "sublocality_level_4";
	public static final String GEO_TYPE_SUBLOCALITY_LEVEL_5 = "sublocality_level_5";
	public static final String GEO_TYPE_NEIGHBORHOOD = "neighborhood";
	public static final String GEO_TYPE_PREMISE = "premise";
	public static final String GEO_TYPE_SUBPREMISE = "subpremise";
	public static final String GEO_TYPE_POSTAL_CODE = "postal_code";
	public static final String GEO_TYPE_NATURAL_FEATURE = "natural_feature";
	public static final String GEO_TYPE_AIRPORT = "airport";
	public static final String GEO_TYPE_PARK = "park";
	public static final String GEO_TYPE_POINT_OF_INTEREST = "point_of_interest";

	public static final String GEO_TYPE_CODE_STREET_ADDRESS = "A";
	public static final String GEO_TYPE_CODE_ROUTE = "B";
	public static final String GEO_TYPE_CODE_INTERSECTION = "C";
	public static final String GEO_TYPE_CODE_POLITICAL = "D";
	public static final String GEO_TYPE_CODE_COUNTRY = "E";
	public static final String GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_1 = "F";
	public static final String GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_2 = "G";
	public static final String GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_3 = "H";
	public static final String GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_4 = "I";
	public static final String GEO_TYPE_CODE_ADMINISTRATIVE_AREA_LEVEL_5 = "J";
	public static final String GEO_TYPE_CODE_COLLOQUIAL_AREA = "K";
	public static final String GEO_TYPE_CODE_LOCALITY = "L";
	public static final String GEO_TYPE_CODE_SUBLOCALITY = "M";
	public static final String GEO_TYPE_CODE_SUBLOCALITY_LEVEL_1 = "N";
	public static final String GEO_TYPE_CODE_SUBLOCALITY_LEVEL_2 = "O";
	public static final String GEO_TYPE_CODE_SUBLOCALITY_LEVEL_3 = "P";
	public static final String GEO_TYPE_CODE_SUBLOCALITY_LEVEL_4 = "Q";
	public static final String GEO_TYPE_CODE_SUBLOCALITY_LEVEL_5 = "R";
	public static final String GEO_TYPE_CODE_NEIGHBORHOOD = "S";
	public static final String GEO_TYPE_CODE_PREMISE = "T";
	public static final String GEO_TYPE_CODE_SUBPREMISE = "U";
	public static final String GEO_TYPE_CODE_POSTAL_CODE = "V";
	public static final String GEO_TYPE_CODE_NATURAL_FEATURE = "W";
	public static final String GEO_TYPE_CODE_AIRPORT = "X";
	public static final String GEO_TYPE_CODE_PARK = "Y";
	public static final String GEO_TYPE_CODE_POINT_OF_INTEREST = "Z";

	public static final String GEO_SYSTEM_TYPE_MY_HOME = "M";
	public static final String GEO_SYSTEM_TYPE_GOOGLE = "G";
	public static final String GEO_SYSTEM_TYPE_OSI = "O";

	private int id;
	private int gridId;
	private double latitude;
	private double longitude;
	private double latitudeBck;
	private double longitudeBck;
	private int crossCheckCode;
	private String crossCheckType;
	private String geocodeCurType;
	private String geocodeBckType;
	private String formattedAddress;
	private String formattedAddressBck;
	private String status;
	private String locationType;
	private boolean partialMatch;
	private int results;
	private String type;
	private Date dateCreated;
	private Date dateUpdated;
	private Set<Address> addresses = new HashSet<Address>(0);

	public GeoCode() {
	}

	public GeoCode(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getGridId() {
		return gridId;
	}

	public void setGridId(int gridId) {
		this.gridId = gridId;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the formattedAddress
	 */
	public String getFormattedAddress() {
		return formattedAddress;
	}

	/**
	 * @param formattedAddress
	 *            the formattedAddress to set
	 */
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * @param locationType
	 *            the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return the partialMatch
	 */
	public boolean isPartialMatch() {
		return partialMatch;
	}

	/**
	 * @param partialMatch
	 *            the partialMatch to set
	 */
	public void setPartialMatch(boolean partialMatch) {
		this.partialMatch = partialMatch;
	}

	/**
	 * @return the results
	 */
	public int getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(int results) {
		this.results = results;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the dateUpdated
	 */
	public Date getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * @param dateUpdated
	 *            the dateUpdated to set
	 */
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	/**
	 * @return the addresses
	 */
	public Set<Address> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses
	 *            the addresses to set
	 */
	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the latitudeBck
	 */
	public double getLatitudeBck() {
		return latitudeBck;
	}

	/**
	 * @return the crossCheckCode
	 */
	public int getCrossCheckCode() {
		return crossCheckCode;
	}

	/**
	 * @param crossCheckCode
	 *            the crossCheckCode to set
	 */
	public void setCrossCheckCode(int crossCheckCode) {
		this.crossCheckCode = crossCheckCode;
	}

	/**
	 * @return the crossCheckType
	 */
	public String getCrossCheckType() {
		return crossCheckType;
	}

	/**
	 * @param crossCheckType
	 *            the crossCheckType to set
	 */
	public void setCrossCheckType(String crossCheckType) {
		this.crossCheckType = crossCheckType;
	}

	/**
	 * @param latitudeBck
	 *            the latitudeBck to set
	 */
	public void setLatitudeBck(double latitudeBck) {
		this.latitudeBck = latitudeBck;
	}

	/**
	 * @return the longitudeBck
	 */
	public double getLongitudeBck() {
		return longitudeBck;
	}

	/**
	 * @param longitudeBck
	 *            the longitudeBck to set
	 */
	public void setLongitudeBck(double longitudeBck) {
		this.longitudeBck = longitudeBck;
	}

	/**
	 * @return the geocodeCurType
	 */
	public String getGeocodeCurType() {
		return geocodeCurType;
	}

	/**
	 * @param geocodeCurType
	 *            the geocodeCurType to set
	 */
	public void setGeocodeCurType(String geocodeCurType) {
		this.geocodeCurType = geocodeCurType;
	}

	/**
	 * @return the geocodeBckType
	 */
	public String getGeocodeBckType() {
		return geocodeBckType;
	}

	/**
	 * @return the formattedAddressBck
	 */
	public String getFormattedAddressBck() {
		return formattedAddressBck;
	}

	/**
	 * @param formattedAddressBck
	 *            the formattedAddressBck to set
	 */
	public void setFormattedAddressBck(String formattedAddressBck) {
		this.formattedAddressBck = formattedAddressBck;
	}

	/**
	 * @param geocodeBckType
	 *            the geocodeBckType to set
	 */
	public void setGeocodeBckType(String geocodeBckType) {
		this.geocodeBckType = geocodeBckType;
	}

	private double mangle(double data) {
		double offset = (data / 100);
		offset = (offset / .7);
		return offset;
	}

	private double unMangle(double data) {
		double offset = (data * .7);
		return offset * 100;
	}

	// Check a number of parameters to determine if Google geocode is reliable
	public boolean isGoogleGeocodeReliable() {
		boolean isReliable = true;

		// 1. Check lat/lng not 0
		if (this.getLatitude() == 0 || this.getLongitude() == 0)
			isReliable = false;

		// 2. Check geocode type not a regional area
		if (GeoCode.GEO_TYPE_CODE_LOCALITY.equals(this.getType())
				|| GeoCode.GEO_TYPE_CODE_SUBLOCALITY_LEVEL_1.equals(this
						.getType()))
			isReliable = false;

		return isReliable;
	}

	public boolean isMyHomeConsensus(GeoCode myHomeGeoCode) {
		
		// Determine if My Home consensus
		if (myHomeGeoCode != null
				&& (Math.abs(this.getLatitude() - myHomeGeoCode.getLatitude()) < GEO_BROAD_PERIMETER && Math
						.abs(this.getLongitude() - myHomeGeoCode.getLongitude()) < GEO_BROAD_PERIMETER)) {
			return true;
		}
		
		return false;
	}
	
	public int checkIfGeoCodeMatch(GeoCode osiGeoCode, GeoCode myHomeGeoCode) {

		boolean osiConsensus = false;
		boolean myHomeConsensus = false;
		boolean myHomeOSIConsensus = false;

		/*
		 * If there is not a consensus from all three,
		 * 
		 * From observation,
		 * 
		 * - OSI most accurate if correct address retrieved. (Not accurate when
		 * string comparison fails or unusual address) - Google accurate when
		 * address not geometric or neighbourhood. Accurate when exact match at
		 * first lookup attempt. - MyHome not as accurate. But is most likely to
		 * give a neighbourhood result, especially for unusual addresses.
		 * Address can then be fed into OSI.
		 * 
		 * - Possibly include another round based on info from first round. This
		 * round can be more adventurous.
		 */

		// Determine OSI consensus
		if (osiGeoCode != null
				&& (Math.abs(this.getLatitude() - osiGeoCode.getLatitude()) < GEO_PERIMETER && Math
						.abs(this.getLongitude() - osiGeoCode.getLongitude()) < GEO_PERIMETER) && !osiGeoCode.isGeoCentricLocation()) {
			osiConsensus = true;
		}

		// Determine My Home consensus
		if (myHomeGeoCode != null
				&& (Math.abs(this.getLatitude() - myHomeGeoCode.getLatitude()) < GEO_PERIMETER && Math
						.abs(this.getLongitude() - myHomeGeoCode.getLongitude()) < GEO_PERIMETER) && !myHomeGeoCode.isGeoCentricLocation()) {
			myHomeConsensus = true;
		}

		// Determine My Home/OSI consensus
		if ((myHomeGeoCode != null && osiGeoCode != null)
				&& Math.abs(osiGeoCode.getLatitude()
						- myHomeGeoCode.getLatitude()) < GEO_PERIMETER
				&& Math.abs(osiGeoCode.getLongitude()
						- myHomeGeoCode.getLongitude()) < GEO_PERIMETER && !myHomeGeoCode.isGeoCentricLocation()) {
			myHomeOSIConsensus = true;
		}

		if (osiConsensus && myHomeConsensus && myHomeOSIConsensus
				&& this.isGoogleGeocodeReliable()) { // Need to exclude
			// 'Geometric' geocodes
			return GeoCode.GEO_CROSS_CHECK_CODE_GOOGLE_RECOM;
		}

		// Include case where OSI and My Home match (but not Google)
		if (myHomeOSIConsensus && !this.isGoogleGeocodeReliable()) { // Google
																		// geocode
																		// has
																		// failed
																		// for
																		// some
																		// reason
			return GeoCode.GEO_CROSS_CHECK_CODE_MYHOME_OSI_CONSENSUS;
		}

		if (osiConsensus && this.isGoogleGeocodeReliable()) {
			return GeoCode.GEO_CROSS_CHECK_CODE_OSI_CONSENSUS;
		}

		if (myHomeConsensus && this.isGoogleGeocodeReliable()) {
			return GeoCode.GEO_CROSS_CHECK_CODE_MYHOME_CONSENSUS;
		}

		/*if (myHomeOSIConsensus) {
			return GeoCode.GEO_CROSS_CHECK_CODE_MYHOME_OSI_CONSENSUS_2;
		}*/


		// Use OSI if not partial match (i.e. house number has been matched)
		if (osiGeoCode != null && osiGeoCode.getLatitude() != 0
				&& osiGeoCode.getLongitude() != 0 && !osiGeoCode.isPartialMatch()) {
			
			return GeoCode.GEO_CROSS_CHECK_CODE_OSI_RECOM;
		}
		
		// Ignore My Home geocode if lat or lng is '0', or a geometric centric location  i.e. O'Connell St. 
		if (myHomeGeoCode != null && myHomeGeoCode.getLatitude() != 0
				&& myHomeGeoCode.getLongitude() != 0 && !myHomeGeoCode.isGeoCentricLocation()) {
			return GeoCode.GEO_CROSS_CHECK_CODE_MYHOME_RECOM;
		}
		
		// Ignore OSI geocode if not top result 
		if (osiGeoCode != null && osiGeoCode.getLatitude() != 0
				&& osiGeoCode.getLongitude() != 0) {
			
			if (!this.formattedAddress.substring(0,10).matches(".*[0-9]+.*")) {
				return GeoCode.GEO_CROSS_CHECK_CODE_OSI_RECOM;
			}
		}

		if (this.isGoogleGeocodeReliable()) {
			return GeoCode.GEO_CROSS_CHECK_CODE_GOOGLE_RECOM;
		}

		// Else return google geocode as default
		return GeoCode.GEO_CROSS_CHECK_CODE_DEFAULT;
	}

	
	public boolean isGeoCentricLocation() {
		
		// O'Connell St, Dublin
		return ((Math.abs(this.latitude-53.3498053) < 0.001) && (Math.abs(-this.longitude-6.2603097) < 0.001));

	}
}
