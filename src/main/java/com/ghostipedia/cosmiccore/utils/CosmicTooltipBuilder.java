package com.ghostipedia.cosmiccore.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.LinkedList;
import java.util.List;

public class CosmicTooltipBuilder {
    private static final Component PERFECT = Component.translatable("frontiers.tooltip.overclock_type.perfect").withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD);
    private static final Component NON_PERFECT = Component.translatable("frontiers.tooltip.overclock_type.non_perfect").withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD);
    private static final Component NONE = Component.translatable("frontiers.tooltip.overclock_type.none").withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD);  // Can't Overclock
    private static final Component AUTHORS = Component.translatable("frontiers.tooltip.author.title");
    private static final Component AUTHOR_CODE = Component.translatable("frontiers.tooltip.author.code.prefix").withStyle(ChatFormatting.AQUA);
    private static final Component AUTHOR_ART = Component.translatable("frontiers.tooltip.author.art.prefix");
    private static final Component AUTHOR_UI = Component.translatable("frontiers.tooltip.author.ui.prefix");
    private static final Component AUTHOR_ART_UI = Component.translatable("frontiers.tooltip.author.art_ui.prefix").withStyle(ChatFormatting.GOLD);
    private static final Component AUTHOR_GHOSTI = Component.translatable("frontiers.tooltip.author.ghosti").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withObfuscated(true));
    private static final Component SEPARATOR = Component.literal("------------------------------------------------------------");
    private static final Component SEPARATOR_SHORT = Component.literal("-------------------------------------------");

    private static Component MachineType(String machineType) {
        return Component.translatable("frontiers.tooltip.machine_type_prefix",
                        Component.translatable("frontiers.tooltip.machine_type."+machineType)
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD));
    }

    private static Component OverclockType(String overclockType) {
        return Component.translatable("frontiers.tooltip.overclock_type.prefix",
                        Component.translatable("frontiers.tooltip.overclock_type."+overclockType.replace(" ", "_").toLowerCase()))
                .withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD);
    }

    private List<Component> outLines;
    private List<Component> shiftLines;

    private String machineType;
    private int outRows = 0;
    private int shiftRows = 0;

    CosmicTooltipBuilder(String machineType) {
        this.outLines = new LinkedList<>();
        this.shiftLines = new LinkedList<>();
        this.machineType = machineType.replace(" ", "_").toLowerCase();
        this.outLines.add(MachineType(this.machineType));
        this.shiftLines.add(SEPARATOR);
    }

    /**
     * Add a line to tooltips.
     *
     * @param style ChatFormatting Style.
     * @param isShift Boolean that control whether the row will be added into Shift display.
     */
    public void addInfo(boolean isShift, ChatFormatting style) {
        (isShift?this.shiftLines:this.outLines).add(Component.translatable("frontiers.tooltip."+this.machineType+"."+(outRows++)).withStyle(style));
    }

    /**
     * Add a line to tooltips.
     *
     * @param text Do nothing, just increasing code readability.
     */
    public void addInfo(String text) {
        addInfo(false, ChatFormatting.WHITE);
    }

    /**
     * Add a shift display line to tooltips.
     * @param text Do nothing, just increasing code readability.
     */
    public void addShiftInfo(String text) {
        addInfo(true, ChatFormatting.WHITE);
    }

    /**
     * Add a line to tooltips.
     *
     * @param text Do nothing, just increasing code readability.
     * @param style ChatFormatting Style.
     */
    public void addInfo(String text, ChatFormatting style) {
        addInfo(false, style);
    }

    /**
     * Add a shift display line to tooltips.
     * @param text Do nothing, just increasing code readability.
     * @param style ChatFormatting Style.
     */
    public void addShiftInfo(String text, ChatFormatting style) {
        addInfo(true, style);
    }

    /**
     * Add a separator to tooltips.
     *
     * @param isShift Boolean that control whether the row will be added into Shift display.
     * @param isLong  Boolean that control which separator will be added.
     * @param style ChatFormatting Style.
     */
    public void addSeparator(boolean isShift, boolean isLong, ChatFormatting style) {
        (isShift?this.shiftLines:this.outLines).add((isLong?SEPARATOR:SEPARATOR_SHORT));
    }
}
