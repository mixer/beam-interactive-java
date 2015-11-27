package pro.beam.interactive.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.protobuf.Message;

@SuppressWarnings("unchecked")
/**
 * An EventRegistry is responsible handling the subscriptions and dispatching of events of several types.
 *
 * It does not currently support:
 *   - subscription removal
 *   - listener priority
 *   - control over event propagation
 *   - asynchronous execution of event handlers
 */
public class EventRegistry {
    protected final Multimap<Class<? extends Message>, EventListener<? extends Message>> listeners;

    /**
     * The default, no-args constructor.
     */
    public EventRegistry() {
        this.listeners = HashMultimap.create();
    }

    /**
     * Subscribes a particular listener to a type of event. These types MUST match.
     *
     * @param listener The listener that will be called upon when the event is received. The "event listener".
     * @param type The Class representation of the type of event that will be listened to.
     * @param <T> The generic type of event to listen to.
     */
    public <T extends Message> void subscribe(EventListener<T> listener, Class<T> type) {
        this.listeners.put(type, listener);
    }

    /**
     * Dispatches a method in a synchronous fashion across all relevant listeners. Dispatch is blocking, which means the
     * following:
     *   1) A particular handler must complete its execution before the next one will be called.
     *   2) All handles must complete before the next event can be dispatched.
     *   3) Event handlers can occur in any order.
     *
     * @param message The message to dispatch.
     * @param <T> The type of the message that is being dispatched.
     */
    public <T extends Message> void dispatch(T message) {
        for (EventListener<?> listener : this.listeners.get(message.getClass())) {
            ((EventListener<T>) listener).handle(message);
        }
    }
}
