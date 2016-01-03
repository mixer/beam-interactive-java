package pro.beam.interactive.websocket;

/**
 * AbstractSocketHolder provides an abstract class with very little functionality but to hold an instance of a
 * Socket and delegate into its Input and OutputStream providers.
 *
 * The held Socket is immutable.
 */
public abstract class AbstractBufferedWebsocketHolder {
    protected final BufferedWebSocket ws;

    /**
     * Default constructor which sets the socket to the one provided.
     * @param socket The socket to bind to.
     */
    public AbstractBufferedWebsocketHolder(BufferedWebSocket ws) {
        this.ws = ws;
    }
}
