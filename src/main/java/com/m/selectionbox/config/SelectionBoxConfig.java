package com.m.selectionbox.config;

import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.ConfigTab;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigEnum;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

import java.util.List;

public final class SelectionBoxConfig extends SimpleConfigs {
    private static final SelectionBoxConfig INSTANCE;
    public static final List<ConfigHotkey> HOTKEYS;
    public static final List<ConfigBase<?>> VALUES;
    public static final List<ConfigBase<?>> COMMON_VALUES;
    public static final List<ConfigBase<?>> GRADIENT_VALUES;
    public static final List<ConfigBase<?>> SOLID_VALUES;

    public static final ConfigHotkey OPEN_CONFIG_SCREEN =
            new ConfigHotkey("selectionBox.openConfig", KeybindMulti.fromStorageString("M,B", KeybindSettings.RELEASE), "Open this config screen.");

    public static final ConfigBoolean ENABLE_BLOCK_OUTLINE =
            new ConfigBoolean("selectionBox.enableBlockOutline", true, "Render colored block selection outlines.");
    public static final ConfigBoolean ENABLE_ENTITY_OUTLINE =
            new ConfigBoolean("selectionBox.enableEntityOutline", true, "Render colored F3+B entity outlines.");
    public static final ConfigEnum<EntityHitboxRenderMode> ENTITY_HITBOX_RENDER_MODE =
            new ConfigEnum<>("selectionBox.entityHitboxRenderMode", EntityHitboxRenderMode.MODERN, "Render entity hitboxes before entities like old versions or after entities like modern versions.");

    public static final ConfigEnum<StylePreset> BLOCK_PRESET =
            new ConfigEnum<>("selectionBox.blockPreset", StylePreset.CUSTOM, "Preset style for block selection highlights.");
    public static final ConfigEnum<OutlineColorMode> COLOR_MODE =
            new ConfigEnum<>("selectionBox.colorMode", OutlineColorMode.GRADIENT, "Use an animated gradient or a single solid block outline color.");
    public static final ConfigColor SOLID_COLOR =
            new ConfigColor("selectionBox.solidColor", "#FFFF4D4D", "The single block outline color used in solid mode.");

    public static final ConfigColor START_COLOR =
            new ConfigColor("selectionBox.gradientStartColor", "#FFFF4D4D", "Block gradient start color.");
    public static final ConfigColor END_COLOR =
            new ConfigColor("selectionBox.gradientEndColor", "#FF4DD2FF", "Block gradient end color. Ignored in solid mode.");
    public static final ConfigEnum<GradientInterpolationMode> INTERPOLATION_MODE =
            new ConfigEnum<>("selectionBox.gradientMode", GradientInterpolationMode.HSV, "RGB or HSV block gradient interpolation.");

    public static final ConfigDouble CYCLE_SECONDS =
            new ConfigDouble("selectionBox.cycleSeconds", 3.0D, 0.2D, 12.0D, true, "Time for one full block color cycle.");
    public static final ConfigDouble LINE_WIDTH =
            new ConfigDouble("selectionBox.lineWidth", 3.0D, 2.0D, 6.0D, true, "Block outline line width.");
    public static final ConfigInteger ALPHA =
            new ConfigInteger("selectionBox.alpha", 204, 16, 255, true, "Block outline alpha (opacity).");
    public static final ConfigEnum<FillRenderMode> BLOCK_FILL_MODE =
            new ConfigEnum<>("selectionBox.blockFillMode", FillRenderMode.OUTLINE_ONLY, "Render only the block outline, only the fill, or both.");
    public static final ConfigEnum<OutlineVisibilityMode> BLOCK_OUTLINE_VISIBILITY =
            new ConfigEnum<>("selectionBox.blockOutlineVisibility", OutlineVisibilityMode.NORMAL, "Whether block outlines respect depth or render through walls.");
    public static final ConfigEnum<OutlineVisibilityMode> BLOCK_FILL_VISIBILITY =
            new ConfigEnum<>("selectionBox.blockFillVisibility", OutlineVisibilityMode.NORMAL, "Whether block fill respects depth or renders through walls.");
    public static final ConfigEnum<AnimationMode> BLOCK_ANIMATION_MODE =
            new ConfigEnum<>("selectionBox.blockAnimationMode", AnimationMode.ANIMATED, "Block outline animation behavior.");

    public static final ConfigEnum<StylePreset> ENTITY_PRESET =
            new ConfigEnum<>("selectionBox.entityPreset", StylePreset.CUSTOM, "Preset style for entity hitbox highlights.");
    public static final ConfigEnum<OutlineColorMode> ENTITY_COLOR_MODE =
            new ConfigEnum<>("selectionBox.entityColorMode", OutlineColorMode.GRADIENT, "Use an animated gradient or a single solid entity outline color.");
    public static final ConfigColor ENTITY_SOLID_COLOR =
            new ConfigColor("selectionBox.entitySolidColor", "#FFFF4D4D", "The single entity outline color used in solid mode.");
    public static final ConfigColor ENTITY_START_COLOR =
            new ConfigColor("selectionBox.entityGradientStartColor", "#FFFF4D4D", "Entity gradient start color.");
    public static final ConfigColor ENTITY_END_COLOR =
            new ConfigColor("selectionBox.entityGradientEndColor", "#FF4DD2FF", "Entity gradient end color. Ignored in solid mode.");
    public static final ConfigEnum<GradientInterpolationMode> ENTITY_INTERPOLATION_MODE =
            new ConfigEnum<>("selectionBox.entityGradientMode", GradientInterpolationMode.HSV, "RGB or HSV entity gradient interpolation.");
    public static final ConfigDouble ENTITY_CYCLE_SECONDS =
            new ConfigDouble("selectionBox.entityCycleSeconds", 3.0D, 0.2D, 12.0D, true, "Time for one full entity color cycle.");
    public static final ConfigDouble ENTITY_LINE_WIDTH =
            new ConfigDouble("selectionBox.entityLineWidth", 3.0D, 2.0D, 6.0D, true, "Entity outline line width.");
    public static final ConfigInteger ENTITY_ALPHA =
            new ConfigInteger("selectionBox.entityAlpha", 204, 16, 255, true, "Entity outline alpha (opacity).");
    public static final ConfigEnum<FillRenderMode> ENTITY_FILL_MODE =
            new ConfigEnum<>("selectionBox.entityFillMode", FillRenderMode.OUTLINE_ONLY, "Render only the entity outline, only the fill, or both.");
    public static final ConfigEnum<OutlineVisibilityMode> ENTITY_OUTLINE_VISIBILITY =
            new ConfigEnum<>("selectionBox.entityOutlineVisibility", OutlineVisibilityMode.NORMAL, "Whether entity outlines respect depth or render through walls.");
    public static final ConfigEnum<OutlineVisibilityMode> ENTITY_FILL_VISIBILITY =
            new ConfigEnum<>("selectionBox.entityFillVisibility", OutlineVisibilityMode.NORMAL, "Whether entity fill respects depth or renders through walls.");
    public static final ConfigEnum<AnimationMode> ENTITY_ANIMATION_MODE =
            new ConfigEnum<>("selectionBox.entityAnimationMode", AnimationMode.ANIMATED, "Entity outline animation behavior.");

    static {
        HOTKEYS = List.of(OPEN_CONFIG_SCREEN);
        COMMON_VALUES = List.of(
                ENABLE_BLOCK_OUTLINE,
                ENABLE_ENTITY_OUTLINE,
                ENTITY_HITBOX_RENDER_MODE,
                BLOCK_PRESET,
                BLOCK_FILL_MODE,
                BLOCK_OUTLINE_VISIBILITY,
                BLOCK_FILL_VISIBILITY,
                BLOCK_ANIMATION_MODE,
                LINE_WIDTH,
                ALPHA,
                ENTITY_PRESET,
                ENTITY_FILL_MODE,
                ENTITY_OUTLINE_VISIBILITY,
                ENTITY_FILL_VISIBILITY,
                ENTITY_ANIMATION_MODE,
                ENTITY_LINE_WIDTH,
                ENTITY_ALPHA
        );
        GRADIENT_VALUES = List.of(
                START_COLOR,
                END_COLOR,
                INTERPOLATION_MODE,
                CYCLE_SECONDS,
                ENTITY_START_COLOR,
                ENTITY_END_COLOR,
                ENTITY_INTERPOLATION_MODE,
                ENTITY_CYCLE_SECONDS
        );
        SOLID_VALUES = List.of(
                COLOR_MODE,
                SOLID_COLOR,
                ENTITY_COLOR_MODE,
                ENTITY_SOLID_COLOR
        );
        VALUES = List.of(
                ENABLE_BLOCK_OUTLINE,
                ENABLE_ENTITY_OUTLINE,
                ENTITY_HITBOX_RENDER_MODE,
                BLOCK_PRESET,
                COLOR_MODE,
                SOLID_COLOR,
                START_COLOR,
                END_COLOR,
                INTERPOLATION_MODE,
                CYCLE_SECONDS,
                LINE_WIDTH,
                ALPHA,
                BLOCK_FILL_MODE,
                BLOCK_OUTLINE_VISIBILITY,
                BLOCK_FILL_VISIBILITY,
                BLOCK_ANIMATION_MODE,
                ENTITY_PRESET,
                ENTITY_COLOR_MODE,
                ENTITY_SOLID_COLOR,
                ENTITY_START_COLOR,
                ENTITY_END_COLOR,
                ENTITY_INTERPOLATION_MODE,
                ENTITY_CYCLE_SECONDS,
                ENTITY_LINE_WIDTH,
                ENTITY_ALPHA,
                ENTITY_FILL_MODE,
                ENTITY_OUTLINE_VISIBILITY,
                ENTITY_FILL_VISIBILITY,
                ENTITY_ANIMATION_MODE
        );
        INSTANCE = new SelectionBoxConfig("selection_box", HOTKEYS, VALUES);
    }

    private SelectionBoxConfig(String name, List<ConfigHotkey> hotkeys, List<?> values) {
        super(name, hotkeys, values, "Configure outline colors, animation, entity hitbox render mode and width.");
    }

    public static SelectionBoxConfig getInstance() {
        return INSTANCE;
    }

    @Override
    public List<ConfigTab> getConfigTabs() {
        return List.of(
                new ConfigTab("common", COMMON_VALUES),
                new ConfigTab("gradient", GRADIENT_VALUES),
                new ConfigTab("solid", SOLID_VALUES),
                new ConfigTab("hotkey", HOTKEYS)
        );
    }
}
