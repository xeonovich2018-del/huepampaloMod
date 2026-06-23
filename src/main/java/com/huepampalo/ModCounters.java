package com.huepampalo;

import net.minecraft.world.level.Level;

public class ModCounters {

    // === Переменная, доступная из всего проекта ===
    public static int teleportCount = 0;
    // === Глобальный счетчик поднятия кубика ===
    public static long diceStartTime = 0;
    public static long lastUseTime = 0;

    /** Запускает таймер при использовании кубика */
    public static void startDiceTimer(Level level) {
        if (diceStartTime == 0) {
            diceStartTime = level.getGameTime();
        }

        if (lastUseTime == 0) {
            lastUseTime = level.getGameTime();
        }
    }

    /** Возвращает, сколько секунд прошло с момента использования кубика */
    public static long getDiceCarryTime(Level level) {
        if (diceStartTime == 0)
            return 0;
        return (level.getGameTime() - diceStartTime) / 20;
    }

    /** Возвращает, сколько тиков прошло с момента использования кубика */
    public static long getDiceCarryTicks(Level level) {
        if (diceStartTime == 0)
            return 0;
        return level.getGameTime() - diceStartTime;
    }
    // === Методы, доступные из всего проекта ===

    /** Увеличивает счётчик телепортаций на 1 */
    public static void addTeleportCounter() {
        teleportCount++;
    }

    /** Возвращает текущее количество телепортаций */
    public static int getTeleportCounter() {
        return teleportCount;
    }

    /** Сбрасывает счётчик в 0 */
    public static void resetTeleportCount() {
        teleportCount = 0;
    }
}