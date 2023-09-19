package com.pocolifo.robobase.bootstrap;

/**
 * Base class for TeleOp code to go.
 *
 * @author youngermax
 */
public abstract class TeleOpOpMode extends BootstrappedOpMode {
    @Override
    public final void init() {
        super.init();

        this.initialize();
    }

    // These methods probably shouldn't be used in TeleOp
    @Override
    public final void init_loop() {
    }

    /**
     * The initialization method for TeleOp.
     * The reason for having a separate initialize method is so that you have to override it. This also
     * eliminates errors occurring if {@code super.init()} isn't called.
     *
     * @author youngermax
     */
    public abstract void initialize();
}
