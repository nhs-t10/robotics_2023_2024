package com.pocolifo.robobase.bootstrap;

import static com.pocolifo.robobase.utils.JSONUtils.JSONify;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;

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
        this.print(obj == null ? "null" : obj.toString());
    }

    @Override
    public void print(boolean b) {
        this.print(Boolean.toString(b));
    }

    @Override
    public void print(char c) {
        this.print(Character.toString(c));
    }

    @Override
    public void print(int i) {
        this.print(Integer.toString(i));
    }

    @Override
    public void print(long l) {
        super.print(Long.toString(l));
    }

    @Override
    public void print(float f) {
        super.print(Float.toString(f));
    }

    @Override
    public void print(double d) {
        super.print(Double.toString(d));
    }

    @Override
    public void print(char[] s) {
        super.print(Arrays.toString(s));
    }

    @Override
    public void print(String s) {
        this.telemetry.addData("", s);
        this.telemetry.update();
    }

    @Override
    public void println() {
        this.telemetry.addLine();
        this.telemetry.update();
    }

    @Override
    public void println(boolean b) {
        this.println(Boolean.toString(b));
    }

    @Override
    public void println(char c) {
        this.println(Character.toString(c));
    }

    @Override
    public void println(int i) {
        this.println(Integer.toString(i));
    }

    @Override
    public void println(long l) {
        super.println(Long.toString(l));
    }

    @Override
    public void println(float f) {
        super.println(Float.toString(f));
    }

    @Override
    public void println(double d) {
        super.println(Double.toString(d));
    }

    @Override
    public void println(char[] s) {
        super.println(Arrays.toString(s));
    }

    @Override
    public void println(String s) {
        this.telemetry.addData("", s);
        this.println();
    }

    @Override
    public final void println(Object obj) {
        this.println(obj == null ? "null" : obj.toString());
    }

    @Override
    public final void flush() {
        this.telemetry.update();
    }

    @Override
    public final PrintStream printf(String format, Object... args) {
        this.print(String.format(format, args)); // IMPORTANT!! IntelliJ shows String.format as a redundant call, this is not true

        return this;
    }
}
