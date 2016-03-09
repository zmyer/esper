/*
 * *************************************************************************************
 *  Copyright (C) 2006-2015 EsperTech, Inc. All rights reserved.                       *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 * *************************************************************************************
 */

package com.espertech.esper.filter;

import com.espertech.esper.client.EventType;

public class FilterNonPropertyRegisteryServiceImpl implements FilterNonPropertyRegisteryService {

    public void registerNonPropertyExpression(String statementName, EventType eventType, FilterSpecLookupable lookupable) {
        // default implementation, no action required
    }

    public FilterSpecLookupable getNonPropertyExpression(String eventTypeName, String expression) {
        // default implementation, no action required
        throw new UnsupportedOperationException();
    }

    public void removeReferencesStatement(String statementName) {
        // default implementation, no action required
    }
}
