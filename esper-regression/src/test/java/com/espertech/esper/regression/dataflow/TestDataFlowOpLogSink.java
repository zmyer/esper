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

package com.espertech.esper.regression.dataflow;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.dataflow.EPDataFlowInstance;
import com.espertech.esper.dataflow.ops.Emitter;
import com.espertech.esper.metrics.instrumentation.InstrumentationHelper;
import com.espertech.esper.supportregression.bean.SupportBean;
import com.espertech.esper.supportregression.client.SupportConfigFactory;
import junit.framework.TestCase;

public class TestDataFlowOpLogSink extends TestCase {

    private EPServiceProvider epService;

    public void setUp() {
        epService = EPServiceProviderManager.getDefaultProvider(SupportConfigFactory.getConfiguration());
        epService.initialize();
        if (InstrumentationHelper.ENABLED) { InstrumentationHelper.startTest(epService, this.getClass(), getName());}
    }

    public void tearDown() {
        if (InstrumentationHelper.ENABLED) { InstrumentationHelper.endTest();}
    }

    public void testConsoleOp() {
        runAssertion(null, null, null, null, null);
        runAssertion("summary", true, null, null, null);
        runAssertion("xml", true, null, null, null);
        runAssertion("json", true, null, null, null);
        runAssertion("summary", false, null, null, null);
        runAssertion("summary", true, "dataflow:%df port:%p instanceId:%i title:%t event:%e", "mytitle", null);
        runAssertion("xml", true, null, null, false);
        runAssertion("json", true, null, "JSON_HERE", true);

        // invalid: output stream
        SupportDataFlowAssertionUtil.tryInvalidInstantiate(epService, "DF1", "create dataflow DF1 LogSink -> s1 {}",
                "Failed to instantiate data flow 'DF1': Failed initialization for operator 'LogSink': LogSink operator does not provide an output stream");

        String docSmple = "create dataflow MyDataFlow\n" +
                "  BeaconSource -> instream {}\n" +
                "  // Output textual event to log using defaults.\n" +
                "  LogSink(instream) {}\n" +
                "  \n" +
                "  // Output JSON-formatted to console.\n" +
                "  LogSink(instream) {\n" +
                "    format : 'json',\n" +
                "    layout : '%t [%e]',\n" +
                "    log : false,\n" +
                "    linefeed : true,\n" +
                "    title : 'My Custom Title:'\n" +
                "  }";
        epService.getEPAdministrator().createEPL(docSmple);
        epService.getEPRuntime().getDataFlowRuntime().instantiate("MyDataFlow");
    }

    private void runAssertion(String format, Boolean log, String layout, String title, Boolean linefeed)
    {
        epService.getEPAdministrator().getConfiguration().addEventType(SupportBean.class);
        
        String graph = "create dataflow MyConsoleOut\n" +
                "Emitter -> instream<SupportBean>{name : 'e1'}\n" +
                "LogSink(instream) {\n" +
                (format == null ? "" : "  format: '" + format + "',\n") +
                (log == null ? "" : "  log: " + log + ",\n") +
                (layout == null ? "" : "  layout: '" + layout + "',\n") +
                (title == null ? "" : "  title: '" + title + "',\n") +
                (linefeed == null ? "" : "  linefeed: " + linefeed + ",\n") +
                "}";
        EPStatement stmtGraph = epService.getEPAdministrator().createEPL(graph);

        EPDataFlowInstance instance = epService.getEPRuntime().getDataFlowRuntime().instantiate("MyConsoleOut");

        Emitter emitter = instance.startCaptive().getEmitters().get("e1");
        emitter.submit(new SupportBean("E1", 1));

        instance.cancel();
        stmtGraph.destroy();
    }
}
