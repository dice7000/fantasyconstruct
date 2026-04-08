package net.dice7000.fantasyconstruct;

import net.dice7000.fantasyconstruct.common.registry.FCItems;
import net.dice7000.fantasyconstruct.common.registry.FCModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FantasyConstruct.MODID)
public class FantasyConstruct {
    public static final String MODID = "fantasyconstruct";

    public FantasyConstruct(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        FCItems.register(modEventBus);
        FCModifier.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        //context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation FCLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
    @SubscribeEvent public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
