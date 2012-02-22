package cgcl.data.structure;

import java.util.Random;

public class RandomGenerator {
	private static final Random r = new Random(System.currentTimeMillis());
	private static final char[] chs = ("0123456789abcdefghijklmnopqrstuvwxyz" +
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	private static final char[] byteNoDigits = ("abcdefghijklmnopqrstuvwxyz" +
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	
	
	 public static String getString(int len){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < len; i++){
			sb.append(chs[Math.abs(r.nextInt()) % chs.length]);
		}
		return sb.toString();
	}
	 
	 public static String getStringNoDigits(int len){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < len; i++){
			sb.append(byteNoDigits[Math.abs(r.nextInt()) % byteNoDigits.length]);
		}
		return sb.toString();
	}
	 
	 public static double getDouble(double  left, double right){
		 assert(left < right);
		 double  ret = Math.random() * right;
		 while(ret < left){
			 ret = Math.random() * right;
		 }
		 return ret;
	 }
	 
	 public static int getInt(){
		 return r.nextInt();
	 }
	 
	 public static void main(String[] args){
		 long s = System.currentTimeMillis();
		 for(int i = 0; i < 1000; i++){
			 System.out.println("randomString:" + getString(10));
			 System.out.println("randomStringNoDigits:" + getStringNoDigits(10));
			 System.out.println("getDouble:" + getDouble(10, 100));
			 System.out.println("getInt:" + getInt());
		 }
		 
		 System.out.println("cost:" + (System.currentTimeMillis() - s) + "ms");
	 }
	 
}
