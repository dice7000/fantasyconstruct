package net.dice7000.fantasyconstruct.common.event;

import com.mega.uom.common.capability.ModCapabilities;
import com.mega.uom.common.capability.entity.runic.IRunicShieldCapability;
import com.mega.uom.common.network.PacketHandler;
import com.mega.uom.util.entity.EntityASMUtil;
import net.dice7000.fantasyconstruct.FantasyConstruct;
import net.dice7000.fantasyconstruct.common.modifier.LastStandModifier;
import net.dice7000.fantasyconstruct.common.modifier.RunicShieldModifier;
import net.dice7000.fantasyconstruct.util.FCUtil;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mega.uom.common.items.armor.FantasyEndingArmorItem.getRespawnXP;

@Mod.EventBusSubscriber(modid = FantasyConstruct.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FCEvent {
    @SubscribeEvent public static void onServerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) return;
        if (event.phase == TickEvent.Phase.START) {
            if (player.deathTime > 0 && FCUtil.shouldNotDead(player)) player.deathTime = 0;
        }
        if (event.phase == TickEvent.Phase.END) {
            AtomicInteger RSLevelCount = new AtomicInteger();
            AtomicInteger LSCount = new AtomicInteger();
            player.getArmorSlots().forEach(stack -> ToolStack.from(stack).getModifierList().forEach(modifierEntry -> {
                if (modifierEntry.getModifier() instanceof RunicShieldModifier) RSLevelCount.addAndGet(modifierEntry.getLevel());
                if (modifierEntry.getModifier() instanceof LastStandModifier) LSCount.getAndIncrement();
            }));

            if (!player.level().isClientSide) {
                float totalRSLevel = RSLevelCount.get();
                if (totalRSLevel >= 4) {
                    IRunicShieldCapability state = ModCapabilities.getCapability(player, ModCapabilities.FE_SHIELD_EC);
                    if (state != null) {
                        if (player.tickCount % 10 == 0) {
                            float conf = Math.max(totalRSLevel / 4, 1);
                            state.setMaxLifeTime(((20 * (long) conf) + 1));
                            state.setPerCooldowns((int) Math.floor(Math.max(20 - (conf - 1), 2)));
                            state.setDamageResistance(128.0F * conf);
                            state.setOutResistance(0.25F);
                            state.setSpawnRunic(false);
                            state.setLevel((short) 4936);
                        }
                    }
                }
            }

            lastStand(player, FCUtil.shouldNotDead(player));
        }
    }

    private static final Map<UUID/*Player's UUID*/, AtomicBoolean/*Last Enabled*/> map = new HashMap<>();
    private static void lastStand(Player player, boolean enable) {
        UUID uuid = player.getUUID();
        if (!map.containsKey(uuid)) map.put(player.getUUID(), new AtomicBoolean(false));
        AtomicBoolean lastEnable = map.get(uuid);
        if (enable) {
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
            if (!lastEnable.get()) {
                lastEnable.set(true);
            }
        } else {
            if (lastEnable.get()) {
                lastEnable.set(false);
            }
        }
    }
}


