package net.dice7000.fantasyconstruct;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = FantasyConstruct.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.BooleanValue UOM_HAS_FORCE_FE = BUILDER.comment("It can toggle UOM ").define("UOMHasForceFE", true);
    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER.comment("A magic number").defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER.comment("What you want the introduction message to be for the magic number").define("magicNumberIntroduction", "The magic number is... ");
    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean UOMHasForceFE;
    public static int magicNumber;
    public static String magicNumberIntroduction;

    @SubscribeEvent static void onLoad(final ModConfigEvent event) {
        UOMHasForceFE = UOM_HAS_FORCE_FE.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();
    }
}
