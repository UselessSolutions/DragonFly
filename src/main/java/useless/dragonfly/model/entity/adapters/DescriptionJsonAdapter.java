package useless.dragonfly.model.entity.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.model.entity.processor.Description;

import java.lang.reflect.Type;

public class DescriptionJsonAdapter implements JsonDeserializer<Description>, JsonSerializer<Description> {
	@Override
	public Description deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		int height = 0;
		int width = 0;
		if (obj.has("texture_height")) height = obj.get("texture_height").getAsInt();
		if (obj.has("texture_width")) width = obj.get("texture_width").getAsInt();
		return new Description(height, width);
	}

	@Override
	public JsonElement serialize(Description src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
