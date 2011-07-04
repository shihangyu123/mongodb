/*
 * Copyright 2011, Allanbank Consulting, Inc. 
 *           All Rights Reserved
 */
package com.allanbank.mongodb.bson.element;

import com.allanbank.mongodb.bson.Element;
import com.allanbank.mongodb.bson.ElementType;
import com.allanbank.mongodb.bson.Visitor;

/**
 * A wrapper for a BSON timestamp as the milliseconds since the epoch.
 * 
 * @copyright 2011, Allanbank Consulting, Inc., All Rights Reserved
 */
public class TimestampElement extends AbstractElement {

	/** The BSON type for a long. */
	public static final ElementType TYPE = ElementType.UTC_TIMESTAMP;

	/** The BSON timestamp value as the milliseconds since the epoch. */
	private final long myTimestamp;

	/**
	 * Constructs a new {@link TimestampElement}.
	 * 
	 * @param name
	 *            The name for the BSON long.
	 * @param value
	 *            The BSON timestamp value as the milliseconds since the epoch.
	 */
	public TimestampElement(String name, long value) {
		super(TYPE, name);

		myTimestamp = value;
	}

	/**
	 * Returns the BSON timestamp value as the milliseconds since the epoch.
	 * 
	 * @return The BSON timestamp value as the milliseconds since the epoch.
	 */
	public long getTime() {
		return myTimestamp;
	}

	/**
	 * Accepts the visitor and calls the {@link Visitor#visitTimestamp} method.
	 * 
	 * @see Element#accept(Visitor)
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visitTimestamp(getName(), getTime());
	}

	/**
	 * Computes a reasonable hash code.
	 * 
	 * @return The hash code value.
	 */
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + super.hashCode();
		result = 31 * result + (int) (myTimestamp & 0xFFFFFFFF);
		result = 31 * result + (int) ((myTimestamp >> 32) & 0xFFFFFFFF);
		return result;
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
	public boolean equals(Object object) {
		boolean result = false;
		if (this == object) {
			result = true;
		} else if ((object != null) && (getClass() == object.getClass())) {
			TimestampElement other = (TimestampElement) object;

			result = (myTimestamp == other.myTimestamp) && super.equals(object);
		}
		return result;
	}

	/**
	 * String form of the object.
	 * 
	 * @return A human readable form of the object.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append('"');
		builder.append(getName());
		builder.append("\" : UTC(");
		builder.append(myTimestamp);
		builder.append(")");

		return builder.toString();
	}
}