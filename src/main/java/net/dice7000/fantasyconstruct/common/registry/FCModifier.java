package net.dice7000.fantasyconstruct.common.registry;

import net.dice7000.fantasyconstruct.FantasyConstruct;
import net.dice7000.fantasyconstruct.common.modifier.FEDamageModifier;
import net.dice7000.fantasyconstruct.common.modifier.FadePowerModifier;
import net.dice7000.fantasyconstruct.common.modifier.LastStandModifier;
import net.dice7000.fantasyconstruct.common.modifier.RunicShieldModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class FCModifier {
    public static final ModifierDeferredRegister MODIFIER = ModifierDeferredRegister.create(FantasyConstruct.MODID);

    public static final StaticModifier<Modifier> FE_DAMAGE = MODIFIER.register("fe_damage", FEDamageModifier::new);
    public static final StaticModifier<Modifier> FADE_POWER = MODIFIER.register("fade_power", FadePowerModifier::new);
    public static final StaticModifier<Modifier> RUNIC_SHIELD = MODIFIER.register("runic_shield", RunicShieldModifier::new);
    public static final StaticModifier<Modifier> LAST_STAND = MODIFIER.register("last_stand", LastStandModifier::new);

    public static void register(IEventBus eventBus) {
        MODIFIER.register(eventBus);
    }
}
