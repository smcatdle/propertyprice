package com.company.propertyprice.processor;


/**
 * Created by smcardle on 25/03/15.
 */
public class GetPropertiesInViewPortProcessor implements IProcessor {


    public String process(String[] endpoints, int discriminator) {

        // Check the discriminator to determine if property is in the peak area
        switch (discriminator) {

            case GetPropertiesDiscriminator.DISCRIMINATOR_PEAK :
                return endpoints[0];

            case GetPropertiesDiscriminator.DISCRIMINATOR_OFF_PEAK :
                return endpoints[1];
        }

        return null;
    }
}
