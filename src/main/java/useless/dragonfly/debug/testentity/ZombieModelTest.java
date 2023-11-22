package useless.dragonfly.debug.testentity;

import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import useless.dragonfly.DragonFly;
import useless.dragonfly.helper.AnimationHelper;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.entity.animation.AnimationData;

public class ZombieModelTest extends BenchEntityModel {
	public Vector3f VEC_ANIMATION = new Vector3f();

	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		if (this.getIndexBones().containsKey("Head")) {
			this.getIndexBones().get("Head").setRotationAngle((float) Math.toRadians(headPitch), (float) Math.toRadians(headYaw), 0);
		}
		if (this.getIndexBones().containsKey("bone")) {
			this.getIndexBones().get("bone").setRotationAngle(0, ticksExisted, 0);
		}
		if (this.getIndexBones().containsKey("RightArm")) {
			this.getIndexBones().get("RightArm").setRotationAngle(MathHelper.cos(limbSwing * (2f/3) + MathHelper.PI) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("LeftArm")) {
			this.getIndexBones().get("LeftArm").setRotationAngle(MathHelper.cos(limbSwing * (2f/3)) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("RightLeg")) {
			this.getIndexBones().get("RightLeg").setRotationAngle(MathHelper.cos(limbSwing * (2f/3)) * 1.4f * limbYaw, 0, 0);
		}
		if (this.getIndexBones().containsKey("LeftLeg")) {
			this.getIndexBones().get("LeftLeg").setRotationAngle(MathHelper.cos(limbSwing * (2f/3) + MathHelper.PI) * 1.4f * limbYaw, 0, 0);
		}
		animateWalk(AnimationHelper.getOrCreateEntityAnimation(DragonFly.MOD_ID, "zombie_test.animation.json").getAnimations().get("test"), ticksExisted, 1.0F, 1.0F, 1.0F);
	}

	protected void animateWalk(AnimationData p_268159_, float p_268057_, float p_268347_, float p_268138_, float p_268165_) {
		long i = (long) (p_268057_ * 50.0F * p_268138_);
		float f = Math.min(p_268347_ * p_268165_, 1.0F);
		AnimationHelper.animate(this, p_268159_, i, f, VEC_ANIMATION);
	}

	protected void applyStatic(AnimationData p_288996_) {
		AnimationHelper.animate(this, p_288996_, 0L, 1.0F, VEC_ANIMATION);
	}
}
