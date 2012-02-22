package cgcl.data.structure;

public class Pair<T1, T2> {

	T1 left;
	T2 right;
	public Pair(T1 left, T2 right){
		this.left = left;
		this.right = right;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof Pair))
			return false;
		if(this == obj)
			return true;
		Pair o = (Pair) obj;
		return left.equals(o.left) && right.equals(o.right);
	}


	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int hash = left == null ?  0  :  left.hashCode();
		hash = hash * 53 +  (right == null ? 0 : right.hashCode());
		return hash;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Pair(left:"+left.toString()+"  right:"+right.toString()+")";
	}


	public T1 getLeft() {
		return left;
	}


	public void setLeft(T1 left) {
		this.left = left;
	}


	public T2 getRight() {
		return right;
	}


	public void setRight(T2 right) {
		this.right = right;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pair<Integer, Integer> pair1 = new  Pair<Integer, Integer>(9, 10);
		Pair<Integer, Integer> pair2 = new  Pair<Integer, Integer>(9, 10);
		Pair<Integer, Integer> pair3 = new  Pair<Integer, Integer>(9, 10);
		assert(pair1.equals(pair2));
		assert(!pair1.equals(pair3));
		System.out.println(pair1);
	}

}
