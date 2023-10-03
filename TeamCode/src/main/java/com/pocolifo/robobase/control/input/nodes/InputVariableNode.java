package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;

public class InputVariableNode extends InputManagerInputNode {
    private final String varname;
    private InputManager boss;

    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public InputVariableNode(String s) {
        this.varname = s;
    }

    @Override
    public void init(InputManager boss) {
        this.boss = boss;
    }

    @Override
    public void update() {
        this.result.setFloat(boss.getInputVariable(varname));
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
