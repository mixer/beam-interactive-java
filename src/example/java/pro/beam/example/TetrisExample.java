package pro.beam.example;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import pro.beam.api.BeamAPI;
import pro.beam.interactive.net.packet.Protocol;
import pro.beam.interactive.robot.Robot;
import pro.beam.interactive.robot.RobotBuilder;

public class TetrisExample {
    public static void main(String[] args) {
        // Construct an instance of the BeamAPI that will be used for communication with Tetris.
        BeamAPI beam = new BeamAPI();

        // This future will return a Robot that is connected. See usage below for how to add a callback to
        // a ListenableFuture.
        //
        // A RobotBuilder is the entry-point to creating a Robot that you can do things with. It follows
        // the typical builder-pattern as expressed in Java, and an example usage may be found below:
        ListenableFuture<Robot> future = new RobotBuilder().username("<username>")
                                                           .password("<password>")
                                                           .channel(127)
                                                           .build(beam);

        // This is a rudimentary example of how to listen for a callback on a ListenableFuture.
        Futures.addCallback(future, new FutureCallback<Robot>() {
            @Override public void onSuccess(Robot robot) {
                // The robot is connected and ready to be used.

                // Set up an event handler to listen to the Report message, and delegate
                // into the given instance of the MouseMoveListener class.
                robot.on(Protocol.Report.class, new MouseMoveListener());
            }

            @Override public void onFailure(Throwable throwable) {
                // There was an error connecting to the socket.

                System.err.println("Error experienced in connecting to Beam.");
                throwable.printStackTrace();
            }
        });
    }
}
