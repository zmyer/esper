/**************************************************************************************
 * Copyright (C) 2006-2015 EsperTech Inc. All rights reserved.                        *
 * http://www.espertech.com/esper                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.core;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.collection.UniformPair;
import com.espertech.esper.core.context.util.AgentInstanceContext;
import com.espertech.esper.epl.agg.service.AggregationRowRemovedCallback;
import com.espertech.esper.epl.agg.service.AggregationService;
import com.espertech.esper.view.Viewable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public interface ResultSetProcessorRowPerGroupUnboundGroupRep extends AggregationRowRemovedCallback {

    void put(Object key, EventBean event);
    void removed(Object key);
    Iterator<EventBean> valueIterator();
    void destroy();
}
