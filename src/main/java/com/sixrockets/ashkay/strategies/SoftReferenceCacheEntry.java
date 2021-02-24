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

import com.sixrockets.ashkay.CacheEntry;
import com.sixrockets.ashkay.ObjectCache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * SoftReferenceCacheEntry represents a cache entry that is wrapped in which
 * the entry object is wrapped in a SoftReference in order to allow the
 * garbage collector to still collect the object if all other hard references
 * are destroyed.
 * <br><br>
 * Often, this strategy will be used in memory sensative caches.
 *
 * @author <a href="mailto:dave@sixrockets.com">Dave Brown</a>
 * @see java.lang.ref.SoftReference
 */

public class SoftReferenceCacheEntry<T extends Object, K> extends CacheEntry<T, K> {

	private CacheEntry<Object, K> theEntry;

	public SoftReferenceCacheEntry(CacheEntry<T, K> originalEntry, ReferenceQueue<T> queue) {
		super();
		theEntry = (CacheEntry<Object, K>) originalEntry;
		theEntry.setEntryObject(new SoftReference<T>((T) theEntry.getEntryObject(), queue));
	}

	SoftReference<T> getEntryReference() {
		return (SoftReference<T>) theEntry.getEntryObject();
	}

	public T getEntryObject() {
		SoftReference<T> ref = (SoftReference<T>) theEntry.getEntryObject();
		return ref.get();
	}

	public void addProperties(Map propertiesToAdd) {
		theEntry.addProperties(propertiesToAdd);
	}

	public void addProperty(Object key, Object value) {
		theEntry.addProperty(key, value);
	}

	public Object getProperty(Object key) {
		return theEntry.getProperty(key);
	}

	public void setEntryObject(Object entryObject) {
		SoftReference ref = new SoftReference(entryObject);
		theEntry.setEntryObject(ref);
	}

	public Object getEntryKey() {
		return theEntry.getEntryKey();
	}

	public void setEntryKey(K key) {
		theEntry.setEntryKey(key);
	}

	public Map getProperties() {
		return theEntry.getProperties();
	}

	public ObjectCache getCache() {
		return theEntry.getCache();
	}

	public void setCache(ObjectCache myCache) {
		theEntry.setCache(myCache);
	}
}
