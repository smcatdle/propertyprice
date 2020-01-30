/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.company.propertyprice.rules;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import com.company.propertyprice.model.Address;
import com.company.propertyprice.model.GeoCode;

/**
 * The address rules application
 */
public class AddressResolver {

    private final static Logger logger = Logger.getLogger(AddressResolver.class.getName());
    
    private KieSession session;

    public AddressResolver() {
	
	createSession();
    }
    
    private KieSession createSession() {
	
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();
        session = kc.newKieSession("AddressKS");
        //session.setGlobal( "services", this );
 
	Address address = new Address();
	address.setAddressLine1("Block F Milners Square");
	address.setAddressLine2("Shanowen road");
	address.setAddressLine3("Santry"); 
	address.setAddressLine4("");
	address.setAddressLine5("Dublin");
	
	GeoCode geocode = new GeoCode();
	geocode.setLocationType(GeoCode.GEO_LOCATION_TYPE_CODE_APPROXIMATE);
	
        session.insert(address);
        session.insert(geocode);
        session.fireAllRules();
        
        return session;
    }

    public static void main(String[] args) {
	AddressResolver resolver = new AddressResolver();
	logger.log(Level.INFO, "Rules Complete");
    }
}
