/*
 * #%L
 * BasicServerMetricsTest.java - mongodb-async-driver - Allanbank Consulting, Inc.
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

package com.allanbank.mongodb.client.metrics.basic;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

/**
 * BasicServerMetricsTest provides tests for the {@link BasicServerMetrics}
 * class.
 *
 * @copyright 2014, Allanbank Consulting, Inc., All Rights Reserved
 */
public class BasicServerMetricsTest {

    /**
     * Test method for {@link BasicServerMetrics#getName()}.
     */
    @Test
    public void testGetName() {
        final BasicServerMetrics metrics = new BasicServerMetrics("server");

        assertThat(metrics.getName(), is("server"));

        metrics.close();
    }

    /**
     * Test method for {@link BasicServerMetrics#writeTo(PrintWriter)}.
     */
    @Test
    public void testWriteToPrintWriter() {
        final StringWriter sink = new StringWriter();
        final PrintWriter writer = new PrintWriter(sink);

        final BasicServerMetrics metrics = new BasicServerMetrics("server");

        metrics.writeTo(writer);
        assertThat(
                sink.toString(),
                is("Server["
                        + "server"
                        + ": sentBytes=0, sentCount=0, receivedBytes=0, receivedCount=0, "
                        + "lastLatency=0 ms, totalLatency=0 ms]"));

        // For Closeable.
        metrics.close();
    }

}
