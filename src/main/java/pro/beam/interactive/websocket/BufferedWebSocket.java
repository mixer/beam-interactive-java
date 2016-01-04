package pro.beam.interactive.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BufferedWebSocket extends WebSocketClient {
    protected final BlockingQueue<byte[]> queue;

    public BufferedWebSocket(URI uri, Draft draft) throws IOException {
        super(uri, draft);

        this.queue = new LinkedBlockingQueue<>();
    }

    @Override public void onMessage(ByteBuffer bytes) {
        this.queue.add(bytes.array());
    }

    public byte[] next() throws InterruptedException {
        return this.queue.take();
    }

    @Override public void onMessage(String s) { }
    @Override public void onOpen(ServerHandshake serverHandshake) { }
    @Override public void onClose(int i, String s, boolean b) { }
    @Override public void onError(Exception e) { e.printStackTrace(); }
}
