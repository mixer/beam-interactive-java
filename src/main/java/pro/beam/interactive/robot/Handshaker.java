package pro.beam.interactive.robot;

import pro.beam.interactive.net.packet.Protocol;

public class Handshaker {
    public static Protocol.Handshake createHandshake(String authkey, int channel) {
        return Protocol.Handshake.newBuilder().setStreamKey(authkey)
                .setChannel(channel)
                .build();
    }
}
