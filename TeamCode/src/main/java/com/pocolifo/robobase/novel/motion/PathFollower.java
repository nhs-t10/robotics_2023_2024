package com.pocolifo.robobase.novel.motion;

public interface PathFollower {
    void followPath(NovelMecanumDrive driver);

    double getDuration();
}
