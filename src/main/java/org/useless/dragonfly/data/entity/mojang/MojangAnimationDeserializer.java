package org.useless.dragonfly.data.entity.mojang;

import com.google.gson.*;
import org.spongepowered.include.com.google.common.collect.Lists;
import org.useless.dragonfly.animation.Animation;
import org.useless.dragonfly.animation.AnimationData;
import org.useless.dragonfly.animation.KeyFrameData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MojangAnimationDeserializer implements JsonDeserializer<Animation> {

	@Override
	public Animation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		List<AnimationData> animations = new ArrayList<>();
		float length = 0;
		boolean loop = false;
		if (json.isJsonObject()) {
			JsonObject obj = (JsonObject) json;
			if (obj.has("animations")) {
				for (JsonElement entry : obj.getAsJsonArray("animations")) {
					animations.add(makeAnimation((JsonObject) entry));
				}
			}
			if (obj.has("length")) {
				length = obj.getAsJsonPrimitive("length").getAsFloat();
			}
			if (obj.has("loop")) {
				loop = obj.getAsJsonPrimitive("loop").getAsBoolean();
			}
			return new Animation(animations, length, loop);
		}
		return new Animation(animations, 0, true);
	}


	private AnimationData makeAnimation(JsonObject object) {
		String bone = object.has("bone") ? object.getAsJsonPrimitive("bone").getAsString() : "";

		String target = object.has("target") ? object.getAsJsonPrimitive("target").getAsString() : "linear";
		List<KeyFrameData> boneMap = object.has("keyframes") ? makeKeyFrames(object.getAsJsonArray("keyframes")) : new ArrayList<>();

		return new AnimationData(bone, target, boneMap);
	}

	private List<KeyFrameData> makeKeyFrames(JsonArray object) {
		List<KeyFrameData> keyFrames = new ArrayList<>();
		for (JsonElement entry : object.asList()) {
			keyFrames.add(makePost(entry.getAsJsonObject()));
		}
		return keyFrames;
	}


	private KeyFrameData makePost(JsonElement element) {
		List<Float> list = Lists.newArrayList();

		if (element instanceof JsonObject) {
			JsonObject jsonObject = (JsonObject) element;
			list.add(0, jsonObject.getAsJsonArray("target").get(0).getAsFloat());
			list.add(1, jsonObject.getAsJsonArray("target").get(1).getAsFloat());
			list.add(2, jsonObject.getAsJsonArray("target").get(2).getAsFloat());

			float timestamp = jsonObject.has("timestamp") ? jsonObject.getAsJsonPrimitive("timestamp").getAsFloat() : 0;
			String interpolation = jsonObject.has("interpolation") ? jsonObject.getAsJsonPrimitive("interpolation").getAsString() : "linear";


			return new KeyFrameData(list, timestamp, interpolation);
		}
		return null;
	}
}
