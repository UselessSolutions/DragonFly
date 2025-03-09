package org.useless.dragonfly.data.block.mojang.state.adapter;

import com.google.gson.*;
import org.apache.commons.lang3.NotImplementedException;
import org.useless.dragonfly.data.block.mojang.state.AppliedData;

import java.lang.reflect.Type;

public class AppliedDataJsonAdapter implements JsonDeserializer<AppliedData>, JsonSerializer<AppliedData> {
	@Override
	public AppliedData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		AppliedData data = new AppliedData();
		if (obj.has("model")) data.model = obj.get("model").getAsString();
		if (obj.has("x")) data.x = obj.get("x").getAsInt();
		if (obj.has("y")) data.y = obj.get("y").getAsInt();
		if (obj.has("uvlock")) data.uvlock = obj.get("uvlock").getAsBoolean();
		if (obj.has("weight")) data.weight = obj.get("weight").getAsInt();
		return data;
	}

	@Override
	public JsonElement serialize(AppliedData src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
