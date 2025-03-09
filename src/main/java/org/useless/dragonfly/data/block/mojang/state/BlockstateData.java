package org.useless.dragonfly.data.block.mojang.state;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import net.minecraft.client.render.texturepack.TexturePackList;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.state.adapter.AppliedDataJsonAdapter;
import org.useless.dragonfly.data.block.mojang.state.adapter.BlockStateJsonAdapter;
import org.useless.dragonfly.data.block.mojang.state.adapter.ModelPartJsonAdapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.useless.DragonFly.DEFAULT_NAMESPACE;

public class BlockstateData {
	public Map<String, ModelPart> variants;
	public ModelPart[] multipart;

	protected final static Logger LOGGER = LogUtils.getLogger();

	public BlockstateData(Map<String, ModelPart> variants){
		this.variants = variants;
	}
	public BlockstateData(ModelPart[] multipart){
		this.multipart = multipart;
	}

	public final static Gson GSON = builder().create();

	protected static GsonBuilder builder() {
		final GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.registerTypeAdapter(ModelPart.class, new ModelPartJsonAdapter());
		builder.registerTypeAdapter(AppliedData.class, new AppliedDataJsonAdapter());
		builder.registerTypeAdapter(BlockstateData.class, new BlockStateJsonAdapter());
		return builder;
	}

	public static class Cache {
		protected static final Map<String, BlockstateData> stateDataCache = new HashMap<>();

		protected static final String RESOURCE_PATH = "/assets/%s/blockstates/%s.json";

		public static @Nullable BlockstateData loadStateData(@NotNull final TexturePackList texturePackList, @NotNull final NamespaceID id) {
			final String s = id.toString();
			if (stateDataCache.containsKey(s)) return stateDataCache.get(s);
			final BlockstateData data = loadFromStream(texturePackList.getResourceAsStream(String.format(RESOURCE_PATH, id.namespace(), id.value())));
			if (data == null) {
				LOGGER.error("Could not locate block model data for id '{}'!", id);
				return null;
			}
			stateDataCache.put(s, data);
			return data;
		}
		public static @Nullable BlockstateData loadStateData(@NotNull final TexturePackList texturePackList, @NotNull final String id) {
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
			if (stateDataCache.containsKey(id)) return stateDataCache.get(id);
			final BlockstateData data = loadFromStream(texturePackList.getResourceAsStream(String.format(RESOURCE_PATH, namespace, value)));
			if (data == null) {
				LOGGER.error("Could not locate block model data for id '{}'!", id);
				return null;
			}
			stateDataCache.put(id, data);
			return data;
		}

		public static void resetCache() {
			stateDataCache.clear();
		}

		protected static @Nullable BlockstateData loadFromStream(@Nullable final InputStream stream) {
			if (stream == null) return null;
			return GSON.fromJson(new JsonReader(new InputStreamReader(stream)), BlockstateData.class);
		}
	}

	/*@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (variants == null){
			builder.append("null\n");
		} else {
			for (String key: variants.keySet()) {
				builder.append(key).append("\n");
				builder.append(Utilities.tabBlock(variants.get(key).toString(),1));
			}
		}
		return builder.toString();
	}*/
}
