package com.huepampalo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.component.CustomData;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DarkSister extends SwordItem implements GeoItem {

    public static final Map<Player, Boolean> charging = new HashMap<>();

    private static final List<ParticleOptions> LIGHTNING_PARTICLES = List.of(
            // ParticleTypes.CRIT,
            ParticleTypes.ENCHANT,
            // ParticleTypes.ELECTRIC_SPARK,
            // ParticleTypes.END_ROD,
            // ParticleTypes.SOUL_FIRE_FLAME,
            // ParticleTypes.DRAGON_BREATH,

            // боевые
            // ParticleTypes.SWEEP_ATTACK,
            // ParticleTypes.DAMAGE_INDICATOR,
            // ParticleTypes.HAPPY_VILLAGER,

            // магические
            ParticleTypes.PORTAL,
            ParticleTypes.REVERSE_PORTAL,
            // ParticleTypes.WITCH,
            ParticleTypes.ENCHANTED_HIT,

            // огонь / энергия
            // ParticleTypes.FLAME,
            // ParticleTypes.SMALL_FLAME,
            // ParticleTypes.LAVA,
            ParticleTypes.HEART

    // дым / тьма
    // ParticleTypes.LARGE_SMOKE,
    // ParticleTypes.SMOKE,
    // ParticleTypes.WHITE_ASH,

    // взрыв / удар
    // ParticleTypes.EXPLOSION,
    // ParticleTypes.EXPLOSION_EMITTER,
    // ParticleTypes.POOF
    );

    private void spawnParticlesAttack(
            Level level,
            Player player,
            int distance) {

        if (!(level instanceof ServerLevel serverLevel))
            return;

        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();

        for (int i = 1; i <= distance; i++) {

            Vec3 pos = start.add(
                    direction.scale(i));

            /*
             * ПАРТИКЛЫ
             */

            ParticleOptions particle1 = LIGHTNING_PARTICLES.get(
                    serverLevel.random.nextInt(
                            LIGHTNING_PARTICLES.size()));

            ParticleOptions particle2 = LIGHTNING_PARTICLES.get(
                    serverLevel.random.nextInt(
                            LIGHTNING_PARTICLES.size()));

            serverLevel.sendParticles(
                    particle1,
                    pos.x,
                    pos.y,
                    pos.z,
                    15,
                    0.15,
                    0.15,
                    0.15,
                    0.02);

            serverLevel.sendParticles(
                    particle2,
                    pos.x,
                    pos.y,
                    pos.z,
                    3,
                    0.1,
                    0.1,
                    0.1,
                    0.01);

            /*
             * УРОН ПО ОБЛАСТИ
             */

            AABB hitBox = new AABB(
                    pos.x - 1,
                    pos.y - 1,
                    pos.z - 1,
                    pos.x + 1,
                    pos.y + 1,
                    pos.z + 1);

            for (LivingEntity target : serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    hitBox)) {

                // чтобы игрок не бил сам себя

                if (target == player)
                    continue;

                target.hurt(
                        serverLevel.damageSources()
                                .playerAttack(player),
                        50F);

                /*
                 * небольшой отлет
                 */

                target.setDeltaMovement(
                        direction.scale(0.8));
            }
        }
    }

    private void spawnSwordLightning(
            Level level,
            Player player,
            double distance) {

        Vec3 start = player.getEyePosition();

        Vec3 forward = player.getLookAngle().normalize();

        // 5 лучей веером
        Vec3[] directions = new Vec3[] {
                forward.yRot((float) Math.toRadians(-24)),
                forward.yRot((float) Math.toRadians(-12)),
                forward,
                forward.yRot((float) Math.toRadians(12)),
                forward.yRot((float) Math.toRadians(24))
        };

        level.playSound(
                null,
                player.blockPosition(),
                SoundEvents.LIGHTNING_BOLT_THUNDER,
                SoundSource.PLAYERS,
                1.3F,
                1.6F);

        for (Vec3 direction : directions) {

            // начинаем не возле игрока,
            // а в 4 блоках перед ним
            for (double i = 4; i <= distance; i += 2) {

                Vec3 pos = start.add(direction.scale(i));

                LightningBolt lightning = new LightningBolt(
                        EntityType.LIGHTNING_BOLT,
                        level);

                if (player instanceof ServerPlayer sp) {
                    lightning.setCause(sp);
                }

                lightning.setVisualOnly(true); // молния не наносит ванильный урон

                lightning.setPos(
                        pos.x,
                        pos.y,
                        pos.z);

                level.addFreshEntity(lightning);

                // Наш собственный урон
                AABB area = new AABB(
                        pos.x - 1.3,
                        pos.y - 1.3,
                        pos.z - 1.3,
                        pos.x + 1.3,
                        pos.y + 1.3,
                        pos.z + 1.3);

                for (LivingEntity target : level.getEntitiesOfClass(
                        LivingEntity.class,
                        area)) {

                    // Не дамажим себя
                    if (target == player)
                        continue;

                    target.hurt(
                            level.damageSources().lightningBolt(),
                            10.0F);

                    target.setRemainingFireTicks(80);
                }
            }
        }
    }

    private void spawnLightningWave(
            Level level,
            Player player) {

        if (!(level instanceof ServerLevel serverLevel))
            return;

        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();

        for (int i = 0; i < 20; i++) {

            final int step = i;

            serverLevel.getServer().tell(new TickTask(
                    serverLevel.getServer().getTickCount() + step,
                    () -> {

                        double distance = 4 + step;

                        Vec3 pos = start.add(direction.scale(distance));

                        LightningBolt lightning = new LightningBolt(
                                EntityType.LIGHTNING_BOLT,
                                serverLevel);
                        lightning.setVisualOnly(true);

                        if (player instanceof ServerPlayer sp) {
                            lightning.setCause(sp);
                        }

                        lightning.setPos(
                                pos.x,
                                pos.y,
                                pos.z);

                        serverLevel.addFreshEntity(lightning);

                        AABB area = new AABB(
                                pos.x - 1,
                                pos.y - 1,
                                pos.z - 1,
                                pos.x + 1,
                                pos.y + 1,
                                pos.z + 1);

                        for (LivingEntity target : serverLevel.getEntitiesOfClass(
                                LivingEntity.class,
                                area)) {

                            if (target == player)
                                continue;

                            target.hurt(
                                    serverLevel.damageSources().lightningBolt(),
                                    10F);
                        }

                    }));
        }
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final Map<LivingEntity, Integer> delayedHits = new HashMap<>();

    public DarkSister() {

        super(
                Tiers.NETHERITE,
                new Properties()
                        .durability(2500)
                        .rarity(net.minecraft.world.item.Rarity.EPIC)
                        .attributes(
                                SwordItem.createAttributes(
                                        Tiers.NETHERITE,
                                        20,
                                        1)));
    }

    @Override
    public boolean isFoil(ItemStack stack) {

        return true;
    }

    // Основное использование
    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }

    public int getUseDuration(ItemStack stack) {

        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {

        return UseAnim.NONE;
    }

    /*
     * Пока держим ПКМ
     * Скорость III
     */

    @Override
    public void onUseTick(
            Level level,
            LivingEntity livingEntity,
            ItemStack stack,
            int remainingUseTicks) {

        if (!level.isClientSide()
                && livingEntity instanceof Player player) {
            int dice = getDiceFace(player);

            if (dice == 1) {
                player.addEffect(
                        new MobEffectInstance(
                                MobEffects.SLOW_FALLING,
                                10,
                                2,
                                false,
                                false,
                                false));
            }

            player.addEffect(
                    new MobEffectInstance(
                            MobEffects.MOVEMENT_SPEED,
                            10,
                            2,
                            false,
                            false,
                            false));

        }
    }

    /*
     * Отпускание ПКМ
     */

    @Override
    public void releaseUsing(
            ItemStack stack,
            Level level,
            LivingEntity entity,
            int timeLeft) {

        if (!(entity instanceof Player player))
            return;

        // player.removeEffect(
        // MobEffects.MOVEMENT_SPEED);

        // player.removeEffect(
        // MobEffects.SLOW_FALLING);

        if (!level.isClientSide()) {

            int dice = getDiceFace(player);

            /*
             * ГРАНЬ 1
             *
             * Телепорт
             */

            if (dice == 1) {

                Vec3 direction = player.getLookAngle()
                        .normalize();

                Vec3 teleport = player.position()
                        .add(
                                direction.x * 15,
                                direction.y * 15,
                                direction.z * 15);

                player.teleportTo(
                        teleport.x,
                        teleport.y,
                        teleport.z);
            }

            if (dice == 5) {
                // spawnSwordLightning(
                // level,
                // player,
                // 20);

                // spawnLightningWave(level, player);

                spawnParticlesAttack(level, player, 20);
                // player.removeEffect(
                // MobEffects.SLOW_FALLING);
            }

            /*
             * ГРАНЬ 6
             *
             * 6 огненных шаров
             */

            if (dice == 6) {

                Vec3 look = player.getLookAngle()
                        .normalize();

                for (int i = 0; i < 6; i++) {

                    double spreadX = (level.random.nextDouble() - 0.5) * 0.15;

                    double spreadY = (level.random.nextDouble() - 0.5) * 0.15;

                    double spreadZ = (level.random.nextDouble() - 0.5) * 0.15;

                    // скорость шара
                    Vec3 motion = new Vec3(
                            look.x + spreadX,
                            look.y + spreadY,
                            look.z + spreadZ)
                            .normalize()
                            .scale(10.0);

                    SmallFireball fireball = new SmallFireball(
                            level,
                            player,
                            motion);

                    fireball.setPos(
                            player.getX(),
                            player.getEyeY(),
                            player.getZ());

                    level.addFreshEntity(
                            fireball);
                }
            }
        }
    }

    /*
     * Получение грани кубика
     */

    private int getDiceFace(Player player) {

        for (ItemStack stack : player.getInventory().items) {

            if (stack.getItem() instanceof HuepampaloItem) {

                CustomData data = stack.getOrDefault(
                        DataComponents.CUSTOM_DATA,
                        CustomData.EMPTY);

                CompoundTag tag = data.copyTag();

                return tag.getInt("DiceFace");
            }
        }

        return 0;
    }

    @Override
    public boolean hurtEnemy(
            ItemStack stack,
            LivingEntity target,
            LivingEntity attacker) {

        ModPlayerTick.delayedHits.put(
                target,
                3);

        Vec3 dir = attacker.getLookAngle()
                .normalize();

        target.setDeltaMovement(
                dir.scale(2.0));

        target.hurt(
                attacker.damageSources()
                        .playerAttack((Player) attacker),
                9.0F);

        return super.hurtEnemy(
                stack,
                target,
                attacker);
    }

    @Override
    public void registerControllers(
            ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {

        return cache;
    }
}