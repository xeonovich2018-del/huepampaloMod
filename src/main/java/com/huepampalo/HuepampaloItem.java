package com.huepampalo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HuepampaloItem extends Item {

    public HuepampaloItem(Properties properties) {
        super(properties);
    }

    // USE
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ModCounters.startDiceTimer(level);

            ItemStack stack = player.getItemInHand(hand);
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            int currentFace = customData.copyTag().getInt("DiceFace");

            if (!stack.has(DataComponents.CUSTOM_DATA)) {
                stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
            }
            if (currentFace == 5) {
                BlockHitResult hitResult = (BlockHitResult) serverPlayer.pick(100.0D, 0.0F, false);

                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos center = hitResult.getBlockPos().above();

                    // 1. Первая молния сразу
                    // сделать ее на 5 блоков ниже + чекнуть свойства молнии
                    spawnLightning(level, center, serverPlayer);

                    // 2. Через 800 мс (40 тиков) — 12 молний с разбросом

                    // level.scheduleTick(center, Blocks.AIR, delay, TickPriority.NORMAL);

                    CompletableFuture.delayedExecutor(1700, TimeUnit.MILLISECONDS).execute(() -> {
                        // Этот код выполнится через 2 секунды
                        spawnLightning(level, center, serverPlayer);
                        spawnLightning(level, center, serverPlayer);
                        spawnLightning(level, center, serverPlayer);
                        spawnLightning(level, center, serverPlayer);
                        spawnLightning(level, center, serverPlayer);

                        // for (int i = 0; i < 12; i++) {
                        // spawnLightning(level, center, serverPlayer);

                        // CompletableFuture.delayedExecutor(1200, TimeUnit.MILLISECONDS).execute(() ->
                        // {
                        // spawnLightning(level, center, serverPlayer);
                        // });
                        // }
                    });

                    CompletableFuture.delayedExecutor(1750, TimeUnit.MILLISECONDS).execute(() -> {
                        // Этот код выполнится через 2 секунды
                        spawnLightning(level, center, serverPlayer);

                    });

                    CompletableFuture.delayedExecutor(1900, TimeUnit.MILLISECONDS).execute(() -> {
                        // Этот код выполнится через 2 секунды
                        spawnLightning(level, center, serverPlayer);
                        spawnLightning(level, center, serverPlayer);

                    });

                    CompletableFuture.delayedExecutor(2000, TimeUnit.MILLISECONDS).execute(() -> {
                        // Этот код выполнится через 2 секунды
                        spawnLightning(level, center, serverPlayer);
                        spawnLightning(level, center, serverPlayer);

                    });

                    serverPlayer.sendSystemMessage(Component.literal("§b§lМОЛНИИ!!!"));
                }
            }
            // // === Добавляем модификатор скорости (убираем замедление + даём ускорение)
            // ===
            // AttributeModifier speedModifier = new AttributeModifier(
            // ResourceLocation.fromNamespaceAndPath("huepampalo", "movement_speed"),
            // 2.7, // добавляем +200% к скорости
            // AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL //
            // );
            // var attribute = serverPlayer.getAttribute(Attributes.MOVEMENT_SPEED);
            // if (attribute != null && !attribute.hasModifier(speedModifier.id())) {
            // attribute.addTransientModifier(speedModifier);
            // }
        }

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // Защита от NullPointerException
        if (!stack.has(DataComponents.CUSTOM_DATA)) {
            return UseAnim.NONE;
        }

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        int diceface = customData.copyTag().getInt("DiceFace");

        if (diceface == 5) {
            return UseAnim.SPEAR;
        }

        return UseAnim.NONE;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingUseDuration) {

        if (!level.isClientSide && entity instanceof ServerPlayer serverPlayer) {
            // === Безопасное чтение CustomData ===
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag tag = customData.copyTag();

            int currentFace = tag.getInt("DiceFace");
            if (currentFace == 0)
                currentFace = 1;
            // Рандомная грань
            int newFace = new Random().nextInt(6) + 1;

            // Сохраняем новую грань
            tag.putInt("DiceFace", newFace);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(newFace));

            // Эффекты
            serverPlayer.setHealth(serverPlayer.getMaxHealth());
            serverPlayer.sendSystemMessage(Component.literal("§aКости показали: §e" + newFace));

            if (newFace == 6) {

                serverPlayer.sendSystemMessage(Component.literal("§cУрон повышен!"));
            }

            // Звук
            BlockHitResult hitResult = (BlockHitResult) serverPlayer.pick(5.0D, 0.0F, false);
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos targetPos = hitResult.getBlockPos();
                level.playSound(null, targetPos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.5F);
            }

        }
    };

    private void spawnLightning(Level level, BlockPos pos, ServerPlayer cause) {
        LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);

        double offsetX = (level.random.nextDouble() - 0.5) * 6;
        double offsetZ = (level.random.nextDouble() - 0.5) * 6;

        lightning.setPos(pos.getX() + 0.5 + offsetX, pos.getY(), pos.getZ() + 0.5 + offsetZ);
        lightning.setCause(cause);
        level.addFreshEntity(lightning);
    }
}