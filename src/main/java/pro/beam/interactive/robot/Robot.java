package pro.beam.interactive.robot;

import com.google.protobuf.Message;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.event.EventRegistry;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class Robot {
    protected final SocketAddress address;
    protected final Socket socket;

    protected final EventRegistry eventRegistry;
    protected final RobotDecoderTask decoderTask;
    protected final RobotIO io;

    public Robot(SocketAddress address) throws IOException {
        this.address = address;
        this.socket = new Socket();

        this.eventRegistry = new EventRegistry();
        this.decoderTask = new RobotDecoderTask(this);
        this.io = new RobotIO(this.socket);
    }

    public void connect(String authkey, BeamChannel channel) throws IOException {
        this.socket.connect(this.address);
        new Thread(this.decoderTask).start();

        this.write(Handshaker.createHandshake(authkey, channel.id));
    }

    public void disconnect() throws IOException {
        this.socket.close();
        this.decoderTask.stop();
    }

    public <T extends Message> void on(Class<T> type, EventListener<T> handler) {
        this.eventRegistry.subscribe(handler, type);
    }

    public void write(Message message) throws IOException {
        this.io.write(message);
    }
}
