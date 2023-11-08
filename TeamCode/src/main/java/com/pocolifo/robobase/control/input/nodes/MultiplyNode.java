package com.pocolifo.robobase.control.input.nodes;

import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;
import com.pocolifo.robobase.control.input.nodeUtils.ArrayUtils;

public class MultiplyNode extends InputManagerInputNode {
    private final InputManagerInputNode input;
    private InputManagerInputNode multiplier;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    /**
     * Multiplies one number by another.
     * @param input The input listed in the MultiplyNode
     * @param coefficient The number that the input will be multiplied by (it can be a second input, if you want)
     * @see MinusNode#MinusNode(InputManagerInputNode, InputManagerInputNode) MinusNode
     */
    public MultiplyNode(InputManagerInputNode coefficient, InputManagerInputNode input) {
        this.input = input;
        this.multiplier = coefficient;
    }
    public MultiplyNode(InputManagerInputNode input, float multiplyBy) {
        this.multiplier = new StaticValueNode(multiplyBy);
        this.input = input;
    }
    public MultiplyNode(float multiplyBy, InputManagerInputNode input) {
        this.multiplier = new StaticValueNode(multiplyBy);
        this.input = input;
    }

    @Override
    public void init(InputManager boss) {
        input.init(boss);
        multiplier.init(boss);
    }

    public void update() {
        input.update();
        multiplier.update();

        float[] f = input.getResult().getFloatArray();
        float[] res = new float[f.length];

        float muliplier = multiplier.getResult().getFloat();
        for(int i = 0; i < res.length; i++) {
            res[i] = f[i] * muliplier;
        }
        result.setFloatArray(res);
    }

    @NonNull
    @Override
    public InputManagerNodeResult getResult() {
        return result;
    }

    @Override
    public int complexity() {
        return input.complexity() + multiplier.complexity() + 1;
    }

    @Override
    public String[] getKeysUsed() {
        return ArrayUtils.concatArrays(input.getKeysUsed(), multiplier.getKeysUsed());
    }

    @Override
    public boolean usesKey(String s) {
        if (input.usesKey(s) || multiplier.usesKey(s)){
            return true;
        }
        return false;
    }
}
