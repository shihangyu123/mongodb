/*
 * Copyright 2011-2013, Allanbank Consulting, Inc.
 *           All Rights Reserved
 */

package com.allanbank.mongodb.client.message;

import com.allanbank.mongodb.ReadPreference;
import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.builder.Aggregate;
import com.allanbank.mongodb.client.VersionRange;

/**
 * Helper class for the aggregation commands.
 * 
 * @api.no This class is <b>NOT</b> part of the drivers API. This class may be
 *         mutated in incompatible ways between any two releases of the driver.
 * @copyright 2011-2013, Allanbank Consulting, Inc., All Rights Reserved
 */
public class AggregateCommand extends Command implements CursorableMessage {

    /** The original aggregation. */
    private final Aggregate myAggregate;

    /** The name of the collection to run the aggregation on. */
    private final String myAggregateCollectionName;

    /**
     * Create a new AggregateCommand.
     * 
     * @param aggregation
     *            The original aggregation.
     * @param databaseName
     *            The name of the database.
     * @param collectionName
     *            The name of the collection to run the aggregation on.
     * @param commandDocument
     *            The command document containing the command and options.
     * @param readPreference
     *            The preference for which servers to use to retrieve the
     *            results.
     * @param requiredServerVersion
     *            The required version of the server to support processing the
     *            message.
     */
    public AggregateCommand(final Aggregate aggregation,
            final String databaseName, final String collectionName,
            final Document commandDocument,
            final ReadPreference readPreference,
            final VersionRange requiredServerVersion) {
        super(databaseName, commandDocument, readPreference,
                requiredServerVersion);

        myAggregate = aggregation;
        myAggregateCollectionName = collectionName;
    }

    /**
     * Determines if the passed object is of this same type as this object and
     * if so that its fields are equal.
     * 
     * @param object
     *            The object to compare to.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = false;
        if (this == object) {
            result = true;
        }
        else if ((object != null) && (getClass() == object.getClass())) {
            final AggregateCommand other = (AggregateCommand) object;

            result = myAggregateCollectionName
                    .equals(other.myAggregateCollectionName)
                    && super.equals(object);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return the batch size from the {@link Aggregate}.
     * </p>
     */
    @Override
    public int getBatchSize() {
        return myAggregate.getBatchSize();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return the name to run the aggregation on instead of the
     * {@link Command#COMMAND_COLLECTION} name.
     * </p>
     */
    @Override
    public String getCollectionName() {
        return myAggregateCollectionName;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overridden to return the limit from the {@link Aggregate}.
     * </p>
     */
    @Override
    public int getLimit() {
        return myAggregate.getCursorLimit();
    }

    /**
     * Computes a reasonable hash code.
     * 
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + super.hashCode();
        result = (31 * result) + myAggregateCollectionName.hashCode();
        return result;
    }
}
