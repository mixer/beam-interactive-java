package pro.beam.interactive.event;

import com.google.protobuf.Message;

/**
 * An EventListener is used to handle all instances of a particular event type.
 * @param <T> The event type on which to listen.
 */
public interface EventListener<T extends Message> {
    /**
     * This method is called whenever a message is sent over the wire to the client.
     *
     * @param message The decoded instance of the message being sent.
     */
    public void handle(T message);
}
