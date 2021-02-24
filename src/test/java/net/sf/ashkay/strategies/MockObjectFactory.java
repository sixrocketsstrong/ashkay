/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package net.sf.ashkay.strategies;

import net.sf.ashkay.ObjectFactory;
import net.sf.ashkay.CreationException;

/**
 *
 */
public class MockObjectFactory implements ObjectFactory
{
	public int counter = 0;
	public Object createObjectFor(Object key, Object data) throws CreationException
	{
		counter++;
		return new Object();
	}
}
