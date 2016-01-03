package pro.beam.interactive.robot;

import com.google.common.util.concurrent.CheckedFuture;
import pro.beam.api.BeamAPI;
import pro.beam.api.exceptions.BeamException;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.tetris.RobotInfo;
import pro.beam.api.services.impl.TetrisService;
import pro.beam.api.services.impl.UsersService;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;

/**
 * Internal class responsible for connecting a robot to the API.
 */
public class RobotConnector {
    protected final RobotBuilder builder;

    /**
     * Default constructor which takes an instance of the builder.
     *
     * @param builder The builder used to create the robot.
     */
    protected RobotConnector(RobotBuilder builder) {
        this.builder = builder;
    }

    /**
     * Prepares the given instance of the BeamAPI (see: Java Client; https://githuib.com/WatchBeam/beam-client-java)
     * by authenticating it with the API so that it may fetch robot network credentials later (see #findCredentials).
     *
     * @param beam The API that will be prepared.
     * @return The prepared version of that API.
     *
     * @throws BeamException Thrown if invalid credentials are given with which to login.
     */
    protected BeamAPI authenticate(BeamAPI beam) throws BeamException {
        UsersService users = beam.use(UsersService.class);
        CheckedFuture<BeamUser, BeamException> loginTask = null;

        if (this.builder.twoFactor != null) {
            loginTask = users.login(this.builder.username, this.builder.password, this.builder.twoFactor);
        } else {
            loginTask = users.login(this.builder.username, this.builder.password);
        }

        loginTask.checkedGet();

        return beam;
    }

    /**
     * Finds the robot's credentials for the given channel in a blocking fashion.
     *
     * @param beam The API to work with.
     * @return The asked for credentials.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    protected RobotInfo findCredentials(BeamAPI beam) throws BeamException, ExecutionException, InterruptedException {
        return beam.use(TetrisService.class).getRobotCredentials(this.builder.channel).checkedGet();
    }

    /**
     * Connects a robot to a channel given an address to connect to and an authkey. This method blocks.
     *
     * @param info The credentials info object.
     * @param channel The channel to connect to.
     * @return A robot which is connected to the requested things. It will have been connected, set-up, and have
     * completed the handshake process by the time that it is returned.
     *
     * @throws IOException If the socket is unopenable, or becomes unreadable.
     */
    protected Robot connectRobotTo(RobotInfo info, BeamChannel channel) throws IOException, InterruptedException {
        URI address = info.address.resolve("/robot");

        Robot robot = new Robot(address);
        robot.connect(info.key, channel);

        return robot;
    }
}
