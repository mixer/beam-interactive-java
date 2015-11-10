package pro.beam.interactive.robot;

import com.google.common.eventbus.EventBus;
import pro.beam.interactive.net.Decoder;

import java.io.IOException;
import java.net.Socket;

public class Robot {
    protected final EventBus eventBus;
    protected final Socket socket;
    protected final String authkey;

    public Robot(Socket socket, String authkey) throws IOException {
        this.socket = socket;
        this.authkey = authkey;

        this.eventBus = new EventBus(String.format("robot-%d", this.hashCode()));
    }

    public void register(Object listenerObject) {
        this.eventBus.register(listenerObject);
    }

    public void unregister(Object listenerObject) {
        this.eventBus.unregister(listenerObject);
    }

    private void dispatch() throws IOException {
        Decoder decoder = new Decoder(this.socket.getInputStream());
        while (!this.socket.isClosed()) {
            this.eventBus.post(decoder.next());
        }
    }
}
