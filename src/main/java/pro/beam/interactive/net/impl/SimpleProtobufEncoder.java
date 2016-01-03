package pro.beam.interactive.net.impl;

import com.google.protobuf.Message;
import pro.beam.interactive.websocket.AbstractBufferedWebsocketHolder;
import pro.beam.interactive.net.Encoder;
import pro.beam.interactive.net.packet.PacketIdentifier;
import pro.beam.interactive.util.Varint;
import pro.beam.interactive.websocket.BufferedWebSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * SimpleProtobufEncoder is an implementation of the Encoder interface designed for furling
 * protobuf messages for use over the socket.
 */
public class SimpleProtobufEncoder extends AbstractBufferedWebsocketHolder implements Encoder {
    /**
     * Public constructor for the SimpleProtobufEncoder class.
     * @param socket The socket to write to.
     */
    public SimpleProtobufEncoder(BufferedWebSocket ws) {
        super(ws);
    }

    /**
     * An implementation of the superclass method "encode". Takes a message instance and writes
     * it in the designated protocol-buffer wire format as described below:
     *
     *   1) <uvarint> message (type) ID
     *   2) <byte[]> payload payload
     *
     * @param message The message to be encoded.
     * @throws IOException Thrown if the socket becomes un-writeable midstream.
     */
    @Override public void encode(Message message) throws IOException {
        int id = PacketIdentifier.fromInstance(message);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(Varint.writeUnsignedVarInt(id));
        out.write(message.toByteArray());

        this.ws.send(out.toByteArray());
    }
}
