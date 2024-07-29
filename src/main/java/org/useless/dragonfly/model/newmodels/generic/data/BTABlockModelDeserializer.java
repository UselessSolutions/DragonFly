package org.useless.dragonfly.model.newmodels.generic.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Side;
import org.useless.dragonfly.model.newmodels.generic.DisplayPosition;
import org.useless.dragonfly.model.newmodels.generic.StaticModel;
import org.useless.dragonfly.model.newmodels.generic.StaticModelRegistry;
import org.useless.dragonfly.model.newmodels.generic.components.ModelComponent;
import org.useless.dragonfly.model.newmodels.generic.components.block.BlockComponent;
import org.useless.dragonfly.model.newmodels.generic.components.sprite.SpriteComponent;
import org.useless.dragonfly.model.newmodels.generic.data.components.block.BlockComponentDeserializer;
import org.useless.dragonfly.model.newmodels.generic.data.components.sprite.SpriteComponentDeserializer;
import org.useless.dragonfly.utilities.SideUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BTABlockModelDeserializer implements ModelDeserializer<StaticModel> {
    private final Map<String, Pair<Type, ModelDeserializer<? extends ModelComponent>>> adapters = new HashMap<>();
    public BTABlockModelDeserializer(){
        addAdapter("BTA_Block_Cube_Component", BlockComponent.class, new BlockComponentDeserializer());
        addAdapter("BTA_Sprite_Component", SpriteComponent.class, new SpriteComponentDeserializer());
    }
    private <T extends ModelComponent> void addAdapter(String componentID, Class<T> classOfT, ModelDeserializer<T> deserializer){
        adapters.put(componentID.toLowerCase(Locale.ROOT), Pair.of(classOfT, deserializer));
    }
    @Override
    public StaticModel deserialize(JsonElement json, int version) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String parent = null;
        if (object.has("parent")) {
            parent = object.get("parent").getAsString();
        }

        Map<String, String> textureSymbolMap = new HashMap<>();
        if (object.has("textures")){
            Map<String, JsonElement> map = object.getAsJsonObject("textures").asMap();
            for (Map.Entry<String, JsonElement> e : map.entrySet()){
                textureSymbolMap.put(e.getKey(), e.getValue().getAsString());
            }
        }

        Map<Side, Boolean> particleColorMap = new HashMap<>();
        if (object.has("particle_colors")){
            Map<String, JsonElement> map = object.getAsJsonObject("particle_colors").asMap();
            for (Map.Entry<String, JsonElement> e : map.entrySet()){
                Side side = SideUtils.getSideFromName(e.getKey());
                particleColorMap.put(side, e.getValue().getAsBoolean());
            }
        }

        List<ModelComponent> components = new ArrayList<>();
        if (object.has("elements")){
            JsonArray eArr = object.getAsJsonArray("elements");
            for (JsonElement e : eArr){
                JsonObject elementObject = e.getAsJsonObject();
                String type = elementObject.get("type").getAsString().toLowerCase(Locale.ROOT);
                JsonObject data = elementObject.getAsJsonObject("data");
                if (adapters.containsKey(type)){
                    components.add(adapters.get(type).getRight().deserialize(data, elementObject.get("version").getAsInt()));
                } else {
                    throw new JsonParseException("No registered adapter for type '" + type + "'!");
                }
            }
        }

        Map<String, DisplayPosition> displayPositionMap = new HashMap<>();
        if (object.has("display")){
            Map<String, JsonElement> elementMap = object.getAsJsonObject("display").asMap();
            for (Map.Entry<String, JsonElement> e : elementMap.entrySet()){
                JsonObject o = e.getValue().getAsJsonObject();
                double[] rotation;
                if (o.has("rotation")){
                    rotation = ModelDeserializer.decodeDoubleArr(o.getAsJsonArray("rotation"));
                } else {
                    rotation = new double[3];
                }


                double[] translation;
                if (o.has("translation")){
                    translation = ModelDeserializer.decodeDoubleArr(o.getAsJsonArray("translation"));
                } else {
                    translation = new double[3];
                }

                double[] scale;
                if (o.has("scale")){
                    scale = ModelDeserializer.decodeDoubleArr(o.getAsJsonArray("scale"));
                } else {
                    scale = new double[]{ 1, 1, 1 };
                }

                displayPositionMap.put(e.getKey(), new DisplayPosition(rotation, translation, scale));
            }
        }

        StaticModel parentModel = null;
        if (parent != null){
            parentModel = StaticModelRegistry.getInstance().retrieveModel(parent, null);
            if (parentModel == null) throw new JsonParseException("Could not retrieve parent model from string '" + parent + "'.");
        }

        ModelComponent[] modelComponentArray = new ModelComponent[components.size()];
        for (int i = 0; i < modelComponentArray.length; i++) {
            modelComponentArray[i] = components.get(i);
        }

        Boolean useAO = null;
        if (object.has("ambientocclusion")){
            useAO = object.get("ambientocclusion").getAsBoolean();
        }

        StaticModel outputModel = new StaticModel(parentModel, useAO, modelComponentArray);

        for (Map.Entry<String, String> entry : textureSymbolMap.entrySet()){
            assert !entry.getValue().isEmpty();
            outputModel.setTexture(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<Side, Boolean> e : particleColorMap.entrySet()){
            outputModel.setParticleColor(e.getKey(), e.getValue());
        }

        for (Map.Entry<String, DisplayPosition> e : displayPositionMap.entrySet()){
            outputModel.setDisplayPosition(e.getKey(), e.getValue());
        }

        return outputModel;
    }
}
