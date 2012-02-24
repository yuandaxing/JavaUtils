/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package cgcl.data.structure;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
public abstract class Filter
{
    int hashCount;

    private static MurmurHash hasher = new MurmurHash();

    int getHashCount()
    {
        return hashCount;
    }

    public int[] getHashBuckets(String key)
    {
        return Filter.getHashBuckets(key, hashCount, buckets());
    }

    public int[] getHashBuckets(byte[] key)
    {
        return Filter.getHashBuckets(key, hashCount, buckets());
    }


    abstract int buckets();

    public abstract void add(String key);

    public abstract boolean isPresent(String key);

    // for testing
    abstract int emptyBuckets();

    ICompactSerializer<Filter> getSerializer()
    {
        Method method = null;
        try
        {
            method = getClass().getMethod("serializer");
            return (ICompactSerializer<Filter>) method.invoke(null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    // Murmur is faster than an SHA-based approach and provides as-good collision
    // resistance.  The combinatorial generation approach described in
    // http://www.eecs.harvard.edu/~kirsch/pubs/bbbf/esa06.pdf
    // does prove to work in actual tests, and is obviously faster
    // than performing further iterations of murmur.
    static int[] getHashBuckets(String key, int hashCount, int max)
    {
        byte[] b;
        try
        {
            b = key.getBytes("UTF-16");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        return getHashBuckets(b, hashCount, max);
    }

    static int[] getHashBuckets(byte[] b, int hashCount, int max)
    {
    	//这样做效率肯定不好，比方说有一万个key需要hash，则需要大量的申请释放空间？？？
        int[] result = new int[hashCount];
        int hash1 = hasher.hash(b, b.length, 0);
        int hash2 = hasher.hash(b, b.length, hash1);
        for (int i = 0; i < hashCount; i++)
        {
        	//这是一种比较常见的双hash方法
            result[i] = Math.abs((hash1 + i * hash2) % max);
        }
        return result;
    }
}