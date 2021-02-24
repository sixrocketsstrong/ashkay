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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import EDU.oswego.cs.dl.util.concurrent.Latch;
import org.apache.log4j.Logger;


/**
 * ObjectCache is the default implementation of an object caching mechanism.
 * <br><br>
 * ObjectCaching makes use of a Factory for the creation of objects that are not
 * cached. The user of an ObjectCache provides an implementation of
 * ObjectFactory to the cache. If the cache does not find the object asked for,
 * it uses the factory to create the object, cache it, and return it.
 * <br><br>
 * Looking up an object from cache requires a key. The key is a unique
 * identifier. The key could be a database record id, a URI, or any other id
 * that is guaranteed to be unique. The key is also used by the factory to
 * create the object. The factory ought to be able to construct an object given
 * only a key. This key is then used as the key in the cache.
 * <br><br>
 * Gets may also be done with a data parameter. The data provides construction
 * information to the factory about the object being created. The cache does not
 * use the data directly but, if the object is not cached, will pass the data
 * to the factory. Common use of the data would be if the user of the cache
 * has an "un-objectified" data source (e.g. XML document) from some other
 * source that would save the factory the lookup call. So, if the user has the
 * key and the data and wants the object form of the data, it simply asks the
 * cache passing in the key and data.
 * <br><br>
 * The ObjectCache uses CachingStrategies to detirmine a strategy for caching
 * objects that is stores. These strategies may affect caching in any number of
 * ways. You might want to use a cache that does not prevent garbage collection
 * of cached objects, only getting benefit of the cache as memory allows. A
 * client may want to place timeouts for objects to force them to be re-loaded
 * after a period of time. Any number of caching strategies may be used, but be
 * careful not to use two caching strategies that work in opposition. That is
 * a check left to the user at this point.
 *
 * @author <a href="mailto:bangroot@users.sf.net">Dave Brown</a>
 */
public class ObjectCache
{
	private static final Logger LOG = Logger.getLogger(ObjectCache.class);

	private ObjectFactory factory;
	private List strategies = new ArrayList();
	private Map cache = new HashMap();

	public ObjectCache()
	{
		this(null, new ArrayList());
	}

	/**
	 * Constructs an ObjectCache from a factory using the default strategy of
	 * NONE.
	 *
	 * @param aFactory - the factory for the cache to use
	 */

	public ObjectCache(ObjectFactory aFactory)
	{
		this(aFactory, new ArrayList());
	}

	/**
	 * Constructs an ObjectCache without a factory using the strategies indicated.
	 *
	 * @param theStrategies
	 */

	public ObjectCache(List theStrategies)
	{
		this(null, theStrategies);
	}

	/**
	 * Constructs an ObjectCache from a factory and strategy.
	 *
	 * @param aFactory      - the factory for the cache to use
	 * @param theStrategies - the caching strategies for this cache
	 */
	public ObjectCache(ObjectFactory aFactory, List theStrategies)
	{
		factory = aFactory;
		strategies = theStrategies;
	}

	/**
	 * Adds a caching strategy to this cache
	 *
	 * @param aStrategy - the strategy to add
	 */
	public ObjectCache addStrategy(CachingStrategy aStrategy)
	{
		synchronized (strategies)
		{
			strategies.add(aStrategy);
		}

		return this;
	}

	/**
	 * Removes a strategy from this cache
	 *
	 * @param aStrategy - the strategy to remove
	 */
	public ObjectCache removeStrategy(CachingStrategy aStrategy)
	{
		synchronized (strategies)
		{
			strategies.remove(aStrategy);
		}

		return this;
	}

	/**
	 * Checks if this cache uses the specified caching strategy
	 *
	 * @param aStrategy - the strategy to check
	 */
	public boolean usesStrategy(CachingStrategy aStrategy)
	{
		synchronized (strategies)
		{
			return strategies.contains(aStrategy);
		}
	}

	/**
	 * Finds an object in the cache and returns it. If the cache contains no
	 * object for the specified key, the cache attempts to construct it using
	 * its factory.
	 *
	 * @param key - the key to lookup
	 * @throws CreationException on any error during creation
	 * @see #get(java.lang.Object, java.lang.Object)
	 */
	public Object get(Object key) throws CreationException
	{
		return this.get(key, null);
	}

	/**
	 * Forcefully evicts an object/key from the cache. This will only evict the
	 * object if it has been created (i.e. not being created by a factory).
	 *
	 * @param key to the object to evict
	 * @return the object evicted or null if none
	 */
	public Object evict(Object key)
	{
		Object val = null;
		Object entry = cache.get(key);
		if (entry instanceof CacheEntry)
		{
			val = ((CacheEntry) entry).getEntryObject();
			synchronized (cache)
			{
				cache.remove(key);
			}
		}
		return val;
	}

	/**
	 * Finds an object in the cache and returns it. If the cache contains no
	 * object for the specified key, the cache attempts to construct it using
	 * its factory and the passed in data. The cache also uses the caching
	 * strategies to validate the entry in the cache.
	 *
	 * @param key  - the key to lookup
	 * @param data - the data to aid construction
	 * @throws CreationException on any error during creation
	 */
	public Object get(Object key, Object data) throws CreationException
	{
		CacheEntry entry = null;
		entry = findCacheEntry(key);

		boolean passes = validateEntry(entry);

		if (!passes)
		{
			Latch buildLatch = null;
			buildLatch = prepareLatch(key);
			try
			{
				entry = createObjectFor(key, data);
			}
			finally
			{
				if (null == entry)
				{
					synchronized (cache)
					{
						cache.put(key, null);
					}
				}
				buildLatch.release();
			}
		}

		Object val = null;
		if (null != entry)
		{
			val = entry.getEntryObject();
		}

		return val;
	}

	private CacheEntry createObjectFor(Object key, Object data) throws CreationException
	{
		CacheEntry entry = null;

		if (null != factory)
		{
			Object tempO = factory.createObjectFor(key, data);
			if (null != tempO)
			{
				entry = new CacheEntry(key, tempO);
				entry.setCache(this);
				entry = prepareEntry(entry);
			}
		}

		synchronized (cache)
		{
			cache.put(key, entry);
		}

		return entry;
	}

	private CacheEntry prepareEntry(CacheEntry entry)
	{
		synchronized (strategies)
		{
			Iterator strategyIter = strategies.iterator();
			while (strategyIter.hasNext())
			{
				CachingStrategy strat = (CachingStrategy) strategyIter.next();
				entry = strat.prepare(entry);
			}
		}
		return entry;
	}

	private Latch prepareLatch(Object key)
	{
		Latch buildLatch;
		synchronized (cache)
		{
			buildLatch = new Latch();
			cache.put(key, buildLatch);
		}
		return buildLatch;
	}

	private boolean validateEntry(CacheEntry entry)
	{
		boolean passes = false;
		if (entry != null)
		{
			passes = true;
			synchronized (strategies)
			{
				Iterator strategyIter = strategies.iterator();
				while (strategyIter.hasNext())
				{
					CachingStrategy strat = (CachingStrategy) strategyIter.next();
					if (!strat.validate(entry))
					{
						passes = false;
					}
				}
			}
		}
		return passes;
	}

	private CacheEntry findCacheEntry(Object key) throws CreationException
	{
		CacheEntry entry = null;
		Object cacheObject = cache.get(key);

		if (cacheObject != null)
		{
			if (cacheObject instanceof Latch)
			{
				try
				{
					((Latch) cacheObject).acquire();
				}
				catch (InterruptedException e)
				{
					throw new CreationException(e);
				}

				entry = findCacheEntry(key);
			}
			else
			{
				entry = (CacheEntry) cacheObject;
			}
		}

		return entry;
	}

	public void put(Object key, Object value)
	{
		CacheEntry entry = new CacheEntry(key, value);
		entry.setCache(this);
		entry = prepareEntry(entry);
		cache.put(key, entry);
	}

	/**
	 * Clears the cache
	 */
	public void clear()
	{
		cache.clear();
	}

	/**
	 * Returns the size of the cache
	 *
	 * @return int size of the cache
	 */
	public int size()
	{
		return cache.size();
	}
}
