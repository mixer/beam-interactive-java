package pro.beam.interactive.robot;

import com.google.protobuf.Message;
import pro.beam.interactive.net.Decoder;
import pro.beam.interactive.net.Encoder;
import pro.beam.interactive.net.impl.SimpleProtobufDecoder;
import pro.beam.interactive.net.impl.SimpleProtobufEncoder;
import pro.beam.interactive.websocket.BufferedWebSocket;

import java.io.IOException;

public class RobotIO {
    protected final BufferedWebSocket ws;

    protected Encoder encoder;
    protected Decoder decoder;

    public RobotIO(BufferedWebSocket ws) {
        this.ws = ws;
    }

    public void open() throws IOException {
        this.encoder = new SimpleProtobufEncoder(this.ws);
        this.decoder = new SimpleProtobufDecoder(this.ws);
    }

    public void write(Message message) throws IOException {
        this.encoder.encode(message);
    }

    public Message next() throws IOException {
        return decoder.next();
    }
}
