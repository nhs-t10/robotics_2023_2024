package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;
import com.pocolifo.robobase.control.input.buttonhandles.ButtonHandle;

public class JoystickNode extends InputManagerInputNode {
    private final String key;
    private ButtonHandle keyHandle;

    private final InputManagerNodeResult result = new InputManagerNodeResult();

    public JoystickNode(String key) {
        this.key = key;
    }

    @Override
    public void init(InputManager boss) {
        this.keyHandle = boss.getButtonHandle(key);
    }

    @Override
    public void update() {

    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        this.result.setFloat(keyHandle.get());
        return result;
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String[] getKeysUsed() {
        return new String[] { key };
    }

    @Override
    public boolean usesKey(String s) {
        return key.equals(s);
    }
}
