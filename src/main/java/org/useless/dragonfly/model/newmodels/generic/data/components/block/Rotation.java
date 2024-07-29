package org.useless.dragonfly.model.newmodels.generic.data.components.block;

import com.google.gson.JsonObject;
import org.useless.dragonfly.model.newmodels.generic.data.ModelDeserializer;

public class Rotation{
    double[] origin = new double[]{8, 8, 8};
    double[] angles;
    boolean rescale_x = false;
    boolean rescale_y = false;
    boolean rescale_z = false;
    public Rotation(JsonObject object){
        if (object.has("origin")){
            origin = ModelDeserializer.decodeDoubleArr(object.getAsJsonArray("origin"));
        }
        angles = ModelDeserializer.decodeDoubleArr(object.getAsJsonArray("angles"));

        if (object.has("rescale_x")){
            rescale_x = object.get("rescale_x").getAsBoolean();
        }
        if (object.has("rescale_y")){
            rescale_y = object.get("rescale_y").getAsBoolean();
        }
        if (object.has("rescale_z")){
            rescale_z = object.get("rescale_z").getAsBoolean();
        }
    }
}
