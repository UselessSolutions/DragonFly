package useless.dragonfly.mixins.mixin;

import net.minecraft.core.achievement.stat.Stat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.dragonfly.mixins.mixin.accessor.StatListAccessor;

@Mixin(value = Stat.class, remap = false)
public class StatMixin {
	@Shadow
	@Final
	public int statId;

	@Inject(method = "registerStat()Lnet/minecraft/core/achievement/stat/Stat;", at = @At("HEAD"), cancellable = true)
	private void preventCrash(CallbackInfoReturnable<Stat> cir){
		if (StatListAccessor.getStatMap().containsKey(statId)) {
			cir.setReturnValue(StatListAccessor.getStatMap().get(statId));
		}
	}
}
