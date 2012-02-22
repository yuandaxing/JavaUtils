package cgcl.data.structure;

public interface ICacheExpungeHook<K,V>
{
    public void callMe(K key , V value);
}