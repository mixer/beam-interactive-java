package pro.beam.interactive.net;

import com.google.protobuf.Message;
import org.apache.mahout.math.Varint;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Decoder {
    protected final DataInputStream stream;

    public Decoder(InputStream input) {
        this.stream = new DataInputStream(input);
    }

    public Message next() throws IOException {
        int id = Varint.readUnsignedVarInt(this.stream);
        int length = Varint.readUnsignedVarInt(this.stream);

        // Wait until the buffer is filled enough to be able to read from it.
        while (this.stream.available() < length);

        Class<? extends Message> messageType = PacketIdentifier.fromId(id);
        byte[] packet = new byte[length];
        int read = this.stream.read(packet);
        if (read != length) {
            return null;
        }

        try {
            Message prototype = messageType.newInstance();
            return prototype.getParserForType().parseFrom(packet);
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

}
