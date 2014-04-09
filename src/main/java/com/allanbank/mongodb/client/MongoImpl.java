/*
 * Copyright 2011-2013, Allanbank Consulting, Inc.
 *           All Rights Reserved
 */

package com.allanbank.mongodb.client;

import com.allanbank.mongodb.MongoClient;
import com.allanbank.mongodb.MongoClientConfiguration;

/**
 * Implements the bootstrap point for interactions with MongoDB.
 * 
 * @api.no This class is <b>NOT</b> part of the drivers API. This class may be
 *         mutated in incompatible ways between any two releases of the driver.
 * @deprecated Use the {@link MongoClient} interface instead. This interface
 *             will be removed on or after the 1.3.0 release.
 * @copyright 2011-2013, Allanbank Consulting, Inc., All Rights Reserved
 */
@Deprecated
public class MongoImpl extends MongoClientImpl implements
        com.allanbank.mongodb.Mongo {

    /**
     * Create a new MongoClient.
     * 
     * @param client
     *            The client interface for interacting with the database.
     */
    public MongoImpl(final Client client) {
        super(client);
    }

    /**
     * Create a new MongoClient.
     * 
     * @param config
     *            The configuration for interacting with MongoDB.
     */
    public MongoImpl(final com.allanbank.mongodb.MongoDbConfiguration config) {
        super(config);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to create a new Mongo instance around a SerialClientImpl.
     * </p>
     * 
     * @deprecated Use the {@link #asSerializedClient()} instead.
     */
    @Override
    @Deprecated
    public com.allanbank.mongodb.Mongo asSerializedMongo() {
        if (getClient() instanceof SerialClientImpl) {
            return this;
        }

        return new MongoImpl(new SerialClientImpl((ClientImpl) getClient()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return the clients configuration.
     * </p>
     */
    @Override
    public com.allanbank.mongodb.MongoDbConfiguration getConfig() {
        final MongoClientConfiguration config = getClient().getConfig();
        if (config instanceof com.allanbank.mongodb.MongoDbConfiguration) {
            return (com.allanbank.mongodb.MongoDbConfiguration) config;
        }
        throw new IllegalStateException(
                "Configuration is not the expected MongoDbConfiguration.");
    }
}
