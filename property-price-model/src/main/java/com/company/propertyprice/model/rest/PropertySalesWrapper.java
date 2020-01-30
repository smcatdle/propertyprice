/**
 * 
 */
package com.company.propertyprice.model.rest;

import java.util.List;

import com.company.propertyprice.model.PropertySale;


/**
 * @author smcardle
 *
 */
public class PropertySalesWrapper {

    private List<PropertySale> sales;

    /**
     * @return the sales
     */
    public List<PropertySale> getSales() {
        return sales;
    }

    /**
     * @param sales the sales to set
     */
    public void setSales(List<PropertySale> sales) {
        this.sales = sales;
    }
    
    
}
