package org.useless.dragonfly;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.client.render.block.model.BlockModel;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.util.GsonHelper;

import java.lang.reflect.Type;

public final class DisplayPos {
    /**
     * No Transformation, used in cases where a full block is meant to render without a world reference, mostly for legacy code reasons
     */
    public static final String NONE = "none";
    /**
     * Transformation while the item being rendered in the mob's third person right hand
     */
    public static final String THIRD_PERSON_RIGHT_HAND = "thirdperson_righthand";
    /**
     * Transformation while the item being rendered in the mob's third person left hand
     */
    public static final String THIRD_PERSON_LEFT_HAND = "thirdperson_lefthand";
    /**
     * Transformation while the item being rendered in the player's first person right hand
     */
    public static final String FIRST_PERSON_RIGHT_HAND = "firstperson_righthand";
    /**
     * Transformation while the item being rendered in the player's first person left hand
     */
    public static final String FIRST_PERSON_LEFT_HAND = "firstperson_lefthand";
    /**
     * Transformation while the item is being rendered in a gui
     */
    public static final String GUI = "gui";
    /**
     * Transformation while the item is equipped on the head slot (except for actual armor which uses a separate system)
     */
    public static final String HEAD = "head";
    /**
     * Transformation while the item is in entity form on the ground
     */
    public static final String GROUND = "ground";
    /**
     * Traditionally refers to the transformation while in an item frame, may end up getting used for the augment table
     */
    public static final String FIXED = "fixed";
    /**
     * Transformation while in a jar
     */
    public static final String JAR = "jar";


    public static final DisplayPos DEFAULT_DISPLAY_POS = new DisplayPos(
        0, 0, 0,
        0, 0, 0,
        1f, 1f, 1f);

    public final float tx;
    public final float ty;
    public final float tz;

    public final float rx;
    public final float ry;
    public final float rz;

    public final float sx;
    public final float sy;
    public final float sz;

    public DisplayPos(
        float tx, float ty, float tz,
        float rx, float ry, float rz,
        float sx, float sy, float sz)
    {
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;

        this.rx = rx;
        this.ry = ry;
        this.rz = rz;

        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
    }

    @Override
    public String toString() {
        return "DisplayPos{" +
            "tx=" + tx +
            ", ty=" + ty +
            ", tz=" + tz +
            ", rx=" + rx +
            ", ry=" + ry +
            ", rz=" + rz +
            ", sx=" + sx +
            ", sy=" + sy +
            ", sz=" + sz +
            '}';
    }

    public static class Serializer implements JsonSerializer<DisplayPos>, JsonDeserializer<DisplayPos> {

        @Override
        public DisplayPos deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            float[] rotation = GsonHelper.getAsFloatArray(object.get("rotation"), 3);
            float[] translation = GsonHelper.getAsFloatArray(object.get("translation"), 3);
            float[] scale = GsonHelper.getAsFloatArray(object.get("scale"), 3);
            return new DisplayPos(
                translation[0] * BlockModelMojangData.BLOCKS_PER_UNIT, translation[1] * BlockModelMojangData.BLOCKS_PER_UNIT, translation[2] * BlockModelMojangData.BLOCKS_PER_UNIT,
                rotation[0], rotation[1], rotation[2],
                scale[0], scale[1], scale[2]);
        }

        @Override
        public JsonElement serialize(DisplayPos src, Type typeOfSrc, JsonSerializationContext context) {
            throw new UnsupportedOperationException();
        }
    }
}
