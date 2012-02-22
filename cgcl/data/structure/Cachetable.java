package cgcl.data.structure;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;



public class Cachetable<K,V> implements ICachetable<K,V>
{
    private class CacheableObject<V>
    {
        private V value_;
        private long age_;
        
        CacheableObject(V o)
        {
            value_ = o;
            age_ = System.currentTimeMillis();
        }

        public boolean equals(Object o)
        {
            return value_.equals(o);
        }

        public int hashCode()
        {
            return value_.hashCode();
        }

        V getValue()
        {
            return value_;
        }           
        
        boolean isReadyToDie(long expiration)
        {
            return ( (System.currentTimeMillis() - age_) > expiration );
        }
    }
    
    private class CacheMonitor extends TimerTask
    {
        private long expiration_;
        
        CacheMonitor(long expiration)
        {
            expiration_ = expiration;
        }
        
        public void run()
        {  
            Map<K,V> expungedValues = new HashMap<K,V>();            
            synchronized(cache_)
            {
                Enumeration<K> e = cache_.keys();
                while( e.hasMoreElements() )
                {        
                    K key = e.nextElement();
                    CacheableObject<V> co = cache_.get(key);
                    if ( co != null && co.isReadyToDie(expiration_) )
                    {
                        V v = co.getValue();
                        if(null != v)
                            expungedValues.put(key, v);
                        cache_.remove(key);                                       
                    }
                }
            }
            
            /* Calling the hooks on the keys that have been expunged */
            Set<K> keys = expungedValues.keySet();                                               
            for ( K key : keys )
            {                                
                V value = expungedValues.get(key);
                ICacheExpungeHook<K,V> hook = hooks_.remove(key);
                try 
                {
                    if ( hook != null )
                    {
                        hook.callMe(key, value);                    
                    }
                    else if ( globalHook_ != null )
                    {
                        globalHook_.callMe(key, value);
                    }
                }
                catch(Throwable e)
                {
                   e.printStackTrace();
                }
            }
            expungedValues.clear();
        }
    }   

    private ICacheExpungeHook<K,V> globalHook_;
    private Hashtable<K, CacheableObject<V>> cache_;
    private Map<K, ICacheExpungeHook<K,V>> hooks_;
    private Timer timer_;
    private static int counter_ = 0;

    private void init(long expiration)
    {
        if ( expiration <= 0 )
            throw new IllegalArgumentException("Argument specified must be a positive number");

        cache_ = new Hashtable<K, CacheableObject<V>>();
        hooks_ = new Hashtable<K, ICacheExpungeHook<K,V>>();
        timer_ = new Timer("CACHETABLE-TIMER-" + (++counter_), true);        
        timer_.schedule(new CacheMonitor(expiration), expiration, expiration);
    }
    
    /*
     * Specify the TTL for objects in the cache
     * in milliseconds.
     */
    public Cachetable(long expiration)
    {
        init(expiration);   
    }    
    
    /*
     * Specify the TTL for objects in the cache
     * in milliseconds and a global expunge hook. If
     * a key has a key-specific hook installed invoke that
     * instead.
     */
    public Cachetable(long expiration, ICacheExpungeHook<K,V> global)
    {
        init(expiration);
        globalHook_ = global;        
    }
    
    public void shutdown()
    {
        timer_.cancel();
    }
    
    public void put(K key, V value)
    {        
        cache_.put(key, new CacheableObject<V>(value));
    }

    public void put(K key, V value, ICacheExpungeHook<K,V> hook)
    {
        put(key, value);
        hooks_.put(key, hook);
    }

    public V get(K key)
    {
        V result = null;
        CacheableObject<V> co = cache_.get(key);
        if ( co != null )
        {
            result = co.getValue();
        }
        return result;
    }

    public V remove(K key)
    {
        CacheableObject<V> co = cache_.remove(key);
        V result = null;
        if ( co != null )
        {
            result = co.getValue();
        }
        return result;
    }

    public int size()
    {
        return cache_.size();
    }

    public boolean containsKey(K key)
    {
        return cache_.containsKey(key);
    }

    public boolean containsValue(V value)
    {
        return cache_.containsValue( new CacheableObject<V>(value) );
    }

    public boolean isEmpty()
    {
        return cache_.isEmpty();
    }

    public Set<K> keySet()
    {
        return cache_.keySet();
    }    
}

