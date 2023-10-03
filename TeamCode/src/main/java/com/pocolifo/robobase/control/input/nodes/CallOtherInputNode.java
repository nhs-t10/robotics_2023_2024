package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;

public class CallOtherInputNode extends InputManagerInputNode {
    private final String target;
    private InputManager boss;

    public CallOtherInputNode(String name) {
        this.target = name;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
    }

    @Override
    public void update() {

    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        InputManagerInputNode targetNode = boss.getInputNode(target);

        if(targetNode == null) return InputManagerNodeResult.FALSE;
        else return targetNode.getResult();
    }

    @Override
    public int complexity() {
        InputManagerInputNode targetNode = boss.getInputNode(target);

        if(targetNode == null) return Integer.MAX_VALUE;
        else return targetNode.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        InputManagerInputNode targetNode = boss.getInputNode(target);

        if(targetNode == null) return new String[0];
        else return targetNode.getKeysUsed();
    }

    @Override
    public boolean usesKey(String s) {
        InputManagerInputNode targetNode = boss.getInputNode(target);

        if(targetNode == null) return false;
        else return targetNode.usesKey(s);
    }
}
