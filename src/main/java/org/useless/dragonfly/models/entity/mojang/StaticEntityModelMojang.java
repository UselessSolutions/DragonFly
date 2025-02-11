package org.useless.dragonfly.models.entity.mojang;

import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.data.entity.mojang.Bone;
import org.useless.dragonfly.data.entity.mojang.Cube;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.data.entity.mojang.Face;
import org.useless.dragonfly.models.entity.BoneTransform;
import org.useless.dragonfly.models.entity.StaticEntityModel;

import java.util.HashMap;
import java.util.Map;

public class StaticEntityModelMojang implements StaticEntityModel {
    private static final BoneTransform DUMMY_TRANSFORM = new BoneTransform();
    public final @NotNull Map<@NotNull String, @NotNull BoneTransform> transformMap = new HashMap<>();
    public final @NotNull BoneTransform @NotNull [] transforms;
    public final @NotNull Bone @NotNull [] bones;
    public final EntityGeometryMojangData data;
    public final double inflation;
    protected final AABB visBounds;
    public StaticEntityModelMojang(final @NotNull EntityGeometryMojangData data, final double inflation) {
        this.data = data;
        this.transforms = new BoneTransform[this.data.bones.size()];
        this.bones = new Bone[this.data.bones.size()];
        this.inflation = inflation;

        int i = 0;
        for (final Map.Entry<String, Bone> entry : this.data.bones.entrySet()) {
            final BoneTransform transform = new BoneTransform();
            this.transformMap.put(entry.getKey(), transform);
            this.transforms[i] = transform;
            this.bones[i] = entry.getValue();
            i++;
        }

        this.visBounds = AABB.getPermanentBB(
            (this.data.vBoundsOffset[0] - this.data.vBoundsWidth /2),
            (this.data.vBoundsOffset[1] - this.data.vBoundsHeight/2),
            (this.data.vBoundsOffset[2] - this.data.vBoundsWidth /2),
            (this.data.vBoundsOffset[0] + this.data.vBoundsWidth /2),
            (this.data.vBoundsOffset[1] + this.data.vBoundsHeight/2),
            (this.data.vBoundsOffset[2] + this.data.vBoundsWidth /2));
    }

    @Override
    public void resetBones() {
        for (int i = 0; i < this.transforms.length; i++) {
            this.transforms[i].reset();
        }
    }

    @Override
    public @NotNull BoneTransform getTransform(@NotNull final String boneId) {
        return this.transformMap.getOrDefault(boneId, DUMMY_TRANSFORM);
    }

    @Override
    public @NotNull AABB visibleBounds() {
        return this.visBounds;
    }

    @Override
    public void translateToBone(@NotNull final String boneId) {
        final Bone bone = this.data.bones.get(boneId);
        if (bone == null) return;
        boneTransform(bone);
        GL11.glTranslated(bone.pivot[0], bone.pivot[1], bone.pivot[2]);
    }

    @Override
    public void render(@NotNull final Tessellator tessellator) {
        for (int i = 0; i < this.bones.length; i++) {
            final Bone bone = this.bones[i];
            GL11.glPushMatrix();
            if (!boneTransform(bone)) {
                renderBone(tessellator, bone);
            }
            GL11.glPopMatrix();
        }
    }

    protected boolean boneTransform(@NotNull final Bone bone) {
        boolean hidden = false;
        final Bone parent = bone.parent != null ? this.data.bones.get(bone.parent) : null;
        if (parent != null) {
            hidden |= boneTransform(parent);
        }
        final BoneTransform transform = this.transformMap.get(bone.name);
        hidden |= !transform.visible;
        GL11.glTranslated(bone.pivot[0], bone.pivot[1], bone.pivot[2]);
        GL11.glTranslated(transform.posX, transform.posY, transform.posZ);
        GL11.glRotated(-(transform.rotZ * MathHelper.RAD_TO_DEG) - bone.rotation[2], 0, 0, 1);
        GL11.glRotated((transform.rotY * MathHelper.RAD_TO_DEG) + bone.rotation[1], 0, 1, 0);
        GL11.glRotated(-(transform.rotX * MathHelper.RAD_TO_DEG) - bone.rotation[0], 1, 0, 0);
        GL11.glTranslated(-bone.pivot[0], -bone.pivot[1], -bone.pivot[2]);
        GL11.glScaled(transform.scaleX, transform.scaleY, transform.scaleZ);
        return hidden;
    }

    private final double[] uvsBuffer = new double[8];
    protected void renderBone(@NotNull final Tessellator tessellator, @NotNull final Bone bone) {
        tessellator.startDrawingQuads();
        for (final Cube cube : bone.cubes) {
            final double minX = cube.origin[0] - (cube.inflate + bone.inflate + this.inflation);
            final double minY = cube.origin[1] - (cube.inflate + bone.inflate + this.inflation);
            final double minZ = cube.origin[2] - (cube.inflate + bone.inflate + this.inflation);

            final double maxX = minX + cube.size[0] + (cube.inflate + bone.inflate + this.inflation) * 2;
            final double maxY = minY + cube.size[1] + (cube.inflate + bone.inflate + this.inflation) * 2;
            final double maxZ = minZ + cube.size[2] + (cube.inflate + bone.inflate + this.inflation) * 2;
            
            if (cube.rotation == null) {
                // Top
                Face face = cube.faces[Direction.UP.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMin;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMax;

                    this.uvsBuffer[1] = vMin;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMax;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(0, 1, 0);
                    tessellator.addVertexWithUV(minX, maxY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, maxY, minZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, maxY, maxZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8]);
                }

                // Bottom
                face = cube.faces[Direction.DOWN.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(0, -1, 0);
                    tessellator.addVertexWithUV(minX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, minY, maxZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, minY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, minY, minZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8]);
                }

                // Front
                face = cube.faces[Direction.NORTH.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(0, 0, -1);
                    tessellator.addVertexWithUV(minX, minY, minZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, minY, minZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, maxY, minZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8]);
                }

                // Back
                face = cube.faces[Direction.SOUTH.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMax;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMin;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMin;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMax;

                    tessellator.setNormal(0, 0, 1);
                    tessellator.addVertexWithUV(minX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, maxY, maxZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, maxY, maxZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, minY, maxZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8]);
                }

                // Right
                face = cube.faces[Direction.WEST.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMax;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMin;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMin;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMax;

                    tessellator.setNormal(1, 0, 0);
                    tessellator.addVertexWithUV(maxX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, maxY, maxZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(maxX, minY, minZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8]);
                }

                // Left
                face = cube.faces[Direction.EAST.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(-1, 0, 0);
                    tessellator.addVertexWithUV(minX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, minY, minZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8]);
                    tessellator.addVertexWithUV(minX, maxY, maxZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8]);
                }
            }
            else {
                // Top
                Face face = cube.faces[Direction.UP.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMin;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMax;

                    this.uvsBuffer[1] = vMin;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMax;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(0, 1, 0);
                    addRotatedVertex(tessellator, minX, maxY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, maxY, minZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, maxY, maxZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);

                }

                // Bottom
                face = cube.faces[Direction.DOWN.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(0, -1, 0);
                    addRotatedVertex(tessellator, minX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, minY, maxZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, minY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, minY, minZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                }

                // Front
                face = cube.faces[Direction.NORTH.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(0, 0, -1);
                    addRotatedVertex(tessellator, minX, minY, minZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, minY, minZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, maxY, minZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                }

                // Back
                face = cube.faces[Direction.SOUTH.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMax;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMin;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMin;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMax;

                    tessellator.setNormal(0, 0, 1);
                    addRotatedVertex(tessellator, minX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, maxY, maxZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, maxY, maxZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, minY, maxZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                }

                // Right
                face = cube.faces[Direction.WEST.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMax;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMin;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMin;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMax;

                    tessellator.setNormal(1, 0, 0);
                    addRotatedVertex(tessellator, maxX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, maxY, maxZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, maxX, minY, minZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                }

                // Left
                face = cube.faces[Direction.EAST.getId()];
                if (face != null) {
                    final double uMin = (face.uv[0]) / this.data.textureWidth;
                    final double uMax = (face.uv[0] + face.uv_size[0]) / this.data.textureWidth;
                    final double vMin = (face.uv[1]) / this.data.textureHeight;
                    final double vMax = (face.uv[1] + face.uv_size[1])  / this.data.textureHeight;

                    this.uvsBuffer[0] = uMin;
                    this.uvsBuffer[2] = uMax;
                    this.uvsBuffer[4] = uMax;
                    this.uvsBuffer[6] = uMin;

                    this.uvsBuffer[1] = vMax;
                    this.uvsBuffer[3] = vMax;
                    this.uvsBuffer[5] = vMin;
                    this.uvsBuffer[7] = vMin;

                    tessellator.setNormal(-1, 0, 0);
                    addRotatedVertex(tessellator, minX, minY, maxZ, this.uvsBuffer[(face.uv_rotation * 2) % 8], this.uvsBuffer[(1 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, minY, minZ, this.uvsBuffer[(2 + face.uv_rotation * 2) % 8], this.uvsBuffer[(3 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, maxY, minZ, this.uvsBuffer[(4 + face.uv_rotation * 2) % 8], this.uvsBuffer[(5 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                    addRotatedVertex(tessellator, minX, maxY, maxZ, this.uvsBuffer[(6 + face.uv_rotation * 2) % 8], this.uvsBuffer[(7 + face.uv_rotation * 2) % 8], cube.pivot, cube.rotation);
                }
            }
        }
        tessellator.draw();
    }

    protected static void addRotatedVertex(@NotNull final Tessellator tessellator, final double x, final double y, final double z, final double u, final double v, final double @NotNull [] pivot, final double @NotNull [] rotation) {
        double _x = x - pivot[0];
        double _y = y - pivot[1];
        double _z = z - pivot[2];

        float sin;
        float cos;
        double nx;
        double ny;
        double nz;

        // X
        sin = MathHelper.sin((float) (-rotation[0] * MathHelper.DEG_TO_RAD));
        cos = MathHelper.cos((float) (-rotation[0] * MathHelper.DEG_TO_RAD));
        nx = _x;
        ny = _y * cos - _z * sin;
        nz = _z * cos + _y * sin;
        _x = nx;
        _y = ny;
        _z = nz;

        // Y
        sin = MathHelper.sin((float) (-rotation[1] * MathHelper.DEG_TO_RAD));
        cos = MathHelper.cos((float) (-rotation[1] * MathHelper.DEG_TO_RAD));
        nx = _x * cos - _z * sin;
        ny = _y;
        nz = _z * cos + _x * sin;
        _x = nx;
        _y = ny;
        _z = nz;

        // Z
        sin = MathHelper.sin((float) (-rotation[2] * MathHelper.DEG_TO_RAD));
        cos = MathHelper.cos((float) (-rotation[2] * MathHelper.DEG_TO_RAD));
        nx = _x * cos - _y * sin;
        ny = _y * cos + _x * sin;
        nz = _z;
        _x = nx;
        _y = ny;
        _z = nz;


        tessellator.addVertexWithUV(_x + pivot[0], _y + pivot[1], _z + pivot[2], u, v);
    }
}
