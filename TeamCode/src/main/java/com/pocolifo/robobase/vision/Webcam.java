package com.pocolifo.robobase.vision;

import android.os.SystemClock;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstracts away certain parts of {@link OpenCvCamera}.
 *
 * @author youngermax
 */
public class Webcam implements AutoCloseable {
	/**
	 * The {@link HardwareMap} associated with this webcam.
	 */
	public final HardwareMap hardwareMap;

	/**
	 * The webcam device.
	 */
	public final WebcamName webcamDevice;

	/**
	 * The {@link OpenCvCamera} associated with this webcam. It can be used for image processing.
	 */
	private OpenCvWebcam webcam;

	/**
	 * The current pipeline that this webcam is using.
	 */
	private AbstractResultCvPipeline<?> pipeline;

	public Webcam(HardwareMap hardwareMap, WebcamName webcamDevice) {
		this.hardwareMap = hardwareMap;
		this.webcamDevice = webcamDevice;
	}

	/**
	 * Initializes a {@link Webcam}.
	 *
	 * @param hardwareMap The {@link HardwareMap} that has the webcam in it.
	 * @param name The name of the webcam in the {@link HardwareMap}.
	 */
	public Webcam(HardwareMap hardwareMap, String name) {
		this(hardwareMap, hardwareMap.get(WebcamName.class, name));
	}

	/**
	 * Opens the camera. For this to be of any use, call {@link Webcam#setPipeline(AbstractResultCvPipeline)}.
	 * Pipelines allow data from the camera to be processed. This is a <strong>blocking method</strong>.
	 * <strong>WHEN YOU'RE DONE WITH THE WEBCAM, CLOSE IT!</strong>
	 *
	 * @param pipeline The initial pipeline which will process camera input.
	 * @throws RuntimeException If there was an error while opening the camera.
	 * @author youngermax
	 */
	public void open(AbstractResultCvPipeline<?> pipeline) throws RuntimeException {
		// Yes, this is equivalent to R.id.cameraMonitorViewId, but it doesn't explicitly require the
		// `FtcRobotController` package, so use this.
		int cameraMonitorView = this.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
							"id", hardwareMap.appContext.getPackageName());

		// TODO: replace this with object lock
		AtomicBoolean ready = new AtomicBoolean(false);
		this.webcam = OpenCvCameraFactory.getInstance().createWebcam(this.webcamDevice, cameraMonitorView);
		this.webcam.setMillisecondsPermissionTimeout(2500);
		this.webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
			@Override
			public void onOpened() {
				Webcam.this.setPipeline(pipeline);
				ready.set(true);
			}

			@Override
			public void onError(int errorCode) {
				throw new RuntimeException("Could not open the camera! Error code: " + errorCode);
			}
		});

		while (!ready.get()) {
			// wait a little so that the comp doesn't burn out
			SystemClock.sleep(250L);
		}
	}

	/**
	 * Sets the pipeline for the webcam which can process the data stream coming in from the webcam.
	 *
	 * @param pipeline The pipeline to use to process data.
	 * @author youngermax
	 */
	public void setPipeline(AbstractResultCvPipeline<?> pipeline) {
		this.pipeline = pipeline;
		this.webcam.setPipeline(pipeline);
		this.webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
		this.pipeline.init();
	}

	/**
	 * Unsets the webcam pipeline; no data will be able to be processed since there is no pipeline active.
	 *
	 * @author youngermax
	 */
	public void clearPipeline() {
		this.pipeline = null;
		this.webcam.setPipeline(null);
		this.webcam.stopStreaming();
	}

	/**
	 * Gets the current active pipeline of the webcam.
	 *
	 * @return The current active pipeline.
	 */
	public AbstractResultCvPipeline<?> getPipeline() {
		return pipeline;
	}

	/**
	 * Closes the camera. <strong>THIS SHOULD BE CALLED AFTER THE CAMERA IS UNNECESSARY TO THE PROGRAM!</strong>
	 *
	 * @throws Exception If an error occurred while closing the camera.
	 * @author youngermax
	 */
	@Override
	public void close() throws Exception {
		this.clearPipeline();
		this.webcam.closeCameraDevice();
	}
}
