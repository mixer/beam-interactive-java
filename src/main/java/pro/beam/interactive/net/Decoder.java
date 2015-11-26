package pro.beam.interactive.net;

import com.google.protobuf.Message;

import java.io.IOException;

public interface Decoder {
    public Message next() throws IOException;
}
