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

package com.espertech.esper.supportunit.util;

public class SupportCtorIntObjectArray
{
    private Object[] arguments;
    private int someValue;

    public SupportCtorIntObjectArray(int someValue)
    {
        this.someValue = someValue;
    }

    public SupportCtorIntObjectArray(Object[] arguments)
    {
        this.arguments = arguments;
    }

    public Object[] getArguments()
    {
        return arguments;
    }

    public int getSomeValue()
    {
        return someValue;
    }
}
