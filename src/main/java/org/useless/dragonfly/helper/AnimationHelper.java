package org.useless.dragonfly.helper;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.MathHelper;
import org.useless.dragonfly.DragonFly;
import org.useless.dragonfly.model.entity.BenchEntityModel;
import org.useless.dragonfly.model.entity.animation.Animation;
import org.useless.dragonfly.model.entity.animation.AnimationData;
import org.useless.dragonfly.model.entity.animation.BoneData;
import org.useless.dragonfly.model.entity.animation.PostData;
import org.useless.dragonfly.model.entity.processor.BenchEntityBones;
import org.useless.dragonfly.utilities.Utilities;
import org.useless.dragonfly.utilities.vector.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.IntPredicate;

public class AnimationHelper {
	public static final Map<String, Animation> registeredAnimations = new HashMap<>();

	public static Animation getOrCreateEntityAnimation(String modID, String animationSource) {
		String animationKey = getAnimationLocation(modID, animationSource);
		if (registeredAnimations.containsKey(animationKey)){
			return registeredAnimations.get(animationKey);
		}

		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(Utilities.getResourceAsStream(animationKey))));
		Animation animation = DragonFly.GSON.fromJson(reader, Animation.class);
		registeredAnimations.put(animationKey, animation);
		return animation;
	}

	public static String getAnimationLocation(String modID, String animationSource) {
		if (!animationSource.endsWith(".json")) {
			animationSource += ".json";
		}
		return "/assets/" + modID + "/animation/" + animationSource;
	}

	public static void animate(BenchEntityModel entityModel, AnimationData animationData, long time, float scale, Vector3f animationVecCache) {
		float seconds = getElapsedSeconds(animationData, time);

		for (Map.Entry<String, BoneData> entry : animationData.getBones().entrySet()) {
			Optional<BenchEntityBones> optional = entityModel.getAnyDescendantWithName(entry.getKey());
			Map<String, PostData> postionMap = entry.getValue().getPosition();
			List<KeyFrame> positionFrame = Lists.newArrayList();

			postionMap.entrySet().stream().sorted(Comparator.comparingDouble((test) -> (Float.parseFloat(test.getKey())))).forEach(key -> {
				positionFrame.add(new KeyFrame(Float.parseFloat(key.getKey()), key.getValue().getPost(), key.getValue().getLerpMode()));
			});
			optional.ifPresent(p_232330_ -> positionFrame.forEach((keyFrame2) -> {
				int i = Math.max(0, binarySearch(0, positionFrame.size(), p_232315_ -> seconds <= positionFrame.get(p_232315_).duration) - 1);
				int j = Math.min(positionFrame.size() - 1, i + 1);
				KeyFrame keyframe = positionFrame.get(i);
				KeyFrame keyframe1 = positionFrame.get(j);
				float f1 = seconds - keyframe.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				AnimationChannel.Interpolations.getInterpolations(keyFrame2.lerp_mode).apply(animationVecCache, f2, positionFrame, i, j, scale);
				AnimationChannel.Targets.ROTATION.apply(p_232330_, animationVecCache);

			}));
			Map<String, PostData> rotationMap = entry.getValue().getRotation();
			List<KeyFrame> rotationFrame = Lists.newArrayList();

			rotationMap.entrySet().stream().sorted(Comparator.comparingDouble((test) -> (Float.parseFloat(test.getKey())))).forEach(key -> {
				rotationFrame.add(new KeyFrame(Float.parseFloat(key.getKey()), key.getValue().getPost(), key.getValue().getLerpMode()));
			});
			optional.ifPresent(p_232330_ -> rotationFrame.forEach((keyFrame3) -> {
				int i = Math.max(0, binarySearch(0, rotationFrame.size(), p_232315_ -> seconds <= rotationFrame.get(p_232315_).duration) - 1);
				int j = Math.min(rotationFrame.size() - 1, i + 1);
				KeyFrame keyframe = rotationFrame.get(i);
				KeyFrame keyframe1 = rotationFrame.get(j);
				float f1 = seconds - keyframe.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				AnimationChannel.Interpolations.getInterpolations(keyFrame3.lerp_mode).apply(animationVecCache, f2, rotationFrame, i, j, scale);
				AnimationChannel.Targets.ROTATION.apply(p_232330_, animationVecCache);

			}));

			Map<String, PostData> scaleMap = entry.getValue().getScale();
			List<KeyFrame> scaleFrame = Lists.newArrayList();

			scaleMap.entrySet().stream().sorted(Comparator.comparingDouble((test) -> (Float.parseFloat(test.getKey())))).forEach(key -> {
				scaleFrame.add(new KeyFrame(Float.parseFloat(key.getKey()), key.getValue().getPost(), key.getValue().getLerpMode()));
			});
			optional.ifPresent(p_232330_ -> scaleFrame.forEach((keyFrame3) -> {
				int i = Math.max(0, binarySearch(0, scaleFrame.size(), p_232315_ -> seconds <= scaleFrame.get(p_232315_).duration) - 1);
				int j = Math.min(scaleFrame.size() - 1, i + 1);
				KeyFrame keyframe = scaleFrame.get(i);
				KeyFrame keyframe1 = scaleFrame.get(j);
				float f1 = seconds - keyframe.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				AnimationChannel.Interpolations.getInterpolations(keyFrame3.lerp_mode).apply(animationVecCache, f2, scaleFrame, i, j, scale);
				AnimationChannel.Targets.SCALE.apply(p_232330_, animationVecCache);
			}));
		}
	}

	public static float fma(float a, float b, float c) {
		return a * b + c;
	}

	public static float catmullrom(float delta, float controlPoint1, float controlPoint2, float controlPoint3, float controlPoint4) {
		return 0.5F
			* (
			2.0F * controlPoint2
				+ (controlPoint3 - controlPoint1) * delta
				+ (2.0F * controlPoint1 - 5.0F * controlPoint2 + 4.0F * controlPoint3 - controlPoint4) * delta * delta
				+ (3.0F * controlPoint2 - controlPoint1 - 3.0F * controlPoint3 + controlPoint4) * delta * delta * delta
		);
	}

	private static int binarySearch(int startIndex, int endIndex, IntPredicate p_14052_) {
		int searchSize = endIndex - startIndex;

		while (searchSize > 0) {
			int j = searchSize / 2;
			int k = startIndex + j;
			if (p_14052_.test(k)) {
				searchSize = j;
			} else {
				startIndex = k + 1;
				searchSize -= j + 1;
			}
		}

		return startIndex;
	}

	public static Vector3f posVec(float x, float y, float z) {
		return new Vector3f(x, -y, z);
	}

	public static Vector3f degreeVec(float degX, float degY, float degZ) {
		return new Vector3f(degX * (float) (Math.PI / 180.0), degY * (float) (Math.PI / 180.0), degZ * (float) (Math.PI / 180.0));
	}

	public static Vector3f posVec(Vector3f vector3f) {
		return new Vector3f(vector3f.x, -vector3f.y, vector3f.z);
	}

	public static Vector3f degreeVec(Vector3f vector3f) {
		return new Vector3f(vector3f.x * (float) (Math.PI / 180.0), vector3f.y * (float) (Math.PI / 180.0), vector3f.z * (float) (Math.PI / 180.0));
	}

	private static float getElapsedSeconds(AnimationData animationData, long ms) {
		float seconds = (float) ms / 1000.0F;
		return animationData.isLoop() ? seconds % animationData.getAnimationLength() : seconds;
	}
}
