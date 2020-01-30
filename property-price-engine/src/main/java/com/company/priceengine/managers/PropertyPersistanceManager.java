/**
 * 
 */
package com.company.priceengine.managers;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.company.propertyprice.dao.PropertyDAO;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;
import com.company.propertyprice.model.PropertySale;

/**
 * @author smcardle
 * 
 */
public class PropertyPersistanceManager {

	private final Logger logger = Logger
			.getLogger(PropertyPersistanceManager.class.getName());

	public void persist(PropertySale sale) {

		Address address = sale.getAddress();
		GeoCode geocode = address.getGeocode();

		try {

			if (geocode.getLatitude() == 0 || geocode.getLongitude() == 0) {

				logger.log(Level.INFO,
						"Cannot persist sale [" + sale.getPprUrl()
								+ "] as geocode values are empty.");
			}

			// Store duplicate addresses as normal.

			// TODO: Need to move below code into method and handle slightly
			// different address spellings
			// Check if this sale already exists in the db, if so then an update
			// to the sale/new sale on the PPR site has occurred.

			List<PropertySale> existingSales = PropertyDAO.getInstance()
					.findExistingAddress(address);

			// Duplicate addresses will be flagged with 'U', so that the
			// update history can be displayed to the user.
			for (PropertySale existingSale : existingSales) {

				logger.log(Level.INFO, "Found existing record for this sale ["
						+ existingSale.getPprUrl() + "]");

				existingSale.setDateUpdated(new Date());
				existingSale.setStatus("U");
				logger.log(Level.INFO, "Updating sale with PPR url ["
						+ existingSale.getPprUrl() + "]");

				PropertyDAO.getInstance().update(existingSale);
			}

			// Set the new sale status to 'U' also
			// TODO: Need to find correct hibernate way to not re-use objects.
			sale.setId(0);
			sale.setAddress(address);
			sale.setStatus("U");
			address.setId(0);
			geocode.setId(0);
			geocode.setAddresses(null);

			logger.log(Level.INFO, "Persisting the new sale with PPR url ["
					+ sale.getPprUrl() + "]");

			// Persist the new sale
			PropertyDAO.getInstance().attachDirty(sale);

			// TODO: Need to persist to all slaves inside a JTA transaction
			// (with full rollback if any fail) .
			// This will ensure the master and all slaves are in synch.

		} catch (Exception ex) {
			logger.severe("Persistence Error : " + ex.getMessage());
			ex.printStackTrace();
		}

	}

}
