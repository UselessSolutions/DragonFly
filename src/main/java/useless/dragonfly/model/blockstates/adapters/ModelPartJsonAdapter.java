package useless.dragonfly.model.blockstates.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.model.blockstates.data.ModelPart;

import java.lang.reflect.Type;

public class ModelPartJsonAdapter implements JsonDeserializer<ModelPart>, JsonSerializer<ModelPart> {
	@Override
	public ModelPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		ModelPart data = new ModelPart();
//		if (obj.has("apply")) data.apply = DragonFly.GSON.fromJson(obj.get("apply"), VariantData.class);
//		if (obj.has("when")) data.when = obj.getAsJsonArray("when").;
		return data;
	}

	@Override
	public JsonElement serialize(ModelPart src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
