package pro.beam.interactive.net;

import com.google.common.collect.ImmutableBiMap;
import com.google.protobuf.GeneratedMessage;

public class PacketIdentifier {
    protected final ImmutableBiMap<Integer, Class<? extends GeneratedMessage>> packets;

    public PacketIdentifier() {
        this.packets = ImmutableBiMap.<Integer, Class<? extends GeneratedMessage>>builder()
                .put(0, Protocol.Handshake.class)
                .put(1, Protocol.HandshakeACK.class)
                .put(2, Protocol.Report.class)
                .put(3, Protocol.Error.class)
                .put(4, Protocol.ProgressUpdate.class)
                .build();
    }

    public Class<? extends GeneratedMessage> fromId(int id) {
        return this.packets.get(id);
    }

    public <T extends GeneratedMessage> int fromInstance(T message) {
        return this.fromClass(message.getClass());
    }

    public int fromClass(Class<? extends GeneratedMessage> clazz) {
        return this.packets.inverse().get(clazz);
    }
}
