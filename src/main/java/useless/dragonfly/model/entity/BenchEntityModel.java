package useless.dragonfly.model.entity;

import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.render.model.ModelBase;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import useless.dragonfly.DragonFly;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.entity.processor.BenchEntityBones;
import useless.dragonfly.model.entity.processor.BenchEntityCube;
import useless.dragonfly.model.entity.processor.BenchEntityGeometry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/bedrock/BedrockModel.java
 */
public class BenchEntityModel extends ModelBase {
	private final HashMap<String, BenchEntityBones> indexBones = new HashMap<>();
	@SerializedName("format_version")
	private String formatVersion;
	@SerializedName("minecraft:geometry")
	private List<BenchEntityGeometry> benchEntityGeometry;

	public HashMap<String, BenchEntityBones> getIndexBones() {
		return indexBones;
	}

	@Override
	public void render(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		super.render(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		this.renderModel(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
	}

	public void renderModel(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		BenchEntityGeometry entityGeometry = this.benchEntityGeometry.get(0);

		int texWidth = entityGeometry.getWidth();
		int texHeight = entityGeometry.getHeight();
		for (BenchEntityBones bones : entityGeometry.getBones()) {
			if (!this.getIndexBones().containsKey(bones.getName())) {
				this.getIndexBones().put(bones.getName(), bones);
			}
		}

		for (BenchEntityBones bones : entityGeometry.getBones()) {
			String name = bones.getName();
			@Nullable List<Float> rotation = bones.getRotation();
			@Nullable String parent = bones.getParent();


			if (parent != null) {
				if (!this.getIndexBones().get(parent).getChildren().contains(bones)) {
					this.getIndexBones().get(parent).addChild(bones);
				}
			}


			if (bones.getCubes() == null) {
				continue;
			}

			for (BenchEntityCube cube : bones.getCubes()) {
				List<Float> uv = cube.getUv();
				List<Float> size = cube.getSize();
				@Nullable List<Float> cubeRotation = cube.getRotation();
				boolean mirror = cube.isMirror();
				float inflate = cube.getInflate();

				cube.addBox(texWidth, texHeight, convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2), false);


				if (!cube.isCompiled()) {
					cube.compileDisplayList(scale);
				}
				GL11.glPushMatrix();
				GL11.glTranslatef(convertPivot(bones, 0) * scale, convertPivot(bones, 1) * scale, convertPivot(bones, 2) * scale);

				if (bones.rotationPointX != 0.0f || bones.rotationPointY != 0.0f || bones.rotationPointZ != 0.0f) {
					GL11.glTranslatef(bones.rotationPointX * scale, bones.rotationPointY * scale, bones.rotationPointZ * scale);
				}
				if (rotation != null) {
					GL11.glRotatef((float) Math.toRadians(rotation.get(0)), 0.0f, 0.0f, 1.0f);
					GL11.glRotatef((float) Math.toRadians(rotation.get(1)), 0.0f, 1.0f, 0.0f);
					GL11.glRotatef((float) Math.toRadians(rotation.get(2)), 1.0f, 0.0f, 0.0f);
				}

				if (bones.rotateAngleZ != 0.0f) {
					GL11.glRotatef((float) (Math.toDegrees(bones.rotateAngleZ)), 0.0f, 0.0f, 1.0f);
				}
				if (bones.rotateAngleY != 0.0f) {
					GL11.glRotatef((float) (Math.toDegrees(bones.rotateAngleY)), 0.0f, 1.0f, 0.0f);
				}
				if (bones.rotateAngleX != 0.0f) {
					GL11.glRotatef((float) (Math.toDegrees(bones.rotateAngleX)), 1.0f, 0.0f, 0.0f);
				}

				GL11.glCallList(cube.getDisplayList());


				GL11.glPopMatrix();
			}

			if (parent != null) {
				if (this.getIndexBones().get(parent).getChildren().contains(bones)) {
					for (BenchEntityBones child : this.getIndexBones().get(parent).getChildren()) {
						for (BenchEntityCube cube2 : bones.getCubes()) {
							cube2.addBox(texWidth, texHeight, convertOrigin(child, cube2, 0), convertOrigin(child, cube2, 1), convertOrigin(child, cube2, 2), false);
							if (!cube2.isCompiled()) {
								cube2.compileDisplayList(scale);
							}
							GL11.glPushMatrix();
							GL11.glTranslatef(convertPivot(child, 0) * scale, convertPivot(child, 1) * scale, convertPivot(child, 2) * scale);
							if (child.rotationPointX != 0.0f || child.rotationPointY != 0.0f || child.rotationPointZ != 0.0f) {
								GL11.glTranslatef(child.rotationPointX * ((float) Math.PI / 180F), child.rotationPointY * scale, child.rotationPointZ * scale);
							}
							GL11.glRotatef((float) (Math.toRadians(convertPivot(child, 0))), 0.0f, 0.0f, 1.0f);
							GL11.glRotatef((float) Math.toRadians(convertPivot(child, 1)), 0.0f, 1.0f, 0.0f);

							GL11.glRotatef((float) Math.toRadians(convertPivot(child, 2)), 1.0f, 0.0f, 0.0f);
							if (child.rotateAngleZ != 0.0f) {
								GL11.glRotatef((float) Math.toDegrees(child.rotateAngleZ), 0.0f, 0.0f, 1.0f);
							}
							if (child.rotateAngleY != 0.0f) {
								GL11.glRotatef((float) Math.toDegrees(child.rotateAngleY), 0.0f, 1.0f, 0.0f);
							}
							if (child.rotateAngleX != 0.0f) {
								GL11.glRotatef((float) Math.toDegrees(child.rotateAngleX), 1.0f, 0.0f, 0.0f);
							}

							GL11.glCallList(cube2.getDisplayList());
							GL11.glPopMatrix();
						}
					}
				}
			}
		}


		this.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);

	}

	public float convertPivot(BenchEntityBones bones, int index) {
		if (bones.getParent() != null) {
			if (index == 1) {
				return getIndexBones().get(bones.getParent()).getPivot().get(index) - bones.getPivot().get(index);
			} else {
				return bones.getPivot().get(index) - getIndexBones().get(bones.getParent()).getPivot().get(index);
			}
		} else {
			if (index == 1) {
				return 24 - bones.getPivot().get(index);
			} else {
				return bones.getPivot().get(index);
			}
		}
	}

	public float convertPivot(BenchEntityBones parent, BenchEntityCube cube, int index) {
		assert cube.getPivot() != null;
		if (index == 1) {
			return parent.getPivot().get(index) - cube.getPivot().get(index);
		} else {
			return cube.getPivot().get(index) - parent.getPivot().get(index);
		}
	}

	public float convertOrigin(BenchEntityBones bone, BenchEntityCube cube, int index) {
		if (index == 1) {
			return bone.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
		} else {
			return cube.getOrigin().get(index) - bone.getPivot().get(index);
		}
	}

	public float convertOrigin(BenchEntityCube cube, int index) {
		assert cube.getPivot() != null;
		if (index == 1) {
			return cube.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
		} else {
			return cube.getOrigin().get(index) - cube.getPivot().get(index);
		}
	}
}
