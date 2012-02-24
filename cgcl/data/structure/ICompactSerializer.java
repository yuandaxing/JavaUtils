
package cgcl.data.structure;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

/**
 * Allows for the controlled serialization/deserialization of a given type.
 */
//���л�DataOutputStream��������Կ�����ICompactSerializer2
public interface ICompactSerializer<T>
{
	/**
     * Serialize the specified type into the specified DataOutputStream instance.
     * @param t type that needs to be serialized
     * @param dos DataOutput into which serialization needs to happen.
     * @throws IOException
     */
    public void serialize(T t, DataOutputStream dos) throws IOException;

    /**
     * Deserialize into the specified DataInputStream instance.
     * @param dis DataInput from which deserialization needs to happen.
     * @throws IOException
     * @return the type that was deserialized
     */
    public T deserialize(DataInputStream dis) throws IOException;
}
