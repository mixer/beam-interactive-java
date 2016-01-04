package pro.beam.interactive.robot;

import com.google.protobuf.Message;
import org.java_websocket.drafts.Draft_17;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.event.EventRegistry;
import pro.beam.interactive.websocket.BufferedWebSocket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URI;

/**
 * The Robot is responsible for maintaining a connection to the Tetris API,
 * as well as being able to send messages into it, receive messages out of it,
 * and dispatch those received messages to their relevant listeners.
 *
 * It is constructed using the @link{pro.beam.interactive.robot.RobotBuilder} class.
 */
public class Robot extends BufferedWebSocket {
    protected final EventRegistry eventRegistry;
    protected final RobotDecoderTask decoderTask;
    protected final RobotIO io;

    /**
     * Default constructor, which takes in the SocketAddress to connect to. This constructor
     * does not attempt to make a connection against that address until it is told to do so.
     *
     * During construction, several things are initialized. The Socket is initialized as a blank,
     * given no arguments and no target to connect to. The event registry is initialized, and no
     * handlers are set up. The DecoderTask is initialized, but not yet run.
     *
     * @param uri The URI of the Tetris Robot server to connect to.
     */
    public Robot(URI uri) throws IOException {
        super(uri, new Draft_17());

        if (uri.getScheme().equals("wss")) {
            try {
                SocketFactory sf = SSLSocketFactory.getDefault();

                this.setSocket(sf.createSocket());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.eventRegistry = new EventRegistry();
        this.decoderTask = new RobotDecoderTask(this);
        this.io = new RobotIO(this);
    }

    /**
     * Connects to a given Channel on Beam, given an authkey. This authkey is supplied by the API.
     *
     * When this method is called, the socket is opened, and an IOException may be thrown. In addition,
     * the decoder task is prepared to be run on a new Thread in a concurrent fashion. Finally, a handshake
     * is sent down to the server.
     *
     * @param key The key to use when establishing a connection to the API.
     * @param channel The channel to connect to.
     * @throws IOException Thrown if the socket fails to establish itself, or closes itself.
     */
    public void connect(String key, BeamChannel channel) throws IOException, InterruptedException {
        this.beginDecoding();
        this.connectBlocking();
        this.write(Handshaker.createHandshake(key, channel.id));
    }

    /**
     * Disconnects from the Beam API, by closing the socket and stopping the decoder task.
     *
     * @throws IOException Thrown if an error was experienced in disconnecting from the established connection.
     */
    public void disconnect() throws InterruptedException {
        this.closeBlocking();
        this.decoderTask.stop();
    }

    /**
     * Registers an event handler on a given type.
     *
     * @param type The type of even on which to subscribe.
     * @param handler An instance of a handler which is capable of receiving events of that type.
     * @param <T> The type of event that will be subscribed.
     */
    public <T extends Message> void on(Class<T> type, EventListener<T> handler) {
        this.eventRegistry.subscribe(handler, type);
    }

    /**
     * Writes a message out over the wire by encoding it and the sending out its bytes over the socket.
     *
     * @param message The message to encode.
     * @throws IOException Thrown if the socket becomes un-writeable during this process.
     */
    public void write(Message message) throws IOException {
        this.io.write(message);
    }

    private void beginDecoding() throws IOException {
        this.io.open();
        new Thread(this.decoderTask).start();
    }
}
