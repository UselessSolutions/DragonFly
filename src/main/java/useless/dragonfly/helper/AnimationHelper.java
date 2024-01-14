package useless.dragonfly.helper;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.MathHelper;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.entity.animation.Animation;
import useless.dragonfly.model.entity.animation.AnimationData;
import useless.dragonfly.model.entity.animation.BoneData;
import useless.dragonfly.model.entity.animation.PostData;
import useless.dragonfly.model.entity.processor.BenchEntityBones;
import useless.dragonfly.utilities.Utilities;
import useless.dragonfly.utilities.vector.Vector3f;

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

	public static void animate(BenchEntityModel entityModel, AnimationData animationData, long time, float scale, Vector3f p_253861_) {
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

				if (keyFrame2.lerp_mode.equals("catmullrom")) {
					Vector3f vector3f = posVec(positionFrame.get(Math.max(0, i - 1)).vector3f());
					Vector3f vector3f1 = posVec(positionFrame.get(i).vector3f());
					Vector3f vector3f2 = posVec(positionFrame.get(j).vector3f());
					Vector3f vector3f3 = posVec(positionFrame.get(Math.min(positionFrame.size() - 1, j + 1)).vector3f());

					p_253861_.set(
						catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale * 0.5F,
						catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale * 0.5F,
						catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale * 0.5F
					);
					p_232330_.setRotationPoint(p_232330_.rotationPointX + p_253861_.x, p_232330_.rotationPointY + p_253861_.y, p_232330_.rotationPointZ + p_253861_.z);
				} else {
					Vector3f vector3f = posVec(positionFrame.get(i).vector3f());
					Vector3f vector3f1 = posVec(positionFrame.get(j).vector3f());
					p_253861_.set(
						fma(vector3f1.x - vector3f.x, f2, vector3f.x) * scale * 0.5F,
						fma(vector3f1.y - vector3f.y, f2, vector3f.y) * scale * 0.5F,
						fma(vector3f1.z - vector3f.z, f2, vector3f.z) * scale * 0.5F
					);
					p_232330_.setRotationPoint(p_232330_.rotationPointX + p_253861_.x, p_232330_.rotationPointY + p_253861_.y, p_232330_.rotationPointZ + p_253861_.z);

				}
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

				if (keyFrame3.lerp_mode.equals("catmullrom")) {
					Vector3f vector3f = degreeVec(rotationFrame.get(Math.max(0, i - 1)).vector3f());
					Vector3f vector3f1 = degreeVec(rotationFrame.get(i).vector3f());
					Vector3f vector3f2 = degreeVec(rotationFrame.get(j).vector3f());
					Vector3f vector3f3 = degreeVec(rotationFrame.get(Math.min(rotationFrame.size() - 1, j + 1)).vector3f());

					p_253861_.set(
						catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale * 0.5F,
						catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale * 0.5F,
						catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale * 0.5F
					);
					p_232330_.setRotationAngle(p_232330_.rotateAngleX + p_253861_.x, p_232330_.rotateAngleY + p_253861_.y, p_232330_.rotateAngleZ + p_253861_.z);
				} else {
					Vector3f vector3f = degreeVec(rotationFrame.get(i).vector3f());
					Vector3f vector3f1 = degreeVec(rotationFrame.get(j).vector3f());
					p_253861_.set(
						fma(vector3f1.x - vector3f.x, f2, vector3f.x) * scale * 0.5F,
						fma(vector3f1.y - vector3f.y, f2, vector3f.y) * scale * 0.5F,
						fma(vector3f1.z - vector3f.z, f2, vector3f.z) * scale * 0.5F
					);
					p_232330_.setRotationAngle(p_232330_.rotateAngleX + p_253861_.x, p_232330_.rotateAngleY + p_253861_.y, p_232330_.rotateAngleZ + p_253861_.z);

				}
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

				if (keyFrame3.lerp_mode.equals("catmullrom")) {
					Vector3f vector3f = degreeVec(scaleFrame.get(Math.max(0, i - 1)).vector3f());
					Vector3f vector3f1 = degreeVec(scaleFrame.get(i).vector3f());
					Vector3f vector3f2 = degreeVec(scaleFrame.get(j).vector3f());
					Vector3f vector3f3 = degreeVec(scaleFrame.get(Math.min(scaleFrame.size() - 1, j + 1)).vector3f());

					p_253861_.set(
						catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale * 0.5F,
						catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale * 0.5F,
						catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale * 0.5F
					);
					p_232330_.setScale(p_232330_.scaleX + p_253861_.x, p_232330_.scaleY + p_253861_.y, p_232330_.scaleZ + p_253861_.z);
				} else {
					Vector3f vector3f = degreeVec(scaleFrame.get(i).vector3f());
					Vector3f vector3f1 = degreeVec(scaleFrame.get(j).vector3f());
					p_253861_.set(
						fma(vector3f1.x - vector3f.x, f2, vector3f.x) * scale * 0.5F,
						fma(vector3f1.y - vector3f.y, f2, vector3f.y) * scale * 0.5F,
						fma(vector3f1.z - vector3f.z, f2, vector3f.z) * scale * 0.5F
					);
					p_232330_.setScale(p_232330_.scaleX + p_253861_.x, p_232330_.scaleY + p_253861_.y, p_232330_.scaleZ + p_253861_.z);

				}
			}));
		}
	}

	public static float fma(float a, float b, float c) {
		return a * b + c;
	}

	private static float catmullrom(float p_216245_, float p_216246_, float p_216247_, float p_216248_, float p_216249_) {
		return 0.5F
			* (
			2.0F * p_216247_
				+ (p_216248_ - p_216246_) * p_216245_
				+ (2.0F * p_216246_ - 5.0F * p_216247_ + 4.0F * p_216248_ - p_216249_) * p_216245_ * p_216245_
				+ (3.0F * p_216247_ - p_216246_ - 3.0F * p_216248_ + p_216249_) * p_216245_ * p_216245_ * p_216245_
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
