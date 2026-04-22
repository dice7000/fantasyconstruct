package net.dice7000.fantasyconstruct.common.event;

import com.mega.uom.common.capability.ModCapabilities;
import com.mega.uom.common.capability.entity.runic.IRunicShieldCapability;
import com.mega.uom.common.network.PacketHandler;
import com.mega.uom.util.entity.EntityASMUtil;
import net.dice7000.fantasyconstruct.FantasyConstruct;
import net.dice7000.fantasyconstruct.common.modifier.LastStandModifier;
import net.dice7000.fantasyconstruct.common.modifier.RunicShieldModifier;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.concurrent.atomic.AtomicInteger;

import static com.mega.uom.common.items.armor.FantasyEndingArmorItem.getRespawnXP;
@Mod.EventBusSubscriber(modid = FantasyConstruct.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FCEvent {
        @SubscribeEvent public static void onServerTick(TickEvent.PlayerTickEvent event) {
            if (!(event.phase == TickEvent.Phase.END)) return;
            Player player = event.player;
            if (player == null) return;

            AtomicInteger RSLevelCount = new AtomicInteger();
            AtomicInteger LSWearsCount = new AtomicInteger();
            player.getArmorSlots().forEach(armorStack -> {
                if (!(armorStack.getItem() instanceof ArmorItem)) return;
                ToolStack.from(armorStack).getModifierList().forEach(modifier -> {
                    if (modifier.getModifier() instanceof RunicShieldModifier)
                        RSLevelCount.addAndGet(modifier.getLevel());
                    if (modifier.getModifier() instanceof LastStandModifier) LSWearsCount.getAndIncrement();
                });
            });

            float totalRSLevel = RSLevelCount.get();
            if (totalRSLevel >= 4) {
                IRunicShieldCapability state = ModCapabilities.getCapability(player, ModCapabilities.MAGIC_SHIELD_EC);
                if (state != null) {
                    state.setMaxLifeTime(20481L);
                    state.setPerCooldowns((int) (20 - (totalRSLevel - 1)));
                    state.setDamageResistance(128.0F * (1 + ((totalRSLevel - 1) / 4)));
                    state.setOutResistance(0.25F);
                    state.setSpawnRunic(false);
                    state.setLevel((short) 4936);
                }
            }
            if (LSWearsCount.get() >= 4) {
                if (player.isDeadOrDying()) {
                    if (!player.level().isClientSide) {
                        player.giveExperiencePoints((int) (-getRespawnXP(player) * 10.0F));
                        ((ServerPlayer) player).connection.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
                        PacketHandler.playSound((ServerPlayer) player, SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 3.0F, 0.8F);
                        player.setHealth(getRespawnXP(player));
                        EntityASMUtil.setHealthDelta(player, 0.0F);
                    }
                    player.deathTime = 0;
                }
            }
        }
    }


