package net.dice7000.fantasyconstruct.common.registry;

import com.mega.uom.common.damagesource.ModDamageSources;
import net.dice7000.fantasyconstruct.FantasyConstruct;
import net.dice7000.fantasyconstruct.common.modifier.FEDamageModifier;
import net.dice7000.fantasyconstruct.common.modifier.FadePowerModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class FCModifier {
    public static final ModifierDeferredRegister MODIFIER = ModifierDeferredRegister.create(FantasyConstruct.MODID);

    public static final StaticModifier<Modifier> FE_DAMAGE = MODIFIER.register("fe_damage", FEDamageModifier::new);
    public static final StaticModifier<Modifier> FADE_POWER = MODIFIER.register("fade_power", FadePowerModifier::new);

    public static final ResourceKey<DamageType> FE_DAMAGE_KEY = ResourceKey.create
            (Registries.DAMAGE_TYPE, ResourceLocation.parse("fantasyconstruct:fe_damage"));

    public static DamageSource anotherFEDamage(Entity attacker) {
        return (new ModDamageSources.EasyDamageSource(FE_DAMAGE_KEY, attacker));
    }

    public static void register(IEventBus eventBus) {
        MODIFIER.register(eventBus);
    }
}
