/** Scanshop Price Engine   **/


package com.company.priceengine;

import java.util.List;
import java.util.Set;

import com.company.priceengine.exception.EngineShuttingDownException;
import com.company.propertyprice.model.IProperty;
import com.company.propertyprice.model.PropertyElement;


public interface IPriceEngine {

    public abstract String retrieveInitialHTMLPage(String s);

    public abstract String retrieveHTMLPage(String s);

    public abstract List<String> findPageLinks(String s, List<String> list);

    public abstract void findProducts(String s, List list) throws EngineShuttingDownException;

    public abstract boolean hasNextPage(String s);

    public abstract void findProductsOnNextScreen(String s, String s1, int i);

    public abstract List findPropertyElements(String s, String s1);

    public abstract IProperty extractPropertyElement(PropertyElement productelement) throws EngineShuttingDownException ;

    public abstract void writeProductInfo(IProperty iproduct);

    public abstract int start(String s, List list);

    public abstract void createOutputFile();

    public abstract void closeOutputFile();

    public abstract String replaceSpecialHtmlChars(String s);
}
