package org.useless.dragonfly.data.entity.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.logging.LogUtils;
import net.minecraft.client.render.texturepack.TexturePack;
import net.minecraft.client.render.texturepack.TexturePackList;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.collection.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.useless.dragonfly.data.entity.EntityModelData;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.dragonfly.models.entity.mojang.StaticEntityModelMojang;
import org.useless.util.GsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EntityGeometryMojangData implements EntityModelData {
    public static final @NotNull String CURRENT_VERSION = "1.8.0";

    public static final int DEFAULT_TEX_WIDTH = 64;
    public static final int DEFAULT_TEX_HEIGHT = 32;

    protected final static Logger LOGGER = LogUtils.getLogger();
    protected final static Gson gson = builder().create();

    protected static GsonBuilder builder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Builder.class, new Builder.Serializer());
        builder.registerTypeAdapter(Bone.Builder.class, new Bone.Builder.Serializer());
        builder.registerTypeAdapter(Cube.Builder.class, new Cube.Builder.Serializer());
        builder.registerTypeAdapter(Face.Builder.class, new Face.Builder.Serializer());
        return builder;
    }

    protected final @NotNull String  modelId;

    public final @NotNull String version;
    public final double vBoundsWidth;
    public final double vBoundsHeight;
    public final double @NotNull [] vBoundsOffset;
    public final int textureWidth;
    public final int textureHeight;
    public final @NotNull Map<@NotNull String, @NotNull Bone> bones;
    protected EntityGeometryMojangData(
        @NotNull final String modelId,
        @NotNull final String version,
        final double vBoundsWidth,
        final double vBoundsHeight,
        final double @NotNull [] vBoundsOffset,
        final int textureWidth,
        final int textureHeight,
        @NotNull final Iterable<Bone.@NotNull Builder> bones
    ) {
        this.modelId = modelId;

        this.version = version;
        this.vBoundsWidth = vBoundsWidth;
        this.vBoundsHeight = vBoundsHeight;
        this.vBoundsOffset = vBoundsOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.bones = new HashMap<>();

        for (final Bone.Builder builder : bones) {
            @NotNull final Bone bone = builder.build();
            this.bones.put(bone.name, bone);
        }

    }

    @Override
    public @NotNull String modelId() {
        return this.modelId;
    }

    @Override
    public @NotNull StaticEntityModel asModel(final double inflation) {
        return new StaticEntityModelMojang(this, inflation);
    }

    public static class Builder {
        private @NotNull String version;
        private final double vBoundsWidth;
        private final double vBoundsHeight;
        private final double @NotNull [] vBoundsOffset;
        private int texWidth;
        private int texHeight;
        private final @NotNull List<Bone. @NotNull Builder> bones = new ArrayList<>();

        public Builder(final double visualBoundsWidth, final double visualBoundsHeight) {
            this.version = CURRENT_VERSION;
            this.vBoundsWidth = visualBoundsWidth;
            this.vBoundsHeight = visualBoundsHeight;
            this.vBoundsOffset = new double[]{0, 0, 0};
            this.texWidth = DEFAULT_TEX_WIDTH;
            this.texHeight = DEFAULT_TEX_HEIGHT;
        }

        public @NotNull Builder setVersion(@NotNull final String version) {
            this.version = version;
            return this;
        }

//        public @NotNull Builder setVisualBoundsSize(final double visualBoundsWidth, final double visualBoundsHeight) {
//            this.vBoundsWidth = visualBoundsWidth;
//            this.vBoundsHeight = visualBoundsHeight;
//            return this;
//        }

        public @NotNull Builder setVisualBoundsOffset(final double offX, final double offY, final double offZ) {
            this.vBoundsOffset[0] = offX;
            this.vBoundsOffset[1] = offY;
            this.vBoundsOffset[2] = offZ;
            return this;
        }

        public @NotNull Builder setTextureSize(final int texWidth, final int texHeight) {
            this.texWidth = texWidth;
            this.texHeight = texHeight;
            return this;
        }

        public @NotNull Builder addBone(final @NotNull Bone.Builder boneBuilder) {
            this.bones.add(boneBuilder);
            return this;
        }

        public EntityGeometryMojangData build(@NotNull final String  modelID) {
            return new EntityGeometryMojangData(modelID, this.version, this.vBoundsWidth, this.vBoundsHeight, this.vBoundsOffset, this.texWidth, this.texHeight, this.bones);
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {

            @Override
            public Builder deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                final JsonObject object = json.getAsJsonObject();
                final Builder builder;
                if (object.has("description")) { // New desc layout
                    final JsonObject description = object.getAsJsonObject("description");
                    builder = new Builder(description.get("visible_bounds_width").getAsDouble(), description.get("visible_bounds_height").getAsDouble());
                    if (description.has("visible_bounds_offset")) {
                        final double[] offset = GsonHelper.getAsDoubleArray(description.get("visible_bounds_offset"), 3);
                        builder.setVisualBoundsOffset(offset[0], offset[1], offset[2]);
                    }
                    if (description.has("texture_width") && description.has("texture_height")) {
                        final int width = description.get("texture_width").getAsInt();
                        final int height = description.get("texture_height").getAsInt();
                        builder.setTextureSize(width, height);
                    }
                } else { // Old desc layout
                    builder = new Builder(object.get("visible_bounds_width").getAsDouble(), object.get("visible_bounds_height").getAsDouble());
                    if (object.has("visible_bounds_offset")) {
                        final double[] offset = GsonHelper.getAsDoubleArray(object.get("visible_bounds_offset"), 3);
                        builder.setVisualBoundsOffset(offset[0], offset[1], offset[2]);
                    }
                    if (object.has("texturewidth") && object.has("textureheight")) {
                        final int width = object.get("texturewidth").getAsInt();
                        final int height = object.get("textureheight").getAsInt();
                        builder.setTextureSize(width, height);
                    }
                }

                if (object.has("bones")) {
                    final JsonArray bones = object.getAsJsonArray("bones");
                    for (final JsonElement e : bones) {
                        builder.addBone(context.deserialize(e, Bone.Builder.class));
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

    public static class Cache {
        protected static final Map<String, EntityGeometryMojangData> modelDataCache = new HashMap<>();

        protected static final @NotNull Map<@NotNull NamespaceID, @NotNull Map<@NotNull String, @NotNull Pair<String, Double>>> localModelMappings = new HashMap<>();

        public static @NotNull StaticEntityModel getFallbackModel() {
            return new Builder(1, 1)
                .addBone(
                    new Bone.Builder("cube")
                        .addCube(new Cube.Builder(-8, 0, -8, 16, 16, 16)
                            .setFaces(0, 0))
                ).build("geometry.missing").asModel(0);
        }

        public static @NotNull StaticEntityModel getModel(@NotNull final String id, final double inflation) {
            final EntityGeometryMojangData data = getGeometry(id);
            if (data == null) {
                LOGGER.error("No model data for model '{}'!", id);
                return getFallbackModel();
            }
            return data.asModel(inflation);
        }

        public static @NotNull StaticEntityModel getModelForEntity(@NotNull final NamespaceID namespaceID, @NotNull final String localModelId) {
            if (localModelMappings.containsKey(namespaceID)) {
                final Map<String, Pair<String, Double>> modelMap = localModelMappings.get(namespaceID);
                final Pair<String, Double> pair = modelMap.get(localModelId);
                if (pair == null) return getFallbackModel();
                return getModel(pair.getLeft(), pair.getRight());
            }
            return getFallbackModel();
        }

        public static @Nullable EntityGeometryMojangData getGeometry(@NotNull final String id) {
            return modelDataCache.get(id);
        }

        public static void loadModelsFromResource(final @NotNull InputStream stream) {
            final JsonObject modelData = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            final String version = modelData.get("format_version").getAsString();
            if (modelData.has("minecraft:geometry")) { // Newer style bedrock entity layout
                final JsonArray geometries = modelData.getAsJsonArray("minecraft:geometry");
                for (final JsonElement e : geometries) {
                    final JsonObject object = e.getAsJsonObject();
                    final String id = object.getAsJsonObject("description").get("identifier").getAsString();
                    try {
                        final EntityGeometryMojangData data = gson.fromJson(e, Builder.class).build(id);
                        modelDataCache.put(data.modelId(), data);
                    } catch (final Exception ex) {
                        LOGGER.warn("Failed to load json data with key {} as geometry data!", id, ex);
                        continue;
                    }
                }
            } else { // Older style bedrock entity layout
                for (final Map.Entry<String, JsonElement> entry : modelData.entrySet()) {
                    if ("format_version".equals(entry.getKey())) continue;
                    final String id = entry.getKey();
                    try {
                        final EntityGeometryMojangData data = gson.fromJson(entry.getValue(), Builder.class).build(id);
                        modelDataCache.put(data.modelId(), data);
                    } catch (final Exception e) {
                        LOGGER.warn("Failed to load json data with key {} as geometry data!", entry.getKey(), e);
                        continue;
                    }
                }
            }
        }

        public static void reload(@NotNull final TexturePackList packList) {
            modelDataCache.clear();

            final Collection<TexturePack> packs = new ArrayList<>();
            packs.add(packList.getDefaultTexturePack());
            packs.addAll(packList.selectedPacks);

            final Collection<String> modelPaths = new HashSet<>();

            for (final TexturePack pack : packs) {
                try (final @Nullable InputStream stream = pack.getResourceAsStream("/assets/minecraft/models/entity/models.json")) {
                    if (stream == null) continue;
                    final JsonObject manifestObject = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                    if (manifestObject.has("model_paths")) {
                        final JsonArray paths = manifestObject.getAsJsonArray("model_paths");
                        for (final JsonElement e : paths) {
                            modelPaths.add(e.getAsString());
                        }
                    }
                    if (manifestObject.has("renderers")) {
                        final JsonObject renderersObj = manifestObject.getAsJsonObject("renderers");
                        for (final Map.Entry<String, JsonElement> entry : renderersObj.entrySet()) {
                            final NamespaceID id;
                            try {
                                id = NamespaceID.getPermanent(entry.getKey());
                            } catch (final HardIllegalArgumentException exception) {
                                LOGGER.error("Id '{}' is malformed!", entry.getKey(), exception);
                                continue;
                            }
                            localModelMappings.putIfAbsent(id, new HashMap<>());
                            final Map<String, Pair<String, Double>> modelMappings = localModelMappings.get(id);
                            final JsonObject mappingsObject = entry.getValue().getAsJsonObject();
                            for (final Map.Entry<String, JsonElement> mappingEntry : mappingsObject.entrySet()) {
                                final JsonElement element = mappingEntry.getValue();
                                if (element.isJsonPrimitive()) {
                                    modelMappings.put(mappingEntry.getKey(), Pair.of(mappingEntry.getValue().getAsString(), 0D));
                                } else if (element.isJsonObject()) {
                                    final JsonObject mapping = element.getAsJsonObject();
                                    modelMappings.put(mappingEntry.getKey(), Pair.of(mapping.get("model").getAsString(), mapping.has("inflation") ? mapping.get("inflation").getAsDouble() : 0D));
                                } else {
                                    LOGGER.warn("Unknown format for element '{}' of key '{}' in pack '{}'", element, mappingEntry.getKey(), pack.packId);
                                }
                            }
                        }
                    }
                } catch (final Exception e) {
                    LOGGER.error("Exception while reading entity models manifest in pack '{}'!", pack.packId, e);
                }
            }

            for (final TexturePack pack : packs) {
                for (final String s : modelPaths) {
                    try (final @Nullable InputStream stream = pack.getResourceAsStream(s)) {
                        if (stream == null) continue;
                        loadModelsFromResource(stream);
                    } catch (final IOException e) {
                        LOGGER.warn("Failed to load data for resource at {}!", s, e);
                    } catch (final Exception e) {
                        LOGGER.warn("Error occurred when reading load data for resource at {}!", s, e);
                    }
                }
            }
        }
    }
}
