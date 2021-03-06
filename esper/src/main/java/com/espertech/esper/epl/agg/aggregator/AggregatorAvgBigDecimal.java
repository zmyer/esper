/**************************************************************************************
 * Copyright (C) 2006-2015 EsperTech Inc. All rights reserved.                        *
 * http://www.espertech.com/esper                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.epl.agg.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Average that generates a BigDecimal numbers.
 */
public class AggregatorAvgBigDecimal implements AggregationMethod
{
    private static final Logger log = LoggerFactory.getLogger(AggregatorAvgBigDecimal.class);
    protected BigDecimal sum;
    protected long numDataPoints;
    protected MathContext optionalMathContext;

    /**
     * Ctor.
     * @param optionalMathContext math context
     */
    public AggregatorAvgBigDecimal(MathContext optionalMathContext)
    {
        sum = new BigDecimal(0.0);
        this.optionalMathContext = optionalMathContext;
    }

    public void clear()
    {
        sum = new BigDecimal(0.0);
        numDataPoints = 0;
    }

    public void enter(Object object)
    {
        if (object == null)
        {
            return;
        }
        numDataPoints++;
        if (object instanceof BigInteger)
        {
            sum = sum.add(new BigDecimal((BigInteger) object));
            return;
        }
        sum = sum.add((BigDecimal) object);
    }

    public void leave(Object object)
    {
        if (object == null)
        {
            return;
        }

        if (numDataPoints <= 1) {
            clear();
        }
        else {
            numDataPoints--;
            if (object instanceof BigInteger) {
                sum = sum.subtract(new BigDecimal((BigInteger) object));
            }
            else {
                sum = sum.subtract((BigDecimal) object);
            }
        }
    }

    public Object getValue()
    {
        if (numDataPoints == 0)
        {
            return null;
        }
        try
        {
            if (optionalMathContext == null) {
                return sum.divide(new BigDecimal(numDataPoints));
            }
            return sum.divide(new BigDecimal(numDataPoints), optionalMathContext);
        }
        catch (ArithmeticException ex)
        {
            log.error("Error computing avg aggregation result: " + ex.getMessage(), ex);
            return new BigDecimal(0);
        }
    }

}
