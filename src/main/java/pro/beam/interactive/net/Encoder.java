package pro.beam.interactive.net;

import com.google.protobuf.Message;

import java.io.IOException;

public interface Encoder {
    public void encode(Message message) throws IOException;
}
