package com.pocolifo.robobase.bootstrap;

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
    
    private void printlnString(String s) {
        this.telemetry.addLine(s);
        this.telemetry.update();
    }

    private void printString(String s) {
        this.telemetry.addData("", s);
        this.telemetry.update();
    }
    

    @Override
    public final void print(Object obj) {
        printString(obj == null ? "null" : obj.toString());
    }

    @Override
    public void print(boolean b) {
        printString(Boolean.toString(b));
    }

    @Override
    public void print(char c) {
        printString(Character.toString(c));
    }

    @Override
    public void print(int i) {
        printString(Integer.toString(i));
    }

    @Override
    public void print(long l) {
        printString(Long.toString(l));
    }

    @Override
    public void print(float f) {
        printString(Float.toString(f));
    }

    @Override
    public void print(double d) {
        printString(Double.toString(d));
    }

    @Override
    public void print(char[] s) {
        printString(Arrays.toString(s));
    }

    @Override
    public void print(String s) {
        printString(s);
    }

    @Override
    public void println() {
        this.telemetry.addLine();
        this.telemetry.update();
    }

    @Override
    public void println(boolean b) {
        printlnString(Boolean.toString(b));
    }

    @Override
    public void println(char c) {
        printlnString(Character.toString(c));
    }

    @Override
    public void println(int i) {
        printlnString(Integer.toString(i));
    }

    @Override
    public void println(long l) {
        printlnString(Long.toString(l));
    }

    @Override
    public void println(float f) {
        printlnString(Float.toString(f));
    }

    @Override
    public void println(double d) {
        printlnString(Double.toString(d));
    }

    @Override
    public void println(char[] s) {
        printlnString(Arrays.toString(s));
    }

    @Override
    public void println(String s) {
        printlnString(s);
    }

    @Override
    public final void println(Object obj) {
        printlnString(obj == null ? "null" : obj.toString());
    }

    @Override
    public final void flush() {
        this.telemetry.update();
    }

    @Override
    public final PrintStream printf(String format, Object... args) {
        printString(String.format(format, args)); // IMPORTANT!! IntelliJ shows String.format as a redundant call, this is not true

        return this;
    }
}
