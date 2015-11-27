package pro.beam.interactive.net;

import com.google.protobuf.Message;

import java.io.IOException;

/**
 * Encoder provides an interface for encoding Messages to be sent over some streamed source.
 */
public interface Encoder {
    /**
     * Encodes and the sends a message over a streamed source.
     *
     * @param message The message to be encoded.
     * @throws IOException If the message is un-encodable, or the socket closes at any point during writing.
     */
    public void encode(Message message) throws IOException;
}
