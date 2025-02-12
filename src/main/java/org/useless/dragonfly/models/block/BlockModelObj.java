package org.useless.dragonfly.models.block;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockModelObj<T extends BlockLogic> extends BlockModelStandard<T> {
    protected final ObjModel model;
    public BlockModelObj(final Block<T> block, final InputStream objStream) {
        super(block);
        try {
            this.model = new ObjModel(Objects.requireNonNull(objStream));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean render(@NotNull final Tessellator tessellator/*, @NotNull final WorldSource worldSource*/, final int x, final int y, final int z) {
        tessellator.setColorRGBA(255, 255, 255, 255);
        final IconCoordinate iconCoordinate = getBlockTextureFromSideAndMetadata(Side.NORTH, renderBlocks.blockAccess.getBlockMetadata(x, y, z));
        if (iconCoordinate == null) return false;
        ObjModel.ObjComponent component = model.component;
        for (int f = 0; f < component.f.length; f++) {
            final int count = component.f[f];
            if (count == 3) {
                for (int i = 0; i < 3; i++) {
                    final int v = component.f[f + i * 3 + 1] -1;
                    final int vt = component.f[f + i * 3 + 2] -1;
                    final int vn = component.f[f + i * 3 + 3] -1;

                    tessellator.addVertexWithUV(component.v[v * 4] + x + 0.5f, component.v[v * 4 + 1] + y, component.v[v * 4 + 2] + z + 0.5f, iconCoordinate.getSubIconU(component.vt[vt * 3]), iconCoordinate.getSubIconV(1 - component.vt[vt * 3 + 1]));
                    if (i == 2) {
                        tessellator.addVertexWithUV(component.v[v * 4] + x + 0.5f, component.v[v * 4 + 1] + y, component.v[v * 4 + 2] + z + 0.5f, iconCoordinate.getSubIconU(component.vt[vt * 3]), iconCoordinate.getSubIconV(1 - component.vt[vt * 3 + 1])); // Tri to quad hack :wolfblock:
                    }
                }
            } else if (count == 4) {
				for (int i = 0; i < 4; i++) {
                    final int v = component.f[f + i * 3 + 1] -1;
                    final int vt = component.f[f + i * 3 + 2] -1;
                    final int vn = component.f[f + i * 3 + 3] -1;

                    tessellator.addVertexWithUV(component.v[v * 4] + x + 0.5f, component.v[v * 4 + 1] + y, component.v[v * 4 + 2] + z + 0.5f, iconCoordinate.getSubIconU(component.vt[vt * 3]), iconCoordinate.getSubIconV(1 - component.vt[vt * 3 + 1]));
                }
            } else {
//                throw new RuntimeException("Unhandled face count '" + count + "'!");
			}
			f += count * 3;
		}
        return true;
    }

    @Override
    public void /*renderStandalone*/renderBlockOnInventory(@NotNull final Tessellator tessellator, final int metadata, final float brightness, final float alpha, @Nullable final Integer lightmapCoordinate) {
        final IconCoordinate iconCoordinate = getBlockTextureFromSideAndMetadata(Side.NORTH, metadata);
        if (iconCoordinate == null) return;
        boolean drawing = false;
        ObjModel.ObjComponent component = model.component;
        for (int f = 0; f < component.f.length; f++) {
            final int count = component.f[f];
            if (count == 3) {
                for (int i = 0; i < 3; i++) {
                    final int v = component.f[f + i * 3 + 1] - 1;
                    final int vt = component.f[f + i * 3 + 2] - 1;
                    final int vn = component.f[f + i * 3 + 3] - 1;

                    if (!drawing) {
                        tessellator.startDrawing(GL11.GL_TRIANGLES);
                        tessellator.setColorRGBA(255, 255, 255, 255);
                        drawing = true;
                    }
                    if (vn >= 0) tessellator.setNormal(component.v[vn * 3], component.v[vn * 3 + 1], component.v[vn * 3 + 2]);
                    tessellator.addVertexWithUV(component.v[v * 4] + 0.5f, component.v[v * 4 + 1], component.v[v * 4 + 2] + 0.5f, iconCoordinate.getSubIconU(component.vt[vt * 3]), iconCoordinate.getSubIconV(1 - component.vt[vt * 3 + 1]));
                }
            } else if (count == 4) {
				for (int i = 0; i < 4; i++) {
                    final int v = component.f[f + i * 3 + 1] - 1;
                    final int vt = component.f[f + i * 3 + 2] - 1;
                    final int vn = component.f[f + i * 3 + 3] - 1;

                    if (!drawing) {
                        tessellator.startDrawingQuads();
                        tessellator.setColorRGBA(255, 255, 255, 255);
                        drawing = true;
                    }
                    if (vn >= 0) tessellator.setNormal(component.v[vn * 3], component.v[vn * 3 + 1], component.v[vn * 3 + 2]);
                    tessellator.addVertexWithUV(component.v[v * 4] + 0.5f, component.v[v * 4 + 1], component.v[v * 4 + 2] + 0.5f, iconCoordinate.getSubIconU(component.vt[vt * 3]), iconCoordinate.getSubIconV(1 - component.vt[vt * 3 + 1]));
                }
            } else {
//                throw new RuntimeException("Unhandled face count '" + count + "'!");
            }
			f += count * 3;
        }
        tessellator.draw();
    }

    public static class ObjModel {
        public static int NULL = 0;
        public ObjComponent component;
        public ObjModel(@NotNull final InputStream stream) throws IOException {
            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                final List<Float> vList = new ArrayList<>(128);
                final List<Float> vtList = new ArrayList<>(128);
                final List<Float> vnList = new ArrayList<>(128);
                final List<Integer> fList = new ArrayList<>(128);
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) continue;
                    final String[] split = line.replace("  ", " ").replace("   ", " ").split("\\s");
                    if (split.length == 0) continue;

                    try {
						if ("v".equalsIgnoreCase(split[0])) {
							final float x = Float.parseFloat(split[1]);
							final float y = Float.parseFloat(split[2]);
							final float z = Float.parseFloat(split[3]);
							final float w = split.length != 5 ? 1 : Float.parseFloat(split[4]);
							vList.add(x);
							vList.add(y);
							vList.add(z);
							vList.add(w);
						} else if ("vt".equalsIgnoreCase(split[0])) {
							final float u = Float.parseFloat(split[1]);
							final float v = split.length != 3 ? 0 : Float.parseFloat(split[2]);
							final float w = split.length != 4 ? 0 : Float.parseFloat(split[3]);
							vtList.add(u);
							vtList.add(v);
							vtList.add(w);
						} else if ("vn".equalsIgnoreCase(split[0])) {
							final float x = Float.parseFloat(split[1]);
							final float y = Float.parseFloat(split[2]);
							final float z = Float.parseFloat(split[3]);
							vnList.add(x);
							vnList.add(y);
							vnList.add(z);
						} else if ("f".equalsIgnoreCase(split[0])) {
							final int count = split.length - 1;
							fList.add(count); // header

							for (int i = 1; i < split.length; i++) {
								final String point = split[i];

								int v = NULL;
								int vt = NULL;
								int vn = NULL;
								if (point.contains("//")) {
									final String[] fragments = point.split("//");
									if (fragments.length == 2) {
										v = Integer.parseInt(fragments[0]);
										vn = Integer.parseInt(fragments[1]);
									} else {
										throw new RuntimeException("Unexpected line '" + line + "'!");
									}
								} else if (point.contains("/")) {
									final String[] fragments = point.split("/");
									if (fragments.length == 2) {
										v = Integer.parseInt(fragments[0]);
										vt = Integer.parseInt(fragments[1]);
									} else if (fragments.length == 3) {
										v = Integer.parseInt(fragments[0]);
										vt = Integer.parseInt(fragments[1]);
										vn = Integer.parseInt(fragments[2]);
									} else {
										throw new RuntimeException("Unexpected line '" + line + "'!");
									}
								} else {
									v = Integer.parseInt(point);
								}

								fList.add(v);
								fList.add(vt);
								fList.add(vn);
							}

						}
					} catch (Exception e) {
						throw new RuntimeException("Failed to parse line '" + line + "'", e);
					}
                }
                this.component = new ObjComponent(toFloatArray(vList), toIntArray(fList), toFloatArray(vtList), toFloatArray(vnList));
            }
        }

        private float[] toFloatArray(final List<Float> floats) {
            final float[] f = new float[floats.size()];
            for (int i = 0; i < f.length; i++) {
                f[i] = floats.get(i);
            }
            return f;
        }

        private int[] toIntArray(final List<Integer> ints) {
            final int[] is = new int[ints.size()];
            for (int i = 0; i < is.length; i++) {
                is[i] = ints.get(i);
            }
            return is;
        }

        public static class ObjComponent {
            public final float[] v; // <x, y, z>
            public final float[] vt; // <u, v>
            public final float[] vn; // <x, y, z>
            public final int[] f; // <p1, p2, p3>
            public ObjComponent(final float[] v, final int[] f, final float[] vt, final float[] vn) {
                this.v = v;
                this.f = f;
                this.vt = vt;
                this.vn = vn;
            }
        }
    }
}
