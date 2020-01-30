package com.company.propertyprice.network;

import com.company.propertyprice.exception.EndPointProcessingException;
import com.company.propertyprice.processor.GetPropertiesByUUIDProcessor;
import com.company.propertyprice.processor.GetPropertiesInViewPortProcessor;
import com.company.propertyprice.processor.IProcessor;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by smcardle on 25/03/15.
 */
public class EndPointManager {

    public static final String GET_PROPERTIES_IN_VIEWPORT_PROCESSOR = "GetPropertiesInViewPort";
    public static final String GET_PROPERTIES_BY_UUID_PROCESSOR = "GetPropertiesByUUID";


    public static final String[] ENDPOINTS = {"http://webcache20-property20.rhcloud.com/web-cache-server/SaleRetrievalServlet?", "http://outsideleinstcache-dublin23.rhcloud.com/ex-leinster-cache/SaleRetrievalServlet?", "http://sales-propertyprod.rhcloud.com/property-price-server/GetSaleByUUIDServlet?"};


    public static final int GET_PROPERTIES_IN_VIEWPORT = 10;
    public static final int GET_GRID_AVERAGES = 20;
    public static final int GET_PROPERTIES_BY_UUID = 30;
    public static final int GET_GRAPH_DATA = 40;

    private static Map<String, IProcessor> processors = null;

    static {
        processors = new Hashtable<String, IProcessor>();

        processors.put(GET_PROPERTIES_IN_VIEWPORT_PROCESSOR, new GetPropertiesInViewPortProcessor());
        processors.put(GET_PROPERTIES_BY_UUID_PROCESSOR, new GetPropertiesByUUIDProcessor());
    }


    public static String getEndPoint(int request, int discriminator, int numberOfAttempts) throws EndPointProcessingException {

        IProcessor processor = null;


        switch(request) {

            case GET_PROPERTIES_IN_VIEWPORT :
                processor = processors.get(GET_PROPERTIES_IN_VIEWPORT_PROCESSOR);
                break;

            case GET_GRID_AVERAGES :
                processor = processors.get(GET_PROPERTIES_IN_VIEWPORT_PROCESSOR);
                break;

            case GET_PROPERTIES_BY_UUID :
                processor = processors.get(GET_PROPERTIES_BY_UUID_PROCESSOR);
                break;

            case GET_GRAPH_DATA :
                processor = processors.get(GET_PROPERTIES_IN_VIEWPORT_PROCESSOR);
                break;
        }

        if (processor != null) {
            return processor.process(ENDPOINTS, discriminator);
        } else {
            throw new EndPointProcessingException("No matching processor for request [" + request + "] found.");
        }
    }


}
