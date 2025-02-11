package org.useless.dragonfly.data.block.mojang;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.core.util.helper.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.util.GsonHelper;

import java.lang.reflect.Type;
import java.util.Locale;

public class Face {
    protected static final int DEFAULT_ROTATION = 0;
    protected static final int DEFAULT_TINT_INDEX = -1;

    public final float u1;
    public final float u2;
    public final float v1;
    public final float v2;

    /**
     * Specifies the texture in form of the texture variable prepended with a <code>#</code>.
     */
    public final String texture;
    /**
     * Specifies whether a face does not need to be rendered when there is a block touching it in the specified position.
     * The position can any {@link Direction}.
     * It also determines the side of the block to use the light level from for lighting the face, and if unset, defaults to the side.
     */
    public final @Nullable Direction cullFace;

    /**
     * Rotates the texture clockwise by the specified number of degrees. Can be 0, 90, 180, or 270. Defaults to 0.
     * Rotation does not affect which part of the texture is used.
     * Instead, it amounts to permutation of the selected texture vertexes (selected implicitly, or explicitly though uv).
     */
    public final int rotation;

    /**
     * Determines whether to tint the texture using a hardcoded tint index.
     * The default value, -1, indicates not to use the tint.
     * Any other number is provided to BlockColors to get the tint value corresponding to that index.
     * However, most blocks do not have a tint value defined (in which case white is used).
     */
    public final int tintIndex;

    protected Face(float u1, float u2, float v1, float v2, @NotNull String texture, @Nullable Direction cullFace, int rotation, int tintIndex) {
        this.u1 = u1;
        this.u2 = u2;
        this.v1 = v1;
        this.v2 = v2;
        this.texture = texture;
        this.cullFace = cullFace;
        this.rotation = rotation;
        this.tintIndex = tintIndex;
    }

    @Override
    public String toString() {
        return "Face{" +
            "u1=" + u1 +
            ", u2=" + u2 +
            ", v1=" + v1 +
            ", v2=" + v2 +
            ", texture='" + texture + '\'' +
            ", cullFace=" + cullFace +
            ", rotation=" + rotation +
            ", tintIndex=" + tintIndex +
            '}';
    }

    @SuppressWarnings("unused")
    public static class Builder {
        protected @Nullable Float u1 = null;
        protected @Nullable Float u2 = null;
        protected @Nullable Float v1 = null;
        protected @Nullable Float v2 = null;

        protected boolean mirrorU = false;
        protected boolean mirrorV = false;

        protected final @NotNull String texture;
        protected @Nullable Direction cullFace = null;
        protected int rotation = DEFAULT_ROTATION;
        protected int tintIndex = DEFAULT_TINT_INDEX;

        public Builder(@NotNull String texture) {
            this.texture = texture;
        }

        public @NotNull Builder setUVs(float u1, float v1, float u2, float v2) {
            this.u1 = u1;
            this.u2 = u2;
            this.v1 = v1;
            this.v2 = v2;
            return this;
        }

        public @NotNull Builder setMirrorU(boolean mirrorU) {
            this.mirrorU = mirrorU;
            return this;
        }

        public @NotNull Builder setMirrorV(boolean mirrorV) {
            this.mirrorV = mirrorV;
            return this;
        }

        public @NotNull Builder setCullFace(@NotNull Direction cullFace) {
            this.cullFace = cullFace;
            return this;
        }

        public @NotNull Builder setRotation(int rotation) {
            this.rotation = rotation;
            return this;
        }

        public @NotNull Builder setTintIndex(int tintIndex) {
            this.tintIndex = tintIndex;
            return this;
        }

        protected @NotNull Face build(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, @NotNull Direction sideFacing) {
            float x1;
            float x2;
            float y1;
            float y2;
            if (u1 == null || u2 == null || v1 == null || v2 == null) {
                switch (sideFacing) {
                    case UP:
                        x1 = fromX;
                        x2 = toX;
                        y1 = fromZ;
                        y2 = toZ;
                        break;
                    case DOWN:
                        x1 = fromX;
                        x2 = toX;
                        y1 = BlockModelMojangData.UNITS_PER_BLOCK - toZ;
                        y2 = BlockModelMojangData.UNITS_PER_BLOCK - fromZ;
                        break;
                    case NORTH:
                        x1 = BlockModelMojangData.UNITS_PER_BLOCK - toX;
                        x2 = BlockModelMojangData.UNITS_PER_BLOCK - fromX;
                        y1 = BlockModelMojangData.UNITS_PER_BLOCK - toY;
                        y2 = BlockModelMojangData.UNITS_PER_BLOCK - fromY;
                        break;
                    case SOUTH:
                        x1 = fromX;
                        x2 = toX;
                        y1 = BlockModelMojangData.UNITS_PER_BLOCK - toY;
                        y2 = BlockModelMojangData.UNITS_PER_BLOCK - fromY;
                        break;
                    case WEST:
                        x1 = fromZ;
                        x2 = toZ;
                        y1 = BlockModelMojangData.UNITS_PER_BLOCK - toY;
                        y2 = BlockModelMojangData.UNITS_PER_BLOCK - fromY;
                        break;
                    case EAST:
                        x1 = BlockModelMojangData.UNITS_PER_BLOCK - toZ;
                        x2 = BlockModelMojangData.UNITS_PER_BLOCK - fromZ;
                        y1 = BlockModelMojangData.UNITS_PER_BLOCK - toY;
                        y2 = BlockModelMojangData.UNITS_PER_BLOCK - fromY;
                        break;
                    default:
                        x1 = 0;
                        x2 = BlockModelMojangData.UNITS_PER_BLOCK;
                        y1 = 0;
                        y2 = BlockModelMojangData.UNITS_PER_BLOCK;
                }
            } else {
                x1 = u1;
                x2 = u2;
                y1 = v1;
                y2 = v2;
            }

            if (mirrorU) {
                float _x = x2;
                x2 = x1;
                x1 = _x;
            }

            if (mirrorV) {
                float _y = y2;
                y2 = y1;
                y1 = _y;
            }

            return new Face(x1, x2, y1, y2, texture, cullFace, rotation, tintIndex);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {

            @Override
            public Builder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject object = json.getAsJsonObject();
                Builder builder = new Builder(object.get("texture").getAsString());
                if (object.has("uv")) {
                    float[] uvs = GsonHelper.getAsFloatArray(object.get("uv"), 4);
                    builder.setUVs(uvs[0], uvs[1], uvs[2], uvs[3]);
                }
                if (object.has("cullface")) builder.setCullFace(Direction.valueOf(object.get("cullface").getAsString().toUpperCase(Locale.ROOT)));
                if (object.has("rotation")) builder.setRotation(object.get("rotation").getAsInt());
                if (object.has("tintindex")) builder.setTintIndex(object.get("tintindex").getAsInt());
                return builder;
            }

            @Override
            public JsonElement serialize(Builder src, Type typeOfSrc, JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
