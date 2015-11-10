package pro.beam.interactive.net;

import com.google.common.collect.ImmutableBiMap;
import com.google.protobuf.GeneratedMessage;

public final class PacketIdentifier {
    // Don't instantiate meeee!
    private PacketIdentifier() { }

    private static final ImmutableBiMap<Integer, Class<? extends GeneratedMessage>> PACKETS =
        ImmutableBiMap.<Integer, Class<? extends GeneratedMessage>>builder()
            .put(0, Protocol.Handshake.class)
            .put(1, Protocol.HandshakeACK.class)
            .put(2, Protocol.Report.class)
            .put(3, Protocol.Error.class)
            .put(4, Protocol.ProgressUpdate.class)
        .build();

    public static Class<? extends GeneratedMessage> fromId(int id) {
        return PACKETS.get(id);
    }

    public static <T extends GeneratedMessage> int fromInstance(T message) {
        return fromClass(message.getClass());
    }

    public static int fromClass(Class<? extends GeneratedMessage> clazz) {
        return PACKETS.inverse().get(clazz);
    }
}
