package useless.dragonfly.model.blockstates.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.blockstates.data.Condition;
import useless.dragonfly.model.blockstates.data.ModelPart;
import useless.dragonfly.model.blockstates.data.VariantData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ModelPartJsonAdapter implements JsonDeserializer<ModelPart>, JsonSerializer<ModelPart> {
	@Override
	public ModelPart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		ModelPart data = new ModelPart();
		if (obj.has("apply")){
			double accumulated = 0;
			if (obj.getAsJsonObject("apply").isJsonArray()){
				JsonArray arr = obj.getAsJsonArray("apply");
				data.apply = new VariantData[arr.size()];
				for (int i = 0; i < arr.size(); i++) {
					VariantData var = DragonFly.GSON.fromJson(arr.get(i), VariantData.class);
					accumulated += var.weight;
					var.weightAccum = accumulated;
					data.apply[i] = var;
				}
			} else {
				data.apply = new VariantData[]{DragonFly.GSON.fromJson(obj.get("apply"), VariantData.class)};
			}
			data.weightAccum = accumulated;

		}
		if (obj.has("when")) {
			JsonObject when = obj.getAsJsonObject("when");
			Map<String, String> conditions = new HashMap<>();
			String id = "and";
			if (when.has("AND")){
				// is and
				JsonArray arr = when.getAsJsonArray("AND");
				for (JsonElement e : arr){
					for (Map.Entry<String, JsonElement> f : e.getAsJsonObject().asMap().entrySet()){
						conditions.put(f.getKey(), f.getValue().getAsString());
					}
				}
			} else if (when.has("OR")) {
				// is or
				id = "or";
				JsonArray arr = when.getAsJsonArray("OR");
				for (JsonElement e : arr){
					for (Map.Entry<String, JsonElement> f : e.getAsJsonObject().asMap().entrySet()){
						conditions.put(f.getKey(), f.getValue().getAsString());
					}
				}
			} else {
				// is single
				JsonObject ob = when.getAsJsonObject();
				for (Map.Entry<String, JsonElement> e : ob.asMap().entrySet()){
					conditions.put(e.getKey(), e.getValue().getAsString());
				}
			}
			data.when = new Condition(id, conditions);
		}
		return data;
	}

	@Override
	public JsonElement serialize(ModelPart src, Type typeOfSrc, JsonSerializationContext context) {
		throw new NotImplementedException();
	}
}
