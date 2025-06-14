package org.useless.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.vector.Vector3f;
import org.useless.DragonFly;
import org.useless.dragonfly.animation.Animation;
import org.useless.dragonfly.animation.AnimationData;
import org.useless.dragonfly.animation.KeyFrameData;
import org.useless.dragonfly.data.entity.mojang.Bone;
import org.useless.dragonfly.data.entity.mojang.MojangAnimationDeserializer;
import org.useless.dragonfly.models.entity.BoneTransform;
import org.useless.dragonfly.models.entity.mojang.StaticEntityModelMojang;

import javax.swing.text.Utilities;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class AnimationHelper {
	public static final Map<String, Animation> registeredAnimations = new HashMap<>();

	private static final double DEGREES_TO_RADIANS = 0.017453292519943295;


	protected final static Gson gson = builder().create();

	protected static GsonBuilder builder() {
		final GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.registerTypeAdapter(Animation.class, new MojangAnimationDeserializer());
		return builder;
	}

	public static InputStream getResourceAsStream(String path) {
		try {
			return Objects.requireNonNull(DataLoader.class.getResourceAsStream(path));
		} catch (Exception ignored) {
		}
		try {
			return Objects.requireNonNull(Utilities.class.getResourceAsStream(path));
		} catch (Exception ignored) {
		}
		try {
			return Objects.requireNonNull(DragonFly.class.getResourceAsStream(path));
		} catch (Exception ignored) {
		}
		try {
			return Objects.requireNonNull(FabricLoader.getInstance().getClass().getResourceAsStream(path));
		} catch (Exception ignored) {
		}
		try {
			return Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		} catch (Exception ignored) {
		}
		throw new RuntimeException("Resource at '" + path + "' returned null! Does this file exist?");
	}

	public static Animation getOrCreateEntityAnimation(String modID, String animationSource) {
		String animationKey = getAnimationLocation(modID, animationSource);
		if (registeredAnimations.containsKey(animationKey)) {
			return registeredAnimations.get(animationKey);
		}

		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(getResourceAsStream(animationKey))));
		Animation animation = gson.fromJson(reader, Animation.class);
		registeredAnimations.put(animationKey, animation);
		return animation;
	}

	public static String getAnimationLocation(String modID, String animationSource) {
		if (!animationSource.endsWith(".json")) {
			animationSource += ".json";
		}
		return "/assets/" + modID + "/animation/" + animationSource;
	}

	public static void animate(StaticEntityModelMojang entityModel, Animation animationData, long time, float scale, Vector3f p_253861_) {
		float seconds = getElapsedSeconds(animationData, time);

		for (AnimationData animationData1 : animationData.getAnimations()) {
			for (@NotNull Bone bone : entityModel.bones) {
				if (animationData1.getBone().equals(bone.name) && animationData1.getKeyFrames() != null) {
					List<KeyFrameData> positionFrame = animationData1.getKeyFrames().stream().sorted(Comparator.comparing(KeyFrameData::getTimestamp)).collect(Collectors.toList());

					int i = Math.max(0, binarySearch(0, positionFrame.size(), p_232315_ -> seconds <= positionFrame.get(p_232315_).getTimestamp()) - 1);
					int j = Math.min(positionFrame.size() - 1, i + 1);
					KeyFrameData keyframe = positionFrame.get(i);
					KeyFrameData keyframe1 = positionFrame.get(j);
					float f1 = seconds - keyframe.getTimestamp();
					float f2;
					if (j != i) {
						f2 = MathHelper.clamp(f1 / (keyframe1.getTimestamp() - keyframe.getTimestamp()), 0.0F, 1.0F);
					} else {
						f2 = 0.0F;
					}

					switch (animationData1.getTarget()) {
						case "position":
							if (keyframe1.getInterpolation().equals("catmullrom")) {
								Vector3f vector3f = posVec(convert(positionFrame.get(Math.max(0, i - 1)).getTarget()));
								Vector3f vector3f1 = posVec(convert(positionFrame.get(i).getTarget()));
								Vector3f vector3f2 = posVec(convert(positionFrame.get(j).getTarget()));
								Vector3f vector3f3 = posVec(convert(positionFrame.get(Math.min(positionFrame.size() - 1, j + 1)).getTarget()));

								p_253861_.set(
									catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale,
									catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale,
									catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale
								);
								BoneTransform boneTransform = entityModel.getTransform(animationData1.getBone());
								boneTransform.posX += p_253861_.x;
								boneTransform.posY += p_253861_.y;
								boneTransform.posZ += p_253861_.z;
							} else {
								Vector3f vector3f = posVec(convert(positionFrame.get(i).getTarget()));
								Vector3f vector3f1 = posVec(convert(positionFrame.get(i).getTarget()));
								p_253861_.set(
									fma(vector3f1.x - vector3f.x, f2, vector3f.x) * scale,
									fma(vector3f1.y - vector3f.y, f2, vector3f.y) * scale,
									fma(vector3f1.z - vector3f.z, f2, vector3f.z) * scale
								);
								BoneTransform boneTransform = entityModel.getTransform(animationData1.getBone());
								boneTransform.posX += p_253861_.x;
								boneTransform.posY += p_253861_.y;
								boneTransform.posZ += p_253861_.z;
							}
							break;
						case "rotation":
							if (keyframe1.getInterpolation().equals("catmullrom")) {
								Vector3f vector3f = degreeVec(convert(positionFrame.get(Math.max(0, i - 1)).getTarget()));
								Vector3f vector3f1 = degreeVec(convert(positionFrame.get(i).getTarget()));
								Vector3f vector3f2 = degreeVec(convert(positionFrame.get(j).getTarget()));
								Vector3f vector3f3 = degreeVec(convert(positionFrame.get(Math.min(positionFrame.size() - 1, j + 1)).getTarget()));

								p_253861_.set(
									catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale,
									catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale,
									catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale
								);
								BoneTransform boneTransform = entityModel.getTransform(animationData1.getBone());
								boneTransform.rotX += p_253861_.x;
								boneTransform.rotY += p_253861_.y;
								boneTransform.rotZ += p_253861_.z;
							} else {
								Vector3f vector3f = degreeVec(convert(positionFrame.get(i).getTarget()));
								Vector3f vector3f1 = degreeVec(convert(positionFrame.get(i).getTarget()));
								p_253861_.set(
									fma(vector3f1.x - vector3f.x, f2, vector3f.x) * scale,
									fma(vector3f1.y - vector3f.y, f2, vector3f.y) * scale,
									fma(vector3f1.z - vector3f.z, f2, vector3f.z) * scale
								);
								BoneTransform boneTransform = entityModel.getTransform(animationData1.getBone());
								boneTransform.rotX += p_253861_.x;
								boneTransform.rotY += p_253861_.y;
								boneTransform.rotZ += p_253861_.z;
							}
							break;
						case "scale":
							if (keyframe1.getInterpolation().equals("catmullrom")) {
								Vector3f vector3f = scaleVec(convert(positionFrame.get(Math.max(0, i - 1)).getTarget()));
								Vector3f vector3f1 = scaleVec(convert(positionFrame.get(i).getTarget()));
								Vector3f vector3f2 = scaleVec(convert(positionFrame.get(j).getTarget()));
								Vector3f vector3f3 = scaleVec(convert(positionFrame.get(Math.min(positionFrame.size() - 1, j + 1)).getTarget()));

								p_253861_.set(
									catmullrom(f2, vector3f.x, vector3f1.x, vector3f2.x, vector3f3.x) * scale,
									catmullrom(f2, vector3f.y, vector3f1.y, vector3f2.y, vector3f3.y) * scale,
									catmullrom(f2, vector3f.z, vector3f1.z, vector3f2.z, vector3f3.z) * scale
								);
								BoneTransform boneTransform = entityModel.getTransform(animationData1.getBone());
								boneTransform.scaleX += p_253861_.x;
								boneTransform.scaleY += p_253861_.y;
								boneTransform.scaleZ += p_253861_.z;
							} else {
								Vector3f vector3f = scaleVec(convert(positionFrame.get(i).getTarget()));
								Vector3f vector3f1 = scaleVec(convert(positionFrame.get(i).getTarget()));
								p_253861_.set(
									fma(vector3f1.x - vector3f.x, f2, vector3f.x) * scale,
									fma(vector3f1.y - vector3f.y, f2, vector3f.y) * scale,
									fma(vector3f1.z - vector3f.z, f2, vector3f.z) * scale
								);
								BoneTransform boneTransform = entityModel.getTransform(animationData1.getBone());
								boneTransform.scaleX += p_253861_.x;
								boneTransform.scaleY += p_253861_.y;
								boneTransform.scaleZ += p_253861_.z;
							}
							break;
					}
				}
			}
		}
	}

	public static float fma(float a, float b, float c) {
		return a * b + c;
	}

	public static float catmullrom(float p_216245_, float p_216246_, float p_216247_, float p_216248_, float p_216249_) {
		return 0.5F * (2.0F * p_216247_ + (p_216248_ - p_216246_) * p_216245_ + (2.0F * p_216246_ - 5.0F * p_216247_ + 4.0F * p_216248_ - p_216249_) * p_216245_ * p_216245_ + (3.0F * p_216247_ - p_216246_ - 3.0F * p_216248_ + p_216249_) * p_216245_ * p_216245_ * p_216245_);
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

	public static Vector3f convert(List<Float> floats) {
		return new Vector3f(floats.get(0), floats.get(1), floats.get(2));
	}

	public static Vector3f posVec(Vector3f vector3f) {
		return new Vector3f(vector3f.x, vector3f.y, vector3f.z);
	}

	public static Vector3f posVec(float p_253691_, float p_254046_, float p_254461_) {
		return new Vector3f(p_253691_, p_254046_, p_254461_);
	}

	public static Vector3f degreeVec(float p_254402_, float p_253917_, float p_254397_) {
		return new Vector3f(p_254402_ * ((float) Math.PI / 180F), p_253917_ * ((float) Math.PI / 180F), p_254397_ * ((float) Math.PI / 180F));
	}

	public static Vector3f degreeVec(Vector3f vector3f) {
		return new Vector3f(vector3f.x * ((float) Math.PI / 180F), vector3f.y * ((float) Math.PI / 180F), vector3f.z * ((float) Math.PI / 180F));
	}

	public static Vector3f scaleVec(double p_253806_, double p_253647_, double p_254396_) {
		return new Vector3f((float) (p_253806_ - (double) 1.0F), (float) (p_253647_ - (double) 1.0F), (float) (p_254396_ - (double) 1.0F));
	}

	public static Vector3f scaleVec(Vector3f vector3f) {
		return new Vector3f((float) (vector3f.x - (double) 1.0F), (float) (vector3f.y - (double) 1.0F), (float) (vector3f.z - (double) 1.0F));
	}

	private static float getElapsedSeconds(Animation animationData, long ms) {
		float seconds = (float) ms / 1000.0F;
		return animationData.isLoop() ? seconds % animationData.getAnimationLength() : seconds;
	}
}
