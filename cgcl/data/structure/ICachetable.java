package cgcl.data.structure;

import java.util.Set;



public interface ICachetable<K,V>
{
    public void put(K key, V value);
    public void put(K key, V value, ICacheExpungeHook<K,V> hook);
	public V get(K key);
    public V remove(K key);
    public int size();
    public boolean containsKey(K key);
    public boolean containsValue(V value);
    public boolean isEmpty();    
    public Set<K> keySet();
    public void shutdown();
}
