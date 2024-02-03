package com.pocolifo.robobase.novel;

public interface PathFollower {
    void followPath(NovelMecanumDrive driver);

    double getDuration();
}
