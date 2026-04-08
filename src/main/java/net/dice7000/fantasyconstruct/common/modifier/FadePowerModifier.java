package net.dice7000.fantasyconstruct.common.modifier;

import com.mega.uom.common.attribute.ModAttributes;
import com.mega.uom.util.entity.EntityASMUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FadePowerModifier extends Modifier implements MeleeHitModifierHook {
    @Override public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        Entity entity = context.getTarget();
        int level = Math.min(modifier.getLevel(), 20);
        if (entity instanceof LivingEntity target) {
            float damage = -((5 + (float) attacker.getAttributeValue(ModAttributes.getFeDamage()) / 10F +
                    attacker.getRandom().nextInt(5) + attacker.getMaxHealth() * 0.002F + attacker.getHealth() * 0.008F));
            EntityASMUtil.addDelta(target, damage * (level / 2.0F));
        }
    }

    @Override protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
}
