package pro.beam.interactive.robot;

import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.ListenableFuture;
import pro.beam.api.BeamAPI;
import pro.beam.api.exceptions.BeamException;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.resource.tetris.RobotInfo;
import pro.beam.api.services.impl.TetrisService;
import pro.beam.api.services.impl.UsersService;

import java.net.Socket;
import java.util.concurrent.Callable;

public class RobotBuilder {
    protected String username;
    protected String password;
    protected String twoFactor;
    protected BeamChannel channel;

    public RobotBuilder username(String username) {
        this.username = username;
        return this;
    }

    public RobotBuilder password(String password) {
        this.password = password;
        return this;
    }

    public RobotBuilder twoFactor(String twoFactor) {
        this.twoFactor = twoFactor;
        return this;
    }

    public RobotBuilder on(int channelId) {
        BeamChannel channel = new BeamChannel();
        channel.id = channelId;

        return this.on(channel);
    }

    public RobotBuilder on(BeamChannel channel) {
        this.channel = channel;
        return this;
    }

    public ListenableFuture<Robot> build(final BeamAPI beam) {
        return beam.executor.submit(new Callable<Robot>() {
            @Override public Robot call() throws Exception {
                RobotBuilder builder = RobotBuilder.this;

                CheckedFuture<BeamUser, BeamException> authentication;
                if (builder.twoFactor != null) {
                    authentication = beam.use(UsersService.class).login(
                            builder.username,
                            builder.password,
                            builder.twoFactor
                    );
                } else {
                    authentication = beam.use(UsersService.class).login(
                            builder.username,
                            builder.password
                    );
                }

                authentication.checkedGet();
                RobotInfo info = beam.use(TetrisService.class).getRobotCredentials(builder.channel).get();

                Socket socket = new Socket(info.address.getHostName(), info.address.getPort());

                return new Robot(socket, info.authkey);
            }
        });
    }
}
