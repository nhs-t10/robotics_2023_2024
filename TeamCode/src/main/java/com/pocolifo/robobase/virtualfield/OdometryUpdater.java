package com.pocolifo.robobase.virtualfield;

import com.pocolifo.robobase.novel.hardware.NovelOdometry;

public class OdometryUpdater extends Thread {
    private NovelOdometry odometry;
    private int updatesPerSecond;
    private boolean running;
    public void stopRunning() {
        running = false;
    }

    public OdometryUpdater(NovelOdometry odometry) {
        this(odometry, 500);
    }

    public OdometryUpdater(NovelOdometry odometry, int updatesPerSecond) {
        this.odometry = odometry;
        this.updatesPerSecond = updatesPerSecond;
        running = true;
    }
    @Override
    public void run() {
        while (running) {
            odometry.update();
            try {
                Thread.sleep(1000 / updatesPerSecond);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
