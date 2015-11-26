package pro.beam.interactive.net.impl;

import com.google.protobuf.Message;
import pro.beam.interactive.net.AbstractSocketHolder;
import pro.beam.interactive.net.Encoder;
import pro.beam.interactive.net.packet.PacketIdentifier;
import pro.beam.interactive.util.Varint;

import java.io.IOException;
import java.net.Socket;

/**
 * SimpleProtobufEncoder is an implementation of the Encoder interface designed for furling
 * protobuf messages for use over the socket.
 */
public class SimpleProtobufEncoder extends AbstractSocketHolder implements Encoder {
    /**
     * Public constructor for the SimpleProtobufEncoder class.
     * @param socket The socket to write to.
     */
    public SimpleProtobufEncoder(Socket socket) {
        super(socket);
    }

    /**
     * An implementation of the superclass method "encode". Takes a message instance and writes
     * it in the designated protocol-buffer wire format as described below:
     *
     *   1) <uvarint> message (body) length
     *   2) <uvarint> message (type) ID
     *   3) <byte[]> payload payload
     *
     * @param message The message to be encoded.
     * @throws IOException Thrown if the socket becomes un-writeable midstream.
     */
    @Override public void encode(Message message) throws IOException {
        int id = PacketIdentifier.fromInstance(message);

        byte[] body = message.toByteArray();
        int length = body.length;

        this.output().write(Varint.writeUnsignedVarInt(length));
        this.output().write(Varint.writeUnsignedVarInt(id));
        this.socket.getOutputStream().write(body);
    }
}
