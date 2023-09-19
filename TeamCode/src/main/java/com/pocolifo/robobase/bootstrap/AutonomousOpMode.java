package com.pocolifo.robobase.bootstrap;

/**
 * Base class for Autonomous code to go.
 *
 * @author youngermax
 */
public abstract class AutonomousOpMode extends BootstrappedOpMode {
    @Override
    public final void init() {
        super.init();
        this.initialize();
    }

    @Override
    public final void start() {
        this.run();
        this.stop();
    }

    // These methods shouldn't be used in Autonomous
    @Override
    public final void init_loop() {
    }

    @Override
    public final void loop() {
    }

    /**
     * The initialization method for Autonomous.
     * The reason for having a separate initialize method is so that you have to override it. This also
     * eliminates errors occurring if {@code super.init()} isn't called.
     *
     * @author youngermax
     */
    public abstract void initialize();

    /**
     * Run the Autonomous code (i.e. move the robot or complete another action).
     * The reason for having a separate run/start method is so that you have to override it.
     *
     * @author youngermax
     */
    public abstract void run();
}
