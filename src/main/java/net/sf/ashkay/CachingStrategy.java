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




/**
 * CachingStrategies provide abstract strategies about how a cache ought to
 * perform its caching. When an object is added to the cache, the
 * CachingStrategy is asked to prepare the CacheEntry for that object. The
 * CachingStrategy may return a new Entry that serves its purposes better, or
 * may just set a few properties on the current entry.
 * <br><br>
 * When the object is looked up in the cache, the CachingStrategy is asked to
 * validate this object, if the validation fails for any strategy, the object is
 * reloaded.
 *
 * @author <a href="mailto:bangroot@users.sf.net">Dave Brown</a>
 */
public interface CachingStrategy {

    /**
     * Prepares the cache entry for caching with this strategy.<br>
     * <b>NOTE:</b> Be Careful: a caching strategy <em>is</em> allowed to return
     * a different CacheEntry, so make sure that you store the results of this
     * method and don't assume the symantics of pass by reference.
     */
    public abstract CacheEntry prepare(CacheEntry entry);

    /**
     * Validates this cache entry for this caching strategy.
     */
    public abstract boolean validate(CacheEntry entry);
}
