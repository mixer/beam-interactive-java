package pro.beam.example;

import pro.beam.interactive.event.EventListener;
import pro.beam.interactive.net.packet.Protocol;

import java.awt.*;

// KeyPressListener is responsible for listening to Report messages and handling them.
public class KeyPressListener implements EventListener<Protocol.Report> {
    // This Robot is an AWT Robot, not a Beam one. It knows how to press keys on the keyboard.
    protected Robot keyboard;

    // Basic no-args constructor, nothing special here...
    public KeyPressListener() {
        try {
            this.keyboard = new Robot();
        } catch (AWTException ignored) { }
    }

    // The handler method. This method is invoked when a new Report message is received over the wire.
    // It takes some information given in the Report message and does something with it (in this case,
    // it moves the mouse across the screen.
    @Override public void handle(Protocol.Report report) {
        // Iterate through each tactile key-press in the report's tactile list, and preform actions
        // based on the state of a particular TactileInfo instance.
        for (Protocol.Report.TactileInfo tactile : report.getTactileList()) {
            // If the key is not pressed down, skip it.
            if (!tactile.hasDown()) continue;

            // Otherwise, trigger an AWT key press with the same keycode.
            this.keyboard.keyPress(tactile.getCode());
        }
    }
}
