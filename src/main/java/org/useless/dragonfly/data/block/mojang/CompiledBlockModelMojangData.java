package org.useless.dragonfly.data.block.mojang;

import com.mojang.logging.LogUtils;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.useless.dragonfly.DisplayPos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.useless.dragonfly.data.block.mojang.BlockModelMojangData.BLOCKS_PER_UNIT;

public class CompiledBlockModelMojangData {
    protected static final Logger LOGGER = LogUtils.getLogger();

    public final BlockModelMojangData data;

    public final @NotNull Map<@NotNull String, @NotNull IconCoordinate> textures = new HashMap<>();
    public final @NotNull C_Element @NotNull [] elements;
    public final int @NotNull [] particleIndices;
    public final @NotNull Map<@NotNull String, @NotNull DisplayPos> displayPosMap;
    public final int renderLayer;

    public CompiledBlockModelMojangData(BlockModelMojangData data) {
        this.data = data;

        if (this.data.textures == null) {
            LOGGER.error("Model '{}' does not have assigned texture data!", data.modelId());
        } else {
            for (String key : data.textures.keySet()) {
                IconCoordinate coordinate = getTexture("#" + key, null, true);
                if (coordinate != null) textures.put("#" + key, coordinate);
            }
        }

        IconCoordinate particleAll = textures.get("#particle");

        if (particleAll != null) { // Modern mojang format support, the modern mc version only has a global particle texture symbol rather than per side
            textures.putIfAbsent("#particle_up", particleAll);
            textures.putIfAbsent("#particle_down", particleAll);
            textures.putIfAbsent("#particle_north", particleAll);
            textures.putIfAbsent("#particle_south", particleAll);
            textures.putIfAbsent("#particle_west", particleAll);
            textures.putIfAbsent("#particle_east", particleAll);
            textures.putIfAbsent("#overlay", particleAll);
        } else {
            if (textures.get("#particle_up") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_up' symbol!", data.modelId());
            if (textures.get("#particle_down") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_down' symbol!", data.modelId());
            if (textures.get("#particle_north") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_north' symbol!", data.modelId());
            if (textures.get("#particle_south") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_south' symbol!", data.modelId());
            if (textures.get("#particle_west") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_west' symbol!", data.modelId());
            if (textures.get("#particle_east") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_east' symbol!", data.modelId());
        }
        if (textures.get("#overlay") == null) LOGGER.warn("Model '{}' does not have an assigned '#overlay' symbol!", data.modelId());

        displayPosMap = new HashMap<>();
        if (data.displayPositions != null) displayPosMap.putAll(data.displayPositions);

        particleIndices = new int[6];
        if (data.particleIndices != null) {
            particleIndices[Direction.UP.getId()] = data.particleIndices.getOrDefault(Direction.UP, 0);
            particleIndices[Direction.DOWN.getId()] = data.particleIndices.getOrDefault(Direction.DOWN, 0);
            particleIndices[Direction.NORTH.getId()] = data.particleIndices.getOrDefault(Direction.NORTH, 0);
            particleIndices[Direction.SOUTH.getId()] = data.particleIndices.getOrDefault(Direction.SOUTH, 0);
            particleIndices[Direction.WEST.getId()] = data.particleIndices.getOrDefault(Direction.WEST, 0);
            particleIndices[Direction.EAST.getId()] = data.particleIndices.getOrDefault(Direction.EAST, 0);
        }

        renderLayer = data.renderLayer == null ? 0 : data.renderLayer;

        elements = new C_Element[data.elements == null ? 0 : data.elements.size()];
        if (data.elements != null) {
            int i = 0;
            for (Element e : data.elements) {
                elements[i++] = new C_Element(e);
            }
        }
    }

    protected IconCoordinate getTexture(@NotNull final String textureSymbol, @Nullable final IconCoordinate defaultCoordinate, final boolean logWarnings) {
        try {
            if (data.textures == null) {
                if (logWarnings) LOGGER.warn("Model '{}' does not have assigned texture data!", data.modelId());
                return defaultCoordinate;
            }

            @Nullable String path = data.textures.get(textureSymbol.startsWith("#") ? textureSymbol.substring(1) : textureSymbol);

            if (path == null) {
                if (logWarnings) LOGGER.warn("Texture for symbol '{}' is not assigned to any value!", textureSymbol);
                return defaultCoordinate;
            } else if (path.startsWith("#")) { // Texture variable
                return getTexture(path.substring(1), defaultCoordinate, logWarnings);
            } else {
                if (!path.contains(":")) path = "minecraft" + ":" + path;
                if (TextureRegistry.hasTexture(path)) {
                    return TextureRegistry.getTexture(path);
                } else {
                    if (logWarnings) LOGGER.warn("Texture for id '{}' does not exist!", path);
                    return BlockModelStandard.BLOCK_TEXTURE_MISSING;
                }
            }

        } catch (StackOverflowError stackOverflowError) {
            LOGGER.error("Recursive texture lookup from symbol '{}' in model '{}'!", textureSymbol, data.modelId());
            return defaultCoordinate;
        }
    }

    public static class C_Element {
        public static final int VERTEX_TOP_LEFT = 0;
        public static final int VERTEX_TOP_RIGHT = 3;
        public static final int VERTEX_BOTTOM_RIGHT = 2;
        public static final int VERTEX_BOTTOM_LEFT = 1;

        public static final int VERTEX_FLOAT_X = 0;
        public static final int VERTEX_FLOAT_Y = 1;
        public static final int VERTEX_FLOAT_Z = 2;
        public static final int VERTEX_FLOAT_U = 3;
        public static final int VERTEX_FLOAT_V = 4;
        public static final int FLOATS_PER_VERTEX = 5;

        public static final int NORMAL_FLOAT_X = 0;
        public static final int NORMAL_FLOAT_Y = 1;
        public static final int NORMAL_FLOAT_Z = 2;
        public static final int FLOATS_PER_NORMAL = 3;

        public final int lightEmission;
        public final boolean shade;

        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public final int faces;
        public final float @NotNull [] vertexData;
        public final float @NotNull [] normalData;
        public final int @NotNull [] tintIndices;
        public final @Nullable Direction @NotNull [] cullfaces;
        public final @NotNull Direction @NotNull [] directions;
        public final @NotNull String @NotNull [] textures;

        public C_Element(Element element) {
            faces = element.faces.size();
            vertexData = new float[faces * 4 * FLOATS_PER_VERTEX];
            normalData = new float[faces * FLOATS_PER_NORMAL];
            tintIndices = new int[faces];
            cullfaces = new Direction[faces];
            directions = new Direction[faces];
            textures = new String[faces];

            shade = element.shade;
            lightEmission = element.lightEmission;

            if (element.rotation != null && element.rotation.rescale) {
                float x = (45 - Math.abs(((element.rotation.angle + 360) % 360) - 45))/45f;
                float scalar = MathHelper.sqrt_float(1 + x * x);

                float cx = element.rotation.originX * BLOCKS_PER_UNIT;
                float cy = element.rotation.originY * BLOCKS_PER_UNIT;
                float cz = element.rotation.originZ * BLOCKS_PER_UNIT;

                switch (element.rotation.axis) {
                    case X:
                        minX = element.fromX * BLOCKS_PER_UNIT;
                        minY = (((element.fromY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;
                        minZ = (((element.fromZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;

                        maxX = element.toX * BLOCKS_PER_UNIT;
                        maxY = (((element.toY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;
                        maxZ = (((element.toZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;
                        break;
                    case Y:
                        minY = element.fromY * BLOCKS_PER_UNIT;
                        minX = (((element.fromX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        minZ = (((element.fromZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;

                        maxY = element.toY * BLOCKS_PER_UNIT;
                        maxX = (((element.toX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        maxZ = (((element.toZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;
                        break;
                    case Z:
                    default:
                        minZ = element.fromZ * BLOCKS_PER_UNIT;
                        minX = (((element.fromX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        minY = (((element.fromY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;

                        maxZ = element.toZ * BLOCKS_PER_UNIT;
                        maxX = (((element.toX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        maxY = (((element.toY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;
                        break;
                }
            } else {
                minX = element.fromX * BLOCKS_PER_UNIT;
                minY = element.fromY * BLOCKS_PER_UNIT;
                minZ = element.fromZ * BLOCKS_PER_UNIT;

                maxX = element.toX * BLOCKS_PER_UNIT;
                maxY = element.toY * BLOCKS_PER_UNIT;
                maxZ = element.toZ * BLOCKS_PER_UNIT;
            }



            int i = 0;
            for (Map.Entry<Direction, Face> e : element.faces.entrySet()) {
                Direction d = e.getKey();
                Face f = e.getValue();

                float minU = f.u1 * BLOCKS_PER_UNIT;
                float maxU = f.u2 * BLOCKS_PER_UNIT;
                float minV = f.v1 * BLOCKS_PER_UNIT;
                float maxV = f.v2 * BLOCKS_PER_UNIT;

                tintIndices[i] = f.tintIndex;
                cullfaces[i] = f.cullFace;
                directions[i] = d;
                textures[i] = f.texture;
                int offset = i * 4 * FLOATS_PER_VERTEX;
                int rot = 4 - f.rotation;
                switch (d) {
                    case UP:
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;
                        break;
                    case DOWN:
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;
                        break;
                    case NORTH:
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;
                        break;
                    case SOUTH:
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;
                        break;
                    case WEST:
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = minX;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;
                        break;
                    case EAST:
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_TOP_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_TOP_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;

                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = maxZ;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = minU;
                        vertexData[offset + ((VERTEX_TOP_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = maxY;
                        vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_BOTTOM_RIGHT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = minV;

                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] = maxX;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] = minY;
                        vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] = minZ;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_U] = maxU;
                        vertexData[offset + ((VERTEX_BOTTOM_LEFT + rot) % 4 * FLOATS_PER_VERTEX) + VERTEX_FLOAT_V] = maxV;
                        break;
                }
                i++;
            }

            if (element.rotation != null) {
                for (int f = 0; f < faces; f++) {
                    for (int v = 0; v < 4; v++) {
                        final int offset = f * 4 * FLOATS_PER_VERTEX + v * FLOATS_PER_VERTEX;
                        final float x = vertexData[offset + VERTEX_FLOAT_X] - element.rotation.originX * BLOCKS_PER_UNIT;
                        final float y = vertexData[offset + VERTEX_FLOAT_Y] - element.rotation.originY * BLOCKS_PER_UNIT;
                        final float z = vertexData[offset + VERTEX_FLOAT_Z] - element.rotation.originZ * BLOCKS_PER_UNIT;
                        final float nx;
                        final float ny;
                        final float nz;
                        final float sin = MathHelper.sin(MathHelper.toRadians(element.rotation.angle));
                        final float cos = MathHelper.cos(MathHelper.toRadians(element.rotation.angle));
                        switch (element.rotation.axis) {
                            case X:
                                nx = x;
                                ny = y * cos - z * sin;
                                nz = z * cos + y * sin;
                                break;
                            case Y:
                                nx = x * cos - z * sin;
                                ny = y;
                                nz = z * cos + x * sin;
                                break;
                            case Z:
                            default:
                                nx = x * cos - y * sin;
                                ny = y * cos + x * sin;
                                nz = z;
                                break;
                        }
                        vertexData[offset + VERTEX_FLOAT_X] = nx + element.rotation.originX * BLOCKS_PER_UNIT;
                        vertexData[offset + VERTEX_FLOAT_Y] = ny + element.rotation.originY * BLOCKS_PER_UNIT;
                        vertexData[offset + VERTEX_FLOAT_Z] = nz + element.rotation.originZ * BLOCKS_PER_UNIT;
                    }
                }
            }

            for (int f = 0; f < faces; f++) {
                int offset = f * 4 * FLOATS_PER_VERTEX;
                float ax = vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] - vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X];
                float ay = vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] - vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y];
                float az = vertexData[offset + (VERTEX_BOTTOM_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] - vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z];

                float bx = vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X] - vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_X];
                float by = vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y] - vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Y];
                float bz = vertexData[offset + (VERTEX_TOP_RIGHT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z] - vertexData[offset + (VERTEX_BOTTOM_LEFT * FLOATS_PER_VERTEX) + VERTEX_FLOAT_Z];

                float nx = (ay * bz) - (az * by);
                float ny = (az * bx) - (ax * bz);
                float nz = (ax * by) - (ay * bx);

                float length = MathHelper.sqrt_float(nx * nx + ny * ny + nz * nz);

                nx /= length;
                ny /= length;
                nz /= length;

                normalData[f * FLOATS_PER_NORMAL + NORMAL_FLOAT_X] = nx;
                normalData[f * FLOATS_PER_NORMAL + NORMAL_FLOAT_Y] = ny;
                normalData[f * FLOATS_PER_NORMAL + NORMAL_FLOAT_Z] = nz;
            }
        }
    }
}
