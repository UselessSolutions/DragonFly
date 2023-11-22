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
import useless.dragonfly.model.entity.processor.BenchEntityBones;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

	public static void animate(BenchEntityModel p_232320_, AnimationData p_232321_, long p_232322_, float scale, Vector3f p_253861_) {
		float f = getElapsedSeconds(p_232321_, p_232322_);

		for (Map.Entry<String, BoneData> entry : p_232321_.getBones().entrySet()) {
			Optional<BenchEntityBones> optional = p_232320_.getAnyDescendantWithName(entry.getKey());
			HashMap<String, List<Float>> postionMap = entry.getValue().getPostion();
			List<KeyFrame> postionFrame = Lists.newArrayList();

			postionMap.forEach((duration, pose) -> {
				postionFrame.add(new KeyFrame(Float.parseFloat(duration), pose));
			});
			optional.ifPresent(p_232330_ -> postionFrame.forEach((keyFrame) -> {
				int i = Math.max(0, binarySearch(0, postionFrame.size(), p_232315_ -> f <= postionFrame.get(p_232315_).duration) - 1);
				int j = Math.min(postionFrame.size() - 1, i + 1);
				KeyFrame keyframe = postionFrame.get(i);
				KeyFrame keyframe1 = postionFrame.get(j);
				float f1 = f - keyFrame.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				KeyFrame vector3f = postionFrame.get(Math.max(0, i - 1));
				KeyFrame vector3f1 = postionFrame.get(i);
				KeyFrame vector3f2 = postionFrame.get(j);
				KeyFrame vector3f3 = postionFrame.get(Math.min(postionFrame.size() - 1, j + 1));
				p_253861_.set(
					catmullrom(f2, vector3f.pose.get(0), vector3f1.pose.get(0), vector3f2.pose.get(0), vector3f3.pose.get(0)) * scale,
					catmullrom(f2, vector3f.pose.get(1), vector3f1.pose.get(1), vector3f2.pose.get(1), vector3f3.pose.get(1)) * scale,
					catmullrom(f2, vector3f.pose.get(2), vector3f1.pose.get(2), vector3f2.pose.get(2), vector3f3.pose.get(2)) * scale
				);
				p_232330_.setRotationPoint(p_232330_.rotationPointX + p_253861_.x, p_232330_.rotationPointY + p_253861_.y, p_232330_.rotationPointZ + p_253861_.z);

			}));
			HashMap<String, List<Float>> rotationMap = entry.getValue().getRotation();
			List<KeyFrame> rotationFrame = Lists.newArrayList();

			rotationMap.forEach((duration, pose) -> {
				rotationFrame.add(new KeyFrame(Float.parseFloat(duration), pose));
			});
			optional.ifPresent(p_232330_ -> rotationFrame.forEach((keyFrame) -> {
				int i = Math.max(0, binarySearch(0, rotationFrame.size(), p_232315_ -> f <= rotationFrame.get(p_232315_).duration) - 1);
				int j = Math.min(rotationFrame.size() - 1, i + 1);
				KeyFrame keyframe = rotationFrame.get(i);
				KeyFrame keyframe1 = rotationFrame.get(j);
				float f1 = f - keyFrame.duration;
				float f2;
				if (j != i) {
					f2 = MathHelper.clamp(f1 / (keyframe1.duration - keyframe.duration), 0.0F, 1.0F);
				} else {
					f2 = 0.0F;
				}

				KeyFrame vector3f = rotationFrame.get(Math.max(0, i - 1));
				KeyFrame vector3f1 = rotationFrame.get(i);
				KeyFrame vector3f2 = rotationFrame.get(j);
				KeyFrame vector3f3 = rotationFrame.get(Math.min(rotationFrame.size() - 1, j + 1));
				p_253861_.set(
					catmullrom(f2, vector3f.pose.get(0), vector3f1.pose.get(0), vector3f2.pose.get(0), vector3f3.pose.get(0)) * scale,
					catmullrom(f2, vector3f.pose.get(1), vector3f1.pose.get(1), vector3f2.pose.get(1), vector3f3.pose.get(1)) * scale,
					catmullrom(f2, vector3f.pose.get(2), vector3f1.pose.get(2), vector3f2.pose.get(2), vector3f3.pose.get(2)) * scale
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

	private static float getElapsedSeconds(AnimationData p_232317_, long p_232318_) {
		float f = (float) p_232318_ / 1000.0F;
		return p_232317_.isLoop() ? f % p_232317_.getAnimationLength() : f;
	}
}
