package org.useless.dragonfly.data.entity.mojang;

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
import java.util.Map;

public class Cube {
    public final double @NotNull [] origin;
    public final double @NotNull [] size;
    public final @Nullable Face @NotNull [] faces;
    public final double inflate;
    public final double @NotNull [] pivot;
    public final double @Nullable [] rotation;

    protected Cube(final double @NotNull [] origin, final double @NotNull [] size, final @Nullable Face.Builder @NotNull [] faces, final double inflate, final double @NotNull [] pivot, final double @Nullable [] rotation) {
        this.origin = origin;
        this.size = size;
        this.pivot = pivot;
        this.rotation = rotation;
        this.faces = new Face[6];
        for (int i = 0; i < 6; i++) {
            final Face.Builder builder = faces[i];
            if (builder == null) continue;
            this.faces[i] = builder.build();
        }
        this.inflate = inflate;
    }

    public static class Builder {
        private final double @NotNull [] origin;
        private final double @NotNull [] size;
        private final @Nullable Face.Builder @NotNull [] faces;
        private double inflate;
        private final double @NotNull [] pivot;
        private double @Nullable [] rotation = null;
        private boolean mirror;

        public Builder(
            final double oX, final double oY, final double oZ,
            final double sX, final double sY, final double sZ
        ) {
            this.origin = new double[]{oX, oY, oZ};
            this.size = new double[]{sX, sY, sZ};
            this.faces = new Face.Builder[6];
            this.inflate = 0;
            this.pivot = new double[]{0, 0, 0};
        }

        public Builder(
            final double @NotNull [] origin,
            final double @NotNull [] size
        ) {
            this.origin = origin;
            this.size = size;
            this.faces = new Face.Builder[6];
            this.inflate = 0;
            this.pivot = new double[]{0, 0, 0};
        }

        public @NotNull Builder setFaces(final double u, final double v) {
            final double sizeX = this.size[0];
            final double sizeY = this.size[1];
            final double sizeZ = this.size[2];
            this.faces[Direction.UP.getId()] = new Face.Builder(u + sizeZ, v, sizeX, sizeZ);
            this.faces[Direction.DOWN.getId()] = new Face.Builder(u + sizeX + sizeZ, v + sizeZ, sizeX, -sizeZ);
            this.faces[Direction.NORTH.getId()] = new Face.Builder(u + sizeZ, v + sizeZ, sizeX, sizeY);
            this.faces[Direction.SOUTH.getId()] = new Face.Builder(u + (sizeZ * 2) + sizeX, v + sizeZ, sizeX, sizeY);
            this.faces[Direction.EAST.getId()] = new Face.Builder(u, v + sizeZ, sizeZ, sizeY);
            this.faces[Direction.WEST.getId()] = new Face.Builder(u + sizeZ + sizeX, v + sizeZ, sizeZ, sizeY);
            return this;
        }

        public @NotNull Builder setFace(final @NotNull Direction direction, final @Nullable Face.Builder builder) {
            this.faces[direction.getId()] = builder;
            return this;
        }

        public @NotNull Builder setInflation(final double inflation) {
            this.inflate = inflation;
            return this;
        }

        public @NotNull Builder setPivot(final double x, final double y, final double z) {
            this.pivot[0] = x;
            this.pivot[1] = y;
            this.pivot[2] = z;
            return this;
        }

        public @NotNull Builder setRotation(final double x, final double y, final double z) {
            if (this.rotation == null) this.rotation = new double[3];
            this.rotation[0] = x;
            this.rotation[1] = y;
            this.rotation[2] = z;
            return this;
        }

        public @NotNull Builder setMirror() {
            this.mirror = !this.mirror;
            return this;
        }

        protected void mirror() {
            final Face.Builder west = this.faces[Direction.WEST.getId()];
            this.faces[Direction.WEST.getId()] = this.faces[Direction.EAST.getId()];
            this.faces[Direction.EAST.getId()] = west;
            for (final Face.Builder builder : this.faces) {
                if (builder == null) continue;
                builder.setMirror();
            }

        }

        protected @NotNull Cube build()
        {
            if (this.mirror) {
                mirror();
            }
            return new Cube(this.origin, this.size, this.faces, this.inflate, this.pivot, this.rotation);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {

            @Override
            public Builder deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                final JsonObject o = json.getAsJsonObject();
                final double[] origin = GsonHelper.getAsDoubleArray(o.getAsJsonArray("origin"), 3);
                final double[] size = GsonHelper.getAsDoubleArray(o.getAsJsonArray("size"), 3);
                final Builder builder = new Builder(origin, size);

                final JsonElement uvElement = o.get("uv");
                if (uvElement.isJsonArray()) {
                    final double[] uv = GsonHelper.getAsDoubleArray(uvElement, 2);
                    builder.setFaces(uv[0], uv[1]);
                } else if (uvElement.isJsonObject()) {
                    final JsonObject uvObject = uvElement.getAsJsonObject();
                    for (final Map.Entry<String, JsonElement> faceEntries : uvObject.entrySet()) {
                        final Direction direction = Direction.valueOf(faceEntries.getKey().toUpperCase(Locale.ROOT));
                        builder.setFace(direction, context.deserialize(faceEntries.getValue(), Face.Builder.class));
                    }
                }

                if (o.has("inflate")) builder.setInflation(o.get("inflate").getAsDouble());

                if (o.has("pivot")) {
                    final double[] pivot = GsonHelper.getAsDoubleArray(o.get("pivot"), 3);
                    builder.setPivot(pivot[0], pivot[1], pivot[2]);
                }

                if (o.has("rotation")) {
                    final double[] rotation = GsonHelper.getAsDoubleArray(o.get("rotation"), 3);
                    builder.setRotation(rotation[0], rotation[1], rotation[2]);
                }

                if (o.has("mirror") && o.get("mirror").getAsBoolean()) {
                    builder.setMirror();
                }

                return builder;
            }

            @Override
            public JsonElement serialize(final Builder src, final Type typeOfSrc, final JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
