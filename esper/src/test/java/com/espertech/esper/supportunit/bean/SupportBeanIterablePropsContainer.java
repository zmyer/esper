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

package com.espertech.esper.supportunit.bean;

import java.io.Serializable;

public class SupportBeanIterablePropsContainer implements Serializable
{
    private SupportBeanIterableProps contained;

    public SupportBeanIterablePropsContainer(SupportBeanIterableProps inner)
    {
        this.contained = inner;
    }

    public static SupportBeanIterablePropsContainer makeDefaultBean()
	{
        return new SupportBeanIterablePropsContainer(SupportBeanIterableProps.makeDefaultBean());
	}

    public SupportBeanIterableProps getContained()
    {
        return contained;
    }
}
