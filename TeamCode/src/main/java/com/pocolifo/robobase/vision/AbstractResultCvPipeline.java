package com.pocolifo.robobase.vision;

import org.openftc.easyopencv.OpenCvPipeline;

/**
 * An {@link OpenCvPipeline} which has a result.
 *
 * @param <T> The type of the result from the pipeline.
 * @author youngermax
 */
public abstract class AbstractResultCvPipeline<T> extends OpenCvPipeline {
	/**
	 * The result of the webcam processor.
	 */
	protected T result;

	/**
	 * Get the current result of the pipeline.
	 *
	 * @return The result of the pipeline.
	 */
	public T getResult() {
		return result;
	}

	/**
	 * Runs when the pipeline is first set.
	 */
	public abstract void init();
}