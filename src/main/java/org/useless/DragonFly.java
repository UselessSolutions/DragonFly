package org.useless;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.useless.dragonfly.animation.Animation;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import org.useless.dragonfly.data.block.mojang.state.AppliedData;
import org.useless.dragonfly.data.block.mojang.state.BlockstateData;
import org.useless.dragonfly.data.block.mojang.state.ModelPart;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import org.useless.util.AnimationHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;

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
		//new ModEntity().initializeEntities();
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

	public static @NotNull BlockModelMojangData loadBlockModel(@NotNull final String id) {
		final Minecraft mc = Minecraft.getMinecraft();
		return Objects.requireNonNull(BlockModelMojangData.Cache.loadModelData(mc.texturePackList, id), "Cannot find model for id '" + id + "'!");
	}


	public static @NotNull StaticEntityModel loadEntityModel(@NotNull final String id, final double inflation) {
		final Minecraft mc = Minecraft.getMinecraft();
		return Objects.requireNonNull(EntityGeometryMojangData.Cache.getModel(id, inflation), "Cannot find model for id '" + id + "'!");
	}

	public static @NotNull Animation loadEntityAnimations(@NotNull final String modid, @NotNull final String id) {
		final Minecraft mc = Minecraft.getMinecraft();
		return Objects.requireNonNull(AnimationHelper.getOrCreateEntityAnimation(modid, id), "Cannot find model for id '" + id + "'!");
	}

	public static @NotNull BlockstateData loadStateData(@NotNull final String id) {
		final Minecraft mc = Minecraft.getMinecraft();
		BlockstateData blockstateData = Objects.requireNonNull(BlockstateData.Cache.loadStateData(mc.texturePackList, id), "Cannot find model for id '" + id + "'!");
		if (blockstateData.variants != null){
			for (ModelPart part : blockstateData.variants.values()) {
				for (AppliedData variantData : part.apply){
					loadBlockModel(variantData.model);
				}
			}
		}
		if (blockstateData.multipart != null){
			for (ModelPart part : blockstateData.multipart){
				for (AppliedData variantData : part.apply){
					loadBlockModel(variantData.model);
				}
			}
		}
		return blockstateData;
	}
}

