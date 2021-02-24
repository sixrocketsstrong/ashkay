/*
 *  Copyright 2004 David C. Brown
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sixrockets.ashkay.strategies;

import com.sixrockets.ashkay.ObjectCache;
import com.sixrockets.ashkay.strategies.FullCachingStrategy;

import junit.framework.TestCase;

/**
 *
 */
public class FullCachingStrategyTest extends TestCase {

  public void testBasicStrategy() throws Exception {
    MockObjectFactory factory = new MockObjectFactory();
    ObjectCache cache = new ObjectCache(factory);
    cache.addStrategy(new FullCachingStrategy());

    assertEquals("Bad initial count", 0, factory.counter);

    String key = "Test";
    cache.get(key);

    assertEquals("Factory not called initially", 1, factory.counter);

    cache.get(key);

    assertEquals("Factory called again.", 1, factory.counter);
  }
}
