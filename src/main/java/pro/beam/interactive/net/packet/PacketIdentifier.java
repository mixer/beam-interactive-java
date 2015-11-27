package pro.beam.interactive.net.packet;

import com.google.common.collect.ImmutableBiMap;
import com.google.protobuf.Message;

/**
 * PacketIdentifier is a singleton class that provides a bi-directional conversion between
 * packet type IDs, and their corresponding implementation classes.
 */
public final class PacketIdentifier {
    private PacketIdentifier() { }

    private static final ImmutableBiMap<Integer, Class<? extends Message>> PACKETS =
            ImmutableBiMap.<Integer, Class<? extends Message>>builder()
                    .put(0, Protocol.Handshake.class)
                    .put(1, Protocol.HandshakeACK.class)
                    .put(2, Protocol.Report.class)
                    .put(3, Protocol.Error.class)
                    .put(4, Protocol.ProgressUpdate.class)
                    .build();

    /**
     * Links the packet type's numeric ID to the implementation class.
     *
     * @param id The type ID of the packet.
     * @return The implementation class corresponding to that type.
     */
    public static Class<? extends Message> fromId(int id) {
        return PACKETS.get(id);
    }

    /**
     * Links the instance of a implementation packet to its corresponding type ID.
     *
     * @param message The message to inspect.
     * @param <T> The type of the message.
     * @return The corresponding type ID of that message.
     */
    public static <T extends Message> int fromInstance(T message) {
        return fromClass(message.getClass());
    }

    /**
     * Links the Class of a packet to its corresponding type ID.
     *
     * @param clazz The Class to inspect.
     * @return The corresponding type ID of that classtype.
     */
    public static int fromClass(Class<? extends Message> clazz) {
        return PACKETS.inverse().get(clazz);
    }
}
