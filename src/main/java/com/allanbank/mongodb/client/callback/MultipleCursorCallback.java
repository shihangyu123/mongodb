/*
 * #%L
 * MultipleCursorCallback.java - mongodb-async-driver - Allanbank Consulting, Inc.
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

package com.allanbank.mongodb.client.callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.allanbank.mongodb.Callback;
import com.allanbank.mongodb.MongoDbException;
import com.allanbank.mongodb.MongoIterator;
import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.client.Client;
import com.allanbank.mongodb.client.MongoIteratorImpl;
import com.allanbank.mongodb.client.message.CursorableMessage;
import com.allanbank.mongodb.client.message.Query;
import com.allanbank.mongodb.client.message.Reply;

/**
 * Callback to convert a {@link CursorableMessage} {@link Reply} into a
 * collection of {@link MongoIteratorImpl}.
 *
 * @api.no This class is <b>NOT</b> part of the drivers API. This class may be
 *         mutated in incompatible ways between any two releases of the driver.
 * @copyright 2014, Allanbank Consulting, Inc., All Rights Reserved
 */
public final class MultipleCursorCallback
        extends AbstractReplyCallback<Collection<MongoIterator<Document>>>
        implements AddressAware {

    /** The server the original request was sent to. */
    private volatile String myAddress;

    /** The original query. */
    private final Client myClient;

    /** The original message to start the cursor. */
    private final CursorableMessage myMessage;

    /** The reply to the query. */
    private volatile Reply myReply;

    /**
     * Initially set to false. Set to true for the first of address or reply
     * being set. The second fails and {@link #trigger() triggers} the callback.
     */
    private final AtomicBoolean mySetOther;

    /**
     * Create a new CursorCallback.
     *
     * @param client
     *            The client interface to the server.
     * @param message
     *            The original query.
     * @param results
     *            The callback to update once the first set of results are
     *            ready.
     */
    public MultipleCursorCallback(final Client client,
            final CursorableMessage message,
            final Callback<Collection<MongoIterator<Document>>> results) {

        super(results);

        myClient = client;
        myMessage = message;

        mySetOther = new AtomicBoolean(false);
    }

    /**
     * Returns the server the original request was sent to.
     *
     * @return The server the original request was sent to.
     */
    public String getAddress() {
        return myAddress;
    }

    /**
     * Sets the value of the server the original request was sent to.
     *
     * @param address
     *            The new value for the server the original request was sent to.
     */
    @Override
    public void setAddress(final String address) {
        myAddress = address;
        trigger();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to add the {@link Query} to the exception.
     * </p>
     *
     * @see AbstractReplyCallback#asError(Reply, int, int, String)
     */
    @Override
    protected MongoDbException asError(final Reply reply, final int okValue,
            final int errorNumber, final String errorMessage) {
        return super.asError(reply, okValue, errorNumber, false, errorMessage,
                myMessage);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to construct a {@link MongoIteratorImpl} around the reply.
     * </p>
     *
     * @see AbstractReplyCallback#convert(Reply)
     */
    @Override
    protected Collection<MongoIterator<Document>> convert(final Reply reply)
            throws MongoDbException {
        final List<Reply> results = CommandCursorTranslator.translateAll(reply);
        final List<MongoIterator<Document>> iters = new ArrayList<MongoIterator<Document>>(
                results.size());
        for (final Reply r : results) {
            iters.add(new MongoIteratorImpl(myMessage, myClient, myAddress, r));
        }
        return iters;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to check if the server address has been set and if so then
     * pass the converted reply to the {@link #getForwardCallback() forward
     * callback}. Otherwise the call is dropped.
     * </p>
     */
    @Override
    protected void handle(final Reply reply) {
        myReply = reply;
        trigger();
    }

    /**
     * Triggers the callback when the address and reply are set.
     */
    private void trigger() {
        if (!mySetOther.compareAndSet(false, true)) {
            super.handle(myReply);
        }
    }
}