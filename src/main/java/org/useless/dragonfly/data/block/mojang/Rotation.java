package org.useless.dragonfly.data.block.mojang;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minecraft.core.util.helper.Axis;
import org.jetbrains.annotations.NotNull;
import org.useless.util.GsonHelper;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Objects;

public class Rotation {
    protected static final boolean DEFAULT_RESCALE = false;

    public final float originX;
    public final float originY;
    public final float originZ;

    /**
     * Specifies the direction of rotation
     */
    public final Axis axis;

    /**
     * Specifies the angle of rotation.
     */
    public final float angle;

    /**
     * Specifies whether or not to scale the faces across the whole block. Can be true or false. Defaults to false
     */
    public final boolean rescale;

    protected Rotation(float originX, float originY, float originZ, @NotNull Axis axis, float angle, boolean rescale) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.axis = axis;
        this.angle = angle;
        this.rescale = rescale;
    }

    @Override
    public String toString() {
        return "Rotation{" +
            "originX=" + originX +
            ", originY=" + originY +
            ", originZ=" + originZ +
            ", axis=" + axis +
            ", angle=" + angle +
            ", rescale=" + rescale +
            '}';
    }

    @SuppressWarnings("unused")
    public static class Builder {
        protected final float originX;
        protected final float originY;
        protected final float originZ;
        protected final @NotNull Axis axis;
        protected final float angle;

        public boolean rescale = DEFAULT_RESCALE;

        public Builder(
            float originX, float originY, float originZ,
            @NotNull Axis axis,
            float angle
        ) {
            this.originX = originX;
            this.originY = originY;
            this.originZ = originZ;
            this.axis = Objects.requireNonNull(axis);
            this.angle = angle;
        }

        public @NotNull Builder setRescale(boolean rescale) {
            this.rescale = rescale;
            return this;
        }

        protected @NotNull Rotation build() {
            return new Rotation(originX, originY, originZ, axis, angle, rescale);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {

            @Override
            public Builder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject object = json.getAsJsonObject();
                float[] origin = GsonHelper.getAsFloatArray(object.get("origin"), 3);
                Builder builder = new Builder(origin[0], origin[1], origin[2], Axis.valueOf(object.get("axis").getAsString().toUpperCase(Locale.ROOT)), object.get("angle").getAsFloat());
                if (object.has("rescale")) builder.setRescale(object.get("rescale").getAsBoolean());
                return builder;
            }

            @Override
            public JsonElement serialize(Builder src, Type typeOfSrc, JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
