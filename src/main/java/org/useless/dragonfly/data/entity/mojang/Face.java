package org.useless.dragonfly.data.entity.mojang;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.useless.util.GsonHelper;

import java.lang.reflect.Type;

public class Face {
    public final double @NotNull [] uv;
    public final double @NotNull [] uv_size;
    public final int uv_rotation;

    protected Face(final double @NotNull [] uv, final double @NotNull [] uvSize, final int uv_rotation) {
        this.uv = uv;
        this.uv_size = uvSize;
        this.uv_rotation = uv_rotation;
    }

    public static class Builder {
        private final double @NotNull [] uv;
        private final double @NotNull [] uv_size;
        private int uv_rotation;

        public Builder(final double u, final double v, final double uSize, final double vSize) {
            this.uv = new double[]{u, v};
            this.uv_size = new double[]{uSize, vSize};
            this.uv_rotation = 0;
        }

        public Builder(final double @NotNull [] uv, final double @NotNull [] uvSize) {
            this.uv = uv;
            this.uv_size = uvSize;
            this.uv_rotation = 0;
        }

        public @NotNull Builder setUVRotation(final int rotation) {
            this.uv_rotation = ((rotation + 360) % 360)/90;
            return this;
        }

        protected void setMirror() {
            this.uv[0] += this.uv_size[0];
            this.uv_size[0] *= -1;
        }

        protected @NotNull Face build() {
            return new Face(this.uv, this.uv_size, this.uv_rotation);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {
            @Override
            public Builder deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                final JsonObject object = json.getAsJsonObject();
                final double[] uv = GsonHelper.getAsDoubleArray(object.get("uv"), 2);
                final double[] uvSize = GsonHelper.getAsDoubleArray(object.get("uv_size"), 2);
                final Builder builder = new Builder(uv, uvSize);
                if (object.has("uv_rotation")) builder.setUVRotation(object.get("uv_rotation").getAsInt());
                return builder;
            }

            @Override
            public JsonElement serialize(final Builder src, final Type typeOfSrc, final JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
