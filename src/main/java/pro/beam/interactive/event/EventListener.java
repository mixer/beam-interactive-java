package pro.beam.interactive.event;

import com.google.protobuf.Message;

public interface EventListener<T extends Message> {
    public void handle(T message);
}
