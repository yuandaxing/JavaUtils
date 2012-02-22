package test;

import java.util.Iterator;

import cgcl.data.structure.RandomGenerator;
import cgcl.data.structure.TimedStatsDeque;

public class TestTimedStatsDeque {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		TimedStatsDeque td = new TimedStatsDeque(5000);
		
		for(int i = 0; i < 10; i++){
			td.add(RandomGenerator.getDouble(0, 10));
			Thread.sleep(1000);
		}
		Iterator<Double> iter = td.iterator();
		while(iter.hasNext()){
			System.out.print(iter.next().doubleValue() + "\t");
		}
		System.out.println("\nthe sum:"+td.sum());
		System.out.println("the mean:"+td.mean());
		System.out.println("the size:"+td.size());
		System.out.println("the sumOfDeviations:"+td.sumOfDeviations());

	}

}
