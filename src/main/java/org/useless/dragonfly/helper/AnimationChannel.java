package org.useless.dragonfly.helper;

import org.useless.dragonfly.model.entity.processor.BenchEntityBones;
import org.useless.dragonfly.utilities.vector.Vector3f;

import java.util.List;

public class AnimationChannel {
	public interface Interpolation {
		Vector3f apply(Vector3f animationVecCache, float keyframeDelta, List<KeyFrame> keyframes, int currentKeyframeIdx, int nextKeyframeIdx, float scale);
	}

	public static class Interpolations {
		public static final AnimationChannel.Interpolation LINEAR = (p_253292_, p_253293_, p_253294_, p_253295_, p_253296_, p_253297_) -> {
			Vector3f vector3f = p_253294_.get(p_253295_).vector3f();
			Vector3f vector3f1 = p_253294_.get(p_253296_).vector3f();
			return vector3f.lerp(vector3f1, p_253293_, p_253292_).mul(p_253297_);
		};
		public static final AnimationChannel.Interpolation CATMULLROM = (p_254076_, p_232235_, p_232236_, p_232237_, p_232238_, p_232239_) -> {
			Vector3f vector3f = p_232236_.get(Math.max(0, p_232237_ - 1)).vector3f();
			Vector3f vector3f1 = p_232236_.get(p_232237_).vector3f();
			Vector3f vector3f2 = p_232236_.get(p_232238_).vector3f();
			Vector3f vector3f3 = p_232236_.get(Math.min(p_232236_.size() - 1, p_232238_ + 1)).vector3f();
			p_254076_.set(
				AnimationHelper.catmullrom(p_232235_, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * p_232239_,
				AnimationHelper.catmullrom(p_232235_, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * p_232239_,
				AnimationHelper.catmullrom(p_232235_, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * p_232239_
			);
			return p_254076_;
		};

		public static Interpolation getInterpolations(String name) {
			if (name.equals("catmullrom")) {
				return CATMULLROM;
			} else {
				return LINEAR;
			}
		}
	}

	public interface Target {
		void apply(BenchEntityBones modelPart, Vector3f animationVector);
	}

	public static class Targets {
		public static final AnimationChannel.Target POSITION = BenchEntityBones::offsetPos;
		public static final AnimationChannel.Target ROTATION = BenchEntityBones::offsetRotation;
		public static final AnimationChannel.Target SCALE = BenchEntityBones::offsetScale;
	}
}
