package com.company.propertyprice.processor;


/**
 * Created by smcardle on 25/03/15.
 */
public class GetPropertiesByUUIDProcessor implements IProcessor {


    public String process(String[] endpoints, int discriminator) {

        return endpoints[2];
    }
}
