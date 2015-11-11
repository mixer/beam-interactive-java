package pro.beam.example;

import com.google.common.eventbus.Subscribe;
import pro.beam.interactive.net.Protocol;
import sun.awt.X11GraphicsDevice;

import java.awt.*;

public class ExampleListener {
    /**
     * Field `robot` is a AWT class designed to control the mouse on the screen.
     */
    protected Robot robot;

    /**
     * Public constructor for ExampleListener. Creates an instance of the AWT
     * Robot necessary to drive the example.
     */
    public ExampleListener() {
        try {
            this.robot = new Robot(new X11GraphicsDevice(0));
        } catch (AWTException ignored) { }
    }

    /**
     * Subscriber method. This hooks into the EventBus and will receive events
     * of the method argument's type (in this case, a Protocol.Report message).
     *
     * This method will be called any time a Protocol.Report message is received
     * over the wire.
     *
     * @param report The report to receive.
     */
    @Subscribe
    public void onReport(Protocol.Report report) {
        // Grab each joystick object from the report. (The x and y-axis are 0 and 1,
        // respectively).
        Protocol.Report.JoystickInfo x = report.getJoystick(0);
        Protocol.Report.JoystickInfo y = report.getJoystick(1);

        // For each axis, grab the mean position and move the cursor to it.
        this.robot.mouseMove(
                (int) x.getInfo().getMean(),
                (int) y.getInfo().getMean()
        );
    }
}
