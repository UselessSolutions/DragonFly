package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.entity.animation.Animation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AnimationHelper {
	public static final Map<String, Animation> registeredAnimations = new HashMap<>();

	public static Animation getOrCreateEntityAnimation(String modID, String modelSource) {
		if (!registeredAnimations.containsKey(ModelHelper.getModelLocation(modID, modelSource))) {
			InputStream inputStream = Animation.class.getResourceAsStream(ModelHelper.getModelLocation(modID, modelSource));
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
}
