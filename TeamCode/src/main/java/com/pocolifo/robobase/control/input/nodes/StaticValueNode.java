package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;

public class StaticValueNode extends InputManagerInputNode {
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public StaticValueNode(float f) {
        result.setFloat(f);
    }

    @Override
    public void init(InputManager boss) {
    }

    @Override
    public void update() {
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        return result;
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String[] getKeysUsed() {
        return new String[0];
    }

    @Override
    public boolean usesKey(String s) {
        return false;
    }
}
