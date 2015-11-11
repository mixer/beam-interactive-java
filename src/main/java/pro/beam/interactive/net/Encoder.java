package pro.beam.interactive.net;

import com.google.protobuf.Message;
import org.apache.mahout.math.Varint;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Encoder {
    protected final DataOutputStream stream;

    public Encoder(OutputStream stream) {
        this.stream = new DataOutputStream(stream);
    }

    public void write(Message message) throws IOException {
        int id = PacketIdentifier.fromInstance(message);
        byte[] bytes = message.toByteArray();

        Varint.writeUnsignedVarInt(id, this.stream);
        Varint.writeUnsignedVarInt(bytes.length, this.stream);
        stream.write(bytes);
    }
}
