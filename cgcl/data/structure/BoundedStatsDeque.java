package cgcl.data.structure;

import java.util.ArrayDeque;
import java.util.Iterator;

public class BoundedStatsDeque extends AbstractStatsDeque {

	private final int size;
	private final ArrayDeque<Double> deque;
	
	public BoundedStatsDeque(int size){
		deque = new ArrayDeque<Double>(size);
		this.size = size;
	}
	
	@Override
	public Iterator<Double> iterator() {
		// TODO Auto-generated method stub
		return deque.iterator();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return deque.size();
	}

	@Override
	public void add(double o) {
		// TODO Auto-generated method stub
		if(deque.size() == size){
			deque.removeFirst();
		}
		deque.add(o);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		deque.clear();
	}
	

}
