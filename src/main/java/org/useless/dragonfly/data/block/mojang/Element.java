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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Element {
    protected static final boolean DEFAULT_SHADE = true;
    protected static final int DEFAULT_LIGHT_EMISSION = 0;

    public final float fromX;
    public final float fromY;
    public final float fromZ;

    public final float toX;
    public final float toY;
    public final float toZ;

	/**
	 * Defines the name of the element.
	 */
	public String name = "";

    /**
     *  Defines the rotation of an element.
     */
    public final @Nullable Rotation rotation;

    /**
     * Defines if shadows are rendered (true - default), not (false).
     */
    public final boolean shade;
    /**
     * Defines the minimum light level that the element can receive. Can be 0-15, defaults to 0.
     */
    public final int lightEmission;
    public final @NotNull Map<@NotNull Direction, @NotNull Face> faces;

    protected Element(
        String name, float fromX, float fromY, float fromZ,
        float toX, float toY, float toZ,
        @Nullable Rotation rotation,
        boolean shade,
        int lightEmission,
        @NotNull Map<@NotNull Direction, @NotNull Face> faces)
    {
		this.name = name;
        this.fromX = fromX;
        this.fromY = fromY;
        this.fromZ = fromZ;
        this.toX = toX;
        this.toY = toY;
        this.toZ = toZ;
        this.rotation = rotation;
        this.shade = shade;
        this.lightEmission = lightEmission;
        this.faces = faces;
    }

    @Override
    public String toString() {
        return "Element{" +
            "fromX=" + fromX +
            ", fromY=" + fromY +
            ", fromZ=" + fromZ +
            ", toX=" + toX +
            ", toY=" + toY +
            ", toZ=" + toZ +
            ", rotation=" + rotation +
            ", shade=" + shade +
            ", lightEmission=" + lightEmission +
            ", faces=" + faces +
            '}';
    }

    @SuppressWarnings("unused")
    public static class Builder {
        protected final float fromX;
        protected final float fromY;
        protected final float fromZ;
        protected final float toX;
        protected final float toY;
        protected final float toZ;

		protected String name;

        protected @Nullable Rotation rotation = null;
        protected boolean shade = DEFAULT_SHADE;
        protected int lightEmission = DEFAULT_LIGHT_EMISSION;
        protected final @NotNull Map<@NotNull Direction, Face. @NotNull Builder> faces = new HashMap<>();

        public Builder(float fromX, float fromY, float fromZ,
                       float toX, float toY, float toZ) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.fromZ = fromZ;
            this.toX = toX;
            this.toY = toY;
            this.toZ = toZ;
        }

		public @NotNull Builder setName(String name) {
			this.name = name;
			return this;
		}

		public @NotNull Builder setRotation(@Nullable Rotation.Builder rotation) {
            this.rotation = rotation == null ? null : rotation.build();
            return this;
        }

        public @NotNull Builder setShade(boolean shade) {
            this.shade = shade;
            return this;
        }

        public @NotNull Builder setLightEmission(int lightEmission) {
            this.lightEmission = lightEmission;
            return this;
        }

        public @NotNull Builder addFace(@NotNull Direction direction, Face. @NotNull Builder face) {
            faces.put(direction, face);
            return this;
        }

        protected @NotNull Element build() {
            Map<Direction, Face> faceMap = new HashMap<>();
            for (Map.Entry<Direction, Face.Builder> e : faces.entrySet()) {
                faceMap.put(e.getKey(), e.getValue().build(fromX, fromY, fromZ, toX, toY, toZ, e.getKey()));
            }
            return new Element(
				name,
                fromX, fromY, fromZ,
                toX, toY, toZ,
                rotation,
                shade,
                lightEmission,
                faceMap);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {

            @Override
            public Builder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject object = json.getAsJsonObject();
                float[] from = GsonHelper.getAsFloatArray(object.get("from"), 3);
                float[] to = GsonHelper.getAsFloatArray(object.get("to"), 3);
                Builder builder = new Builder(from[0], from[1], from[2], to[0], to[1], to[2]);
				if (object.has("name")) builder.setName(object.get("name").getAsString());
                if (object.has("rotation")) builder.setRotation(context.deserialize(object.get("rotation"), Rotation.Builder.class));
                if (object.has("shade")) builder.setShade(object.get("shade").getAsBoolean());
                if (object.has("light_emission")) builder.setLightEmission(object.get("light_emission").getAsInt());
                JsonObject faces = object.getAsJsonObject("faces");
                for (Map.Entry<String, JsonElement> e : faces.entrySet()) {
                    builder.addFace(Direction.valueOf(e.getKey().toUpperCase(Locale.ROOT)), context.deserialize(e.getValue(), Face.Builder.class));
                }
                return builder;
            }

            @Override
            public JsonElement serialize(Builder src, Type typeOfSrc, JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }


        }
    }
}
