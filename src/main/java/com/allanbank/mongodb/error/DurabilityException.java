/*
 * Copyright 2011-2013, Allanbank Consulting, Inc.
 *           All Rights Reserved
 */

package com.allanbank.mongodb.error;

import com.allanbank.mongodb.Durability;
import com.allanbank.mongodb.client.Message;
import com.allanbank.mongodb.client.message.Reply;

/**
 * Exception raised when a write encounters a durability violation.
 * <p>
 * This can be due to using a {@link Durability#journalDurable(int) journal}
 * durability without journaling enabled on the server or the durability
 * requirements are not met within the specified wait time.
 * </p>
 * 
 * @api.yes This class is part of the driver's API. Public and protected members
 *          will be deprecated for at least 1 non-bugfix release (version
 *          numbers are &lt;major&gt;.&lt;minor&gt;.&lt;bugfix&gt;) before being
 *          removed or modified.
 * @copyright 2011-2013, Allanbank Consulting, Inc., All Rights Reserved
 */
public class DurabilityException extends ReplyException {

    /** The serialization version for the class. */
    private static final long serialVersionUID = -3588171889388956082L;

    /**
     * Create a new DurabilityException.
     * 
     * @param okValue
     *            The value of the "ok" field in the reply document.
     * @param errorNumber
     *            The value of the "errNo" field in the reply document.
     * @param errorMessage
     *            The value of the 'errmsg" field in the reply document.
     * @param message
     *            The message that triggered the message.
     * @param reply
     *            The reply with the error.
     */
    public DurabilityException(final int okValue, final int errorNumber,
            final String errorMessage, final Message message, final Reply reply) {
        super(okValue, errorNumber, errorMessage, message, reply);
    }

    /**
     * Create a new DurabilityException.
     * 
     * @param okValue
     *            The value of the "ok" field in the reply document.
     * @param errorNumber
     *            The value of the "errNo" field in the reply document.
     * @param errorMessage
     *            The value of the 'errmsg" field in the reply document.
     * @param reply
     *            The reply with the error.
     */
    public DurabilityException(final int okValue, final int errorNumber,
            final String errorMessage, final Reply reply) {
        this(okValue, errorNumber, errorMessage, null, reply);
    }

}
