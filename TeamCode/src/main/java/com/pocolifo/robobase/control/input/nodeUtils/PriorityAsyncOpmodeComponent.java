package com.pocolifo.robobase.control.input.nodeUtils;

public class PriorityAsyncOpmodeComponent {
    public static void start(Runnable r) {
        (new AsyncRunnerThread(r)).start();
    }
    private static class AsyncRunnerThread extends Thread {
        private final Runnable r;

        public AsyncRunnerThread(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            try {
                /*while (FeatureManager.isOpModeRunning) {
                    r.run();
                    Thread.yield();
                }*/
            } catch(Throwable t) {
                System.out.println("Silent error in 'PriorityAsyncOpmodeComponent'");
            }
        }
    }
}
