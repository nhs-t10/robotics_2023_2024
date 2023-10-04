package com.pocolifo.robobase.control.input;

import com.pocolifo.robobase.control.input.nodes.InputManagerInputNode;

public class InputUpdateThread extends Thread {
    private final InputManagerInputNode node;

    public InputUpdateThread(InputManagerInputNode node) {
        this.node = node;
    }

    public void run() {
        try {
            while(InputManager.isOpModeRunning) {
                node.update();

            }
        } catch(Throwable t) {
        }
    }
}
