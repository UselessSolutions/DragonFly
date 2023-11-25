package useless.dragonfly.debug.testentity.Warden;

import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.util.helper.MathHelper;
import useless.dragonfly.helper.AnimationHelper;
import useless.dragonfly.model.entity.BenchEntityModel;
import useless.dragonfly.model.entity.animation.Animation;

import static useless.dragonfly.DragonFly.MOD_ID;

public class WardenModel extends BenchEntityModel {
	public static EntityWarden warden;

	@Override
	public void setLivingAnimations(EntityLiving entityliving, float limbSwing, float limbYaw, float renderPartialTicks) {
		super.setLivingAnimations(entityliving, limbSwing, limbYaw, renderPartialTicks);
		if (entityliving instanceof EntityWarden) {
			warden = (EntityWarden) entityliving;
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale) {
		// If you need play some animation. you should reset with this
		this.getIndexBones().forEach((s, benchEntityBones) -> benchEntityBones.resetPose());

		super.setRotationAngles(limbSwing, limbYaw, ticksExisted, headYaw, headPitch, scale);
		if (this.getIndexBones().containsKey("head")) {
			this.getIndexBones().get("head").setRotationAngle((float) Math.toRadians(headPitch), (float) Math.toRadians(headYaw), 0);
		}
		if (this.getIndexBones().containsKey("right_arm")) {
			this.getIndexBones().get("right_arm").setRotationAngle(MathHelper.cos(limbSwing * (2f / 3) + MathHelper.PI) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("left_arm")) {
			this.getIndexBones().get("left_arm").setRotationAngle(MathHelper.cos(limbSwing * (2f / 3)) * 2.0f * limbYaw * 0.5f, 0, 0);
		}
		if (this.getIndexBones().containsKey("right_leg")) {
			this.getIndexBones().get("right_leg").setRotationAngle(MathHelper.cos(limbSwing * (2f / 3)) * 1.4f * limbYaw, 0, 0);
		}
		if (this.getIndexBones().containsKey("left_leg")) {
			this.getIndexBones().get("left_leg").setRotationAngle(MathHelper.cos(limbSwing * (2f / 3) + MathHelper.PI) * 1.4f * limbYaw, 0, 0);
		}

		if (this.getIndexBones().containsKey("right_ribcage")) {
			this.getIndexBones().get("right_ribcage").setRotationAngle(0, Math.max(MathHelper.cos(ticksExisted / 20), 0), 0);
		}
		if (this.getIndexBones().containsKey("left_ribcage")) {
			this.getIndexBones().get("left_ribcage").setRotationAngle(0, -Math.max(MathHelper.cos(ticksExisted / 20), 0), 0);
		}

		if (this.getIndexBones().containsKey("right_tendril")) {
			this.getIndexBones().get("right_tendril").setRotationAngle(0, Math.max(MathHelper.sin(ticksExisted / 2) - 0.8f, 0), 0);
		}
		if (this.getIndexBones().containsKey("left_tendril")) {
			this.getIndexBones().get("left_tendril").setRotationAngle(0, -Math.max(MathHelper.sin(ticksExisted / 2) - 0.8f, 0), 0);
		}
		Animation testAnimation = AnimationHelper.getOrCreateEntityAnimation(MOD_ID, "warden.animation");
		if (warden != null) {
			animate(warden.attackState, testAnimation.getAnimations().get("animation.warden.attack"), ticksExisted, 1.0F);
		}
	}
}
