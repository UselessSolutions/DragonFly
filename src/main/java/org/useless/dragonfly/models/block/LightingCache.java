package org.useless.dragonfly.models.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.WorldSource;

import java.util.Arrays;

final public class LightingCache {
    private final Minecraft mc = Minecraft.getMinecraft();

    private static final int CACHE_RANGE = 1;
    private static final int CACHE_SIZE = CACHE_RANGE*2+1;

    private final boolean[] brightnessCached = new boolean[CACHE_SIZE*CACHE_SIZE*CACHE_SIZE];
    private final float[] brightnessValue = new float[CACHE_SIZE*CACHE_SIZE*CACHE_SIZE];

    private final boolean[] opacityCached = new boolean[CACHE_SIZE*CACHE_SIZE*CACHE_SIZE];
    private final boolean[] opacityValue = new boolean[CACHE_SIZE*CACHE_SIZE*CACHE_SIZE];

    private final boolean[] lightmapCoordCached = new boolean[CACHE_SIZE*CACHE_SIZE*CACHE_SIZE];
    private final int[] lightmapCoordValue = new int[CACHE_SIZE*CACHE_SIZE*CACHE_SIZE];

    private int offsetX = Integer.MAX_VALUE;
    private int offsetY = Integer.MAX_VALUE;
    private int offsetZ = Integer.MAX_VALUE;

    private Block<?> block;
    private WorldSource access;

    public void setupCache(Block<?> block, WorldSource access, int x, int y, int z){
        if(x != offsetX || y != offsetY || z != offsetZ || this.block != block || this.access != access) {
            this.block = block;
            this.access = access;
            Arrays.fill(brightnessCached, false);
            Arrays.fill(opacityCached, false);
            Arrays.fill(lightmapCoordCached, false);
            offsetX = x;
            offsetY = y;
            offsetZ = z;
        }
    }

    private float calcBrightness(final int x, final int y, final int z) {
        final int blockId = this.access.getBlockId(x, y, z);
        final Block<?> block = Blocks.getBlock(blockId);

        if(LightmapHelper.isLightmapEnabled() || this.mc.fullbright) {
            if(this.block.emission >= 15) {
                return 1.0f;
            }
            if(!this.mc.isAmbientOcclusionEnabled()) {
                return 1.0f;
            }
            if(block == null) {
                return 1.0f;
            }
            return 1.0f - block.getAmbientOcclusionStrength(this.access, x, y, z);
        }
        final int emission = Blocks.lightEmission[blockId];
        final float brightness = this.access.getBrightness(x, y, z, emission);

        return Math.min(brightness, (this.mc.isAmbientOcclusionEnabled() && block != null) ? (1 - block.getAmbientOcclusionStrength(this.access, x, y, z)) : 1);
    }

	public float getBrightness(int relX, int relY, int relZ) {
		int index = (relX + CACHE_RANGE) * CACHE_SIZE * CACHE_SIZE + (relY + CACHE_RANGE) * CACHE_SIZE + (relZ + CACHE_RANGE);

		if(!brightnessCached[index]) {
			int x1 = relX + offsetX;
			int y1 = relY + offsetY;
			int z1 = relZ + offsetZ;

			brightnessValue[index] = calcBrightness(x1, y1, z1);
			brightnessCached[index] = true;
		}

		return brightnessValue[index];
	}

    public boolean getOpacity(int relX, int relY, int relZ) {
        int index = (relX+CACHE_RANGE)*CACHE_SIZE*CACHE_SIZE+(relY+CACHE_RANGE)*CACHE_SIZE+(relZ+CACHE_RANGE);
        if(!opacityCached[index]){
            opacityValue[index] = Blocks.solid[access.getBlockId(relX+ offsetX, relY+ offsetY, relZ+ offsetZ)];
            opacityCached[index] = true;
        }
        return opacityValue[index];
    }

    public int getLightmapCoord(int relX, int relY, int relZ) {
    	int index = (relX + CACHE_RANGE) * CACHE_SIZE * CACHE_SIZE + (relY + CACHE_RANGE) * CACHE_SIZE + (relZ + CACHE_RANGE);
    	if(!lightmapCoordCached[index]) {
    		int lightEmission = block.emission;
    		lightmapCoordValue[index] = access.getLightmapCoord(relX + offsetX, relY + offsetY, relZ + offsetZ, lightEmission);
    		lightmapCoordCached[index] = true;
    	}
    	return lightmapCoordValue[index];
    }
}
