package pro.beam.interactive.net.impl;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Message;
import pro.beam.interactive.net.AbstractSocketHolder;
import pro.beam.interactive.net.Decoder;
import pro.beam.interactive.net.packet.PacketIdentifier;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * SimpleProtobufDecoder is an implementation of the Decoder interface designed for unfurling
 * protobuf messages from over the socket.
 */
public class SimpleProtobufDecoder extends AbstractSocketHolder implements Decoder {
    protected final CodedInputStream stream;

    /**
     * Public constructor for the SimpleProtobufDecoder.
     *
     * @param socket The socket to read from.
     * @throws IOException Thrown if the input stream of the socket cannot be accessed.
     */
    public SimpleProtobufDecoder(Socket socket) throws IOException {
        super(socket);

        this.stream = CodedInputStream.newInstance(this.input());
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
        int length = this.stream.readRawVarint32();
        int id = this.stream.readRawVarint32();
        byte[] payload = this.stream.readRawBytes(length);

        Method parser = this.getParser(PacketIdentifier.fromId(id));
        try {
            Object parsed = parser.invoke(null, payload);
            if (parsed instanceof Message) {
                return (Message) parsed;
            }

            return null;
        } catch (IllegalAccessException | InvocationTargetException e) {
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
