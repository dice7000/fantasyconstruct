package net.dice7000.fantasyconstruct.common.modifier;

import com.mega.uom.common.damagesource.ModDamageSources;
import com.mega.uom.util.entity.EntityActuallyHurt;
import net.dice7000.fantasyconstruct.common.registry.FCModifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FEDamageModifier extends Modifier implements MeleeHitModifierHook {
    @Override public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        Entity entity = context.getTarget();
        int level = Math.min(modifier.getLevel(), 20);
        if (entity instanceof LivingEntity target) {
            float strengthScale = attacker instanceof Player player ? player.getAttackStrengthScale(0.5F) : 0.8F;
            float attackConf = 0.2F + strengthScale * strengthScale * 0.8F;
            float defaultDamageAmount = (15 + attacker.getRandom().nextInt(5) +
                    target.getMaxHealth() * 0.002F + target.getHealth() * 0.008F) * attackConf;
            EntityActuallyHurt hurt = new AnotherActuallyHurt(target);
            hurt.actuallyHurt(ModDamageSources.causeDeathFeDamage(attacker), defaultDamageAmount * (level / 2.0F), true);
        }

    }

    @Override protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
}
