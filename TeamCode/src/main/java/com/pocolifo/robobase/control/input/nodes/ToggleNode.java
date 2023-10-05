package com.pocolifo.robobase.control.input.nodes;


import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;

public class ToggleNode extends InputManagerInputNode {
    private final InputManagerInputNode node;

    private boolean isPressed;
    private boolean wasPressed;
    private boolean toggledOn;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    /**
     * Turns everything inside this node into a toggle. <br>
     * When the button is pressed, it toggles on and will remain acting like it is held until the button is pressed again.
     * @param node The input contained within the ToggleNode
     */
    public ToggleNode(InputManagerInputNode node) {
        this.node = node;
    }

    @Override
    public void init(InputManager boss) {
        node.init(boss);
    }

    @Override
    public void update() {
        node.update();
        isPressed = node.getResult().getBool();
        if(isPressed && !wasPressed) {
            toggledOn = !toggledOn;
        }
        wasPressed = isPressed;
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        result.setBool(toggledOn);
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