package vbartalis.engine.window;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.glfw.GLFW;

/**
 * Found this code on the internet here: http://forum.lwjgl.org/index.php?topic=6582.0
 *
 * @author Riven
 * @author kappaOne
 */
public class Sync {

	private static final long NANOS_IN_SECOND = 1000L * 1000L * 1000L;
	private long nextFrame = 0;
	private boolean initialised = false;
	private RunningAvg sleepDurations = new RunningAvg(10);
	private RunningAvg yieldDurations = new RunningAvg(10);

	@Getter @Setter
	private int fps;

	public Sync(int fps) {
		this.fps = fps;
	}

	public void sync() {
		if (fps <= 0)
			return;
		if (!initialised)
			initialise();

		try {
			for (long t0 = getTime(), t1; (nextFrame - t0) > sleepDurations.avg(); t0 = t1) {
				Thread.sleep(1);
				sleepDurations.add((t1 = getTime()) - t0); // update average
															// sleep time
			}
			sleepDurations.dampenForLowResTicker();

			for (long t0 = getTime(), t1; (nextFrame - t0) > yieldDurations.avg(); t0 = t1) {
				Thread.yield();
				yieldDurations.add((t1 = getTime()) - t0); // update average
															// yield time
			}
		} catch (InterruptedException e) {

		}
		nextFrame = Math.max(nextFrame + NANOS_IN_SECOND / fps, getTime());
	}

	private void initialise() {
		initialised = true;

		sleepDurations.init(1000 * 1000);
		yieldDurations.init((int) (-(getTime() - getTime()) * 1.333));

		nextFrame = getTime();

		String osName = System.getProperty("os.name");

		if (osName.startsWith("Win")) {
			// On windows the sleep functions can be highly inaccurate by
			// over 10ms making in unusable. However it can be forced to
			// be a bit more accurate by running a separate sleeping daemon
			// thread.
			Thread timerAccuracyThread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(Long.MAX_VALUE);
					} catch (Exception e) {
					}
				}
			});

			timerAccuracyThread.setName("LWJGL3 Timer");
			timerAccuracyThread.setDaemon(true);
			timerAccuracyThread.start();
		}
	}

	private long getTime() {
		return (long) (GLFW.glfwGetTime() * NANOS_IN_SECOND);
	}

	private class RunningAvg {
		private final long[] slots;
		private int offset;

		private static final long DAMPEN_THRESHOLD = 10 * 1000L * 1000L; // 10ms
		private static final float DAMPEN_FACTOR = 0.9f; // don't change: 0.9f
															// is exactly right!

		public RunningAvg(int slotCount) {
			this.slots = new long[slotCount];
			this.offset = 0;
		}

		public void init(long value) {
			while (this.offset < this.slots.length) {
				this.slots[this.offset++] = value;
			}
		}

		public void add(long value) {
			this.slots[this.offset++ % this.slots.length] = value;
			this.offset %= this.slots.length;
		}

		public long avg() {
			long sum = 0;
			for (int i = 0; i < this.slots.length; i++) {
				sum += this.slots[i];
			}
			return sum / this.slots.length;
		}

		public void dampenForLowResTicker() {
			if (this.avg() > DAMPEN_THRESHOLD) {
				for (int i = 0; i < this.slots.length; i++) {
					this.slots[i] *= DAMPEN_FACTOR;
				}
			}
		}
	}
}