package test;

import java.util.ArrayList;
import java.util.HashSet;

import cgcl.data.structure.BloomFilter;
import cgcl.data.structure.RandomGenerator;

public class TestBloomFilter {

	private static final int nkeys = 100000; 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<String> keys = new HashSet<String>();
		ArrayList<String> tests = new ArrayList<String>();
 		for(int i = 0; i < nkeys; i++){
			keys.add(RandomGenerator.getString(16));
			tests.add(RandomGenerator.getString(11));
		}
		
		BloomFilter bf = BloomFilter.getFilter(nkeys, 16);
		
		for(String key : keys){
			bf.add(key);
		}
		
		int count = 0;
		for(String t : tests){
			if(bf.isPresent(t)){
				count++;
			//	System.out.println("collision" + count+":"+t);
			}
		}
		
		System.out.println("total collision:" + count);
	}

}
