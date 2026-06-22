package com.m.selectionbox.mixin;

import com.m.selectionbox.render.SelectionBoxStyle;
import com.m.selectionbox.render.SelectionBoxTarget;
import net.minecraft.AxisAlignedBB;
import net.minecraft.Block;
import net.minecraft.EntityPlayer;
import net.minecraft.RaycastCollision;
import net.minecraft.RenderGlobal;
import net.minecraft.WorldClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces selected block outline rendering with a configurable outline style.
 */
@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {

    @Shadow
    private WorldClient theWorld;

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    private void renderSelectionBox(EntityPlayer player, RaycastCollision rc, int pass, float partialTicks, CallbackInfo ci) {
        if (!SelectionBoxStyle.renderBlockOutline() || pass != 0 || rc == null || !rc.isBlock()) {
            return;
        }

        // Take over only when the custom outline is active; otherwise vanilla rendering remains intact.
        ci.cancel();

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_LINE_BIT);
        try {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glLineWidth(SelectionBoxStyle.getLineWidth(SelectionBoxTarget.BLOCK));
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDepthMask(false);

            float expand = 0.002F;
            Block block = rc.getBlockHit();
            double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
            double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
            double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

            AxisAlignedBB box = block
                    .getSelectedBoundingBoxFromPool(this.theWorld, rc.block_hit_x, rc.block_hit_y, rc.block_hit_z)
                    .expand(expand, expand, expand)
                    .getOffsetBoundingBox(-px, -py, -pz);
            SelectionBoxStyle.drawBoundingBox(box, SelectionBoxTarget.BLOCK);
        } finally {
            GL11.glPopAttrib();
        }
    }
}
