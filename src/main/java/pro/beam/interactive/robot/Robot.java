package pro.beam.interactive.robot;

import com.google.protobuf.Message;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.interactive.net.impl.SimpleProtobufDecoder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class Robot {
    protected final SocketAddress address;
    protected final Socket socket;

    protected final RobotIO io;

    public Robot(SocketAddress address) throws IOException {
        this.address = address;
        this.socket = new Socket();

        this.io = new RobotIO(this.socket);
    }

    public void connect(String authkey, BeamChannel channel) throws IOException {
        this.socket.connect(this.address);

        this.write(Handshaker.createHandshake(authkey, channel.id));

        BufferedInputStream bis = new BufferedInputStream(this.socket.getInputStream());
        for(;;) {
            Message next = new SimpleProtobufDecoder(this.socket).next();
            System.out.println(next.getClass().getName() + " " + next.toString());
        }
    }

    public void write(Message message) throws IOException {
        io.write(message);
    }
}
