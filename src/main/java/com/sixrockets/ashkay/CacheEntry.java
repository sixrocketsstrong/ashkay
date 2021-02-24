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
package com.sixrockets.ashkay;

import java.util.HashMap;
import java.util.Map;

/**
 * CacheEntry represents a single entry in the object cache. A cache entry
 * contains the cached object and properties associated with the entry. These
 * properties may be used by the CachingStrategy to detirmine if an entry
 * passes validation.
 *
 * @author <a href="mailto:dave@sixrockets.com">Dave Brown</a>
 */

public class CacheEntry<T, K> {

  private T cachedObject;

  private K key;
  private Map properties;
  private ObjectCache<T, K> myCache;

  /**
   * Empty constructor for subclasses only
   */
  protected CacheEntry() {}

  /**
   * Creates a default CacheEntry
   *
   * @param entryObject - the object this entry represents
   */
  public CacheEntry(K entryKey, T entryObject) {
    this(entryKey, entryObject, new HashMap(), null);
  }

  /**
   * Creates a CacheEntry with properties pre-specified
   *
   * @param entryObject     - the object this entry represents
   * @param entryProperties - the properties of this entry
   */
  public CacheEntry(K entryKey, T entryObject, Map entryProperties) {
    this(entryKey, entryObject, entryProperties, null);
  }

  public CacheEntry(K entryKey, T entryObject, Map entryProperties, ObjectCache theCache) {
    key = entryKey;
    cachedObject = entryObject;
    properties = entryProperties;
    myCache = theCache;
  }

  public Object getEntryKey() {
    return key;
  }

  public void setEntryKey(K key) {
    this.key = key;
  }

  /**
   * Returns the object this entry represents.
   *
   * @return the entries object
   */
  public T getEntryObject() {
    return cachedObject;
  }

  /**
   * Sets the object this entry represents
   *
   * @param entryObject - the new object to represent
   */
  public void setEntryObject(T entryObject) {
    cachedObject = entryObject;
  }

  /**
   * Adds a property to this entry.
   *
   * @param key   - the key to this property
   * @param value - the value to hold in the property
   */
  public void addProperty(Object key, Object value) {
    synchronized (properties) {
      properties.put(key, value);
    }
  }

  /**
   * Looks up a property of this entry
   *
   * @param key - the key of the property to lookup
   * @return the value of the property or null if not set
   */
  public Object getProperty(Object key) {
    synchronized (properties) {
      return properties.get(key);
    }
  }

  /**
   * Adds a group of properties to this entry, en masse
   *
   * @param propertiesToAdd - map of properties to add
   */
  public void addProperties(Map propertiesToAdd) {
    synchronized (properties) {
      properties.putAll(propertiesToAdd);
    }
  }

  /**
   * Returns the entire Map of this entry's properties
   *
   * @return the properties
   */
  public Map getProperties() {
    return properties;
  }

  public ObjectCache<T, K> getCache() {
    return myCache;
  }

  public void setCache(ObjectCache<T, K> myCache) {
    this.myCache = myCache;
  }
}
