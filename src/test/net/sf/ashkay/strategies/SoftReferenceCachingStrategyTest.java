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
package net.sf.ashkay.strategies;

import junit.framework.TestCase;
import net.sf.ashkay.ObjectCache;
import net.sf.ashkay.CreationException;

/**
 *
 */
public class SoftReferenceCachingStrategyTest extends TestCase
{
	public void testCachingStrategy() throws Exception
	{
		MockObjectFactory factory = new MockObjectFactory();
		ObjectCache cache = new ObjectCache(factory);
		cache.addStrategy(new SoftReferenceCachingStrategy());

		assertEquals("Bad initial counter", 0, factory.counter);

		String key = "Test";
		cache.get(key);

		assertEquals("Factory not called initially",1,factory.counter);

		int expected = createNewObject(cache, key, factory, 1);

		cache.get(key);
		assertEquals("Factory not called after death", (expected + 1), factory.counter);
	}

	private int createNewObject(ObjectCache cache, String key, MockObjectFactory factory, int currentCount) throws CreationException
	{
		cache.get(key);
		assertEquals("Factory called when object alive", currentCount, factory.counter);
		for (int i = 0; i < 10000; i++)
		{
			cache.get(new Integer(i));
			currentCount++;
		}
		System.gc();
		return currentCount;
	}
}
