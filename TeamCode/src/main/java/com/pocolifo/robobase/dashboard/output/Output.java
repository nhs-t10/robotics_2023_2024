package com.pocolifo.robobase.dashboard.output;

import java.util.concurrent.Callable;

public class Output {
    private String name;
    private Callable<String> outputDataAccessorAccessor;
    private String data;

    /**
     * Output to be sent to the web.
     * @param name The name of this output (label)
     * @param outputDataAccessorAccessor A `Callable` that returns a `String`
     */
    public Output(String name, Callable<String> outputDataAccessorAccessor) throws Exception {
        this.name = name;
        this.outputDataAccessorAccessor = outputDataAccessorAccessor;
        OutputManager.addValue(this);
        update();
    }

    public void update() throws Exception {
        data = outputDataAccessorAccessor.call();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return data;
    }
}
