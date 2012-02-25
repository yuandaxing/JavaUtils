/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cgcl.function.net;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.collections.iterators.CollatingIterator;

import cgcl.data.structure.Pair;


public class FBUtilities
{

    public static final BigInteger TWO = new BigInteger("2");

    private static InetAddress localInetAddress_;

    public static String[] strip(String string, String token)
    {
    	//有split方法，这个没有必要, 或许
        StringTokenizer st = new StringTokenizer(string, token);
        List<String> result = new ArrayList<String>();
        while ( st.hasMoreTokens() )
        {
            result.add( (String)st.nextElement() );
        }
        return result.toArray( new String[0] );
    }

    public static InetAddress getLocalAddress()
    {
    	//
        if (localInetAddress_ == null)
            try
            {//这个是根据/etc/hosts中的设置读取地址的
                localInetAddress_ = InetAddress.getLocalHost();
            }
            catch (UnknownHostException e)
            {
                throw new RuntimeException(e);
            }
        return localInetAddress_;
    }

    /**
     * Given two bit arrays represented as BigIntegers, containing the given
     * number of significant bits, calculate a midpoint.
     *
     * @param left The left point.
     * @param right The right point.
     * @param sigbits The number of bits in the points that are significant.
     * @return A midpoint that will compare bitwise halfway between the params, and
     * a boolean representing whether a non-zero lsbit remainder was generated.
     */
    //利用BigInteger计算一个环状区域的中间值，这个值得借鉴
    public static Pair<BigInteger,Boolean> midpoint(BigInteger left, BigInteger right, int sigbits)
    {
        BigInteger midpoint;
        boolean remainder;
        //left < right normal
        if (left.compareTo(right) < 0)
        {
            BigInteger sum = left.add(right);
            remainder = sum.testBit(0);
            midpoint = sum.shiftRight(1);
        }
        else
        {//left >= right wrap around case
            BigInteger max = TWO.pow(sigbits);
            // wrapping case
            BigInteger distance = max.add(right).subtract(left);
            remainder = distance.testBit(0);
            midpoint = distance.shiftRight(1).add(left).mod(max);
        }
        
        return  new Pair(midpoint, remainder);
    }
    
    public static void main(String args[])
    {
    	System.out.println(FBUtilities.midpoint(new BigInteger("0"),new BigInteger("0"),127));
    }

    public static byte[] toByteArray(int i)
    {
        byte[] bytes = new byte[4];
        bytes[0] = (byte)( ( i >>> 24 ) & 0xFF);
        bytes[1] = (byte)( ( i >>> 16 ) & 0xFF);
        bytes[2] = (byte)( ( i >>> 8 ) & 0xFF);
        bytes[3] = (byte)( i & 0xFF );
        return bytes;
    }

    public static int byteArrayToInt(byte[] bytes)
    {
    	return byteArrayToInt(bytes, 0);
    }

    public static int byteArrayToInt(byte[] bytes, int offset)
    {
        if ( bytes.length - offset < 4 )
        {
            throw new IllegalArgumentException("An integer must be 4 bytes in size.");
        }
        int n = 0;
        for ( int i = 0; i < 4; ++i )
        {
            n <<= 8;
            n |= bytes[offset + i] & 0xFF;
        }
        return n;
    }

    //compare as char string like
    public static int compareByteArrays(byte[] bytes1, byte[] bytes2){
        if(null == bytes1){
            if(null == bytes2) return 0;
            else return -1;
        }
        if(null == bytes2) return 1;

        int minLength = Math.min(bytes1.length, bytes2.length);
        for(int i = 0; i < minLength; i++)
        {
            if(bytes1[i] == bytes2[i])
                continue;
            // compare non-equal bytes as unsigned
            return (bytes1[i] & 0xFF) < (bytes2[i] & 0xFF) ? -1 : 1;
        }
        if(bytes1.length == bytes2.length) return 0;
        else return (bytes1.length < bytes2.length)? -1 : 1;
    }

    /**
     * @return The bitwise XOR of the inputs. The output will be the same length as the
     * longer input, but if either input is null, the output will be null.
     */
    public static byte[] xor(byte[] left, byte[] right)
    {
        if (left == null || right == null)
            return null;
        if (left.length > right.length)
        {
            byte[] swap = left;
            left = right;
            right = swap;
        }

        // left.length is now <= right.length
        byte[] out = Arrays.copyOf(right, right.length);
        for (int i = 0; i < left.length; i++)
        {
            out[i] = (byte)((left[i] & 0xFF) ^ (right[i] & 0xFF));
        }
        return out;
    }

    public static BigInteger hash(String data)
    {
        byte[] result = hash(HashingSchemes.MD5, data.getBytes());
        BigInteger hash = new BigInteger(result);
        return hash.abs();        
    }

    public static byte[] hash(String type, byte[]... data)
    {
    	byte[] result = null;
    	try
        {
    		//使用md5算法，计算hash值
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            for(byte[] block : data)
                messageDigest.update(block);
            result = messageDigest.digest();
    	}
    	catch (Exception e)
        {
            throw new RuntimeException(e);
    	}
    	return result;
	}

    // The given byte array is compressed onto the specified stream.
    // The method does not close the stream. The caller will have to do it.
    public static void compressToStream(byte[] input, ByteArrayOutputStream bos) throws IOException
    {
    	 // Create the compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();

        // Write the compressed data to the stream
        byte[] buf = new byte[1024];
        while (!compressor.finished())
        {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
    }

    public static byte[] decompress(byte[] compressedData, int off, int len) throws IOException, DataFormatException
    {
    	 // Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(compressedData, off, len);

        // Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(compressedData.length);

        // Decompress the data
        byte[] buf = new byte[1024];
        while (!decompressor.finished())
        {
            int count = decompressor.inflate(buf);
            bos.write(buf, 0, count);
        }
        bos.close();

        // Get the decompressed data
        return bos.toByteArray();
    }

    public static void writeByteArray(byte[] bytes, DataOutput out) throws IOException
    {
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    
    
    public static byte[] hexToBytes(String str)
    {
        assert str.length() % 2 == 0;
        byte[] bytes = new byte[str.length()/2];
        for (int i = 0; i < bytes.length; i++)
        {
        	//这个转换没有用过，而且这里没有跳过0x
            bytes[i] = (byte)Integer.parseInt(str.substring(i*2, i*2+2), 16);
        }
        return bytes;
    }


    
    public static String bytesToHex(byte... bytes)
    {
    	//StringBuilder is more efficient than StringBuffer
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
        {
            int bint = b & 0xff;
            if (bint <= 0xF)
                // toHexString does not 0 pad its results.
                sb.append("0");
            sb.append(Integer.toHexString(bint));
        }
        return sb.toString();
    }

    public static String mapToString(Map<?,?> map)
    {
        StringBuilder sb = new StringBuilder("{");

        for (Map.Entry entry : map.entrySet())
        {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }

        return sb.append("}").toString();
    }

    public static void writeNullableString(String key, DataOutput dos) throws IOException
    {
        dos.writeBoolean(key == null);
        if (key != null)
        {
            dos.writeUTF(key);
        }
    }

    public static String readNullableString(DataInput dis) throws IOException
    {
        if (dis.readBoolean())
            return null;
        return dis.readUTF();
    }

    public static void renameWithConfirm(String tmpFilename, String filename) throws IOException
    {
        if (!new File(tmpFilename).renameTo(new File(filename)))
        {
            throw new IOException("rename failed of " + filename);
        }
    }

    //
    public static <T extends Comparable<T>> CollatingIterator getCollatingIterator()
    {
  
        // CollatingIterator will happily NPE if you do not specify a comparator explicitly
    	//这个使用priorityQueue很容易实现
        return new CollatingIterator(new Comparator<T>()
        {
            public int compare(T o1, T o2)
            {
                return o1.compareTo(o2);
            }
        });
    }
}

//    public static void serialize(TSerializer serializer, TBase struct, DataOutput out)
//    throws IOException
//    {
//        assert serializer != null;
//        assert struct != null;
//        assert out != null;
//        byte[] bytes;
//        try
//        {
//            bytes = serializer.serialize(struct);
//        }
//        catch (TException e)
//        {
//            throw new RuntimeException(e);
//        }
//        out.writeInt(bytes.length);
//        out.write(bytes);
//    }
//
//    public static void deserialize(TDeserializer deserializer, TBase struct, DataInput in)
//    throws IOException
//    {
//        assert deserializer != null;
//        assert struct != null;
//        assert in != null;
//        byte[] bytes = new byte[in.readInt()];
//        in.readFully(bytes);
//        try
//        {
//            deserializer.deserialize(struct, bytes);
//        }
//        catch (TException ex)
//        {
//            throw new IOException(ex);
//        }
//    }
//}
