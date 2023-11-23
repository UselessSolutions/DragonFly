package useless.dragonfly.helper;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.entity.animation.Animation;
import useless.dragonfly.model.entity.animation.AnimationData;
import useless.dragonfly.model.entity.animation.BoneData;
import useless.dragonfly.model.entity.animation.PostData;
import useless.dragonfly.model.entity.processor.BenchEntityBones;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.IntPredicate;

public class AnimationHelper {
	public static final Map<String, Animation> registeredAnimations = new HashMap<>();

	public static Animation getOrCreateEntityAnimation(String modID, String modelSource) {
		if (!registeredAnimations.containsKey(getAnimationLocation(modID, modelSource))) {
			InputStream inputStream = Animation.class.getResourceAsStream(getAnimationLocation(modID, modelSource));
			JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
			Animation model = DragonFly.GSON.fromJson(reader, Animation.class);
			registeredAnimations.put(getAnimationLocation(modID, modelSource), model);
			return model;
		} else {
			return registeredAnimations.get(getAnimationLocation(modID, modelSource));
		}
	}

	public static String getAnimationLocation(String modID, String modelSource) {
		if (!modelSource.contains(".json")) {
			modelSource += ".json";
		}
		return "/assets/" + modID + "/animation/" + modelSource;
	}

	public static void animate(BenchEntityModel entityModel, AnimationData animationData, long p_232322_, float scale, Vector3f p_253861_) {
		float f = getElapsedSeconds(animationData, p_232322_);

		for (Map.Entry<String, BoneData> entry : animationData.getBones().entrySet()) {
			Optional<BenchEntityBones> optional = entityModel.getAnyDescendantWithName(entry.getKey());
			HashMap<String, PostData> postionMap = entry.getValue().getPostion();
			List<KeyFrame> postionFrame = Lists.newArrayList();

			postionMap.entrySet().stream().sorted(Comparator.comparingDouble((test) -> (Float.parseFloat(test.getKey())))).forEach(key -> {
				postionFrame.add(new KeyFrame(Float.parseFloat(key.getKey()), key.getValue().getPost()));
			});
			optional.ifPresent(p_232330_ -> postionFrame.forEach((keyFrame2) -> {
				int i = Math.max(0, binarySearch(0, postionFrame.size(), p_232315_ -> f <= postionFrame.get(p_232315_).duration) - 1);
				int j = Math.min(postionFrame.size() - 1, i + 1);
				KeyFrame keyframe = postionFrame.get(i);
				KeyFrame keyframe1 = postionFrame.get(j);
				float f1 = f - keyframe.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				Vector3f vector3f = posVec(postionFrame.get(Math.max(0, i - 1)).vector3f());
				Vector3f vector3f1 = posVec(postionFrame.get(i).vector3f());
				Vector3f vector3f2 = posVec(postionFrame.get(j).vector3f());
				Vector3f vector3f3 = posVec(postionFrame.get(Math.min(postionFrame.size() - 1, j + 1)).vector3f());

				p_253861_.set(
					catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale,
					catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale,
					catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale
				);
				p_232330_.setRotationPoint(p_232330_.rotationPointX + p_253861_.x, p_232330_.rotationPointY + p_253861_.y, p_232330_.rotationPointZ + p_253861_.z);

			}));
			HashMap<String, PostData> rotationMap = entry.getValue().getRotation();
			List<KeyFrame> rotationFrame = Lists.newArrayList();

			rotationMap.entrySet().stream().sorted(Comparator.comparingDouble((test) -> (Float.parseFloat(test.getKey())))).forEach(key -> {
				rotationFrame.add(new KeyFrame(Float.parseFloat(key.getKey()), key.getValue().getPost()));
			});
			optional.ifPresent(p_232330_ -> rotationFrame.forEach((keyFrame3) -> {
				int i = Math.max(0, binarySearch(0, rotationFrame.size(), p_232315_ -> f <= rotationFrame.get(p_232315_).duration) - 1);
				int j = Math.min(rotationFrame.size() - 1, i + 1);
				KeyFrame keyframe = rotationFrame.get(i);
				KeyFrame keyframe1 = rotationFrame.get(j);
				float f1 = f - keyframe.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				Vector3f vector3f = degreeVec(rotationFrame.get(Math.max(0, i - 1)).vector3f());
				Vector3f vector3f1 = degreeVec(rotationFrame.get(i).vector3f());
				Vector3f vector3f2 = degreeVec(rotationFrame.get(j).vector3f());
				Vector3f vector3f3 = degreeVec(rotationFrame.get(Math.min(rotationFrame.size() - 1, j + 1)).vector3f());

				p_253861_.set(
					catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale,
					catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale,
					catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale
				);
				p_232330_.setRotationAngle(p_232330_.rotateAngleX + p_253861_.x, p_232330_.rotateAngleY + p_253861_.y, p_232330_.rotateAngleZ + p_253861_.z);
			}));
		}
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

	private static int binarySearch(int p_14050_, int p_14051_, IntPredicate p_14052_) {
		int i = p_14051_ - p_14050_;

		while (i > 0) {
			int j = i / 2;
			int k = p_14050_ + j;
			if (p_14052_.test(k)) {
				i = j;
			} else {
				p_14050_ = k + 1;
				i -= j + 1;
			}
		}

		return p_14050_;
	}

	public static Vector3f posVec(float p_253691_, float p_254046_, float p_254461_) {
		return new Vector3f(p_253691_, -p_254046_, p_254461_);
	}

	public static Vector3f degreeVec(float p_254402_, float p_253917_, float p_254397_) {
		return new Vector3f(p_254402_ * (float) (Math.PI / 180.0), p_253917_ * (float) (Math.PI / 180.0), p_254397_ * (float) (Math.PI / 180.0));
	}

	public static Vector3f posVec(Vector3f vector3f) {
		return new Vector3f(vector3f.x, -vector3f.y, vector3f.z);
	}

	public static Vector3f degreeVec(Vector3f vector3f) {
		return new Vector3f(vector3f.x * (float) (Math.PI / 180.0), vector3f.y * (float) (Math.PI / 180.0), vector3f.z * (float) (Math.PI / 180.0));
	}

	private static float getElapsedSeconds(AnimationData p_232317_, long p_232318_) {
		float f = (float) p_232318_ / 1000.0F;
		return p_232317_.isLoop() ? f % p_232317_.getAnimationLength() : f;
	}
}
