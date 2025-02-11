package org.useless.dragonfly.data.entity.mojang;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.util.GsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bone {
    public final @NotNull String name;
    public final @Nullable String parent;
    public final double @NotNull [] pivot;
    public final double @NotNull [] rotation;
    public final @NotNull Cube @NotNull [] cubes;
    public final double inflate;

    protected Bone(
        @NotNull final String name,
        @Nullable final String parent,
        final double @NotNull [] pivot,
        final double @NotNull [] rotation,
        final double inflate,
        @NotNull final Collection<Cube.Builder> cubes) {
        this.name = name;
        this.parent = parent;
        this.pivot = pivot;
        this.rotation = rotation;
        this.cubes = new Cube[cubes.size()];
        this.inflate = inflate;

        int i = 0;
        for (final Cube.Builder builder : cubes) {
            this.cubes[i] = builder.build();
            i++;
        }
    }

    public static class Builder {
        private final @NotNull String name;
        private @Nullable String parent;
        private final double @NotNull [] pivot;
        private final double @NotNull [] rotation;
        private double inflate;
        private final @NotNull List<Cube. @NotNull  Builder> cubes = new ArrayList<>();
        private boolean mirror;
        public Builder(@NotNull final String name) {
            this.name = name;
            this.parent = null;
            this.pivot = new double[]{0, 0, 0};
            this.rotation = new double[]{0, 0, 0};
            this.inflate = 0;
            this.mirror = false;
        }

        public @NotNull Builder setParent(final @NotNull String parent) {
            this.parent = parent;
            return this;
        }

        public @NotNull Builder setPivot(final double x, final double y, final double z) {
            this.pivot[0] = x;
            this.pivot[1] = y;
            this.pivot[2] = z;
            return this;
        }

        public @NotNull Builder setRotation(final double x, final double y, final double z) {
            this.rotation[0] = x;
            this.rotation[1] = y;
            this.rotation[2] = z;
            return this;
        }

        public @NotNull Builder setInflation(final double inflation) {
            this.inflate = inflation;
            return this;
        }

        public @NotNull Builder setMirror(final boolean mirror) {
            this.mirror = mirror;
            return this;
        }

        public @NotNull Builder addCube(final @NotNull Cube.Builder cube) {
            this.cubes.add(cube);
            return this;
        }

        protected @NotNull Bone build() {
            if (this.mirror) {
                for (final Cube.Builder builder : this.cubes) {
                    builder.setMirror();
                }
            }
            return new Bone(this.name, this.parent, this.pivot, this.rotation, this.inflate, this.cubes);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {

            @Override
            public Builder deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                final JsonObject object = json.getAsJsonObject();
                final Builder builder = new Builder(object.get("name").getAsString());
                if (object.has("parent")) builder.setParent(object.get("parent").getAsString());
                if (object.has("pivot")) {
                    final double[] pivot = GsonHelper.getAsDoubleArray(object.get("pivot"), 3);
                    builder.setPivot(pivot[0], pivot[1], pivot[2]);
                }
                if (object.has("rotation")) {
                    final double[] rotation = GsonHelper.getAsDoubleArray(object.get("rotation"), 3);
                    builder.setRotation(rotation[0], rotation[1], rotation[2]);
                }
                if (object.has("inflate")) builder.setInflation(object.get("inflate").getAsDouble());
                if (object.has("mirror")) builder.setMirror(object.get("mirror").getAsBoolean());
                if (object.has("cubes")) {
                    final JsonArray cubes = object.getAsJsonArray("cubes");
                    for (final JsonElement e : cubes) {
                        builder.addCube(context.deserialize(e, Cube.Builder.class));
                    }
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
