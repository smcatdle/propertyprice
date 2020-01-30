package com.company.propertyprice.model;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class GridAveragesWrapperTest {

	private final Logger logger = Logger.getLogger(GridAveragesWrapper.class.getName());
	
	@Test
	public void testJson() {
		
		String jsonString = "{\"grids\":[{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":1214300.0,\"quantity\":5.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":747344.0,\"quantity\":4.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":868000.0,\"quantity\":2.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":111766.0,\"quantity\":1.0,\"saleDate\":\"2011\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":6877995.0,\"quantity\":20.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":2446500.0,\"quantity\":11.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":1492612.0,\"quantity\":6.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":1115500.0,\"quantity\":6.0,\"saleDate\":\"2011\"},\"2010\":{\"average\":0.0,\"total\":985000.0,\"quantity\":5.0,\"saleDate\":\"2010\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":7362385.0,\"quantity\":22.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":7471250.0,\"quantity\":27.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":3116700.0,\"quantity\":15.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":2667114.53,\"quantity\":11.0,\"saleDate\":\"2011\"},\"2010\":{\"average\":0.0,\"total\":2403000.0,\"quantity\":9.0,\"saleDate\":\"2010\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":7255411.0,\"quantity\":20.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":8148000.0,\"quantity\":20.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":4885180.06,\"quantity\":15.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":5441673.63,\"quantity\":17.0,\"saleDate\":\"2011\"},\"2010\":{\"average\":0.0,\"total\":855000.0,\"quantity\":2.0,\"saleDate\":\"2010\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":4318805.0,\"quantity\":9.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":6862229.0,\"quantity\":13.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":4378500.0,\"quantity\":7.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":3790000.0,\"quantity\":7.0,\"saleDate\":\"2011\"},\"2010\":{\"average\":0.0,\"total\":3273000.0,\"quantity\":6.0,\"saleDate\":\"2010\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":7462500.0,\"quantity\":16.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":7147612.33,\"quantity\":17.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":2447500.0,\"quantity\":7.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":1163000.0,\"quantity\":4.0,\"saleDate\":\"2011\"},\"2010\":{\"average\":0.0,\"total\":460000.0,\"quantity\":2.0,\"saleDate\":\"2010\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":2990000.0,\"quantity\":6.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":4519090.0,\"quantity\":9.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":1096500.0,\"quantity\":5.0,\"saleDate\":\"2012\"},\"2011\":{\"average\":0.0,\"total\":725000.0,\"quantity\":3.0,\"saleDate\":\"2011\"}}},{\"graphPoints\":{\"2014\":{\"average\":0.0,\"total\":3996000.0,\"quantity\":6.0,\"saleDate\":\"2014\"},\"2013\":{\"average\":0.0,\"total\":2307000.0,\"quantity\":6.0,\"saleDate\":\"2013\"},\"2012\":{\"average\":0.0,\"total\":1354405.0,\"quantity\":3.0,\"saleDate\":\"2012\"}}}]}";
		Type collectionType = new TypeToken<List<GridAveragesWrapper>>() {
		}.getType();
		List<GridAveragesWrapper> gridAveragesWrapper =(List<GridAveragesWrapper>) new GsonBuilder()
				.create()
				.fromJson((String) jsonString.substring(9, jsonString.length()-1), collectionType);
		
		logger.info("There are [" + gridAveragesWrapper.size() + "]");
		logger.info("There are [" + gridAveragesWrapper.get(0).getG().get("2014").getA() + "]");
	}

}
