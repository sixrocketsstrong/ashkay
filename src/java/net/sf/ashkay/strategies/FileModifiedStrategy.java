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

import java.io.File;

import net.sf.ashkay.CacheEntry;
import net.sf.ashkay.CachingStrategy;


public class FileModifiedStrategy implements CachingStrategy
{
	private static final String PROPERTY_MODIFIED_TIME = FileModifiedStrategy.class.getName() + ".fileModified";
	/**
	 * Prepares the cache entry for caching with this strategy.<br>
	 * <b>NOTE:</b> Be Careful: a caching strategy <em>is</em> allowed to return
	 * a different CacheEntry, so make sure that you store the results of this
	 * method and don't assume the symantics of pass by reference.
	 */
	public CacheEntry prepare(CacheEntry entry)
	{
		File entryFile = getEntryFile(entry);
		long modifiedTime = 0;
		if (entryFile != null)
		{
			modifiedTime = entryFile.lastModified();
		}
		entry.addProperty(PROPERTY_MODIFIED_TIME, new Long(modifiedTime));
		return entry;
	}

	/**
	 * Validates this cache entry for this caching strategy.
	 */
	public boolean validate(CacheEntry entry)
	{
		boolean val = true;
		Long entry_modified = (Long) entry.getProperty(PROPERTY_MODIFIED_TIME);
		long file_modified = 0;

		File entryFile = getEntryFile(entry);

		if (entryFile != null)
		{
			file_modified = entryFile.lastModified();

			if (entry_modified != null)
			{
				if (file_modified != entry_modified.longValue())
				{
					val = false;
				}
			}
		}

		return val;
	}

	private File getEntryFile(CacheEntry entry)
	{
		File val = null;

		if (entry.getEntryKey() instanceof File)
		{
			String fileName = ((File) entry.getEntryKey()).getAbsolutePath();
			val = new File(fileName);
		}
		else if (entry.getEntryKey() instanceof String)
		{
			String fileName = (String) entry.getEntryKey();
			File entryFile = new File(fileName);
			if (entryFile.exists())
			{
				val = entryFile;
			}
		}

		return val;
	}

}
