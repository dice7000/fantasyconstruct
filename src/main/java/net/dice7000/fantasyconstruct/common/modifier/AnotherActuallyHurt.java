package net.dice7000.fantasyconstruct.common.modifier;

import com.mega.endinglib.mixin.accessor.AccessorLivingEntity;
import com.mega.uom.common.attribute.ModAttributes;
import com.mega.uom.common.damagesource.ModDamageSources;
import com.mega.uom.mixin.SyncEntityDataAccessor;
import com.mega.uom.util.FeMapping;
import com.mega.uom.util.data.LivingEntityExpandedContext;
import com.mega.uom.util.entity.EntityActuallyHurt;
import com.mega.uom.util.entity.EntityDataInjector;
import com.mega.uom.util.itf.LivingEntityEC;
import com.mega.uom.util.java.InstrumentationHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

import java.lang.invoke.MethodType;

public class AnotherActuallyHurt extends EntityActuallyHurt {
    public AnotherActuallyHurt(LivingEntity entity) {
        super(entity);
    }

    @Override public void actuallyHurt(DamageSource source, float amount, boolean special) {
        try {
            if (this.entity.level().isClientSide) return;
            if (this.entity.isDeadOrDying() && !EntityDataInjector.getAllowHurtMethod(this.entity)) return;
            if (this.entity.isSleeping() && !this.entity.level().isClientSide) this.entity.stopSleeping();

            AccessorLivingEntity accessorEntity = (AccessorLivingEntity)this.entity;
            accessorEntity.setNoActionTime(0);
            float f = amount;
            boolean flag = false;

            if (source.is(DamageTypeTags.IS_FREEZING) && this.entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                amount *= 5.0F;
            }

            this.entity.walkAnimation.setSpeed(1.5F);
            accessorEntity.setLastHurt(amount);
            this.entity.invulnerableTime = 20;
            LivingEntityExpandedContext entityEC = ((LivingEntityEC)this.entity).uom$livingECData();
            if (entityEC.invulnerableTime > 10) {
                float lastFeHurt = entityEC.lastFEHurt;
                if (amount < lastFeHurt) {
                    return;
                }

                this.actuallyHurt0(source, amount - lastFeHurt, special);
            } else {
                entityEC.lastFEHurt = amount;
                this.actuallyHurt0(source, amount, special);
            }

            this.entity.hurtTime = this.entity.hurtDuration;
            if (source.is(DamageTypeTags.DAMAGES_HELMET) && !this.entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                accessorEntity.callHurtHelmet(source, amount);
                amount *= 0.75F;
            }

            Entity entity1 = source.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity livingentity1) {
                    if (!source.is(DamageTypeTags.NO_ANGER)) {
                        this.entity.setLastHurtByMob(livingentity1);
                    }
                }

                if (entity1 instanceof Player player1) {
                    accessorEntity.setLastHurtByPlayerTime(100);
                    this.entity.setLastHurtByPlayer(player1);
                } else if (entity1 instanceof TamableAnimal tamableEntity) {
                    if (tamableEntity.isTame()) {
                        accessorEntity.setLastHurtByPlayerTime(100);
                        LivingEntity livingentity2 = tamableEntity.getOwner();
                        if (livingentity2 instanceof Player player) {
                            this.entity.setLastHurtByPlayer(player);
                        } else {
                            this.entity.setLastHurtByPlayer((Player)null);
                        }
                    }
                }
            }

            if (!source.is(DamageTypeTags.NO_IMPACT)) {
                markHurt(this.entity);
            }

            if (entity1 != null && !source.is(DamageTypeTags.IS_EXPLOSION)) {
                double d0 = entity1.getX() - this.entity.getX();

                double d1;
                for(d1 = entity1.getZ() - this.entity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
                    d0 = (Math.random() - Math.random()) * 0.01;
                }

                this.entity.knockback((double)0.4F, d0, d1);
                if (!flag) {
                    this.entity.indicateDamage(d0, d1);
                }
            }

            this.entity.level().broadcastDamageEvent(this.entity, source);
            if (this.entity.isDeadOrDying()) {
                if (!checkTotemDeathProtection(this.entity, source)) {
                    SoundEvent soundevent = getDeathSound(this.entity);
                    if (soundevent != null && special) {
                        this.entity.playSound(soundevent, getSoundVolume(this.entity), this.entity.getVoicePitch());
                    }

                    this.die(this.entity, source);
                }
            } else if (special) {
                playHurtSound(this.entity, source);
            }

            accessorEntity.setLastDamageSource(source);
            accessorEntity.setLastDamageStamp(this.entity.level().getGameTime());
            if (this.entity instanceof ServerPlayer) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)this.entity, source, f, amount, flag);
            }

            if (entity1 instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, this.entity, source, f, amount, flag);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static float getSoundVolume(LivingEntity living) {
        return ((AccessorLivingEntity)living).invokeGetSoundVolume();
    }
    private static boolean checkTotemDeathProtection(LivingEntity living, DamageSource source) {
        return ((AccessorLivingEntity)living).callCheckTotemDeathProtection(source);
    }
    private static SoundEvent getDeathSound(LivingEntity living) {
        return ((AccessorLivingEntity)living).invokeGetDeathSound();
    }
    private static void playHurtSound(LivingEntity living, DamageSource source) {
        ((AccessorLivingEntity)living).invokePlayHurtSound(source);
    }
    private static void markHurt(Entity entity) {
        try {
            InstrumentationHelper.IMPL_LOOKUP().findVirtual(Entity.class, FeMapping.Entity$METHOD$markHurt.get(), MethodType.methodType(Void.TYPE)).bindTo(entity).invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void actuallyHurt0(DamageSource source, float amount, boolean special) {
        if (!(amount <= 0.0F)) {
                if (!special && source.is(ModDamageSources.FE_SOURCE)) {
                    amount = (float)((double)amount * ((double)2.0F - this.entity.getAttributeValue(ModAttributes.getFeDamageResistance())));
                }

                this.entity.getCombatTracker().recordDamage(source, amount);
                if (amount == Float.POSITIVE_INFINITY) {
                    this.entity.setHealth(Float.NEGATIVE_INFINITY);
                    if (special) {
                        anotherCatchSetTrueHealth(this.entity, Float.NEGATIVE_INFINITY);
                    }
                } else {
                    this.entity.setHealth(Math.min(this.entity.getHealth() - amount, this.entity.getMaxHealth()));
                    if (special) {
                        anotherCatchSetTrueHealth(this.entity, Math.min(this.entity.getHealth() - amount, this.entity.getMaxHealth()));
                    }
                }

                this.entity.gameEvent(GameEvent.ENTITY_DAMAGE);
                if (!this.entity.isDeadOrDying()) {
                    this.entity.deathTime = 0;
                }


        }
    }


    public static void anotherCatchSetTrueHealth(LivingEntity living, float value) {
        checkAndSave(living);
        IndexAndType indexAndType = LivingEntityExpandedContext.getIndexAndType(living);
        SynchedEntityData eD = living.getEntityData();
        SyncEntityDataAccessor accessor = (SyncEntityDataAccessor)eD;
        if (indexAndType != null) {
            if (indexAndType.isFloat()) {
                set(accessor, ((SynchedEntityData.DataItem)accessor.itemsById().get(indexAndType.index())).getAccessor(), value);
            } else {
                set(accessor, ((SynchedEntityData.DataItem)accessor.itemsById().get(indexAndType.index())).getAccessor(), (double)value);
            }
        }

    }
}
