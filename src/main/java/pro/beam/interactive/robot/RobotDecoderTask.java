package pro.beam.interactive.robot;

import com.google.protobuf.Message;

import java.io.IOException;

/**
 * Runnable task responsible for dispatching packets received over the network to the eventbus.
 */
public class RobotDecoderTask implements Runnable {
    protected final Robot robot;
    protected boolean shouldStop;

    /**
     * Default constructor.
     * @param robot The robot to decode from.
     */
    public RobotDecoderTask(Robot robot) {
        this.robot = robot;
    }

    /**
     * Decodes messages until told otherwise. Dispatches those messages across to the event bus.
     */
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

    /**
     * Requests the task be stopped (and subsequently the Thread to be GC'd) at the beginning of the next cycle
     * of execution.
     */
    public void stop() {
        this.shouldStop = true;
    }
}
