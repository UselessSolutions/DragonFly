package org.useless.dragonfly.data.block.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import net.minecraft.client.render.texturepack.TexturePackList;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.useless.dragonfly.DisplayPos;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.models.block.StaticBlockModel;
import org.useless.dragonfly.models.block.mojang.StaticBlockModelMojang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Modern Minecraft Block Model Data scheme as laid out on the {@see <a href="https://minecraft.wiki/w/Model#Block_models">minecraft.wiki/w/Model#Block_models</a>} page of the minecraft wiki.</p>
 * <p>Implementation should align with Modern's as of the release of 1.21.4</>
 */
public class BlockModelMojangData implements BlockModelData {
    public static final float UNITS_PER_BLOCK = 16f;
    public static final float BLOCKS_PER_UNIT = 1/16f;
    protected final static Logger LOGGER = LogUtils.getLogger();
    protected final static Gson gson = builder().create();

    protected static GsonBuilder builder() {
        final GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Builder.class, new Builder.Serializer());
        builder.registerTypeAdapter(Element.Builder.class, new Element.Builder.Serializer());
        builder.registerTypeAdapter(Face.Builder.class, new Face.Builder.Serializer());
        builder.registerTypeAdapter(Rotation.Builder.class, new Rotation.Builder.Serializer());
        builder.registerTypeAdapter(DisplayPos.class, new DisplayPos.Serializer());
        return builder;
    }

    protected static final String DEFAULT_NAMESPACE = "minecraft";

    protected static final String DEFAULT_PARENT = null;
    protected static final boolean DEFAULT_AMBIENT_OCCLUSION = true;
    protected static final int DEFAULT_RENDER_LAYER = 0;

    protected final NamespaceID modelId;

    /**
     * <p>
     * Loads a different model from the given path, in form of a resource location.
     * If both "parent" and "elements" are set, the "elements" tag overrides the "elements" tag from the previous model.
     * </p>
     * Can be set to "builtin/generated" to use a model that is created out of the specified icon.
     * Only the first layer is supported, and rotation can be achieved only by using block states files.
     */
    public final @Nullable String parent;
    /**
     * Whether to use ambient occlusion (true - default), or not (false).
     */
    public final boolean ambientOcclusion;

    /**
     * Render pass to render model on
     */
    public final @Nullable Integer renderLayer;

    /**
     * Holds the different transforms for where item models are displayed.
     */
    public final @Nullable  Map<@NotNull String, @NotNull DisplayPos> displayPositions;
    /**
     * Holds the textures of the model, in form of a resource location or can be another texture variable.
     */
    public final @Nullable  Map<@NotNull String, @NotNull String> textures;

    /**
     * Each direction's color index, -1 represents no color
     */
    public final @Nullable Map<@NotNull Direction, @NotNull Integer> particleIndices;

    /**
     * Contains all the elements of the model.
     * They can have only cubic forms.
     */
    public final @Nullable List<@NotNull Element> elements;

    protected BlockModelMojangData(
        @NotNull final TexturePackList texturePackList,
        @NotNull final NamespaceID id,
        @Nullable final String parent,
        final boolean ambientOcclusion,
        @Nullable final Integer renderLayer,
        @Nullable final Map<@NotNull String, @NotNull DisplayPos> displayPositions,
        @Nullable final Map<@NotNull String, @NotNull String> textures,
        @Nullable final Map<@NotNull Direction, @NotNull Integer> particleIndices,
        @Nullable final List<@NotNull Element> elements)
    {
        this.modelId = id.makePermanent();
        this.parent = parent;
        if (parent != null) {
            final BlockModelMojangData parentData = Cache.loadModelData(texturePackList, parent);
            this.ambientOcclusion = ambientOcclusion;
            if (parentData != null) {
                this.displayPositions = new HashMap<>();
                this.textures = new HashMap<>();
                this.particleIndices = new HashMap<>();
                // Elements overrides parent's elements rather than merges them like other fields
                this.elements = (parentData.elements != null && elements == null) ? new ArrayList<>(parentData.elements) : elements;
                this.renderLayer = (parentData.renderLayer != null && renderLayer == null) ? parentData.renderLayer : renderLayer;

                if (parentData.displayPositions != null) this.displayPositions.putAll(parentData.displayPositions);
                if (displayPositions != null) this.displayPositions.putAll(displayPositions);
                if (parentData.textures != null) this.textures.putAll(parentData.textures);
                if (textures != null) this.textures.putAll(textures);
                if (parentData.particleIndices != null) this.particleIndices.putAll(parentData.particleIndices);
                if (particleIndices != null) this.particleIndices.putAll(particleIndices);
            } else {
                this.displayPositions = displayPositions;
                this.particleIndices = particleIndices;
                this.renderLayer = renderLayer;
                this.textures = textures;
                this.elements = elements;
            }
        } else {
            this.ambientOcclusion = ambientOcclusion;
            this.displayPositions = displayPositions;
            this.particleIndices = particleIndices;
            this.renderLayer = renderLayer;
            this.textures = textures;
            this.elements = elements;
        }

    }

    @Override
    public @NotNull NamespaceID modelId() {
        return this.modelId;
    }

    @Override
    public @NotNull StaticBlockModel asModel() {
        return new StaticBlockModelMojang(this);
    }

    @Override
    public String toString() {
        return "BlockModelMojangData{" +
            "modelId=" + this.modelId +
            ", parent='" + this.parent + '\'' +
            ", ambientOcclusion=" + this.ambientOcclusion +
            ", displayPositions=" + this.displayPositions +
            ", textures=" + this.textures +
            ", particleIndices=" + this.particleIndices +
            ", elements=" + this.elements +
            '}';
    }

    @SuppressWarnings("unused")
    public static class Builder {
        protected @Nullable String parent = DEFAULT_PARENT;
        protected boolean ambientOcclusion = DEFAULT_AMBIENT_OCCLUSION;
        protected int renderLayer = DEFAULT_RENDER_LAYER;
        protected @Nullable Map<@NotNull String, @NotNull DisplayPos> displayPosMap = null;
        protected @Nullable Map<@NotNull String, @NotNull String> textures = null;
        protected @Nullable Map<@NotNull Direction, @NotNull Integer> particleIndices = null;
        protected @Nullable List<Element. @NotNull Builder> elements = null;

        public @NotNull Builder setParent(@Nullable final String parent) {
            this.parent = parent;
            return this;
        }

        public @NotNull Builder setAO(final boolean ao) {
            this.ambientOcclusion = ao;
            return this;
        }

        public @NotNull Builder setRenderLayer(final int renderLayer) {
            this.renderLayer = renderLayer;
            return this;
        }

        public @NotNull Builder setDisplay(@NotNull final String id, @Nullable final DisplayPos displayPos) {
            if (displayPos == null && this.displayPosMap != null) {
                this.displayPosMap.remove(id);
            } else if (displayPos != null) {
                prepareDisplayPosMap().put(id, displayPos);
            }
            return this;
        }

        public @NotNull Builder setTexture(@NotNull final String textureVariable, @NotNull final String textureSymbol) {
            prepareTextureMap().put(textureVariable, textureSymbol);
            return this;
        }

        public @NotNull Builder setParticleIndex(@NotNull final Direction direction, final int index) {
            prepareParticleIndicesMap().put(direction, index);
            return this;
        }

        public @NotNull Builder addElement(final Element. @NotNull Builder elementBuilder) {
            prepareElementsList().add(elementBuilder);
            return this;
        }

        private @NotNull Map<@NotNull String, @NotNull DisplayPos> prepareDisplayPosMap() {
            if (this.displayPosMap == null) this.displayPosMap = new HashMap<>();
            return this.displayPosMap;
        }

        private @NotNull Map<@NotNull String, @NotNull String> prepareTextureMap() {
            if (this.textures == null) this.textures = new HashMap<>();
            return this.textures;
        }

        private @NotNull Map<@NotNull Direction, @NotNull Integer> prepareParticleIndicesMap() {
            if (this.particleIndices == null) this.particleIndices = new HashMap<>();
            return this.particleIndices;
        }

        private @NotNull List<Element. @NotNull Builder> prepareElementsList() {
            if (this.elements == null) this.elements = new ArrayList<>();
            return this.elements;
        }

        protected @NotNull BlockModelMojangData build(@NotNull final TexturePackList texturePackList, @NotNull String id) {
            if (!id.contains(":")) {
                id = DEFAULT_NAMESPACE + ":" + id;
            }
            List<Element> elementList = null;
            if (this.elements != null) {
                elementList = new ArrayList<>();
                for (final Element.Builder b : this.elements) {
                    elementList.add(b.build());
                }
            }
            try {
                return new BlockModelMojangData(texturePackList, NamespaceID.getPermanent(id), this.parent, this.ambientOcclusion, this.renderLayer, this.displayPosMap, this.textures, this.particleIndices, elementList);
            } catch (final HardIllegalArgumentException e) {
                throw new IllegalArgumentException(e);
            }
        }

        public static class Serializer implements JsonSerializer<Builder>, JsonDeserializer<Builder> {
            @Override
            public Builder deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                final JsonObject object = json.getAsJsonObject();
                final Builder dataBuilder = new Builder();
                if (object.has("parent")) dataBuilder.setParent(object.get("parent").getAsString());
                if (object.has("ambientocclusion")) dataBuilder.setAO(object.get("ambientocclusion").getAsBoolean());
                if (object.has("renderlayer")) dataBuilder.setRenderLayer(object.get("renderlayer").getAsInt());
                if (object.has("display")) {
                    final JsonObject display = object.getAsJsonObject("display");
                    for (final Map.Entry<String, JsonElement> e : display.entrySet()) {
                        dataBuilder.setDisplay(e.getKey(), context.deserialize(e.getValue(), DisplayPos.class));
                    }
                }
                if (object.has("textures")) {
                    final JsonObject textures = object.getAsJsonObject("textures");
                    for (final Map.Entry<String, JsonElement> e : textures.entrySet()) {
                        dataBuilder.setTexture(e.getKey(), e.getValue().getAsString());
                    }
                }
                if (object.has("elements")) {
                    final JsonArray elements = object.getAsJsonArray("elements");
                    for (final JsonElement e : elements) {
                        dataBuilder.addElement(context.deserialize(e, Element.Builder.class));
                    }
                }
                if (object.has("particles")) {
                    final JsonObject textures = object.getAsJsonObject("particles");
                    for (final Map.Entry<String, JsonElement> e : textures.entrySet()) {
                        dataBuilder.setParticleIndex(Direction.valueOf(e.getKey().toUpperCase(Locale.ROOT)), e.getValue().getAsInt());
                    }
                }

                return dataBuilder;
            }

            @Override
            public JsonElement serialize(final Builder src, final Type typeOfSrc, final JsonSerializationContext context) {
                throw new UnsupportedOperationException();
            }
        }
    }

    @SuppressWarnings("unused")
    public static class Cache {
        protected static final Map<String, BlockModelMojangData> modelDataCache = new HashMap<>();

        protected static final String RESOURCE_PATH = "/assets/%s/models/%s.json";

        public static @Nullable BlockModelMojangData loadModelData(@NotNull final TexturePackList texturePackList, @NotNull final NamespaceID id) {
            final String s = id.toString();
            if (modelDataCache.containsKey(s)) return modelDataCache.get(s);
            final Builder data = loadFromStream(texturePackList.getResourceAsStream(String.format(RESOURCE_PATH, id.namespace(), id.value())));
            if (data == null) {
                LOGGER.error("Could not locate block model data for id '{}'!", id);
                return null;
            }
            final BlockModelMojangData model = data.build(texturePackList, id.toString());
            modelDataCache.put(model.modelId().toString(), model);
            return model;
        }
        public static @Nullable BlockModelMojangData loadModelData(@NotNull final TexturePackList texturePackList, @NotNull final String id) {
            final String namespace;
            final String value;
            if (!id.contains(":")) {
                namespace = DEFAULT_NAMESPACE;
                value = id;
            } else {
                final String[] strings = id.split(":");
                if (strings.length != 2) throw new  IllegalArgumentException("Block model id '" + id + "' cannot have more then 1 ':' character!");
                namespace = strings[0];
                value = strings[1];
            }
            if (modelDataCache.containsKey(id)) return modelDataCache.get(id);
            final Builder data = loadFromStream(texturePackList.getResourceAsStream(String.format(RESOURCE_PATH, namespace, value)));
            if (data == null) {
                LOGGER.error("Could not locate block model data for id '{}'!", id);
                return null;
            }
            final BlockModelMojangData model = data.build(texturePackList, namespace + ":" + value);
            modelDataCache.put(model.modelId().toString(), model);
            return model;
        }

        public static void resetCache() {
            modelDataCache.clear();
        }

        protected static @Nullable BlockModelMojangData.Builder loadFromStream(@Nullable final InputStream stream) {
            if (stream == null) return null;
            return gson.fromJson(new JsonReader(new InputStreamReader(stream)), Builder.class);
        }
    }
}
