/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package com.sixrockets.ashkay.strategies;

import com.sixrockets.ashkay.CreationException;
import com.sixrockets.ashkay.ObjectFactory;

/**
 *
 */
public class MockObjectFactory implements ObjectFactory {

  public int counter = 0;

  public Object createObjectFor(Object key, Object data) throws CreationException {
    counter++;
    return new Object();
  }
}
