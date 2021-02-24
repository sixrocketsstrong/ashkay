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
package net.sf.ashkay;

import junit.framework.TestCase;

/**
 *
 */
public class BasicCacheTest extends TestCase
{
	public void testBasicCache() throws Exception
	{
		ObjectCache cache = new ObjectCache();
		assertEquals("Cache not empty", 0, cache.size());
		Object value = new Object();
		cache.put("Test", value);
		assertEquals("Cache empty", 1, cache.size());
		cache.put("Test2", new Object());
		assertEquals("Cache not correct size", 2, cache.size());
		assertEquals("Wrong object retrieved", value, cache.get("Test"));
		cache.evict("Test");
		assertEquals("Object not evicted", 1, cache.size());
		assertNull("Object still returned", cache.get("Test"));
		cache.clear();
		assertEquals("Cache not cleared", 0, cache.size());
	}

}
