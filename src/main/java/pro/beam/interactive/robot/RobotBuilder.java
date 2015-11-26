package pro.beam.interactive.robot;

import com.google.common.util.concurrent.ListenableFuture;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.tetris.RobotInfo;

import java.util.concurrent.Callable;

/**
 * RobotBuilder is a factory class designed for building Robots. It expects to have information about
 * a username and password (optional: two-factor code), and a channel to connect to. When the `#build` method is called,
 * a robot is generated and returned via Guava's ListenableFuture mechanism.
 */
public class RobotBuilder {
    protected String username;
    protected String password;
    protected String twoFactor;
    protected BeamChannel channel;

    /**
     * Sets the username of the user to be authenticated.
     * Required to be set before a robot may be attempted to be constructed.
     *
     * @param username The username, case-sensitive.
     * @return The current instance of the RobotBuilder.
     */
    public RobotBuilder username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the password of the user to be authenticated.
     * Required to be set before a robot may be attempted to be constructed.
     *
     * If the account in question has two-factor enabled, the `#twoFactor` method MUST
     * be called separately.
     *
     * @param password The password, case-sensitive.
     * @return The current instance of the RobotBuilder.
     */
    public RobotBuilder password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Sets the two-factor auth code of the user to be authenticated.
     * MUST be set before a robot may be attempted to be constructed IF the user in question
     * has two-factor authentication enabled on their account.
     *
     * @param twoFactor The 6-character authentication code.
     * @return The current instance of the RobotBuilder.
     */
    public RobotBuilder twoFactor(String twoFactor) {
        this.twoFactor = twoFactor;
        return this;
    }

    /**
     * Sets the channel to connect to. Either this or this method's counterpart (RobotBuilder#channel(BeamChannel))
     * MUST be called before attempting to construct a Robot.
     *
     * @param id The numerical ID of the channel trying to be reached.
     * @return The current instance of the RobotBuilder.
     */
    public RobotBuilder channel(int id) {
        BeamChannel channel = new BeamChannel();
        channel.id = id;

        return this.channel(channel);
    }

    /**
     * Sets the channel to connect to. Either this or this method's counterpart (RobotBuilder#channel(int))
     * MUST be called before attempting to construct a Robot.
     *
     * @param channel An instance of the channel trying to be reached.
     * @return The current instance of the RobotBuilder.
     */
    public RobotBuilder channel(BeamChannel channel) {
        this.channel = channel;
        return this;
    }

    /**
     * Returns a ListenableFuture which will eventually deliver an instance of a connected, authenticated Robot,
     * ready to send and receive events.
     *
     * This method does, at some point, attempt to authenticate with the BeamAPI using the Beam API client,
     * which will, in turn, tarnish its credentials. It is RECOMMENDED that a new instance (`new BeamAPI()`) be
     * constructed and sent into this method's parameter list.
     *
     * @param beam An instance of the BeamAPI.
     * @return A ListenableFuture of type Robot that will return the connected, authenticated instance of the Robot.
     */
    public ListenableFuture<Robot> build(final BeamAPI beam) {
        return beam.executor.submit(new Callable<Robot>() {
            @Override public Robot call() throws Exception {
                RobotConnector connector = new RobotConnector(RobotBuilder.this);
                connector.authenticate(beam);

                RobotInfo info = connector.findCredentials(beam);
                Robot robot = connector.connectRobotTo(info, channel);

                return robot;
            }
        });
    }
}
