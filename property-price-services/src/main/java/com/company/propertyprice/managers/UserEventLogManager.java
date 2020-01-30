/**
 * 
 */
package com.company.propertyprice.managers;

import com.company.propertyprice.dao.UserEventDAO;
import com.company.propertyprice.model.UserEvent;

/**
 * @author smcardle
 *
 */
public class UserEventLogManager {

    public void loqEvent(UserEvent userEvent) {
	
	UserEventDAO.getInstance().attachDirty(userEvent);
    }
}
