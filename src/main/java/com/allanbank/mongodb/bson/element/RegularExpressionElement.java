/*
 * Copyright 2011, Allanbank Consulting, Inc. 
 *           All Rights Reserved
 */
package com.allanbank.mongodb.bson.element;

import com.allanbank.mongodb.bson.Element;
import com.allanbank.mongodb.bson.ElementType;
import com.allanbank.mongodb.bson.Visitor;

/**
 * A wrapper for a BSON regular expression.
 * 
 * @copyright 2011, Allanbank Consulting, Inc., All Rights Reserved
 */
public class RegularExpressionElement extends AbstractElement {

	/** Option for case insensitive matching. */
	public static final int CASE_INSENSITIVE;

	/** Option for dotall mode ('.' matches everything). */
	public static final int DOT_ALL;

	/** Option to make \w, \W, etc. locale dependent. */
	public static final int LOCALE_DEPENDENT;

	/** Option for multiline matching. */
	public static final int MULTILINE;

	/** Option for case insensitive matching. */
	public static final int OPTION_I;

	/** Option to make \w, \W, etc. locale dependent. */
	public static final int OPTION_L;

	/** Option for multiline matching. */
	public static final int OPTION_M;

	/** Option for verbose mode. */
	public static final int OPTION_MASK;

	/** Option for dotall mode ('.' matches everything). */
	public static final int OPTION_S;

	/** Option to make \w, \W, etc. match unicode. */
	public static final int OPTION_U;

	/** Option for verbose mode. */
	public static final int OPTION_X;

	/** The options for each possible bit field. */
	private static final String[] OPTIONS;

	/** The BSON type for a string. */
	public static final ElementType TYPE = ElementType.REGEX;

	/** Option to make \w, \W, etc. match unicode. */
	public static final int UNICODE;

	/** Option for verbose mode. */
	public static final int VERBOSE;

	static {

		OPTION_I = 0x01;
		OPTION_L = 0x02;
		OPTION_M = 0x04;
		OPTION_S = 0x08;
		OPTION_U = 0x10;
		OPTION_X = 0x20;
		OPTION_MASK = 0x3F;

		CASE_INSENSITIVE = OPTION_I;
		LOCALE_DEPENDENT = OPTION_L;
		MULTILINE = OPTION_M;
		DOT_ALL = OPTION_S;
		UNICODE = OPTION_U;
		VERBOSE = OPTION_X;

		String[] options = new String[OPTION_MASK];

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < OPTION_MASK; ++i) {
			builder.setLength(0);

			// Options must be in alphabetic order.
			if ((i & OPTION_I) == OPTION_I) {
				builder.append('i');
			}
			if ((i & OPTION_L) == OPTION_L) {
				builder.append('l');
			}
			if ((i & OPTION_M) == OPTION_M) {
				builder.append('m');
			}
			if ((i & OPTION_S) == OPTION_S) {
				builder.append('s');
			}
			if ((i & OPTION_U) == OPTION_U) {
				builder.append('u');
			}
			if ((i & OPTION_X) == OPTION_X) {
				builder.append('x');
			}
			options[i] = builder.toString();
		}

		OPTIONS = options;
	}

	/** The BSON regular expression options. */
	private final int myOptions;

	/** The BSON regular expression pattern. */
	private final String myPattern;

	/**
	 * Constructs a new {@link RegularExpressionElement}.
	 * 
	 * @param name
	 *            The name for the BSON string.
	 * @param pattern
	 *            The BSON regular expression pattern.
	 * @param options
	 *            The BSON regular expression options.
	 */
	public RegularExpressionElement(String name, String pattern, int options) {
		super(TYPE, name);

		myPattern = pattern;
		myOptions = options;
	}

	/**
	 * Constructs a new {@link RegularExpressionElement}.
	 * 
	 * @param name
	 *            The name for the BSON string.
	 * @param pattern
	 *            The BSON regular expression pattern.
	 * @param options
	 *            The BSON regular expression options.
	 */
	public RegularExpressionElement(String name, String pattern, String options) {
		this(name, pattern, optionsAsInt(options));
	}

	/**
	 * Converts the options string into a options value.
	 * 
	 * @param options
	 *            The possibly non-normalized options string.
	 * @return The options integer value.
	 */
	private static int optionsAsInt(String options) {
		int optInt = 0;

		if (options != null) {
			for (char c : options.toCharArray()) {
				switch (c) {
				case 'i':
				case 'I':
					optInt |= OPTION_I;
					break;
				case 'l':
				case 'L':
					optInt |= OPTION_L;
					break;
				case 'm':
				case 'M':
					optInt |= OPTION_M;
					break;
				case 's':
				case 'S':
					optInt |= OPTION_S;
					break;
				case 'u':
				case 'U':
					optInt |= OPTION_U;
					break;
				case 'x':
				case 'X':
					optInt |= OPTION_X;
					break;
				default:
					throw new IllegalArgumentException(
							"Invalid regular expression option '" + c
									+ "' in options '" + options + "'.");
				}
			}
		}

		return optInt;
	}

	/**
	 * Returns the regular expression options.
	 * 
	 * @return The regular expression options.
	 */
	public int getOptions() {
		return myOptions;
	}

	/**
	 * Returns the regular expression pattern.
	 * 
	 * @return The regular expression pattern.
	 */
	public String getPattern() {
		return myPattern;
	}

	/**
	 * Accepts the visitor and calls the {@link Visitor#visitRegularExpression}
	 * method.
	 * 
	 * @see Element#accept(Visitor)
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visitRegularExpression(getName(), getPattern(),
				OPTIONS[getOptions() & OPTION_MASK]);
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
		result = 31 * result + ((myPattern != null) ? myPattern.hashCode() : 3);
		result = 31 * result + myOptions;
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
			RegularExpressionElement other = (RegularExpressionElement) object;

			result = (myOptions == other.myOptions) && super.equals(object)
					&& nullSafeEquals(myPattern, other.myPattern);
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
		builder.append("\" : /");
		builder.append(myPattern);
		builder.append("/");
		builder.append(OPTIONS[getOptions() & OPTION_MASK]);

		return builder.toString();
	}
}