package org.useless.dragonfly.model.newmodels.generic.data.components.sprite;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.useless.dragonfly.model.newmodels.generic.components.sprite.SpriteComponent;
import org.useless.dragonfly.model.newmodels.generic.data.ModelDeserializer;

public class SpriteComponentDeserializer implements ModelDeserializer<SpriteComponent> {
    @Override
    public SpriteComponent deserialize(JsonElement element, int version) throws JsonParseException {
        return null;
    }
}
