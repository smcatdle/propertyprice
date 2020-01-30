/**
 * 
 */
package com.company.propertyprice.processor;

import java.lang.reflect.Type;

import com.company.propertyprice.model.GridWrapper;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author smcardle
 *
 */
public class GetPropertiesPostProcessor {

	
	public GridWrapper process(String jsonString) {
		
		Type collectionType = new TypeToken<GridWrapper>() {
		}.getType();
		return new GsonBuilder()
				.setDateFormat("MMM dd, yyyy").create()
				.fromJson((String) jsonString, collectionType);
		
	}
}
