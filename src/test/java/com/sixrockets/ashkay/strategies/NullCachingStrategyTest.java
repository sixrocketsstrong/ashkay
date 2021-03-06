/*
 * Copyright (c) 2004 Your Corporation. All Rights Reserved.
 */
package com.sixrockets.ashkay.strategies;

import com.sixrockets.ashkay.ObjectCache;
import com.sixrockets.ashkay.strategies.NullCachingStrategy;

import junit.framework.TestCase;

/**
 *
 */
public class NullCachingStrategyTest extends TestCase {

  public void testCachingStrategy() throws Exception {
    MockObjectFactory factory = new MockObjectFactory();
    ObjectCache cache = new ObjectCache(factory);
    cache.addStrategy(new NullCachingStrategy());

    assertEquals("Bad starting counter", 0, factory.counter);

    String key = "Test";
    cache.get(key);

    assertEquals("Factory not called", 1, factory.counter);

    cache.get(key);

    assertEquals("Factory not recalled", 2, factory.counter);
  }
}
