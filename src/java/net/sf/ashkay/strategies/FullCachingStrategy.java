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

import net.sf.ashkay.CacheEntry;
import net.sf.ashkay.CachingStrategy;


/**
 * FullCachingStrategy implements the CachingStrategy interface to provide a
 * hard cache with no expiration. This means that the cache stores a full
 * reference to any object and the factory will not be called for this object
 * again, unless the ObjectCache is cleared.
 *
 * @author <a href="mailto:bangroot@users.sf.net">Dave Brown</a>
 * @see CachingStrategy
 */
public class FullCachingStrategy implements CachingStrategy
{
	public CacheEntry prepare(CacheEntry entry)
	{
		return entry;
	}

	public boolean validate(CacheEntry entry)
	{
		return true;
	}
}
