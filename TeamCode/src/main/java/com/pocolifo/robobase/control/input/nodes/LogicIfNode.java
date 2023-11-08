package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;
import com.pocolifo.robobase.control.input.nodeUtils.*;

public class LogicIfNode extends InputManagerInputNode {
    private final InputManagerInputNode node;
    private final InputManagerInputNode nodeIfOff;
    private final InputManagerInputNode nodeIfOn;

    private boolean wasPressed;
    private boolean toggledOn;
    private boolean on;

    public LogicIfNode(InputManagerInputNode onOff, InputManagerInputNode ifOn, InputManagerInputNode ifOff) {
        this.node = onOff;
        this.nodeIfOn = ifOn;
        this.nodeIfOff = ifOff;
    }

    @Override
    public void init(InputManager boss) {
        node.init(boss);
        nodeIfOff.init(boss);
        nodeIfOn.init(boss);
    }

    @Override
    public void update() {
        node.update();
        nodeIfOn.update();
        nodeIfOff.update();

        on = node.getResult().getBool();
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        if(on) return nodeIfOn.getResult();
        else return nodeIfOff.getResult();
    }

    @Override
    public int complexity() {
        return node.complexity() + nodeIfOff.complexity() + nodeIfOn.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return ArrayUtils.concatArrays(node.getKeysUsed(), nodeIfOff.getKeysUsed(), nodeIfOn.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        return node.usesKey(s) ||  nodeIfOn.usesKey(s) || nodeIfOff.usesKey(s);
    }
}
