package net.dice7000.fantasyconstruct.mixin.mixin;

import com.mega.endinglib.util.mc.entity.armor.ArmorUtils;
import net.dice7000.fantasyconstruct.util.FCUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ArmorUtils.class, remap = false)
public class FEArmorUtilsMixin {
    @Inject(method = "armorSet", at = @At("HEAD"), cancellable = true)
    private static void armorSetInject(LivingEntity living, ArmorMaterial material, CallbackInfoReturnable<Boolean> cir) {
        if (FCUtil.additionalCondition(living)) cir.setReturnValue(true);
    }
}
