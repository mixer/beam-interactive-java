package pro.beam.interactive.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.protobuf.Message;

@SuppressWarnings("unchecked")
public class EventRegistry {
    protected final Multimap<Class<? extends Message>, EventListener<? extends Message>> listeners;

    public EventRegistry() {
        this.listeners = HashMultimap.create();
    }

    public <T extends Message> void subscribe(EventListener<T> listener, Class<T> type) {
        this.listeners.put(type, listener);
    }

    public <T extends Message> void dispatch(T message) {
        for (EventListener<?> listener : this.listeners.get(message.getClass())) {
            ((EventListener<T>) listener).handle(message);
        }
    }
}
