package pro.beam.interactive.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * AbstractSocketHolder provides an abstract class with very little functionality but to hold an instance of a
 * Socket and delegate into its Input and OutputStream providers.
 *
 * The held Socket is immutable.
 */
public abstract class AbstractSocketHolder {
    protected final Socket socket;

    /**
     * Default constructor which sets the socket to the one provided.
     * @param socket The socket to bind to.
     */
    public AbstractSocketHolder(Socket socket) {
        this.socket = socket;
    }

    /**
     * Delegates into the InputStream of the Socket.
     *
     * @return The InputStream of the held socket.
     * @throws IOException If the socket becomes unreadable.
     */
    protected InputStream input() throws IOException {
        return this.socket.getInputStream();
    }

    /**
     * Delegates into the OutputStream of the Socket.
     *
     * @return The OutputStream of the held socket.
     * @throws IOException If the socket becomes unwriteable.
     */
    protected OutputStream output() throws IOException {
        return this.socket.getOutputStream();
    }
}
