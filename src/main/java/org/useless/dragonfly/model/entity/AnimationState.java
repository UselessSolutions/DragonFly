package org.useless.dragonfly.model.entity;

import java.util.function.Consumer;

public class AnimationState {
	private static final long STOPPED = Long.MAX_VALUE;
	private long lastTime = Long.MAX_VALUE;
	private long accumulatedTime;

	public void start(int tickCount) {
		this.lastTime = (long) tickCount * 1000L / 20L;
		this.accumulatedTime = 0L;
	}

	public void startIfStopped(int tickCount) {
		if (!this.isStarted()) {
			this.start(tickCount);
		}
	}

	public void animateWhen(boolean condition, int tickCount) {
		if (condition) {
			this.startIfStopped(tickCount);
		} else {
			this.stop();
		}
	}

	public void stop() {
		this.lastTime = Long.MAX_VALUE;
	}

	public void ifStarted(Consumer<AnimationState> action) {
		if (this.isStarted()) {
			action.accept(this);
		}
	}

	public void updateTime(float ageInTicks, float speed) {
		if (this.isStarted()) {
			long i = lfloor((double) (ageInTicks * 1000.0F / 20.0F));
			this.accumulatedTime = this.accumulatedTime + (long) ((float) (i - this.lastTime) * speed);
			this.lastTime = i;
		}
	}

	public void fastForward(int duration, float speed) {
		if (this.isStarted()) {
			this.accumulatedTime += (long) ((float) (duration * 1000) * speed) / 20L;
		}
	}

	public long getAccumulatedTime() {
		return this.accumulatedTime;
	}

	public boolean isStarted() {
		return this.lastTime != Long.MAX_VALUE;
	}

	private static long lfloor(double p_14135_) {
		long i = (long) p_14135_;
		return p_14135_ < (double) i ? i - 1L : i;
	}
}
