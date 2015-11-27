package pro.beam.interactive.net;

import com.google.protobuf.Message;

import java.io.IOException;

/**
 * Decoder provides an interface for decoding Messages sent over some streamed source.
 */
public interface Decoder {
    /**
     * Fetches and parses the next message from a stream.
     *
     * @return The parsed message.
     * @throws IOException Thrown if the message is malformed, or the socket closes.
     */
    public Message next() throws IOException;
}
