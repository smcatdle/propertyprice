/**
 * 
 */
package com.company.propertyprice.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.geo.Coordinate;
import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.PropertySale;

/**
 * @author smcardle
 * 
 */
public class GeoPropertyService {

    private final static Logger logger = Logger
	    .getLogger(GeoPropertyService.class.getName());

    private static GeoPropertyService instance = null;

    private GeoPropertyService() {

    }

    public static GeoPropertyService getInstance() {

	if (instance == null) {
	    instance = new GeoPropertyService();
	}

	return instance;
    }

    public List<PropertySale> getPropertiesWithinViewport(Coordinate topLeft,
	    Coordinate bottomRight) {

	List<PropertySale> properties = null;
	List<PropertySale> convertedProperties = null;
	long startTime = System.currentTimeMillis();

	try {
	    properties = PropertyDAO.getInstance().getPropertiesWithinViewport(
		    topLeft, bottomRight);

	    if (properties.size() > 0)
		logger.log(Level.INFO, "getPropertiesWithinViewport size ["
			+ properties.size() + "]");

	    // Convert from HibernateProxy classes before converting to JSON.
	    convertedProperties = new ArrayList();

	    //TODO: Need to write custom type adaptors for hibernate, see commented code below;
	    for (PropertySale property : properties) {
		PropertySale p = (PropertySale) property;
		Address a = new Address();
		GeoCode g = new GeoCode();
		a.setAddressLine1(p.getAddress().getAddressLine1());
		a.setAddressLine2(p.getAddress().getAddressLine2());
		a.setAddressLine3(p.getAddress().getAddressLine3());
		a.setAddressLine4(p.getAddress().getAddressLine4());
		a.setAddressLine5(p.getAddress().getAddressLine5());
		g.setLatitude(p.getAddress().getGeocode().getLatitude());
		g.setLongitude(p.getAddress().getGeocode().getLongitude());
		g.setDateCreated(p.getAddress().getGeocode().getDateCreated());
		g.setDateUpdated(p.getAddress().getGeocode().getDateUpdated());
		g.setPartialMatch(p.getAddress().getGeocode().isPartialMatch());
		g.setResults(p.getAddress().getGeocode().getResults());
		g.setStatus(p.getAddress().getGeocode().getStatus());
		g.setType(p.getAddress().getGeocode().getType());
		g.setFormattedAddress(p.getAddress().getGeocode()
			.getFormattedAddress());

		a.setGeocode(g);
		p.setAddress(a);
		convertedProperties.add(p);

	    }

	    long endTime = System.currentTimeMillis();
	    logger.log(Level.INFO, "Service time is : ["
		    + (endTime - startTime) + "] milliseconds.");

	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    ex.getStackTrace();
	} finally {
	    properties = null;
	}

	return convertedProperties;
    }

    public List<PropertySale> getPropertiesByDateWithinViewport(
	    Coordinate topLeft, Coordinate bottomRight, Date dateFrom,
	    Date dateTo) {

	String jsonString = null;
	List<PropertySale> properties = null;
	List<PropertySale> convertedProperties = null;
	long startTime = System.currentTimeMillis();

	try {
	    properties = PropertyDAO.getInstance()
		    .getPropertiesByDateWithinViewport(topLeft, bottomRight,
			    dateFrom, dateTo);

	    // Convert from HibernateProxy classes before converting to JSON.
	    convertedProperties = new ArrayList();

	    //TODO: Need to write custom type adaptors for hibernate, see commented code below;
	    for (PropertySale property : properties) {
		PropertySale p = (PropertySale) property;
		Address a = new Address();
		GeoCode g = new GeoCode();

		a.setAddressLine1(p.getAddress().getAddressLine1());
		a.setAddressLine2(p.getAddress().getAddressLine2());
		a.setAddressLine3(p.getAddress().getAddressLine3());
		a.setAddressLine4(p.getAddress().getAddressLine4());
		a.setAddressLine5(p.getAddress().getAddressLine5());

		g.setLatitude(p.getAddress().getGeocode().getLatitude());
		g.setLongitude(p.getAddress().getGeocode().getLongitude());
		g.setDateCreated(p.getAddress().getGeocode().getDateCreated());
		g.setDateUpdated(p.getAddress().getGeocode().getDateUpdated());
		g.setPartialMatch(p.getAddress().getGeocode().isPartialMatch());
		g.setResults(p.getAddress().getGeocode().getResults());
		g.setStatus(p.getAddress().getGeocode().getStatus());
		g.setType(p.getAddress().getGeocode().getType());

		a.setGeocode(g);
		p.setAddress(a);

	    }

	    long endTime = System.currentTimeMillis();
	    logger.log(Level.INFO, "Service time is : ["
		    + (endTime - startTime) + "] milliseconds.");

	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    ex.getStackTrace();
	} finally {
	    properties = null;
	    convertedProperties = null;
	    System.gc();
	}

	return convertedProperties;
    }
    
    public List<PropertySale> getPropertyByUUID(String uuid) {

	List<PropertySale> properties = null;
	List<PropertySale> convertedProperties = null;
	long startTime = System.currentTimeMillis();

	try {
	    properties = PropertyDAO.getInstance().getPropertyByUUID(
		    uuid);

	    if (properties.size() > 0)
		logger.log(Level.INFO, "getPropertyByUUID size ["
			+ properties.size() + "]");

	    // Convert from HibernateProxy classes before converting to JSON.
	    convertedProperties = new ArrayList();

	    //TODO: Need to write custom type adaptors for hibernate, see commented code below;
	    for (PropertySale property : properties) {
		PropertySale p = (PropertySale) property;
		Address a = new Address();
		GeoCode g = new GeoCode();
		a.setAddressLine1(p.getAddress().getAddressLine1());
		a.setAddressLine2(p.getAddress().getAddressLine2());
		a.setAddressLine3(p.getAddress().getAddressLine3());
		a.setAddressLine4(p.getAddress().getAddressLine4());
		a.setAddressLine5(p.getAddress().getAddressLine5());
		g.setLatitude(p.getAddress().getGeocode().getLatitude());
		g.setLongitude(p.getAddress().getGeocode().getLongitude());
		g.setDateCreated(p.getAddress().getGeocode().getDateCreated());
		g.setDateUpdated(p.getAddress().getGeocode().getDateUpdated());
		g.setPartialMatch(p.getAddress().getGeocode().isPartialMatch());
		g.setResults(p.getAddress().getGeocode().getResults());
		g.setStatus(p.getAddress().getGeocode().getStatus());
		g.setType(p.getAddress().getGeocode().getType());
		g.setFormattedAddress(p.getAddress().getGeocode()
			.getFormattedAddress());

		a.setGeocode(g);
		p.setAddress(a);
		convertedProperties.add(p);

	    }

	    long endTime = System.currentTimeMillis();
	    logger.log(Level.INFO, "Service time is : ["
		    + (endTime - startTime) + "] milliseconds.");

	} catch (Exception ex) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    ex.getStackTrace();
	} finally {
	    properties = null;
	}

	return convertedProperties;
    }
    

    /**
     * This TypeAdapter unproxies Hibernate proxied objects, and serializes them
     * through the registered (or default) TypeAdapter of the base class.
     */
    /*public class HibernateProxyTypeAdapter extends TypeAdapter<HibernateProxy> {

        public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                return (HibernateProxy.class.isAssignableFrom(type.getRawType()) ? (TypeAdapter<T>) new HibernateProxyTypeAdapter(gson) : null);
            }
        };
        private final Gson context;

        private HibernateProxyTypeAdapter(Gson context) {
            this.context = context;
        }

        @Override
        public HibernateProxy read(JsonReader in) throws IOException {
            throw new UnsupportedOperationException("Not supported");
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public void write(JsonWriter out, HibernateProxy value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            // Retrieve the original (not proxy) class
            Class<?> baseType = Hibernate.getClass(value);
            // Get the TypeAdapter of the original class, to delegate the serialization
            TypeAdapter delegate = context.getAdapter(TypeToken.get(baseType));
            // Get a filled instance of the original class
            Object unproxiedValue = ((HibernateProxy) value).getHibernateLazyInitializer()
                    .getImplementation();
            // Serialize the value
            delegate.write(out, unproxiedValue);
        }
    }*/
}
