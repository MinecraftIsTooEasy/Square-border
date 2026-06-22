package com.m.selectionbox.render;

import com.m.selectionbox.config.AnimationMode;
import com.m.selectionbox.config.EntityHitboxRenderMode;
import com.m.selectionbox.config.FillRenderMode;
import com.m.selectionbox.config.GradientInterpolationMode;
import com.m.selectionbox.config.OutlineColorMode;
import com.m.selectionbox.config.OutlineVisibilityMode;
import com.m.selectionbox.config.SelectionBoxConfig;
import com.m.selectionbox.config.StylePreset;
import net.minecraft.AxisAlignedBB;
import net.minecraft.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public final class SelectionBoxStyle {
    private static final float FILL_ALPHA_SCALE = 0.22F;

    private SelectionBoxStyle() {
    }

    public static boolean renderBlockOutline() {
        return SelectionBoxConfig.ENABLE_BLOCK_OUTLINE.getBooleanValue();
    }

    public static boolean renderEntityOutline() {
        return SelectionBoxConfig.ENABLE_ENTITY_OUTLINE.getBooleanValue();
    }

    public static boolean renderEntityOutlineBeforeEntity() {
        return renderEntityOutline() && SelectionBoxConfig.ENTITY_HITBOX_RENDER_MODE.getEnumValue() == EntityHitboxRenderMode.OLD;
    }

    public static boolean renderEntityOutlineAfterEntity() {
        return renderEntityOutline() && SelectionBoxConfig.ENTITY_HITBOX_RENDER_MODE.getEnumValue() == EntityHitboxRenderMode.MODERN;
    }

    public static float getLineWidth(SelectionBoxTarget target) {
        return resolveStyle(target).lineWidth;
    }

    public static void drawBoundingBox(AxisAlignedBB box, SelectionBoxTarget target) {
        Style style = resolveStyle(target);
        float time = getAnimationProgress(style);

        if (style.drawFill()) {
            withDepthMode(style.fillVisibility, () -> drawFilledBoundingBox(box, style, time));
        }
        if (style.drawOutline()) {
            withDepthMode(style.outlineVisibility, () -> drawOutlinedBoundingBox(box, style, time));
        }
    }

    private static void withDepthMode(OutlineVisibilityMode visibilityMode, Runnable drawAction) {
        boolean throughWalls = visibilityMode == OutlineVisibilityMode.THROUGH_WALLS;
        if (throughWalls) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        try {
            drawAction.run();
        } finally {
            if (throughWalls) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        }
    }

    private static float getAnimationProgress(Style style) {
        if (style.animationMode == AnimationMode.OFF || style.animationMode == AnimationMode.STATIC_GRADIENT || style.animationMode == AnimationMode.BREATHING_ALPHA) {
            return 0.0F;
        }

        double cycleMillis = style.cycleSeconds * 1000.0D;
        double progress = (System.currentTimeMillis() / cycleMillis) % 1.0D;
        return (float) progress;
    }

    private static float getBreathingAlphaScale(Style style) {
        if (style.animationMode != AnimationMode.BREATHING_ALPHA) {
            return 1.0F;
        }

        double cycleMillis = style.cycleSeconds * 1000.0D;
        double progress = (System.currentTimeMillis() / cycleMillis) % 1.0D;
        double wave = (Math.sin(progress * Math.PI * 2.0D) + 1.0D) * 0.5D;
        return (float) (0.55D + wave * 0.45D);
    }

    private static void drawOutlinedBoundingBox(AxisAlignedBB box, Style style, float time) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(3);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.minZ, time, 0.0F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.minZ, time, 0.25F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.maxZ, time, 0.5F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.maxZ, time, 0.75F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.minZ, time, 1.0F, style.alpha);
        tessellator.draw();

        tessellator.startDrawing(3);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.minZ, time, 0.125F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.minZ, time, 0.375F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.maxZ, time, 0.625F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.maxZ, time, 0.875F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.minZ, time, 0.125F, style.alpha);
        tessellator.draw();

        tessellator.startDrawing(1);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.minZ, time, 0.0F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.minZ, time, 0.0F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.minZ, time, 0.25F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.minZ, time, 0.25F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.maxZ, time, 0.5F, style.alpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.maxZ, time, 0.5F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.maxZ, time, 0.75F, style.alpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.maxZ, time, 0.75F, style.alpha);
        tessellator.draw();
    }

    private static void drawFilledBoundingBox(AxisAlignedBB box, Style style, float time) {
        Tessellator tessellator = Tessellator.instance;
        float fillAlpha = style.alpha * FILL_ALPHA_SCALE;

        tessellator.startDrawingQuads();
        addStyledVertex(tessellator, style, box.minX, box.minY, box.minZ, time, 0.0F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.minZ, time, 0.25F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.maxZ, time, 0.5F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.maxZ, time, 0.75F, fillAlpha);

        addStyledVertex(tessellator, style, box.minX, box.maxY, box.minZ, time, 0.125F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.maxZ, time, 0.875F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.maxZ, time, 0.625F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.minZ, time, 0.375F, fillAlpha);

        addStyledVertex(tessellator, style, box.minX, box.minY, box.minZ, time, 0.0F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.minY, box.maxZ, time, 0.75F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.maxZ, time, 0.875F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.minZ, time, 0.125F, fillAlpha);

        addStyledVertex(tessellator, style, box.maxX, box.minY, box.minZ, time, 0.25F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.minZ, time, 0.375F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.maxZ, time, 0.625F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.maxZ, time, 0.5F, fillAlpha);

        addStyledVertex(tessellator, style, box.minX, box.minY, box.minZ, time, 0.0F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.minZ, time, 0.125F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.minZ, time, 0.375F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.minZ, time, 0.25F, fillAlpha);

        addStyledVertex(tessellator, style, box.minX, box.minY, box.maxZ, time, 0.75F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.minY, box.maxZ, time, 0.5F, fillAlpha);
        addStyledVertex(tessellator, style, box.maxX, box.maxY, box.maxZ, time, 0.625F, fillAlpha);
        addStyledVertex(tessellator, style, box.minX, box.maxY, box.maxZ, time, 0.875F, fillAlpha);
        tessellator.draw();
    }

    private static void addStyledVertex(Tessellator tessellator, Style style, double x, double y, double z, float timeBase, float gradientOffset, float alpha) {
        int rgb = resolveColor(style, timeBase, gradientOffset);
        float scaledAlpha = clamp01(alpha * getBreathingAlphaScale(style));

        float red = ((rgb >> 16) & 0xFF) / 255.0F;
        float green = ((rgb >> 8) & 0xFF) / 255.0F;
        float blue = (rgb & 0xFF) / 255.0F;
        tessellator.setColorRGBA_F(red, green, blue, scaledAlpha);
        tessellator.addVertex(x, y, z);
    }

    private static int resolveColor(Style style, float timeBase, float gradientOffset) {
        if (style.preset == StylePreset.RAINBOW) {
            float position = wrap01(timeBase + gradientOffset);
            return Color.HSBtoRGB(position, 0.85F, 1.0F) & 0x00FFFFFF;
        }
        if (style.animationMode == AnimationMode.OFF) {
            return style.colorMode == OutlineColorMode.SOLID ? style.solidColor & 0x00FFFFFF : style.startColor & 0x00FFFFFF;
        }
        if (style.colorMode == OutlineColorMode.SOLID) {
            return style.solidColor & 0x00FFFFFF;
        }

        float position = wrap01(timeBase + gradientOffset);
        return interpolateBetween(style.startColor, style.endColor, position, style.interpolationMode);
    }

    private static int interpolateBetween(int start, int end, float t, GradientInterpolationMode mode) {
        if (mode == GradientInterpolationMode.HSV) {
            return interpolateHsv(start, end, t);
        }
        return interpolateRgb(start, end, t);
    }

    private static int interpolateRgb(int start, int end, float t) {
        int sr = (start >> 16) & 0xFF;
        int sg = (start >> 8) & 0xFF;
        int sb = start & 0xFF;

        int er = (end >> 16) & 0xFF;
        int eg = (end >> 8) & 0xFF;
        int eb = end & 0xFF;

        int r = lerpInt(sr, er, t);
        int g = lerpInt(sg, eg, t);
        int b = lerpInt(sb, eb, t);
        return (r << 16) | (g << 8) | b;
    }

    private static int interpolateHsv(int start, int end, float t) {
        float[] a = Color.RGBtoHSB((start >> 16) & 0xFF, (start >> 8) & 0xFF, start & 0xFF, null);
        float[] b = Color.RGBtoHSB((end >> 16) & 0xFF, (end >> 8) & 0xFF, end & 0xFF, null);

        float hue = lerpHue(a[0], b[0], t);
        float saturation = lerpFloat(a[1], b[1], t);
        float brightness = lerpFloat(a[2], b[2], t);
        return Color.HSBtoRGB(hue, saturation, brightness) & 0x00FFFFFF;
    }

    private static Style resolveStyle(SelectionBoxTarget target) {
        Style style = target == SelectionBoxTarget.BLOCK ? resolveBlockStyle() : resolveEntityStyle();
        return style.preset == StylePreset.CUSTOM ? style : applyPreset(style, style.preset);
    }

    private static Style resolveBlockStyle() {
        return new Style(
                SelectionBoxConfig.BLOCK_PRESET.getEnumValue(),
                SelectionBoxConfig.COLOR_MODE.getEnumValue(),
                SelectionBoxConfig.SOLID_COLOR.getColorInteger(),
                SelectionBoxConfig.START_COLOR.getColorInteger(),
                SelectionBoxConfig.END_COLOR.getColorInteger(),
                SelectionBoxConfig.INTERPOLATION_MODE.getEnumValue(),
                SelectionBoxConfig.CYCLE_SECONDS.getDoubleValue(),
                SelectionBoxConfig.LINE_WIDTH.getDoubleValue(),
                SelectionBoxConfig.ALPHA.getIntegerValue(),
                SelectionBoxConfig.BLOCK_FILL_MODE.getEnumValue(),
                SelectionBoxConfig.BLOCK_OUTLINE_VISIBILITY.getEnumValue(),
                SelectionBoxConfig.BLOCK_FILL_VISIBILITY.getEnumValue(),
                SelectionBoxConfig.BLOCK_ANIMATION_MODE.getEnumValue()
        );
    }

    private static Style resolveEntityStyle() {
        return new Style(
                SelectionBoxConfig.ENTITY_PRESET.getEnumValue(),
                SelectionBoxConfig.ENTITY_COLOR_MODE.getEnumValue(),
                SelectionBoxConfig.ENTITY_SOLID_COLOR.getColorInteger(),
                SelectionBoxConfig.ENTITY_START_COLOR.getColorInteger(),
                SelectionBoxConfig.ENTITY_END_COLOR.getColorInteger(),
                SelectionBoxConfig.ENTITY_INTERPOLATION_MODE.getEnumValue(),
                SelectionBoxConfig.ENTITY_CYCLE_SECONDS.getDoubleValue(),
                SelectionBoxConfig.ENTITY_LINE_WIDTH.getDoubleValue(),
                SelectionBoxConfig.ENTITY_ALPHA.getIntegerValue(),
                SelectionBoxConfig.ENTITY_FILL_MODE.getEnumValue(),
                SelectionBoxConfig.ENTITY_OUTLINE_VISIBILITY.getEnumValue(),
                SelectionBoxConfig.ENTITY_FILL_VISIBILITY.getEnumValue(),
                SelectionBoxConfig.ENTITY_ANIMATION_MODE.getEnumValue()
        );
    }

    private static Style applyPreset(Style base, StylePreset preset) {
        switch (preset) {
            case CLASSIC_BLACK:
                return base.withStyle(OutlineColorMode.SOLID, 0x000000, 0x000000, 0x000000, GradientInterpolationMode.RGB, 3.0D, 2.0D, 255, AnimationMode.OFF);
            case RED_BLUE:
                return base.withStyle(OutlineColorMode.GRADIENT, 0xFF4D4D, 0xFF4D4D, 0x4DD2FF, GradientInterpolationMode.HSV, 3.0D, 3.0D, 204, AnimationMode.ANIMATED);
            case RAINBOW:
                return base.withStyle(OutlineColorMode.GRADIENT, 0xFF4D4D, 0xFF4D4D, 0x4DD2FF, GradientInterpolationMode.HSV, 2.2D, 3.0D, 220, AnimationMode.ANIMATED);
            case LOW_ALPHA:
                return base.withStyle(OutlineColorMode.GRADIENT, 0xFFFFFF, 0xFFFFFF, 0x4DD2FF, GradientInterpolationMode.RGB, 4.0D, 2.0D, 96, AnimationMode.STATIC_GRADIENT);
            case BOLD:
                return base.withStyle(OutlineColorMode.SOLID, 0xFFE066, 0xFFE066, 0xFFE066, GradientInterpolationMode.RGB, 3.0D, 5.0D, 255, AnimationMode.BREATHING_ALPHA);
            default:
                return base;
        }
    }

    private static int lerpInt(int a, int b, float t) {
        return Math.round(a + (b - a) * t);
    }

    private static float lerpFloat(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float lerpHue(float startHue, float endHue, float t) {
        float delta = endHue - startHue;
        if (delta > 0.5F) {
            delta -= 1.0F;
        } else if (delta < -0.5F) {
            delta += 1.0F;
        }
        return wrap01(startHue + delta * t);
    }

    private static float wrap01(float value) {
        float wrapped = value % 1.0F;
        if (wrapped < 0.0F) {
            wrapped += 1.0F;
        }
        return wrapped;
    }

    private static float clamp01(float value) {
        if (value < 0.0F) {
            return 0.0F;
        }
        if (value > 1.0F) {
            return 1.0F;
        }
        return value;
    }

    private static final class Style {
        private final StylePreset preset;
        private final OutlineColorMode colorMode;
        private final int solidColor;
        private final int startColor;
        private final int endColor;
        private final GradientInterpolationMode interpolationMode;
        private final double cycleSeconds;
        private final float lineWidth;
        private final float alpha;
        private final FillRenderMode fillMode;
        private final OutlineVisibilityMode outlineVisibility;
        private final OutlineVisibilityMode fillVisibility;
        private final AnimationMode animationMode;

        private Style(StylePreset preset, OutlineColorMode colorMode, int solidColor, int startColor, int endColor, GradientInterpolationMode interpolationMode, double cycleSeconds, double lineWidth, int alpha, FillRenderMode fillMode, OutlineVisibilityMode outlineVisibility, OutlineVisibilityMode fillVisibility, AnimationMode animationMode) {
            this.preset = preset;
            this.colorMode = colorMode;
            this.solidColor = solidColor & 0x00FFFFFF;
            this.startColor = startColor & 0x00FFFFFF;
            this.endColor = endColor & 0x00FFFFFF;
            this.interpolationMode = interpolationMode;
            this.cycleSeconds = Math.max(0.2D, cycleSeconds);
            this.lineWidth = (float) Math.max(2.0D, lineWidth);
            this.alpha = clamp01(alpha / 255.0F);
            this.fillMode = fillMode;
            this.outlineVisibility = outlineVisibility;
            this.fillVisibility = fillVisibility;
            this.animationMode = animationMode;
        }

        private boolean drawOutline() {
            return this.fillMode == FillRenderMode.OUTLINE_ONLY || this.fillMode == FillRenderMode.OUTLINE_AND_FILL;
        }

        private boolean drawFill() {
            return this.fillMode == FillRenderMode.FILL_ONLY || this.fillMode == FillRenderMode.OUTLINE_AND_FILL;
        }

        private Style withStyle(OutlineColorMode colorMode, int solidColor, int startColor, int endColor, GradientInterpolationMode interpolationMode, double cycleSeconds, double lineWidth, int alpha, AnimationMode animationMode) {
            return new Style(this.preset, colorMode, solidColor, startColor, endColor, interpolationMode, cycleSeconds, lineWidth, alpha, this.fillMode, this.outlineVisibility, this.fillVisibility, animationMode);
        }
    }
}
