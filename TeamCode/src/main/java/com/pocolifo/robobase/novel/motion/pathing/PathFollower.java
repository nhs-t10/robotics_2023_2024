package com.pocolifo.robobase.novel.motion.pathing;

import com.pocolifo.robobase.novel.motion.NovelMecanumDriver;

public interface PathFollower {
    void followPath(NovelMecanumDriver driver);

    double getDuration();
}
