package com.huepampalo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.Blocks;

public class ModServerInteractions {

    public static void chatMessageAfterTeleport(ServerPlayer player, int teleportCount) {
        chatMessageAfterTeleport(player, teleportCount, null);
    }

    public static void chatMessageAfterTeleport(ServerPlayer player, int teleportCount, String specialMessage) {

        // 0 телепортаций
        if (teleportCount == 0) {
            player.sendSystemMessage(Component.literal("Вы телепортировались впервые!"));
        }

        // 1 телепортация
        if (teleportCount == 1) {
            player.sendSystemMessage(Component.literal("Вы телепортировались во второй раз!"));
        }

        // 2–4 телепортации
        if (teleportCount > 1 && teleportCount < 5) {
            player.sendSystemMessage(Component.literal("Вы телепортировались " + teleportCount + " раз!"));
        }

        // 5–9 телепортаций
        if (teleportCount >= 5 && teleportCount < 10) {
            player.sendSystemMessage(Component.literal("Вы телепортировались " + teleportCount + " раз! % $ # @ !"));
        }

        // Ровно 20 телепортаций
        if (teleportCount == 20) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались 20 раз! Тайна мира для вас слегка приоткрылась!"));
            player.level().setBlock(player.blockPosition().below(), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);

            // тестирую асинхронное сообщение с задержкой
            CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS).execute(() -> {
                // Этот код выполнится через 2 секунды
                player.sendSystemMessage(Component.literal("Вы чувствуете прилив сил!"));
            });

        }

        // 21–49 телепортаций
        if (teleportCount > 20 && teleportCount < 50) {
            player.sendSystemMessage(Component.literal(
                    "Сюжет начался! Когда вы уснёте - этого не будет! " + teleportCount + " раз!"));
        }

        // Ровно 50 телепортаций
        if (teleportCount == 50) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! ! ! + + +"));
        }

        // 51+ телепортаций (с парящим алмазным блоком)
        if (teleportCount > 50 && teleportCount < 100) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! ! ! + + +"));

            FallingBlockEntity fallingDiamondBlock = FallingBlockEntity.fall(
                    player.level(),
                    player.blockPosition().above().above(),
                    Blocks.DIAMOND_BLOCK.defaultBlockState());
            fallingDiamondBlock.setNoGravity(true);
            fallingDiamondBlock.setDeltaMovement(0, 0, 0);
            fallingDiamondBlock.time = 100;
            fallingDiamondBlock.dropItem = false;

            player.level().addFreshEntity(fallingDiamondBlock);
        }

        // 100-499 телепортаций
        if (teleportCount >= 100 && teleportCount < 500) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! Все только и думают, что о вас!"));
        }

        // 500–699 телепортаций
        if (teleportCount >= 500 && teleportCount < 700) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! Вы уже не можете различать где вы... !"));
        }

        // 700–899 телепортаций
        if (teleportCount >= 700 && teleportCount < 900) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! Вы - бессмертный!"));
        }

        // 900–999 телепортаций
        if (teleportCount >= 900 && teleportCount < 1000) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! Вы - бог!"));
        }

        // 1000–1999 телепортаций
        if (teleportCount >= 1000 && teleportCount < 2000) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! Вы - бессмертный!"));
        }

        // 2000+ телепортаций
        if (teleportCount >= 2000) {
            player.sendSystemMessage(Component.literal(
                    "Вы телепортировались " + teleportCount + " раз! Вы - бог!"));
        }

        // Кастомное сообщение (если есть)
        if (specialMessage != null && !specialMessage.isEmpty()) {
            player.sendSystemMessage(Component.literal(specialMessage));
        }
    }
}