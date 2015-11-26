package pro.beam.interactive.robot;

import com.google.protobuf.Message;
import pro.beam.interactive.net.Decoder;
import pro.beam.interactive.net.Encoder;
import pro.beam.interactive.net.impl.SimpleProtobufDecoder;
import pro.beam.interactive.net.impl.SimpleProtobufEncoder;

import java.io.IOException;
import java.net.Socket;

public class RobotIO {
    protected final Encoder encoder;
    protected final Decoder decoder;

    public RobotIO(Socket socket) throws IOException {
        this.encoder = new SimpleProtobufEncoder(socket);
        this.decoder = new SimpleProtobufDecoder(socket);
    }

    public void write(Message message) throws IOException {
        this.encoder.encode(message);
    }

    public Message next() throws IOException {
        return decoder.next();
    }
}
