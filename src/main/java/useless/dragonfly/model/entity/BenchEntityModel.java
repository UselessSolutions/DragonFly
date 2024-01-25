package useless.dragonfly.model.entity;

import net.minecraft.client.render.model.ModelBase;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import useless.dragonfly.helper.AnimationHelper;
import useless.dragonfly.model.entity.animation.AnimationData;
import useless.dragonfly.model.entity.processor.BenchEntityBones;
import useless.dragonfly.model.entity.processor.BenchEntityCube;
import useless.dragonfly.model.entity.processor.BenchEntityGeometry;
import useless.dragonfly.model.entity.processor.BenchEntityModelData;
import useless.dragonfly.utilities.vector.Vector3f;

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
	public BenchEntityModelData geometry;
	public HashMap<String, BenchEntityBones> getIndexBones() {
		return indexBones;
	}
	public BenchEntityModel(){

	}
	@Override
	public void render(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		super.render(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		this.renderModel(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
	}

	public void renderModel(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2884);
		GL11.glDisable(GL11.GL_CULL_FACE);
		BenchEntityGeometry entityGeometry = geometry.benchEntityGeometry.get(0);

		int texWidth = entityGeometry.getWidth();
		int texHeight = entityGeometry.getHeight();
		for (BenchEntityBones bones : entityGeometry.getBones()) {
			@Nullable String parent = bones.getParent();


			if (!this.getIndexBones().containsKey(bones.getName())) {
				this.getIndexBones().put(bones.getName(), bones);
			}

			if (parent != null) {
				if (!this.getIndexBones().get(parent).getChildren().contains(bones)) {
					this.getIndexBones().get(parent).addChild(bones);
				}
			}
		}
		if (!this.getIndexBones().isEmpty()) {
			//DON'T MOVE IT! because the rotation and rotation position of entities of the same model being mixed.
			this.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		}

		for (BenchEntityBones bones : entityGeometry.getBones()) {
			@Nullable String parent = bones.getParent();

			String name = bones.getName();
			Vector3f rotation = bones.getRotation();

			if (bones.getCubes() == null) {
				continue;
			}

			for (BenchEntityCube cube : bones.getCubes()) {
				List<Float> uv = cube.getUv();
				Vector3f size = cube.getSize();
				Vector3f cubeRotation = cube.getRotation();
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

				if (cube.getPivot() != null) {
					GL11.glTranslatef(convertPivot(bones, cube, 0) * scale, convertPivot(bones, cube, 1) * scale, convertPivot(bones, cube, 2) * scale);

				}


				if (bones.rotationPointX != 0.0f || bones.rotationPointY != 0.0f || bones.rotationPointZ != 0.0f) {
					GL11.glTranslatef(bones.rotationPointX * scale, bones.rotationPointY * scale, bones.rotationPointZ * scale);
				}
				float rx = 0;
				float ry = 0;
				float rz = 0;
				if (rotation != null) {
					rx += rotation.x;
					ry += rotation.y;
					rz += rotation.z;
				}
				if (cubeRotation != null){
					rx += cubeRotation.x;
					ry += cubeRotation.y;
					rz += cubeRotation.z;
				}
				GL11.glRotatef(rx, 1.0f, 0.0f, 0.0f);
				GL11.glRotatef(ry, 0.0f, 1.0f, 0.0f);
				GL11.glRotatef(rz, 0.0f, 0.0f, 1.0f);
				if (bones.rotateAngleY != 0.0f) {
					GL11.glRotatef((float) Math.toDegrees(bones.rotateAngleY), 0.0f, 1.0f, 0.0f);
				}
				if (bones.rotateAngleX != 0.0f) {
					GL11.glRotatef((float) Math.toDegrees(bones.rotateAngleX), 1.0f, 0.0f, 0.0f);
				}
				if (bones.rotateAngleZ != 0.0f) {
					GL11.glRotatef((float) Math.toDegrees(bones.rotateAngleZ), 0.0f, 0.0f, 1.0f);
				}


				if (bones.scaleX != 0.0f || bones.scaleY != 0.0f || bones.scaleZ != 0.0f) {
					GL11.glScalef(bones.scaleX, bones.scaleY, bones.scaleZ);
				}


				GL11.glCallList(cube.getDisplayList());


				GL11.glPopMatrix();
			}
		}
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	/*
	 * This method used Translate for item render
	 */
	public void postRender(BenchEntityBones bones, float scale) {
		Vector3f rotation = bones.getRotation();
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
			GL11.glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(rotation.y, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(rotation.z, 0.0f, 0.0f, 1.0f);
		}
		if (bones.rotateAngleY != 0.0f) {
			GL11.glRotatef((float) Math.toDegrees(bones.rotateAngleY), 0.0f, 1.0f, 0.0f);
		}
		if (bones.rotateAngleX != 0.0f) {
			GL11.glRotatef((float) Math.toDegrees(bones.rotateAngleX), 1.0f, 0.0f, 0.0f);
		}
		if (bones.rotateAngleZ != 0.0f) {
			GL11.glRotatef((float) Math.toDegrees(bones.rotateAngleZ), 0.0f, 0.0f, 1.0f);
		}


		if (bones.scaleX != 0.0f || bones.scaleY != 0.0f || bones.scaleZ != 0.0f) {
			GL11.glScalef(bones.scaleX, bones.scaleY, bones.scaleZ);
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
			GL11.glRotatef(parentBone.getRotation().x, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(parentBone.getRotation().y, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(parentBone.getRotation().z, 0.0f, 0.0f, 1.0f);
		}

		if (parentBone.rotateAngleZ != 0.0f) {
			GL11.glRotatef(parentBone.rotateAngleZ, 0.0f, 0.0f, 1.0f);
		}
		if (parentBone.rotateAngleY != 0.0f) {
			GL11.glRotatef(parentBone.rotateAngleY, 0.0f, 1.0f, 0.0f);
		}
		if (parentBone.rotateAngleX != 0.0f) {
			GL11.glRotatef(parentBone.rotateAngleX, 1.0f, 0.0f, 0.0f);
		}

		if (parentBone.scaleX != 0.0f || parentBone.scaleY != 0.0f || parentBone.scaleZ != 0.0f) {
			GL11.glScalef(parentBone.scaleX, parentBone.scaleY, parentBone.scaleZ);
		}
	}

	public float convertPivot(BenchEntityBones bones, int index) {
		if (bones.getParent() != null) {
			if (index == 1) {
				return getIndexBones().get(bones.getParent()).getPivot().getY() - bones.getPivot().getY();
			} else {
				return bones.getPivot().getFromIndex(index) - getIndexBones().get(bones.getParent()).getPivot().getFromIndex(index);
			}
		} else {
			if (index == 1) {
				return 24 - bones.getPivot().getFromIndex(index);
			} else {
				return bones.getPivot().getFromIndex(index);
			}
		}
	}

	public float convertPivot(BenchEntityBones parent, BenchEntityCube cube, int index) {
		assert cube.getPivot() != null;
		if (index == 1) {
			return parent.getPivot().getFromIndex(index) - cube.getPivot().getFromIndex(index);
		} else {
			return cube.getPivot().getFromIndex(index) - parent.getPivot().getFromIndex(index);
		}
	}

	public float convertOrigin(BenchEntityBones bone, BenchEntityCube cube, int index) {
		if (index == 1) {
			return bone.getPivot().getFromIndex(index) - cube.getOrigin().getFromIndex(index) - cube.getSize().getFromIndex(index);
		} else {
			return cube.getOrigin().getFromIndex(index) - bone.getPivot().getFromIndex(index);
		}
	}

	public float convertOrigin(BenchEntityCube cube, int index) {
		assert cube.getPivot() != null;
		if (index == 1) {
			return cube.getPivot().getFromIndex(index) - cube.getOrigin().getFromIndex(index) - cube.getSize().getFromIndex(index);
		} else {
			return cube.getOrigin().getFromIndex(index) - cube.getPivot().getFromIndex(index);
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

	public void deco() {
		if (!getIndexBones().isEmpty()) {
			getIndexBones().entrySet().forEach(bonesItem -> {
				if (bonesItem.getValue().getCubes() != null) {
					bonesItem.getValue().getCubes().forEach(cubesItem -> {
						if (!cubesItem.isHasMirror()) {
							cubesItem.setMirror(bonesItem.getValue().isMirror());
						}
					});
				}
			});
		}
	}
}
