package com.huepampalo.client;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModClientMessages {

    public static void chatMessageAfterTeleport(ServerPlayer player, int teleportCount) {

        if (teleportCount < 10) {
            player.sendSystemMessage(Component.literal("Вы ..."));
        } else if (teleportCount > 100) {
            player.sendSystemMessage(Component.literal("Вы АУУУ ... " + teleportCount + " раз!"));
        }
        player.sendSystemMessage(Component.literal("Телепорт ии ии я . . .: " + teleportCount));
    }
}
