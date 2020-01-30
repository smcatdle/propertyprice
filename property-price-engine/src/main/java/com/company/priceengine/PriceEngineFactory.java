/** Daft Price Engine   **/


package com.company.priceengine;


public class PriceEngineFactory
    implements IPriceEngineFactory {

	
    public static final String DAFT = "daft";

    private static PriceEngineFactory instance = null;
    
    
    private PriceEngineFactory() {
    	
    }

    public static PriceEngineFactory getInstance() {
        if(instance == null) {
            instance = new PriceEngineFactory();
        }
        
        return instance;
    }

    public IPriceEngine create(String type) {
    	
        IPriceEngine priceEngine = null;
        
        if(DAFT.equals(type)) {
            priceEngine = new PPRPriceEngine();
        } 
        
        return priceEngine;
    }


}
