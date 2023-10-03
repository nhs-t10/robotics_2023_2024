package com.pocolifo.robobase.control.input.nodes;


import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class InputManagerInputNode {
    public InputManagerInputNode[] overlappingNodes = new InputManagerInputNode[0];

    public abstract void init(InputManager boss);
    public abstract void update();
    @NotNull
    public abstract InputManagerNodeResult getResult();

    public abstract int complexity();
    public abstract String[] getKeysUsed();
    public abstract boolean usesKey(String s);

    public String name;

    public void setName(String name) {
        this.name = name;
    }

    public void updateOverlaps(InputManager.InputOverlapResolutionMethod newMethod, InputManagerInputNode[] allRootNodes) {
        if(newMethod == InputManager.InputOverlapResolutionMethod.BOTH_CHILDREN_CAN_SPEAK) {
            //nothing
            this.overlappingNodes = new InputManagerInputNode[0];
        } else {
            String[] keysUsed = getKeysUsed();
            ArrayList<InputManagerInputNode> overlappingNodeList = new ArrayList<>();
            for(InputManagerInputNode comparedNode : allRootNodes) {
                if(comparedNode == this) continue;

                for(String key : keysUsed) {
                    if(comparedNode.usesKey(key)) {
                        if(newMethod == InputManager.InputOverlapResolutionMethod.LEAST_COMPLEX_ARE_THE_FAVOURITE_CHILD &&comparedNode.complexity() < complexity()) overlappingNodeList.add(comparedNode);
                        else if(newMethod == InputManager.InputOverlapResolutionMethod.MOST_COMPLEX_ARE_THE_FAVOURITE_CHILD && comparedNode.complexity() > complexity()) overlappingNodeList.add(comparedNode);
                        break;
                    }
                }
            }
            this.overlappingNodes = overlappingNodeList.toArray(new InputManagerInputNode[0]);
        }
    }
    public boolean isOverlapped() {
        for(InputManagerInputNode n : overlappingNodes) {
            if(n.getResult().getBool()) return true;
        }
        return false;
    }
    public InputManagerNodeResult getOverlappedResult() {
        if(isOverlapped()) return InputManagerNodeResult.FALSE;
        else return getResult();
    }
}
