package cgcl.data.structure;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.BitSet;

/*
 * stream 和不带stream的对象之间的区别是一个使用了buff，一个没有使用buffer
 */
public class BitSetSerializer
{
    public static void serialize(BitSet bs, DataOutputStream dos) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(dos);
        oos.writeObject(bs);
        oos.flush();
    }

    public static BitSet deserialize(DataInputStream dis) throws IOException
    {
        ObjectInputStream ois = new ObjectInputStream(dis);
        try
        {
            return (BitSet) ois.readObject();
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
