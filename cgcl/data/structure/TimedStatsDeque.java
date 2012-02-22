package cgcl.data.structure;

import java.util.ArrayDeque;
import java.util.Iterator;

public class TimedStatsDeque extends AbstractStatsDeque {
	static public class Tuple
	{
	    public final double value;
	    public final long timestamp;

	    public Tuple(double value, long timestamp)
	    {
	        this.value = value;
	        this.timestamp = timestamp;
	    }
	}
	
	
	   private final ArrayDeque<Tuple> deque;
	    private final long period;
	    
	    public TimedStatsDeque(long period){
	    	deque = new ArrayDeque<Tuple>();
	    	this.period = period;
	    }
	    private void purge(){
	    	long cur = System.currentTimeMillis();
	    	while(!deque.isEmpty() && cur - deque.peek().timestamp > period){
	    		deque.remove();
	    	}
	    }
	    
	@Override
	public synchronized  Iterator<Double> iterator() {
		// TODO Auto-generated method stub
		ArrayDeque<Double> dq = new ArrayDeque<Double>();
		purge();
		
		for(Tuple t : deque){
			dq.add(t.value);
		}
		return dq.iterator();
	}

	@Override
	public synchronized int size() {
		// TODO Auto-generated method stub
		purge();
		return deque.size();
	}

	@Override
	public synchronized void  add(double o) {
		// TODO Auto-generated metod stub
		purge();
		deque.addLast(new Tuple(o, System.currentTimeMillis()));
	}

	@Override
	public synchronized void clear() {
		// TODO Auto-generated method stub
			deque.clear();
	}

}
