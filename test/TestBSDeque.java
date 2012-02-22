package test;

import java.util.Iterator;
import java.util.Random;

import cgcl.data.structure.BoundedStatsDeque;
import cgcl.data.structure.RandomGenerator;

public class TestBSDeque {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BoundedStatsDeque bsd = new BoundedStatsDeque(10);
		
		for(int i = 0; i < 2; i++){
			bsd.add(RandomGenerator.getDouble(0, 10));
		}
		Iterator<Double> iter = bsd.iterator();
		while(iter.hasNext()){
			System.out.print(iter.next().doubleValue() + "\t");
		}
		System.out.println("\nthe sum:"+bsd.sum());
		System.out.println("the mean:"+bsd.mean());
		System.out.println("the size:"+bsd.size());
		System.out.println("the sumOfDeviations:"+bsd.sumOfDeviations());
		
	}

}
