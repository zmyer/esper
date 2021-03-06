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

package com.espertech.esper.regression.context;

import com.espertech.esper.client.*;
import com.espertech.esper.client.context.ContextPartitionSelector;
import com.espertech.esper.client.context.ContextPartitionSelectorCategory;
import com.espertech.esper.metrics.instrumentation.InstrumentationHelper;
import com.espertech.esper.supportregression.client.SupportConfigFactory;
import junit.framework.TestCase;

import java.util.*;

public class TestDocExamples extends TestCase {

    private EPServiceProvider epService;

    public void setUp()
    {
        Configuration configuration = SupportConfigFactory.getConfiguration();
        epService = EPServiceProviderManager.getDefaultProvider(configuration);
        epService.initialize();
        if (InstrumentationHelper.ENABLED) { InstrumentationHelper.startTest(epService, this.getClass(), getName());}
    }

    public void tearDown() {
        if (InstrumentationHelper.ENABLED) { InstrumentationHelper.endTest();}
    }

    public void testDocSamples() {
        epService.getEPAdministrator().getConfiguration().addEventType(BankTxn.class);
        epService.getEPAdministrator().getConfiguration().addEventType(LoginEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(LogoutEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(SecurityEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(SensorEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(TrafficEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(TrainEnterEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(TrainLeaveEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(CumulativePrice.class);
        epService.getEPAdministrator().getConfiguration().addEventType(PassengerScanEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(MyStartEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(MyEndEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(MyInitEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(MyTermEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType(MyEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType("StartEventOne", MyStartEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType("StartEventTwo", MyStartEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType("MyOtherEvent", MyStartEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType("EndEventOne", MyEndEvent.class);
        epService.getEPAdministrator().getConfiguration().addEventType("EndEventTwo", MyEndEvent.class);

        create("create context SegmentedByCustomer partition by custId from BankTxn");
        create("context SegmentedByCustomer select custId, account, sum(amount) from BankTxn group by account");
        create("context SegmentedByCustomer\n" +
                "select * from pattern [\n" +
                "every a=BankTxn(amount > 400) -> b=BankTxn(amount > 400) where timer:within(10 minutes)\n" +
                "]");
        epService.getEPAdministrator().destroyAllStatements();
        create("create context SegmentedByCustomer partition by\n" +
                "custId from BankTxn, loginId from LoginEvent, loginId from LogoutEvent");
        epService.getEPAdministrator().destroyAllStatements();
        create("create context SegmentedByCustomer partition by\n" +
                "custId from BankTxn, loginId from LoginEvent(failed=false)");
        epService.getEPAdministrator().destroyAllStatements();
        create("create context ByCustomerAndAccount partition by custId and account from BankTxn");
        create("context ByCustomerAndAccount select custId, account, sum(amount) from BankTxn");
        create("context ByCustomerAndAccount\n" +
                "  select context.name, context.id, context.key1, context.key2 from BankTxn");
        epService.getEPAdministrator().destroyAllStatements();
        create("create context ByCust partition by custId from BankTxn");
        create("context ByCust\n" +
                "select * from BankTxn as t1 unidirectional, BankTxn#time(30) t2\n" +
                "where t1.amount = t2.amount");
        create("context ByCust\n" +
                "select * from SecurityEvent as t1 unidirectional, BankTxn#time(30) t2\n" +
                "where t1.customerName = t2.customerName");
        epService.getEPAdministrator().destroyAllStatements();
        create("create context CategoryByTemp\n" +
                "group temp < 65 as cold,\n" +
                "group temp between 65 and 85 as normal,\n" +
                "group temp > 85 as large\n" +
                "from SensorEvent");
        create("context CategoryByTemp select context.label, count(*) from SensorEvent");
        create("context CategoryByTemp\n" +
                "select context.name, context.id, context.label from SensorEvent");
        create("create context NineToFive start (0, 9, *, *, *) end (0, 17, *, *, *)");
        create("context NineToFive select * from TrafficEvent(speed >= 100)");
        create("context NineToFive\n" +
                "select context.name, context.startTime, context.endTime from TrafficEvent(speed >= 100)");
        create("create context CtxTrainEnter\n" +
                "initiated by TrainEnterEvent as te\n" +
                "terminated after 5 minutes");
        create("context CtxTrainEnter\n" +
                "select *, context.te.trainId, context.id, context.name from TrainLeaveEvent(trainId = context.te.trainId)");
        create("context CtxTrainEnter\n" +
                "select t1 from pattern [\n" +
                "t1=TrainEnterEvent -> timer:interval(5 min) and not TrainLeaveEvent(trainId = context.te.trainId)]");
        create("create context CtxEachMinute\n" +
                "initiated by pattern [every timer:interval(1 minute)]\n" +
                "terminated after 1 minutes");
        create("context CtxEachMinute select avg(temp) from SensorEvent");
        create("context CtxEachMinute\n" +
                "select context.id, avg(temp) from SensorEvent output snapshot when terminated");
        create("context CtxEachMinute\n" +
                "select context.id, avg(temp) from SensorEvent output snapshot every 1 minute and when terminated");
        create("select venue, ccyPair, side, sum(qty)\n" +
                "from CumulativePrice\n" +
                "where side='O'\n" +
                "group by venue, ccyPair, side");
        create("create context MyContext partition by venue, ccyPair, side from CumulativePrice(side='O')");
        create("context MyContext select venue, ccyPair, side, sum(qty) from CumulativePrice");

        create("create context SegmentedByCustomerHash\n" +
                "coalesce by consistent_hash_crc32(custId) from BankTxn granularity 16 preallocate");
        create("context SegmentedByCustomerHash\n" +
                "select custId, account, sum(amount) from BankTxn group by custId, account");
        create("create context HashedByCustomer as coalesce\n" +
                "consistent_hash_crc32(custId) from BankTxn,\n" +
                "consistent_hash_crc32(loginId) from LoginEvent,\n" +
                "consistent_hash_crc32(loginId) from LogoutEvent\n" +
                "granularity 32 preallocate");

        epService.getEPAdministrator().destroyAllStatements();
        create("create context HashedByCustomer\n" +
                "coalesce consistent_hash_crc32(loginId) from LoginEvent(failed = false)\n" +
                "granularity 1024 preallocate");
        create("create context ByCustomerHash coalesce consistent_hash_crc32(custId) from BankTxn granularity 1024");
        create("context ByCustomerHash\n" +
                "select context.name, context.id from BankTxn");
        
        create("create context NineToFiveSegmented\n" +
                "context NineToFive start (0, 9, *, *, *) end (0, 17, *, *, *),\n" +
                "context SegmentedByCustomer partition by custId from BankTxn");
        create("context NineToFiveSegmented\n" +
                "select custId, account, sum(amount) from BankTxn group by account");
        create("create context CtxNestedTrainEnter\n" +
                "context InitCtx initiated by TrainEnterEvent as te terminated after 5 minutes,\n" +
                "context HashCtx coalesce by consistent_hash_crc32(tagId) from PassengerScanEvent\n" +
                "granularity 16 preallocate");
        create("context CtxNestedTrainEnter\n" +
                "select context.InitCtx.te.trainId, context.HashCtx.id,\n" +
                "tagId, count(*) from PassengerScanEvent group by tagId");
        create("context NineToFiveSegmented\n" +
                "select context.NineToFive.startTime, context.SegmentedByCustomer.key1 from BankTxn");
        create("context NineToFiveSegmented select context.name, context.id from BankTxn");

        create("create context MyContext start MyStartEvent end MyEndEvent");
        create("create context MyContext2 initiated MyEvent(level > 0) terminated after 10 seconds");
        create("create context MyContext3 \n" +
                "start MyEvent as myevent\n" +
                "end MyEvent(id=myevent.id)");
        create("create context MyContext4 \n" +
                "initiated by MyInitEvent as e1 \n" +
                "terminated by MyTermEvent(id=e1.id, level <> e1.level)");
        create("create context MyContext5 start pattern [StartEventOne or StartEventTwo] end after 5 seconds");
        create("create context MyContext6 initiated by pattern [every MyInitEvent -> MyOtherEvent where timer:within(5)] terminated by MyTermEvent");
        create("create context MyContext7 \n" +
                "  start pattern [a=StartEventOne or  b=StartEventTwo]\n" +
                "  end pattern [EndEventOne(id=a.id) or EndEventTwo(id=b.id)]");
        create("create context MyContext8 initiated (*, *, *, *, *) terminated after 10 seconds");
        create("create context NineToFive start after 10 seconds end after 1 minute");
        create("create context Overlap5SecFor1Min initiated after 5 seconds terminated after 1 minute");
        create("create context CtxSample\n" +
                "initiated by MyStartEvent as startevent\n" +
                "terminated by MyEndEvent(id = startevent.id) as endevent");
        create("context CtxSample select context.endevent.id, count(*) from MyEvent output snapshot when terminated");

        create("create context TxnCategoryContext \n" +
                "  group by amount < 100 as small, \n" +
                "  group by amount between 100 and 1000 as medium, \n" +
                "  group by amount > 1000 as large from BankTxn");
        EPStatement stmt = create("context TxnCategoryContext select * from BankTxn#time(1 minute)");
        ContextPartitionSelectorCategory categorySmall = new ContextPartitionSelectorCategory() {
            public Set<String> getLabels() {
                return Collections.singleton("small");
            }
        };
        stmt.iterator(categorySmall);
        ContextPartitionSelectorCategory categorySmallMed = new ContextPartitionSelectorCategory() {
            public Set<String> getLabels() {
                return new HashSet<String>(Arrays.asList("small", "medium"));
            }
        };
        create("context TxnCategoryContext create window BankTxnWindow#time(1 min) as BankTxn");
        epService.getEPRuntime().executeQuery("select count(*) from BankTxnWindow", new ContextPartitionSelector[] {categorySmallMed});

        epService.getEPAdministrator().getConfiguration().addEventType(MyTwoKeyInit.class);
        epService.getEPAdministrator().getConfiguration().addEventType(MyTwoKeyTerm.class);
        create("create context CtxPerKeysAndExternallyControlled\n" +
                "context PartitionedByKeys " +
                "  partition by " +
                "    key1, key2 from MyTwoKeyInit\n," +
                "    key1, key2 from SensorEvent\n," +
                "context InitiateAndTerm initiated by MyTwoKeyInit as e1 terminated by MyTwoKeyTerm(key1=e1.key1 and key2=e1.key2)");
        create("context CtxPerKeysAndExternallyControlled\n" +
                "select key1, key2, avg(temp) as avgTemp, count(*) as cnt\n" +
                "from SensorEvent\n" +
                "output snapshot when terminated\n" +
                "// note: no group-by needed since \n");
    }

    private EPStatement create(String epl) {
        return epService.getEPAdministrator().createEPL(epl);
    }

    public static class CumulativePrice {
        private String venue;
        private String ccyPair;
        private String side;
        private double qty;

        public String getVenue() {
            return venue;
        }

        public String getCcyPair() {
            return ccyPair;
        }

        public String getSide() {
            return side;
        }

        public double getQty() {
            return qty;
        }
    }

    public static class TrainLeaveEvent {
        private int trainId;

        public int getTrainId() {
            return trainId;
        }
    }

    public static class TrainEnterEvent {
        private int trainId;

        public int getTrainId() {
            return trainId;
        }
    }

    public static class TrafficEvent {
        private double speed;

        public double getSpeed() {
            return speed;
        }
    }

    public static class SensorEvent {
        private double temp;
        private int key1;
        private int key2;

        public double getTemp() {
            return temp;
        }

        public int getKey1() {
            return key1;
        }

        public void setKey1(int key1) {
            this.key1 = key1;
        }

        public int getKey2() {
            return key2;
        }

        public void setKey2(int key2) {
            this.key2 = key2;
        }
    }

    public static class LoginEvent {
        private String loginId;
        private boolean failed;

        public String getLoginId() {
            return loginId;
        }

        public boolean isFailed() {
            return failed;
        }
    }

    public static class LogoutEvent {
        private String loginId;

        public String getLoginId() {
            return loginId;
        }
    }

    public static class SecurityEvent {
        private String customerName;

        public String getCustomerName() {
            return customerName;
        }
    }

    public static class BankTxn {
        private String custId;
        private String account;
        private long amount;
        private String customerName;

        public String getCustId() {
            return custId;
        }

        public String getAccount() {
            return account;
        }

        public long getAmount() {
            return amount;
        }

        public String getCustomerName() {
            return customerName;
        }
    }

    public static class PassengerScanEvent {

        private final String tagId;

        public PassengerScanEvent(String tagId) {
            this.tagId = tagId;
        }

        public String getTagId() {
            return tagId;
        }
    }
    
    public static class MyStartEvent {
        private int id;
        private int level;

        public int getLevel() {
            return level;
        }

        public int getId() {
            return id;
        }
    }

    public static class MyEndEvent {
        private int id;
        private int level;

        public int getLevel() {
            return level;
        }

        public int getId() {
            return id;
        }
    }

    public static class MyInitEvent {
        private int id;
        private int level;

        public int getLevel() {
            return level;
        }

        public int getId() {
            return id;
        }
    }

    public static class MyTermEvent {
        private int id;
        private int level;

        public int getLevel() {
            return level;
        }

        public int getId() {
            return id;
        }
    }

    public static class MyEvent {
        private int id;
        private int level;

        public int getLevel() {
            return level;
        }

        public int getId() {
            return id;
        }
    }

    public static class MyTwoKeyInit {
        private int key1;
        private int key2;

        public int getKey1() {
            return key1;
        }

        public int getKey2() {
            return key2;
        }
    }

    public static class MyTwoKeyTerm {
        private int key1;
        private int key2;

        public int getKey1() {
            return key1;
        }

        public int getKey2() {
            return key2;
        }
    }
}
