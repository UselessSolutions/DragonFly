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
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.slf4j.Logger;
import org.useless.dragonfly.DisplayPos;

import java.util.HashMap;
import java.util.Map;

import static org.useless.dragonfly.data.block.mojang.BlockModelMojangData.BLOCKS_PER_UNIT;

public class CompiledBlockModelMojangData {
    protected static final Logger LOGGER = LogUtils.getLogger();

    public final BlockModelMojangData data;

    public final @NotNull Map<@NotNull String, IconCoordinate> textures = new HashMap<>();
    public final @NotNull C_Element @NotNull [] elements;
    public final int @NotNull [] particleIndices;
    public final @NotNull Map<@NotNull String, DisplayPos> displayPosMap;
    public final int renderLayer;

    public CompiledBlockModelMojangData(BlockModelMojangData data) {
        this.data = data;

        if (this.data.textures == null) {
            LOGGER.error("Model '{}' does not have assigned texture data!", data.modelId());
        } else {
            for (String key : data.textures.keySet()) {
                IconCoordinate coordinate = getTexture("#" + key, null, true);
                if (coordinate != null) this.textures.put("#" + key, coordinate);
            }
        }

        IconCoordinate particleAll = this.textures.get("#particle");

        if (particleAll != null) { // Modern mojang format support, the modern mc version only has a global particle texture symbol rather than per side
            this.textures.putIfAbsent("#particle_up", particleAll);
            this.textures.putIfAbsent("#particle_down", particleAll);
            this.textures.putIfAbsent("#particle_north", particleAll);
            this.textures.putIfAbsent("#particle_south", particleAll);
            this.textures.putIfAbsent("#particle_west", particleAll);
            this.textures.putIfAbsent("#particle_east", particleAll);
            this.textures.putIfAbsent("#overlay", particleAll);
        } else {
            if (this.textures.get("#particle_up") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_up' symbol!", data.modelId());
            if (this.textures.get("#particle_down") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_down' symbol!", data.modelId());
            if (this.textures.get("#particle_north") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_north' symbol!", data.modelId());
            if (this.textures.get("#particle_south") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_south' symbol!", data.modelId());
            if (this.textures.get("#particle_west") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_west' symbol!", data.modelId());
            if (this.textures.get("#particle_east") == null) LOGGER.warn("Model '{}' does not have an assigned '#particle_east' symbol!", data.modelId());
        }
        if (this.textures.get("#overlay") == null) LOGGER.warn("Model '{}' does not have an assigned '#overlay' symbol!", data.modelId());

        this.displayPosMap = new HashMap<>();
        if (data.displayPositions != null) this.displayPosMap.putAll(data.displayPositions);

        this.particleIndices = new int[6];
        if (data.particleIndices != null) {
            this.particleIndices[Direction.UP.getId()] = data.particleIndices.getOrDefault(Direction.UP, 0);
            this.particleIndices[Direction.DOWN.getId()] = data.particleIndices.getOrDefault(Direction.DOWN, 0);
            this.particleIndices[Direction.NORTH.getId()] = data.particleIndices.getOrDefault(Direction.NORTH, 0);
            this.particleIndices[Direction.SOUTH.getId()] = data.particleIndices.getOrDefault(Direction.SOUTH, 0);
            this.particleIndices[Direction.WEST.getId()] = data.particleIndices.getOrDefault(Direction.WEST, 0);
            this.particleIndices[Direction.EAST.getId()] = data.particleIndices.getOrDefault(Direction.EAST, 0);
        }

        this.renderLayer = data.renderLayer == null ? 0 : data.renderLayer;

        this.elements = new C_Element[data.elements == null ? 0 : data.elements.size()];
        if (data.elements != null) {
            int i = 0;
            for (Element e : data.elements) {
                this.elements[i] = new C_Element(this, e);
                i++;
            }
        }
    }

    protected IconCoordinate getTexture(@NotNull final String textureSymbol, @Nullable final IconCoordinate defaultCoordinate, final boolean logWarnings) {
        try {
            if (this.data.textures == null) {
                if (logWarnings) LOGGER.warn("Model '{}' does not have assigned texture data!", this.data.modelId());
                return defaultCoordinate;
            }

            @Nullable String path = this.data.textures.get(textureSymbol.startsWith("#") ? textureSymbol.substring(1) : textureSymbol);

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
            LOGGER.error("Recursive texture lookup from symbol '{}' in model '{}'!", textureSymbol, this.data.modelId());
            return defaultCoordinate;
        }
    }

    public static class C_Element {
        public static final int VERTEX_TOP_LEFT = 0;
        public static final int VERTEX_TOP_RIGHT = 3;
        public static final int VERTEX_BOTTOM_RIGHT = 2;
        public static final int VERTEX_BOTTOM_LEFT = 1;

        public final int lightEmission;
        public final boolean shade;
        public final boolean flip;

        public final float minX;
        public final float minY;
        public final float minZ;
        public final float maxX;
        public final float maxY;
        public final float maxZ;

        public final int faces;
        public final @NotNull Element sourceElement;
        public final @NotNull Vector3dc @NotNull [] vertexPoses;
        public final @NotNull Vector2fc @NotNull [] vertexUvs;
        public final @NotNull Vector3fc @NotNull [] faceNormals;
        public final @NotNull Vector3fc @NotNull [] faceUps;
        public final @NotNull Vector3fc @NotNull [] faceLefts;
        public final int @NotNull [] tintIndices;
        public final @Nullable Direction @NotNull [] cullfaces;
        public final @NotNull Direction @NotNull [] directions;
        public final @NotNull IconCoordinate @NotNull [] textures;

        public C_Element(@NotNull CompiledBlockModelMojangData data, @NotNull Element element) {
            this.faces = element.faces.size();
            this.sourceElement = element;
            this.vertexPoses = new Vector3dc[this.faces * 4];
            this.vertexUvs = new Vector2fc[this.faces * 4];
            this.faceNormals = new Vector3fc[this.faces];
            this.faceUps = new Vector3fc[this.faces];
            this.faceLefts = new Vector3fc[this.faces];
            this.tintIndices = new int[this.faces];
            this.cullfaces = new Direction[this.faces];
            this.directions = new Direction[this.faces];
            this.textures = new IconCoordinate[this.faces];

            this.shade = element.shade;
            this.lightEmission = element.lightEmission;

            int negs = 0;
            if (element.fromX > element.toX) negs++;
            if (element.fromY > element.toY) negs++;
            if (element.fromZ > element.toZ) negs++;
            this.flip = (negs & 0b1) != 0;

            if (element.rotation != null && element.rotation.rescale) {
                float x = (45 - Math.abs(((element.rotation.angle + 90) % 90) - 45))/45f;
                float scalar = MathHelper.sqrt_float(1 + x * x);

                float cx = element.rotation.originX * BLOCKS_PER_UNIT;
                float cy = element.rotation.originY * BLOCKS_PER_UNIT;
                float cz = element.rotation.originZ * BLOCKS_PER_UNIT;

                switch (element.rotation.axis) {
                    case X:
                        this.minX = element.fromX * BLOCKS_PER_UNIT;
                        this.minY = (((element.fromY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;
                        this.minZ = (((element.fromZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;

                        this.maxX = element.toX * BLOCKS_PER_UNIT;
                        this.maxY = (((element.toY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;
                        this.maxZ = (((element.toZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;
                        break;
                    case Y:
                        this.minY = element.fromY * BLOCKS_PER_UNIT;
                        this.minX = (((element.fromX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        this.minZ = (((element.fromZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;

                        this.maxY = element.toY * BLOCKS_PER_UNIT;
                        this.maxX = (((element.toX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        this.maxZ = (((element.toZ * BLOCKS_PER_UNIT) - cz) * scalar) + cz;
                        break;
                    case Z:
                    default:
                        this.minZ = element.fromZ * BLOCKS_PER_UNIT;
                        this.minX = (((element.fromX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        this.minY = (((element.fromY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;

                        this.maxZ = element.toZ * BLOCKS_PER_UNIT;
                        this.maxX = (((element.toX * BLOCKS_PER_UNIT) - cx) * scalar) + cx;
                        this.maxY = (((element.toY * BLOCKS_PER_UNIT) - cy) * scalar) + cy;
                        break;
                }
            } else {
                this.minX = element.fromX * BLOCKS_PER_UNIT;
                this.minY = element.fromY * BLOCKS_PER_UNIT;
                this.minZ = element.fromZ * BLOCKS_PER_UNIT;

                this.maxX = element.toX * BLOCKS_PER_UNIT;
                this.maxY = element.toY * BLOCKS_PER_UNIT;
                this.maxZ = element.toZ * BLOCKS_PER_UNIT;
            }



            int i = 0;
            for (Map.Entry<Direction, Face> e : element.faces.entrySet()) {
                Direction d = e.getKey();
                Face f = e.getValue();

                float minU = f.u1 * BLOCKS_PER_UNIT;
                float maxU = f.u2 * BLOCKS_PER_UNIT;
                float minV = f.v1 * BLOCKS_PER_UNIT;
                float maxV = f.v2 * BLOCKS_PER_UNIT;

                this.tintIndices[i] = f.tintIndex;
                this.cullfaces[i] = f.cullFace;
                this.directions[i] = d;
                this.textures[i] = data.textures.getOrDefault(f.texture, BlockModelStandard.BLOCK_TEXTURE_UNASSIGNED);
                int vertexIndex = i * 4;
                int rot = 4 - (f.rotation / 90);
                switch (d) {
                    case UP:
                        this.faceUps[i] = new Vector3f(0, 0, 1);
                        this.faceLefts[i] = new Vector3f(1, 0, 0);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_LEFT] = new Vector3d(this.maxX, this.maxY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_LEFT + rot) & 0b11)] = new Vector2f(maxU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_RIGHT] = new Vector3d(this.minX, this.maxY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_RIGHT + rot) & 0b11)] = new Vector2f(minU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_RIGHT] = new Vector3d(this.minX, this.maxY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_RIGHT + rot) & 0b11)] = new Vector2f(minU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_LEFT] = new Vector3d(this.maxX, this.maxY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_LEFT + rot) & 0b11)] = new Vector2f(maxU, minV);
                        break;
                    case DOWN:
                        this.faceUps[i] = new Vector3f(0, 0, 1);
                        this.faceLefts[i] = new Vector3f(-1, 0, 0);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_LEFT] = new Vector3d(this.minX, this.minY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_LEFT + rot) & 0b11)] = new Vector2f(minU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_RIGHT] = new Vector3d(this.maxX, this.minY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_RIGHT + rot) & 0b11)] = new Vector2f(maxU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_RIGHT] = new Vector3d(this.maxX, this.minY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_RIGHT + rot) & 0b11)] = new Vector2f(maxU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_LEFT] = new Vector3d(this.minX, this.minY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_LEFT + rot) & 0b11)] = new Vector2f(minU, maxV);
                        break;
                    case NORTH:
                        this.faceUps[i] = new Vector3f(-1, 0, 0);
                        this.faceLefts[i] = new Vector3f(0, 1, 0);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_LEFT] = new Vector3d(this.minX, this.maxY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_LEFT + rot) & 0b11)] = new Vector2f(maxU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_RIGHT] = new Vector3d(this.minX, this.minY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_RIGHT + rot) & 0b11)] = new Vector2f(maxU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_RIGHT] = new Vector3d(this.maxX, this.minY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_RIGHT + rot) & 0b11)] = new Vector2f(minU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_LEFT] = new Vector3d(this.maxX, this.maxY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_LEFT + rot) & 0b11)] = new Vector2f(minU, minV);
                        break;
                    case SOUTH:
                        this.faceUps[i] = new Vector3f(0, 1, 0);
                        this.faceLefts[i] = new Vector3f(-1, 0, 0);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_LEFT] = new Vector3d(this.minX, this.maxY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_LEFT + rot) & 0b11)] = new Vector2f(minU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_RIGHT] = new Vector3d(this.maxX, this.maxY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_RIGHT + rot) & 0b11)] = new Vector2f(maxU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_RIGHT] = new Vector3d(this.maxX, this.minY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_RIGHT + rot) & 0b11)] = new Vector2f(maxU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_LEFT] = new Vector3d(this.minX, this.minY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_LEFT + rot) & 0b11)] = new Vector2f(minU, maxV);
                        break;
                    case WEST:
                        this.faceUps[i] = new Vector3f(0, 0, 1);
                        this.faceLefts[i] = new Vector3f(0, 1, 0);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_LEFT] = new Vector3d(this.minX, this.maxY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_LEFT + rot) & 0b11)] = new Vector2f(maxU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_RIGHT] = new Vector3d(this.minX, this.minY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_RIGHT + rot) & 0b11)] = new Vector2f(maxU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_RIGHT] = new Vector3d(this.minX, this.minY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_RIGHT + rot) & 0b11)] = new Vector2f(minU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_LEFT] = new Vector3d(this.minX, this.maxY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_LEFT + rot) & 0b11)] = new Vector2f(minU, minV);
                        break;
                    case EAST:
                        this.faceUps[i] = new Vector3f(0, 0, 1);
                        this.faceLefts[i] = new Vector3f(0, -1, 0);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_LEFT] = new Vector3d(this.maxX, this.minY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_LEFT + rot) & 0b11)] = new Vector2f(minU, maxV);

                        this.vertexPoses[vertexIndex + VERTEX_TOP_RIGHT] = new Vector3d(this.maxX, this.maxY, this.maxZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_TOP_RIGHT + rot) & 0b11)] = new Vector2f(minU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_RIGHT] = new Vector3d(this.maxX, this.maxY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_RIGHT + rot) & 0b11)] = new Vector2f(maxU, minV);

                        this.vertexPoses[vertexIndex + VERTEX_BOTTOM_LEFT] = new Vector3d(this.maxX, this.minY, this.minZ);
                        this.vertexUvs[vertexIndex + ((VERTEX_BOTTOM_LEFT + rot) & 0b11)] = new Vector2f(maxU, maxV);
                        break;
                }
                i++;
            }

            for (int f = 0; f < this.faces; f++) {
                int offset = f * 4;
                Vector3dc br = this.vertexPoses[offset + VERTEX_BOTTOM_RIGHT];
                Vector3dc bl = this.vertexPoses[offset + VERTEX_BOTTOM_LEFT];
                Vector3dc tr = this.vertexPoses[offset + VERTEX_TOP_RIGHT];

                Vector3d a = br.sub(bl, new Vector3d());
                Vector3d b = tr.sub(bl, new Vector3d());

                this.faceNormals[f] = new Vector3f(a.cross(b, new Vector3d())).normalize();
            }

            if (element.rotation != null) {
                for (int f = 0; f < this.faces; f++) {
                    for (int v = 0; v < 4; v++) {
                        final int offset = f * 4 + v;
                        switch (element.rotation.axis) {
                            case X:
                                this.vertexPoses[offset] = this.vertexPoses[offset]
                                    .sub(element.rotation.originX * BLOCKS_PER_UNIT, element.rotation.originY * BLOCKS_PER_UNIT, element.rotation.originZ * BLOCKS_PER_UNIT, new Vector3d())
                                    .rotateX(MathHelper.toRadians(element.rotation.angle))
                                    .add(element.rotation.originX * BLOCKS_PER_UNIT, element.rotation.originY * BLOCKS_PER_UNIT, element.rotation.originZ * BLOCKS_PER_UNIT);
                                break;
                            case Y:
                                this.vertexPoses[offset] = this.vertexPoses[offset]
                                    .sub(element.rotation.originX * BLOCKS_PER_UNIT, element.rotation.originY * BLOCKS_PER_UNIT, element.rotation.originZ * BLOCKS_PER_UNIT, new Vector3d())
                                    .rotateY(MathHelper.toRadians(element.rotation.angle))
                                    .add(element.rotation.originX * BLOCKS_PER_UNIT, element.rotation.originY * BLOCKS_PER_UNIT, element.rotation.originZ * BLOCKS_PER_UNIT);
                                break;
                            case Z:
                            default:
                                this.vertexPoses[offset] = this.vertexPoses[offset]
                                    .sub(element.rotation.originX * BLOCKS_PER_UNIT, element.rotation.originY * BLOCKS_PER_UNIT, element.rotation.originZ * BLOCKS_PER_UNIT, new Vector3d())
                                    .rotateZ(MathHelper.toRadians(element.rotation.angle))
                                    .add(element.rotation.originX * BLOCKS_PER_UNIT, element.rotation.originY * BLOCKS_PER_UNIT, element.rotation.originZ * BLOCKS_PER_UNIT);
                                break;
                        }
                    }

					switch (element.rotation.axis) {
						case X:
							this.faceUps[f] = this.faceUps[f].rotateX(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							this.faceLefts[f] = this.faceLefts[f].rotateX(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							this.faceNormals[f] = this.faceNormals[f].rotateX(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							break;
						case Y:
							this.faceUps[f] = this.faceUps[f].rotateY(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							this.faceLefts[f] = this.faceLefts[f].rotateY(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							this.faceNormals[f] = this.faceNormals[f].rotateY(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							break;
						case Z:
							this.faceUps[f] = this.faceUps[f].rotateZ(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							this.faceLefts[f] = this.faceLefts[f].rotateZ(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							this.faceNormals[f] = this.faceNormals[f].rotateZ(org.joml.Math.toRadians(element.rotation.angle), new Vector3f());
							break;
					}
                }
            }
        }
    }
}
