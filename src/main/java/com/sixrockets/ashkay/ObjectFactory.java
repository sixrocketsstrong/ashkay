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

/**
 * ObjectFactory is the factory for creating objects used by the ObjectCache.
 * Implementations of ObjectFactories will need to exist for each type of object
 * to be cached.
 *
 * @see com.sixrockets.ashkay.ObjectCache
 * @author <a href="mailto:dave@sixrockets.com">Dave Brown</a>
 */
public interface ObjectFactory<T, K> {
  T createObjectFor(K key, Object data) throws CreationException;
}
