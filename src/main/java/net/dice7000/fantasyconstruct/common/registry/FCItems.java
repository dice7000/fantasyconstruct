package net.dice7000.fantasyconstruct.common.registry;

import net.dice7000.fantasyconstruct.FantasyConstruct;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FCItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create
            (ForgeRegistries.ITEMS, FantasyConstruct.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create
            (Registries.CREATIVE_MODE_TAB, FantasyConstruct.MODID);

    /*
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties()));

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register
            ("example_tab", () -> CreativeModeTab.builder()
                            .title(Component.literal("Fantasy Construct"))
                            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(EXAMPLE_ITEM.get());
                            })
                    .build()
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
     */
}
