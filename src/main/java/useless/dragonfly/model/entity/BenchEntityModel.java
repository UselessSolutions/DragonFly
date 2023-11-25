package useless.dragonfly.model.entity;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.render.model.ModelBase;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import useless.dragonfly.helper.AnimationHelper;
import useless.dragonfly.model.entity.animation.AnimationData;
import useless.dragonfly.model.entity.processor.BenchEntityBones;
import useless.dragonfly.model.entity.processor.BenchEntityCube;
import useless.dragonfly.model.entity.processor.BenchEntityGeometry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/bedrock/BedrockModel.java
 */
public class BenchEntityModel extends ModelBase {
	public Vector3f VEC_ANIMATION = new Vector3f();
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
		if (!this.getIndexBones().isEmpty()) {
			//DON'T MOVE IT! because the rotation and rotation position of entities of the same model being mixed.
			this.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
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


				//parent time before rotate it self
				if (parent != null) {
					BenchEntityBones parentBone = this.getIndexBones().get(parent);

					convertWithMoreParent(parentBone, scale);
				}

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


		}

	}

	/*
	 * This method used Translate for item render
	 */
	public void postRender(BenchEntityBones bones, float scale) {
		List<Float> rotation = bones.getRotation();
		//parent time before rotate it self
		if (bones.getParent() != null) {
			BenchEntityBones parentBone = this.getIndexBones().get(bones.getParent());

			convertWithMoreParent(parentBone, scale);
		}

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
	}

	private void convertWithMoreParent(BenchEntityBones parentBone, float scale) {
		//don't forget some parent has more parent
		if (parentBone.getParent() != null) {
			convertWithMoreParent(this.getIndexBones().get(parentBone.getParent()), scale);
		}
		GL11.glTranslatef(convertPivot(parentBone, 0) * scale, convertPivot(parentBone, 1) * scale, convertPivot(parentBone, 2) * scale);


		if (parentBone.rotationPointX != 0.0f || parentBone.rotationPointY != 0.0f || parentBone.rotationPointZ != 0.0f) {
			GL11.glTranslatef(parentBone.rotationPointX * scale, parentBone.rotationPointY * scale, parentBone.rotationPointZ * scale);
		}
		if (parentBone.getRotation() != null) {
			GL11.glRotatef((float) Math.toRadians(parentBone.getRotation().get(0)), 0.0f, 0.0f, 1.0f);
			GL11.glRotatef((float) Math.toRadians(parentBone.getRotation().get(1)), 0.0f, 1.0f, 0.0f);
			GL11.glRotatef((float) Math.toRadians(parentBone.getRotation().get(2)), 1.0f, 0.0f, 0.0f);
		}

		if (parentBone.rotateAngleZ != 0.0f) {
			GL11.glRotatef((float) (Math.toDegrees(parentBone.rotateAngleZ)), 0.0f, 0.0f, 1.0f);
		}
		if (parentBone.rotateAngleY != 0.0f) {
			GL11.glRotatef((float) (Math.toDegrees(parentBone.rotateAngleY)), 0.0f, 1.0f, 0.0f);
		}
		if (parentBone.rotateAngleX != 0.0f) {
			GL11.glRotatef((float) (Math.toDegrees(parentBone.rotateAngleX)), 1.0f, 0.0f, 0.0f);
		}

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

	public Optional<BenchEntityBones> getAnyDescendantWithName(String key) {
		Optional<Map.Entry<String, BenchEntityBones>> bones = this.getIndexBones().entrySet().stream().filter((benchBone) -> benchBone.getKey().equals(key)).findFirst();
		if (bones.isPresent()) {
			return Optional.of(bones.get().getValue());
		} else {
			return Optional.empty();
		}
	}

	protected void animateWalk(AnimationData animationData, float p_268057_, float p_268347_, float p_268138_, float p_268165_) {
		long time = (long) (p_268057_ * 50.0F * p_268138_);
		float scale = Math.min(p_268347_ * p_268165_, 1.0F);
		AnimationHelper.animate(this, animationData, time, scale, VEC_ANIMATION);
	}

	protected void applyStatic(AnimationData animationData) {
		AnimationHelper.animate(this, animationData, 0L, 1.0F, VEC_ANIMATION);
	}

	protected void animate(AnimationState animationState, AnimationData animationData, float p_233388_, float p_233389_) {
		animationState.updateTime(p_233388_, p_233389_);
		animationState.ifStarted(p_233392_ -> AnimationHelper.animate(this, animationData, p_233392_.getAccumulatedTime(), 1.0F, VEC_ANIMATION));
	}
}
