package com.pocolifo.robobase.control.input.nodes;



import androidx.annotation.NonNull;

import com.pocolifo.robobase.control.input.InputManager;
import com.pocolifo.robobase.control.input.InputManagerNodeResult;
import com.pocolifo.robobase.control.input.buttonhandles.ButtonHandle;

public class ButtonNode extends InputManagerInputNode {
    private ButtonHandle keyHandle;
    private final String key;
    private InputManager boss;
    private final InputManagerNodeResult result = new InputManagerNodeResult();

    /**
     * Takes the name of a specified button on the controller. <br>
     * If the button is pressed, it returns true. If the button is not pressed, it returns false. <br>
     * @param key The name of the button, in any format. It's not picky.
     */
    public ButtonNode(String key) {
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
        if(keyHandle == null) {
            System.out.println(key);
        }
        this.result.setFloat(keyHandle.get());
        return result;
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String[] getKeysUsed() {
        return new String[] {key};
    }

    @Override
    public boolean usesKey(String s) {
        return key.equals(s);
    }
}
