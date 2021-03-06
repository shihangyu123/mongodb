/*
 * #%L
 * JsonSerializationVisitorITest.java - mongodb-async-driver - Allanbank Consulting, Inc.
 * %%
 * Copyright (C) 2011 - 2014 Allanbank Consulting, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package com.allanbank.mongodb.bson.element;

import static org.junit.Assert.assertEquals;

import java.net.InetSocketAddress;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.allanbank.mongodb.ClusterTestSupport;
import com.allanbank.mongodb.Durability;
import com.allanbank.mongodb.ManagedProcess;
import com.allanbank.mongodb.MongoClient;
import com.allanbank.mongodb.MongoClientConfiguration;
import com.allanbank.mongodb.MongoCollection;
import com.allanbank.mongodb.MongoDatabase;
import com.allanbank.mongodb.MongoFactory;
import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.bson.builder.BuilderFactory;
import com.allanbank.mongodb.util.IOUtils;

/**
 * JsonSerializationVisitorTest provides tests that the parsing of various JSON
 * extensions matches those done by the MongoDB shell.
 *
 * @copyright 2012-2013, Allanbank Consulting, Inc., All Rights Reserved
 */
public class JsonSerializationVisitorITest {

    /** Provides support for stopping and starting the processes. */
    private static ClusterTestSupport ourTestSupport = null;

    /**
     * Stop the server we started.
     */
    @BeforeClass
    public static void setUp() {
        ourTestSupport = new ClusterTestSupport();
        ourTestSupport.startStandAlone();
    }

    /**
     * Stop the server we started.
     */
    @AfterClass
    public static void tearDown() {
        if (ourTestSupport != null) {
            ourTestSupport.stopAll();
        }
    }

    /**
     * Tests that the parsing of an ObjectId in the shell matches that by the
     * driver.
     */
    @Test
    public void testMongoTimeStampParsingMatches() {
        int seconds = 0x12345 * 1000; // Times are truncated.
        final int offset = 0xAB;

        final MongoClientConfiguration config = new MongoClientConfiguration();
        config.addServer(new InetSocketAddress("127.0.0.1", 27017));
        config.setMaxConnectionCount(1);

        final MongoClient mongo = MongoFactory.createClient(config);
        final MongoDatabase db = mongo.getDatabase("test");
        final MongoCollection collection = db.getCollection("test");

        // Make sure the collection is created and empty.
        db.createCollection("test", BuilderFactory.start());
        collection.delete(BuilderFactory.start(), Durability.ACK);

        final MongoTimestampElement element = new MongoTimestampElement("_id",
                0x00012345000000ABL);
        assertEquals("'_id' : Timestamp(" + seconds + ", " + offset + ")",
                element.toString());

        final Document doc = BuilderFactory.start().add(element).build();
        assertEquals("{ '_id' : Timestamp(" + seconds + ", " + offset + ") }",
                doc.toString());

        ManagedProcess mp = ourTestSupport.run(null, "mongo",
                "localhost:27017/test", "-eval",
                "db.test.insert( { '_id' : Timestamp(" + seconds + ", "
                        + offset + ") } );");
        mp.waitFor();

        // Now pull the document back and make sure it matches what we expect.
        Document result = collection.findOne(BuilderFactory.start());

        // For 2.4 the shell parsing changes to be seconds.
        if (!doc.equals(result)) {
            seconds = 0x12345;
            collection.delete(BuilderFactory.start(), Durability.ACK);
            mp = ourTestSupport.run(null, "mongo", "localhost:27017/test",
                    "-eval", "db.test.insert( { '_id' : Timestamp(" + seconds
                            + ", " + offset + ") } );");
            mp.waitFor();

            result = collection.findOne(BuilderFactory.start());
        }
        assertEquals(doc, result);

        IOUtils.close(mongo);
    }

    /**
     * Tests that the parsing of an ObjectId in the shell matches that by the
     * driver.
     */
    @Test
    public void testObjectIdParsingMatches() {
        final MongoClientConfiguration config = new MongoClientConfiguration();
        config.addServer(new InetSocketAddress("127.0.0.1", 27017));
        config.setMaxConnectionCount(1);

        final MongoClient mongo = MongoFactory.createClient(config);
        final MongoDatabase db = mongo.getDatabase("test");
        final MongoCollection collection = db.getCollection("test");

        // Make sure the collection is created and empty.
        db.createCollection("test", BuilderFactory.start());
        collection.delete(BuilderFactory.start(), Durability.ACK);

        final ObjectId id = new ObjectId(0x11223344, 0x5566778899001122L);
        final ObjectIdElement element = new ObjectIdElement("_id", id);
        assertEquals("'_id' : ObjectId('112233445566778899001122')",
                element.toString());

        final Document doc = BuilderFactory.start().add(element).build();
        assertEquals("{ '_id' : ObjectId('112233445566778899001122') }",
                doc.toString());

        final ManagedProcess mp = ourTestSupport
                .run(null, "mongo", "localhost:27017/test", "-eval",
                        "db.test.insert( { '_id' : ObjectId('112233445566778899001122') } );");
        mp.waitFor();

        // Now pull the document back and make sure it matches what we expect.
        final Document result = collection.findOne(BuilderFactory.start());

        assertEquals(doc, result);

        IOUtils.close(mongo);
    }
}
