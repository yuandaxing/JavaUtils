package test;

import java.util.HashSet;

import cgcl.data.structure.Cachetable;
import cgcl.data.structure.ICacheExpungeHook;
import cgcl.data.structure.RandomGenerator;

/*
 * CacheTable 充分利用了内部类可以直接访问外部类的特点，由monitor将数据从hashtable中剔除
 * 这里最好使用hashMap，因为HashTable是线程安全的，效率低，
 * 这点和vector,ArrayList之间的关系是一样的
 * 然后当数据从hash表中剔除，可以安装hook，这样程序的可扩展性就很好
 * 而且这里使用abstract programming
 */



class MyHook implements ICacheExpungeHook<String, String>{
	@Override
	public void callMe(String key, String value) {
		// TODO Auto-generated method stub
		System.err.println("expire key:" + key+"\tval:"+value);
	}
	
}
public class TestCachetable {
	
	public static void main(String[]args) throws InterruptedException{
		Cachetable<String, String>  ct = new Cachetable<String, String>(5000);
		HashSet<String> hs = new HashSet<String>();
		for(int i = 0; i < 10; i++){
			String s1 = RandomGenerator.getString(10);
			String s2 = RandomGenerator.getString(10);
			hs.add(s1);
			ct.put(s1, s2, new MyHook() );
			Thread.sleep(1000);
		}
		
		for(String key : hs){
			String val = ct.get(key);
			System.out.println("key :" + key+"\t"+val);
		}
	}
	

}
