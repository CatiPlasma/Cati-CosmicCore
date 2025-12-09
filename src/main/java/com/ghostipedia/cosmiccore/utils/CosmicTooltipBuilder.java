package com.ghostipedia.cosmiccore.utils;

import com.ghostipedia.cosmiccore.CosmicCore;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * This makes Cosmic's custom multi-structure tooltips content more comprehensive and easier to add.<br>
 * Info is divided into 3 main categories: Head info, Tail info and Inner info.<br>
 * Head and Tail info will always display, while inner info will only display when Shift is pressed.<br>
 * Inner info will display between Introduction and Hatch Info.<br>
 * <br>
 * Head info order:<br>
 * MachineType, will be automatically generated if not set manually<br>
 * Introduction or Lore, Optional<br>
 * <br>
 * Tail info order:<br>
 * Laser Hatch and Cosmic Hatch info<br>
 * Authors, divided into two parts: code and art<br>
 * Overclock Type, will be automatically generated if left blank<br>
 * <br>
 * Inner info order:<br>
 * LongSeparator, used to separate outer and inner, automatically generate<br>
 * More info, can use ShortSeparator to make content more hierarchical<br>
 * LongSeparator, used to separate outer and inner,  automatically generate<br>
 */
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

    private List<Component> headLines;
    private List<Component> innerLines;
    private List<Component> tailLines;
    private short headRows = 0;
    private short innerRows = 0;
    private short tailRows = 0;

    private MultiblockMachineDefinition machineDefinition;
    private MultiblockMachineBuilder machineBuilder;
    private String machineType;
    private String machineID;

    private LinkedList<String> codeAuthors = null;
    private LinkedList<String> artAuthors = null;
    private LinkedList<String> uiAuthors = null;

    public CosmicTooltipBuilder() {
        this.headLines = new LinkedList<>();
        this.innerLines = new LinkedList<>();
        this.tailLines = new LinkedList<>();
    }

    public CosmicTooltipBuilder setMultiDefinition(@NotNull MultiblockMachineDefinition machineDefinition) {
        this.machineDefinition = machineDefinition;
        this.machineID = this.machineDefinition.getName();
        this.machineType = capitalize(machineID.replace('_', ' '));
        CosmicCore.LOGGER.info("MultiBlockName is: "+this.machineType);
        return this;
    }

    public CosmicTooltipBuilder setMultiBuilder(@NotNull MultiblockMachineBuilder machineBuilder) {
        this.machineBuilder = machineBuilder;
        this.machineType = capitalize(this.machineBuilder.langValue());
        return this;
    }

    /**
     * Add a line to tooltips.
     *
     * @param style ChatFormatting Style.
     * @param isShift Boolean that control whether the row will be added into Shift display.
     */
    public CosmicTooltipBuilder addInfo(boolean isShift, ChatFormatting style) {
        (isShift?this.innerLines :this.headLines).add(Component.translatable("frontiers.tooltip."+this.machineType.toLowerCase()+"."+(headRows++)).withStyle(style));
        return this;
    }

    /**
     * Add a line to tooltips.
     *
     * @param text Do nothing, just increasing code readability.
     */
    public CosmicTooltipBuilder addInfo(String text) {
        addInfo(false, ChatFormatting.WHITE);
        return this;
    }

    /**
     * Add a shift display line to tooltips.
     * @param text Do nothing, just increasing code readability.
     */
    public CosmicTooltipBuilder addShiftInfo(String text) {
        addInfo(true, ChatFormatting.WHITE);
        return this;
    }

    /**
     * Add a line to tooltips.
     *
     * @param text Do nothing, just increasing code readability.
     * @param style ChatFormatting Style.
     */
    public CosmicTooltipBuilder addInfo(String text, ChatFormatting style) {
        addInfo(false, style);
        return this;
    }

    /**
     * Add a shift display line to tooltips.
     * @param text Do nothing, just increasing code readability.
     * @param style ChatFormatting Style.
     */
    public CosmicTooltipBuilder addShiftInfo(String text, ChatFormatting style) {
        addInfo(true, style);
        return this;
    }

    /**
     * Add a separator to tooltips.
     *
     * @param isShift Boolean that control whether the row will be added into Shift display.
     * @param isLong  Boolean that control which separator will be added.
     * @param style ChatFormatting Style.
     */
    public CosmicTooltipBuilder addSeparator(boolean isShift, boolean isLong, ChatFormatting style) {
        (isShift?this.innerLines :this.headLines).add((isLong?SEPARATOR:SEPARATOR_SHORT));
        return this;
    }

    public List<Component> getTooltip() {
        List<Component> tt = new LinkedList<>();
        tt.addAll(this.headLines);
        if (this.innerLines != null && GTUtil.isShiftDown()) tt.addAll(this.innerLines);
        // tt.addAll(this.tailLines);
        return tt;
    }
}
