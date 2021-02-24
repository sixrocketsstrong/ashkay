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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;

import net.sf.ashkay.CacheEntry;
import net.sf.ashkay.ObjectCache;

/**
 * SoftReferenceCacheEntry represents a cache entry that is wrapped in which
 * the entry object is wrapped in a SoftReference in order to allow the
 * garbage collector to still collect the object if all other hard references
 * are destroyed.
 * <br><br>
 * Often, this strategy will be used in memory sensative caches.
 *
 * @author <a href="mailto:bangroot@users.sf.net">Dave Brown</a>
 * @see java.lang.ref.SoftReference
 */

public class SoftReferenceCacheEntry extends CacheEntry
{
	private CacheEntry theEntry;

	public SoftReferenceCacheEntry(CacheEntry originalEntry, ReferenceQueue queue)
	{
		super();
		theEntry = originalEntry;
		theEntry.setEntryObject(new SoftReference(theEntry.getEntryObject(), queue));
	}

	SoftReference getEntryReference()
	{
		return (SoftReference) theEntry.getEntryObject();
	}

	public Object getEntryObject()
	{
		SoftReference ref = (SoftReference) theEntry.getEntryObject();
		return ref.get();
	}

	public void addProperties(Map propertiesToAdd)
	{
		theEntry.addProperties(propertiesToAdd);
	}

	public void addProperty(Object key, Object value)
	{
		theEntry.addProperty(key, value);
	}

	public Object getProperty(Object key)
	{
		return theEntry.getProperty(key);
	}

	public void setEntryObject(Object entryObject)
	{
		SoftReference ref = new SoftReference(entryObject);
		theEntry.setEntryObject(ref);
	}

	public Object getEntryKey()
	{
		return theEntry.getEntryKey();
	}

	public void setEntryKey(Object key)
	{
		theEntry.setEntryKey(key);
	}

	public Map getProperties()
	{
		return theEntry.getProperties();
	}

	public ObjectCache getCache()
	{
		return theEntry.getCache();
	}

	public void setCache(ObjectCache myCache)
	{
		theEntry.setCache(myCache);
	}
}