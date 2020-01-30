package com.company.propertyprice.processor;

import com.company.geo.ViewPort;
import com.company.geo.ViewPortGridDivisor;


/**
 * Created by smcardle on 03/04/15.
 */
public class GetPropertiesDiscriminator {


    public static final int DISCRIMINATOR_PEAK = 0;
    public static final int DISCRIMINATOR_OFF_PEAK = 1;

    public static final int getDiscriminator(ViewPortGridDivisor gridDivisor, ViewPort cellViewPort) {

        if (gridDivisor.checkIfGridWithinPeakArea(cellViewPort)) {
            return DISCRIMINATOR_PEAK;
        } else {
            return DISCRIMINATOR_OFF_PEAK;
        }
    }
}
