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
import com.espertech.esper.collection.MultiKey;
import com.espertech.esper.collection.UniformPair;

import java.util.*;

public interface ResultSetProcessorRowPerGroupOutputAllHelper {

    void processView(EventBean[] newData, EventBean[] oldData, boolean isGenerateSynthetic);
    void processJoin(Set<MultiKey<EventBean>> newData, Set<MultiKey<EventBean>> oldData, boolean isGenerateSynthetic);
    UniformPair<EventBean[]> outputView(boolean isSynthesize);
    UniformPair<EventBean[]> outputJoin(boolean isSynthesize);
    void destroy();
}
