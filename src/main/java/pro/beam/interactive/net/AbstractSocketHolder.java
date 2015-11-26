package pro.beam.interactive.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractSocketHolder {
    protected final Socket socket;

    public AbstractSocketHolder(Socket socket) {
        this.socket = socket;
    }

    protected InputStream input() throws IOException {
        return this.socket.getInputStream();
    }

    protected OutputStream output() throws IOException {
        return this.socket.getOutputStream();
    }
}
