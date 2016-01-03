package pro.beam.interactive.net.impl;

import com.google.protobuf.Message;
import pro.beam.interactive.util.Varint;
import pro.beam.interactive.websocket.AbstractBufferedWebsocketHolder;
import pro.beam.interactive.net.Decoder;
import pro.beam.interactive.net.packet.PacketIdentifier;
import pro.beam.interactive.websocket.BufferedWebSocket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SimpleProtobufDecoder is an implementation of the Decoder interface designed for unfurling
 * protobuf messages from over the socket.
 */
public class SimpleProtobufDecoder extends AbstractBufferedWebsocketHolder implements Decoder {
    /**
     * Public constructor for the SimpleProtobufDecoder.
     *
     * @param socket The socket to read from.
     * @throws IOException Thrown if the input stream of the socket cannot be accessed.
     */
    public SimpleProtobufDecoder(BufferedWebSocket ws) throws IOException {
        super(ws);
    }

    /**
     * Implementation of the superclass method "next". Reads some data from the socket in a
     * blocking fashion and decodes the necessary information from off of it. It finds the appropriate
     * packet type, fetches the decoder for that packet, and then executes a "decode" operation.
     *
     * @return The decoded message instance.
     * @throws IOException Thrown if the stream becomes unreadable.
     */
    @Override public Message next() throws IOException {
        DataInputStream stream;
        try {
            stream = new DataInputStream(new ByteArrayInputStream(this.ws.next()));
        } catch (InterruptedException e) {
            return null;
        }

        int id = Varint.readUnsignedVarInt(stream);
        byte[] payload = new byte[stream.available()];
        stream.read(payload);

        Method parser = this.getParser(PacketIdentifier.fromId(id));
        try {
            Object parsed = parser.invoke(null, payload);
            if (parsed instanceof Message) {
                return (Message) parsed;
            }

            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the parser for a particular Message-type. Reflects away the "parseFrom" method which is expected
     * to return a `byte[]`. This method will break if/when the ProtocolBuffer API breaks. Consult @taylor before
     * updating protobuf-related dependencies in the pom.xml first.
     *
     * @param type The message type.
     * @return A method which will parse a byte[] into an instance.
     */
    private Method getParser(Class<? extends Message> type) {
        try {
            return type.getMethod("parseFrom", byte[].class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
