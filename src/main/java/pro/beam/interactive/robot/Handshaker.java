package pro.beam.interactive.robot;

import pro.beam.interactive.net.packet.Protocol;

/**
 * Handshaker is responsible for creating the Handshake packet between a Robot, and a channel given an authkey.
 */
public class Handshaker {
    /**
     * Generates the Handshake packet between us and the given channel, using the authkey provided.
     *
     * @param authkey The authkey to use when handshaking.
     * @param channel The channel to connect to.
     * @return The formulated handshake packet.
     */
    public static Protocol.Handshake createHandshake(String authkey, int channel) {
        return Protocol.Handshake.newBuilder().setStreamKey(authkey)
                .setChannel(channel)
                .build();
    }
}
