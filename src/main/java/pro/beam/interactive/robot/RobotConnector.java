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
import java.util.concurrent.ExecutionException;

public class RobotConnector {
    protected final RobotBuilder builder;

    protected RobotConnector(RobotBuilder builder) {
        this.builder = builder;
    }

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

    protected RobotInfo findCredentials(BeamAPI beam) throws ExecutionException, InterruptedException {
        return beam.use(TetrisService.class).getRobotCredentials(this.builder.channel).get();
    }

    protected Robot connectRobotTo(RobotInfo info, BeamChannel channel) throws IOException {
        Robot robot = new Robot(info.address);
        robot.connect(info.authkey, channel);

        return robot;
    }
}
