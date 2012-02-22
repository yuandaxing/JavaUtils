package cgcl.data.structure;

import java.util.Iterator;

/*
 *  这个数据结构主要是用来保存状态，比方说保存从这个服务器登录的最新的5000名用户的登录事件
 * 或者保存最新5s内的用户。
 */

public abstract class AbstractStatsDeque implements Iterable<Double>{
	  public abstract Iterator<Double> iterator();
	    public abstract int size();
	    public abstract void add(double o);
	    public abstract void clear();
	    public double sum()
	    {
	        double sum = 0d;
	        for (Double interval : this)
	        {
	            sum += interval;
	        }
	        return sum;
	    }
	    
	    public double sumOfDeviations()
	    {
	        double sumOfDeviations = 0d;
	        double mean = mean();

	        for (Double interval : this)
	        {
	            double v = interval - mean;
	            sumOfDeviations += v * v;
	        }

	        return sumOfDeviations;
	    }
	    
	    public double mean()
	    {
	        return sum() / size();
	    }
	    
	    
}
