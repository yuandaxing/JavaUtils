package test;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.collections.iterators.CollatingIterator;

import cgcl.function.net.FBUtilities;
import cgcl.function.net.FileUtils;
import cgcl.function.net.FileUtils.Deleter;

public class TestGeneral {
	private static InetAddress localInetAddress_;
	public static void main(String[] args) throws IOException{
		localInetAddress_ = InetAddress.getLocalHost();
		
//		InetAddress[] addresses = InetAddress.getAllByName(
//				"localhost");
//		InetAddress[] addresses = InetAddress.getAllByName(
//				 InetAddress.getLocalHost().getHostName());
//		for(InetAddress e : addresses){
//			System.out.println(e.getHostAddress());
//		}
//		String str = new String();
//		String s1 = "abc: ;efg";
//		String[] ret = null;
//		ret = s1.split(": ;"); //strip as the regular expressions
//		for(String e: ret){
//			System.out.println(e);
//		}
//		ret = FBUtilities.strip(s1, ": ;");
//		
//		
//		
//		
//		for(String e: ret){
//			System.out.println(e);
//		}
//		
//		System.out.println(localInetAddress_.getHostAddress());
		
		
		BigInteger bi1 = new BigInteger("123");
		BigInteger bi2 = new BigInteger("22");
		System.out.println(FBUtilities.midpoint(bi1, bi2, 8));
		System.out.println(FBUtilities.midpoint(bi2, bi1, 8));
		
		int x = 0x414243;
		int y = 0x414243;
		//java中char的长度是2个字节
		System.out.println(new String("abc".toCharArray()));
		System.out.println(FBUtilities.toByteArray(x)[1]);
		System.out.println(FBUtilities.byteArrayToInt(FBUtilities.toByteArray(x)));
		System.out.println(FBUtilities.compareByteArrays(FBUtilities.toByteArray(x),
				FBUtilities.toByteArray(y)));
		byte x1 = 65;
		System.out.println(FBUtilities.bytesToHex(x1, x1));
		
		//FBUtilities.renameWithConfirm("fuck", "test.cpp");
//		ArrayList<Integer> ai1 = new ArrayList<Integer>();
//		ArrayList<Integer> ai2 = new ArrayList<Integer>();
//		ai1.add(1); ai1.add(2);
//		ai2.add(3); ai2.add(4);
//		CollatingIterator ci = FBUtilities.<Iterator<Integer>>getCollatingIterator();
//		ci.addIterator(ai1.iterator());
//		ci.addIterator(ai2.iterator());
//		
		File f = new File("test/fuck.cpp");
		String dir = "test/fuck";
		String f1 = dir+"/fuck.cpp";
		//FileUtils.deleteWithConfirm(new File("fuck.cpp"));
		//new Deleter(f).run();
		FileUtils.createDirectory(dir);
		FileUtils.createFile(f1);
		
		long use = FileUtils.getUsedDiskSpaceForPath(dir);
		System.out.println(FileUtils.stringifyFileSize(use));
		FileUtils.deleteDir(new File(dir));
		FileUtils.createHardLink(new File("test.cpp"), new File("whatever"));
	}
}
