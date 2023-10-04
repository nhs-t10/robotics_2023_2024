package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;

public class DoubleClickNode extends InputManagerInputNode {
    private final InputManagerInputNode node;
    private InputManager boss;

    private boolean lastTimeClicked;

    boolean firstClicked = false;
    private long timeClickedFirst = 0;
    private boolean currentlyDblClicked;

    private final long clickLimitOffsetMs = InputManager.DOUBLE_CLICK_TIME_MS;

    private final InputManagerNodeResult result = new InputManagerNodeResult();


    public DoubleClickNode(InputManagerInputNode node) {
        this.node = node;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
        node.init(boss);
    }

    @Override
    public void update() {
        node.update();
        boolean clickedCheck = node.getResult().getBool();

        //only register a doubleclick on rising edge
        boolean isClickedRisingEdge = clickedCheck && !lastTimeClicked;

        long now = System.currentTimeMillis();
        long lastValidClickTime = now - clickLimitOffsetMs;

        //if the first click was before the last valid time, reset the time-keepy-tracky variables
        if (timeClickedFirst <= lastValidClickTime) {
            firstClicked = false;
            timeClickedFirst = 0;
        }

        if(isClickedRisingEdge) {
            if(firstClicked) {
                currentlyDblClicked = true;
            } else {
                timeClickedFirst = System.currentTimeMillis();
                firstClicked = true;
            }
        } else {
            currentlyDblClicked = false;
        }

        lastTimeClicked = clickedCheck;
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        result.setBool(currentlyDblClicked);
        return result;
    }

    @Override
    public int complexity() {
        return node.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return node.getKeysUsed();
    }

    @Override
    public boolean usesKey(String s) {
        return node.usesKey(s);
    }
}
