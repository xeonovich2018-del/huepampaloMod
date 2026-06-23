package com.huepampalo.client.mixin;

import com.huepampalo.HuepampaloItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void onRenderArmWithItem(AbstractClientPlayer player, float partialTicks,
            float pitch, InteractionHand hand, float swingProgress,
            ItemStack stack, float equippedProgress,
            PoseStack poseStack, MultiBufferSource buffer,
            int combinedLight, CallbackInfo ci) {

        if (stack.getItem() instanceof HuepampaloItem && player.isUsingItem()) {

            // === Получаем время использования ===
            float useTicks = player.getUseItemRemainingTicks();
            float time = (72000 - useTicks + partialTicks) * 0.5f; // скорость анимации

            // === Быстрое вращение как юла (по оси Y) ===
            float spinSpeed = 45f; // чем больше — тем быстрее крутится
            poseStack.mulPose(Axis.YP.rotationDegrees(time * spinSpeed));

            // === Лёгкое покачивание (как юла) ===
            float wobble = (float) Math.sin(time * 0.3f) * 8f;
            poseStack.mulPose(Axis.XP.rotationDegrees(wobble));

            // === Подпрыгивание вверх-вниз ===
            float bounce = (float) Math.sin(time * 1.8f) * 0.08f; // амплитуда подпрыгивания
            poseStack.translate(0, bounce, 0);

            // === Небольшой наклон вперёд (чтобы было видно грани) ===
            poseStack.mulPose(Axis.XP.rotationDegrees(15));
        }
    }
}