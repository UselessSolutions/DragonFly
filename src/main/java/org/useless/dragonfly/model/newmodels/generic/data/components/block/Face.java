package org.useless.dragonfly.model.newmodels.generic.data.components.block;

import com.google.gson.JsonObject;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.model.newmodels.generic.data.ModelDeserializer;
import org.useless.dragonfly.utilities.SideUtils;

public class Face{
    double @Nullable [] uv = null;
    int[] uv_scale = new int[]{16, 16};
    String texture;
    Side cullface;
    int textureRotation = 0;
    boolean useColor = false;
    boolean flipNormals = false;
    boolean doubledSided = false;

    public Face(JsonObject sourceObject){
        if (sourceObject.has("uv")){
            uv = ModelDeserializer.decodeDoubleArr(sourceObject.getAsJsonArray("uv"));
        }
        if (sourceObject.has("uv_scale")){
            uv_scale = ModelDeserializer.decodeIntArr(sourceObject.getAsJsonArray("uv_scale"));
        }
        texture = sourceObject.get("texture").getAsString();

        if (sourceObject.has("cullface")){
            cullface = SideUtils.getSideFromName(sourceObject.get("cullface").getAsString());
        } else {
            cullface = Side.NONE;
        }

        if (sourceObject.has("rotation")){
            textureRotation = sourceObject.get("rotation").getAsInt();
        }

        if (sourceObject.has("use_color")){
            useColor = sourceObject.get("use_color").getAsBoolean();
        }

        if (sourceObject.has("flip_normal")){
            flipNormals = sourceObject.get("flip_normal").getAsBoolean();
        }

        if (sourceObject.has("doubled_sided")){
            doubledSided = sourceObject.get("doubled_sided").getAsBoolean();
        }
    }
}
