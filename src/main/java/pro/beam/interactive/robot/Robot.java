package pro.beam.interactive.robot;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class Robot {
    protected final SocketAddress address;
    protected final Socket socket;

    public Robot(SocketAddress address) {
        this.address = address;
        this.socket = new Socket();
    }

    public void connect(String authkey) throws IOException {
        this.socket.connect(this.address);
    }
}
