package com.pocolifo.robobase.control.input.nodes;

import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;
import com.pocolifo.robobase.control.input.nodeUtils.MathUtils;

public class LogicOrNode extends InputManagerInputNode {
    private final InputManagerInputNode[] inputs;

    private final InputManagerNodeResult result = new InputManagerNodeResult();

    /**
     * Takes an array of multiple inputs and returns true if any of the inputs individually evaluate to true. <br>
     * If and only if all of the inputs evaluate false, the AnyNode will evaluate false.
     * @param inputs The list of inputs included in the AnyNode
     * @see LogicAndNode#LogicAndNode(InputManagerInputNode...)  AllNode
     */
    public LogicOrNode(InputManagerInputNode... inputs) {
        this.inputs = inputs;
    }

    @Override
    public void init(InputManager boss) {
        for(InputManagerInputNode node : inputs) node.init(boss);
    }

    @Override
    public void update() {
        for(InputManagerInputNode n : inputs) n.update();
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        boolean isTrue = false;
        for(InputManagerInputNode b : inputs) {
            if(b.getResult().getBool()){
                isTrue = true;
                break;
            }
        }
        result.setBool(isTrue);
        return result;
    }

    @Override
    public int complexity() {
        int r = 0;
        for (InputManagerInputNode n : inputs) r += n.complexity();
        return r + 1;
    }

    @Override
    public String[] getKeysUsed() {
        String[][] keylists = new String[inputs.length][];
        for(int i = 0; i < inputs.length; i++) {
            keylists[i] = inputs[i].getKeysUsed();
        }
        return MathUtils.concatArrays(keylists);
    }

    @Override
    public boolean usesKey(String s) {
        for(InputManagerInputNode n : inputs) {
            if(n.usesKey(s)) return true;
        }
        return false;
    }
}
