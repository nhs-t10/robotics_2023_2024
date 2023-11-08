package com.pocolifo.robobase.control.input.nodes;

import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;
import com.pocolifo.robobase.control.input.nodeUtils.ArrayUtils;

public class InversionNode extends InputManagerInputNode {
    private final InputManagerInputNode input;
    private InputManagerInputNode inverter;
    private final InputManagerNodeResult result = new InputManagerNodeResult();
    private boolean inverted = false;

    /**
     * Multiplies one number by another.
     * @param input The input listed in the MultiplyNode.
     * @param inverted An input that determines whether or not the input should be inverted.
     * @see MinusNode#MinusNode(InputManagerInputNode, InputManagerInputNode) MinusNode
     */
    public InversionNode(InputManagerInputNode input, InputManagerInputNode inverted) {
        this.inverter = inverted;
        this.input = input;
    }

    @Override
    public void init(InputManager boss) {
        input.init(boss);
        inverter.init(boss);
    }

    public void update() {
        input.update();
        inverter.update();
        inverted = inverter.getResult().getBool();
        float f = input.getResult().getFloat();
        if (inverted) {
                f = f * -1;
        }
        result.setFloat(f);
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        return result;
    }

    @Override
    public int complexity() {
        return input.complexity() + inverter.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return ArrayUtils.concatArrays(input.getKeysUsed(), inverter.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        if (input.usesKey(s) || inverter.usesKey(s)){
            return true;
        }
        return false;
    }
}
