package pro.beam.interactive.robot;

import com.google.protobuf.Message;

import java.io.IOException;

public class RobotDecoderTask implements Runnable {
    protected final Robot robot;
    protected boolean shouldStop;

    public RobotDecoderTask(Robot robot) {
        this.robot = robot;
    }

    @Override public void run() {
        while (!this.shouldStop) {
            Message decoded = null;
            try {
                decoded = robot.io.decoder.next();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (decoded != null) {
                robot.eventRegistry.dispatch(decoded);
            }
        }
    }

    public void stop() {
        this.shouldStop = true;
    }
}
