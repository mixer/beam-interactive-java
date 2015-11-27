package pro.beam.interactive.robot;

import com.google.protobuf.Message;
import pro.beam.interactive.net.Decoder;
import pro.beam.interactive.net.Encoder;
import pro.beam.interactive.net.impl.SimpleProtobufDecoder;
import pro.beam.interactive.net.impl.SimpleProtobufEncoder;

import java.io.IOException;
import java.net.Socket;

public class RobotIO {
    protected final Socket socket;

    protected Encoder encoder;
    protected Decoder decoder;

    public RobotIO(Socket socket) {
        this.socket = socket;
    }

    public void open() throws IOException {
        this.encoder = new SimpleProtobufEncoder(this.socket);
        this.decoder = new SimpleProtobufDecoder(this.socket);
    }

    public void write(Message message) throws IOException {
        this.encoder.encode(message);
    }

    public Message next() throws IOException {
        return decoder.next();
    }
}
