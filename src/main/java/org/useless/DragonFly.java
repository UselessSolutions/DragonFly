package org.useless;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.state.AppliedData;
import org.useless.dragonfly.data.block.mojang.state.BlockstateData;
import org.useless.dragonfly.data.block.mojang.state.ModelPart;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.util.Objects;
import java.util.Random;

public class DragonFly implements GameStartEntrypoint {
	public static final String MOD_ID = "dragonfly";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String DEFAULT_NAMESPACE = "minecraft";

	public static String version;
	static {
		version = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
	}

	@Override
	public void beforeGameStart() {

	}


	@Override
	public void afterGameStart() {
		LOGGER.info("DragonFly initialized.");

	}

	public static Random getRandomFromPos(int x, int y, int z){
		Random rand = new Random(0);
		long l1 = rand.nextLong() / 2L * 2L + 1L;
		long l2 = rand.nextLong() / 2L * 2L + 1L;
		long l3 = rand.nextLong() / 2L * 2L + 1L;
		rand.setSeed((long)x * l1 + (long)z * l2 + y * l3);
		return rand;
	}

	public static @NotNull BlockModelMojangData loadDataModel(@NotNull final String id) {
		final Minecraft mc = Minecraft.getMinecraft();
		return Objects.requireNonNull(BlockModelMojangData.Cache.loadModelData(mc.texturePackList, id), "Cannot find model for id '" + id + "'!");
	}

	public static @NotNull BlockstateData loadStateData(@NotNull final String id) {
		final Minecraft mc = Minecraft.getMinecraft();
		BlockstateData blockstateData = Objects.requireNonNull(BlockstateData.Cache.loadStateData(mc.texturePackList, id), "Cannot find model for id '" + id + "'!");
		if (blockstateData.variants != null){
			for (ModelPart part : blockstateData.variants.values()) {
				for (AppliedData variantData : part.apply){
					loadDataModel(variantData.model);
				}
			}
		}
		if (blockstateData.multipart != null){
			for (ModelPart part : blockstateData.multipart){
				for (AppliedData variantData : part.apply){
					loadDataModel(variantData.model);
				}
			}
		}
		return blockstateData;
	}
}

