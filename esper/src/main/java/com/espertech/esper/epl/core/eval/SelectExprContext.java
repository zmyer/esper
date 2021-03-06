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

package com.espertech.esper.epl.core.eval;

import com.espertech.esper.epl.expression.core.ExprEvaluator;
import com.espertech.esper.event.EventAdapterService;

public class SelectExprContext {
    private ExprEvaluator[] expressionNodes;
    private final String[] columnNames;
    private final EventAdapterService eventAdapterService;

    public SelectExprContext(ExprEvaluator[] expressionNodes, String[] columnNames, EventAdapterService eventAdapterService) {
        this.expressionNodes = expressionNodes;
        this.columnNames = columnNames;
        this.eventAdapterService = eventAdapterService;
    }

    public ExprEvaluator[] getExpressionNodes() {
        return expressionNodes;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public EventAdapterService getEventAdapterService() {
        return eventAdapterService;
    }

    public void setExpressionNodes(ExprEvaluator[] expressionNodes) {
        this.expressionNodes = expressionNodes;
    }
}
