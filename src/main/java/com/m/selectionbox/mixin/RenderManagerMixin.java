package com.m.selectionbox.mixin;

import com.m.selectionbox.render.SelectionBoxStyle;
import net.minecraft.AxisAlignedBB;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces entity hitbox rendering (F3+B) with configurable timing and colors.
 */
@Mixin(RenderManager.class)
public class RenderManagerMixin {
    @Shadow
    private static boolean field_85095_o;

    @Inject(method = "func_85094_b", at = @At("HEAD"), cancellable = true)
    private void renderEntityHitboxBeforeEntity(Entity par1Entity, double par2, double par4, double par6, float par8, float par9, CallbackInfo ci) {
        // Fully take over entity debug hitbox rendering so we can control render timing.
        ci.cancel();

        if (!field_85095_o || par1Entity.isInvisible() || !SelectionBoxStyle.renderEntityOutlineBeforeEntity()) {
            return;
        }

        renderOutline(par1Entity, par2, par4, par6);
    }

    @Inject(
            method = "renderEntityWithPosYaw",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Render;doRenderShadowAndFire(Lnet/minecraft/Entity;DDDFF)V", shift = At.Shift.AFTER)
    )
    private void renderEntityHitboxAfterEntity(Entity par1Entity, double par2, double par4, double par6, float par8, float par9, CallbackInfo ci) {
        if (!field_85095_o || par1Entity.isInvisible() || !SelectionBoxStyle.renderEntityOutlineAfterEntity()) {
            return;
        }

        renderOutline(par1Entity, par2, par4, par6);
    }

    private static void renderOutline(Entity entity, double x, double y, double z) {
        if (entity instanceof EntityClientPlayerMP) {
            y -= entity.yOffset;
        }

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(SelectionBoxStyle.getLineWidth());

        double halfWidth = entity.width / 2.0F;
        AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
                x - halfWidth,
                y,
                z - halfWidth,
                x + halfWidth,
                y + entity.height,
                z + halfWidth
        );
        SelectionBoxStyle.drawOutlinedBoundingBox(box);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }
}
