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
 * TimeExpirationCachingStrategy expires objects in the cache after a set
 * amount of time.
 * @author <a href="mailto:bangroot@users.sf.net">Dave Brown</a>
 */

public class TimeExpirationCachingStrategy implements CachingStrategy
{
    private static final String ENTRY_KEY =
            "com.cepm_us.util.cache.TimeExpirationCachingStrategy.Timeout";

    private long expirationTime;

    /**
     * Creates the caching strategy with the specified timeout.
     * @param timeOut - the time out length for the cache entries
     */
    public TimeExpirationCachingStrategy(long timeOut)
    {
        expirationTime = timeOut;
    }

    public CacheEntry prepare(CacheEntry entry)
    {
        entry.addProperty(ENTRY_KEY, new Long(System.currentTimeMillis()));
        return entry;
    }

    public boolean validate(CacheEntry entry)
    {
        boolean val = true;
        Object temp = entry.getProperty(ENTRY_KEY);
        if ((temp != null) && (temp instanceof Long))
        {
            long cachedTime = ((Long) temp).longValue();
            long currentTime = System.currentTimeMillis();
            if (cachedTime + expirationTime <= currentTime)
            {
                val = false;
            }

        }
        else
        {
            val = false;
        }

        return val;
    }
}