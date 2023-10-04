package com.pocolifo.robobase.bootstrap;

import static com.pocolifo.robobase.utils.JSONUtils.JSONify;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * An implementation of PrintStream which is used as the implementation of System.out and System.err.
 * The purpose of this is so that the use of System.out#println and other methods work properly. This
 * is <strong>much</strong> more friendly for developers who already know Java.
 *
 * @author youngermax
 * @see PrintStream
 */
public class RobotDebugPrintStream extends PrintStream {
    /**
     * An implementation of OutputStream which does not write to anything.
     * An {@link OutputStream} is needed to construct a {@link PrintStream}, so just use one that goes to the void.
     *
     * @see OutputStream
     * @see RobotDebugPrintStream
     */
    public static final OutputStream VOID_STREAM = new OutputStream() {
        @Override
        public void write(int i) {
        }
    };

    private final Telemetry telemetry;

    RobotDebugPrintStream(Telemetry telemetry) {
        super(VOID_STREAM);

        // Set up Telemetry instance to be more like standard Java
        this.telemetry = telemetry;

        this.telemetry.setCaptionValueSeparator("");
        this.telemetry.setItemSeparator("");
        this.telemetry.setMsTransmissionInterval(10);
        this.telemetry.setAutoClear(false);
        this.telemetry.update();
    }

    @Override
    public final void print(Object obj) {
        this.telemetry.addData("", JSONify(obj));
        this.telemetry.update();
    }

    @Override
    public final void println(Object obj) {
        this.telemetry.addLine();
        this.telemetry.addData("", JSONify(obj));
        this.telemetry.update();
    }

    @Override
    public final void flush() {
        this.telemetry.update();
    }

    @Override
    public final PrintStream printf(String format, Object... args) {
        this.print(String.format(format, args)); // IntelliJ shows String.format as a redundant call, this is not true

        return this;
    }
}
