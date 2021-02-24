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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.IdentityHashMap;
import java.util.Map;

import net.sf.ashkay.CacheEntry;
import net.sf.ashkay.CachingStrategy;


/**
 * A Soft Reference caching strategy uses soft references to store objects in
 * the cache. Soft references do not prevent the garbage collection of the
 * objects.
 *
 * @author <a href="mailto:bangroot@users.sf.net">Dave Brown</a>
 */

public class SoftReferenceCachingStrategy implements CachingStrategy
{
	private Map entryMap = new IdentityHashMap();
	private ReferenceQueue queue = new ReferenceQueue();

	public CacheEntry prepare(CacheEntry entry)
	{
		checkQueue();

		SoftReferenceCacheEntry theEntry = new SoftReferenceCacheEntry(entry, queue);
		entryMap.put(theEntry.getEntryReference(), theEntry);
		return theEntry;
	}


	public boolean validate(CacheEntry entry)
	{
		checkQueue();

		boolean val = true;
		if (entry.getEntryObject() == null)
		{
			val = false;
		}

		return val;
	}

	private void checkQueue()
	{
		Reference ref = queue.poll();
		while (ref != null)
		{
			CacheEntry entryToClear = (CacheEntry) entryMap.get(ref);
			if (entryToClear != null)
			{
				entryToClear.getCache().evict(entryToClear.getEntryKey());
				entryMap.remove(ref);
			}
			ref = queue.poll();
		}
	}
}