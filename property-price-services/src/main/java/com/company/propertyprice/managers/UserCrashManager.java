/**
 * 
 */
package com.company.propertyprice.managers;

import com.company.propertyprice.dao.UserCrashDAO;
import com.company.propertyprice.model.UserCrash;

/**
 * @author smcardle
 *
 */
public class UserCrashManager {

    public void loqCrash(UserCrash userCrash) {
	
	UserCrashDAO.getInstance().attachDirty(userCrash);
    }
}
