package net.dice7000.fantasyconstruct.util;

import net.dice7000.fantasyconstruct.common.modifier.LastStandModifier;
import net.dice7000.fantasyconstruct.common.modifier.RunicShieldModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.concurrent.atomic.AtomicInteger;

public class FCUtil {
    public static boolean additionalCondition(LivingEntity living) {
        if (!(living instanceof Player player)) return false;
        AtomicInteger RSCount = new AtomicInteger();
        player.getArmorSlots().forEach(armorStack -> {
            if (!(armorStack.getItem() instanceof ArmorItem)) return;
            ToolStack.from(armorStack).getModifierList().forEach(modifier -> {
                if (modifier.getModifier() instanceof RunicShieldModifier) RSCount.getAndIncrement();
            });
        });
        return RSCount.get() >= 4;
    }

    public static boolean shouldNotDead(LivingEntity entity) {
        if (!(entity instanceof Player player)) return false;
        AtomicInteger LSCount = new AtomicInteger();
        player.getArmorSlots().forEach(stack -> ToolStack.from(stack).getModifierList().stream().filter(modifierEntry -> modifierEntry.getModifier() instanceof LastStandModifier).forEach(entry -> LSCount.getAndIncrement()));
        return LSCount.get() >= 4 && (player.experienceLevel > 0 || player.experienceProgress > 0.0F);
    }
}
