package net.dice7000.fantasyconstruct.common.registry;

import net.dice7000.fantasyconstruct.FantasyConstruct;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.TierSortingRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import java.util.List;

public class FCMaterial {
    public static final MaterialId FE_INGOT = new MaterialId(FantasyConstruct.MODID, "fe_ingot");
    public static final Tier TESTTIER = TierSortingRegistry.registerTier(
            new TestTier(),
            FantasyConstruct.FCLocation("fe_ingot"),
            List.of(Tiers.NETHERITE),
            List.of()
    );
    public static class TestTier implements Tier {
        @Override public int getUses() {
            return 0;
        }
        @Override public float getSpeed() {
            return 0;
        }
        @Override public float getAttackDamageBonus() {
            return 0;
        }
        @Override public int getLevel() {
            return 0;
        }
        @Override public int getEnchantmentValue() {
            return 0;
        }
        @Override public Ingredient getRepairIngredient() {
            return null;
        }
    }
}
