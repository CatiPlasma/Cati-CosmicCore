package com.ghostipedia.cosmiccore.utils;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import lombok.Getter;

import java.util.*;

import javax.annotation.Nullable;

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
 * LongSeparator, used to separate outer and inner, automatically generate<br>
 */
public class CosmicTooltipBuilder {

    private static final String MACHINE_TYPE_FLAG = "cosmiccore.multiblock.tooltip.machine_type";
    private static final String MACHINE_TYPE_PREFIX = "cosmiccore.multiblock.tooltip.machine_type.";

    private static final String TOOLTIP_PREFIX = "cosmiccore.multiblock.";
    private static final String TOOLTIP_POSTFIX = ".tooltip";
    private static final String HEAD_FLAG = ".head.";
    private static final String INNER_FLAG = ".inner.";
    private static final String TAIL_FLAG = ".tail.";

    private static final String VANILLA_PARALLEL_HATCH = "cosmiccore.multiblock.tooltip.parallel_hatch.vanilla";
    private static final String COSMIC_PARALLEL_HATCH = "cosmiccore.multiblock.tooltip.parallel_hatch.cosmic";

    private static final String LASER = "cosmiccore.multiblock.tooltip.laser";
    private static final String ONE_ENERGY_INPUT = "cosmiccore.multiblock.tooltip.one_energy_input";

    private static final String OVERCLOCK_TYPE_FLAG = "cosmiccore.multiblock.tooltip.overclock_type.prefix";
    private static final String OVERCLOCK_TYPE_PREFIX = "cosmiccore.multiblock.tooltip.overclock_type.";
    private static final String PERFECT_OVERCLOCK = "cosmiccore.multiblock.tooltip.overclock_type.perfect";
    private static final String NON_PERFECT_OVERCLOCK = "cosmiccore.multiblock.tooltip.overclock_type.non_perfect";
    private static final String NONE_OVERCLOCK = "cosmiccore.multiblock.tooltip.overclock_type.silent";

    private static final String AUTHORS_TITLE = "cosmiccore.multiblock.tooltip.authors.title";
    private static final String AUTHORS_CODE_FLAG = "cosmiccore.multiblock.tooltip.authors.code.prefix";
    private static final String AUTHORS_ART_FLAG = "cosmiccore.multiblock.tooltip.authors.art.prefix";
    private static final String AUTHORS_UI_FLAG = "cosmiccore.multiblock.tooltip.author.ui.prefix";
    private static final String AUTHORS_ART_UI_FLAG = "cosmiccore.multiblock.tooltip.author.art_ui.prefix";

    private static final String SEPARATOR = "------------------------------------------------------------";
    private static final String SEPARATOR_SHORT = "-------------------------------------------";
    private static final Component HOLD_SHIFT = Component.translatable("cosmiccore.multiblock.tooltip.hold_shift");
    private static final Component HOLD_CTRL = Component.translatable("cosmiccore.multiblock.tooltip.hold_ctrl");

    private List<Component> headLines;
    private List<Component> innerLines;
    private List<Component> tailLines;
    private short headRows = 0;
    private short innerRows = 0;
    private short tailRows = 0;

    private MultiblockMachineDefinition machineDefinition;
    private Component machineTypeComponent = null;
    private Component overclockTypeComponent = null;
    private String machineID;

    private boolean parallelHatchSupport = false;
    private Component parallelHatchComponent;
    private boolean laserSupport = false;
    private boolean onlyAllowOneEnergy = false;

    private List<String> codeAuthors = new LinkedList<>();
    private List<String> artAuthors = new LinkedList<>();
    private List<String> uiAuthors = new LinkedList<>();

    @Getter
    private static HashMap<Item, List<Component>> normalMultiblockMachineTooltips = new HashMap<>();
    @Getter
    private static HashMap<Item, List<Component>> detailedMultiblockMachineTooltips = new HashMap<>();
    @Getter
    private static HashMap<Item, List<Component>> creditMultiblockMachineTooltips = new HashMap<>();
    @Getter
    private static HashMap<Item, List<Component>> allMultiblockMachineTooltips = new HashMap<>();
    @Getter
    private static Set<Item> multiblockMachines;

    public CosmicTooltipBuilder(MultiblockMachineDefinition machineDefinition) {
        this.headLines = new LinkedList<>();
        this.innerLines = new LinkedList<>();
        this.tailLines = new LinkedList<>();
        this.machineDefinition = machineDefinition;
        this.machineID = machineDefinition.getId().toString().replace("cosmiccore:", "");
    }

    private Component setComponent(String flag, String key) {
        return Component.translatable(flag, Component.translatable(key));
    }

    private Component setComponent(String flag, List<String> key) {
        return Component.translatable(flag, key);
    }

    private Component setComponent(String text) {
        return Component.translatable(text);
    }

    public CosmicTooltipBuilder setMachineType(String machineType) {
        this.machineTypeComponent = setComponent(MACHINE_TYPE_FLAG, MACHINE_TYPE_PREFIX + machineID);
        return this;
    }

    public CosmicTooltipBuilder addHeadInfo(String... info) {
        List<Component> infoComponents = new LinkedList<>();
        for (String ignored : info) {
            infoComponents.add(setComponent(TOOLTIP_PREFIX + machineID + TOOLTIP_POSTFIX + HEAD_FLAG + (headRows++)));
        }
        this.headLines.addAll(infoComponents);
        return this;
    }

    public CosmicTooltipBuilder addInnerInfo(String... info) {
        List<Component> infoComponents = new LinkedList<>();
        for (String ignored : info) {
            infoComponents.add(setComponent(TOOLTIP_PREFIX + machineID + TOOLTIP_POSTFIX + INNER_FLAG + (innerRows++)));
        }
        this.innerLines.addAll(infoComponents);
        return this;
    }

    public CosmicTooltipBuilder addTailInfo(String... info) {
        List<Component> infoComponents = new LinkedList<>();
        for (String ignored : info) {
            infoComponents.add(setComponent(TOOLTIP_PREFIX + machineID + TOOLTIP_POSTFIX + TAIL_FLAG + (tailRows++)));
        }
        this.tailLines.addAll(infoComponents);
        return this;
    }

    public CosmicTooltipBuilder addParallelHatchInfo(ParallelInfo parallelInfo) {
        parallelHatchSupport = true;
        this.parallelHatchComponent = setComponent(
                switch (parallelInfo) {
                    case VANILLA -> VANILLA_PARALLEL_HATCH;
                    case COSMIC -> COSMIC_PARALLEL_HATCH;
                });
        return this;
    }

    public CosmicTooltipBuilder laserSupport() {
        this.laserSupport = true;
        return this;
    }

    public CosmicTooltipBuilder onlyOneEnergyInput() {
        this.onlyAllowOneEnergy = true;
        return this;
    }

    public CosmicTooltipBuilder setOverclockType(OverclockType overclockType, String... customOverclockType) {
        this.overclockTypeComponent = setComponent(OVERCLOCK_TYPE_FLAG,
                switch (overclockType) {
                    case SILENT -> NONE_OVERCLOCK;
                    case NON_PERFECT -> NON_PERFECT_OVERCLOCK;
                    case PERFECT -> PERFECT_OVERCLOCK;
                    case CUSTOM -> OVERCLOCK_TYPE_PREFIX + customOverclockType[0].replace(" ", "_").toLowerCase();
                });
        return this;
    }

    private void addAuthor(ContributeAspect contributeAspect, String... name) {
        switch (contributeAspect) {
            case CODE -> this.codeAuthors.addAll(Arrays.asList(name));
            case ART -> this.artAuthors.addAll(Arrays.asList(name));
            case UI -> this.uiAuthors.addAll(Arrays.asList(name));
        }
    }

    public CosmicTooltipBuilder addAuthors(ContributeAspect contributeAspect, String... name) {
        addAuthor(contributeAspect, name);
        return this;
    }

    public CosmicTooltipBuilder addAuthors(String name, ContributeAspect... contributeAspect) {
        for (ContributeAspect i : contributeAspect) {
            addAuthor(i, name);
        }
        return this;
    }

    private List<Component> authorsCompile() {
        List<Component> authorsComponent = new LinkedList<>();
        authorsComponent.add(setComponent(AUTHORS_TITLE));
        if (!codeAuthors.isEmpty()) {
            if (codeAuthors.size() == 2) {
                authorsComponent.add(setComponent(AUTHORS_CODE_FLAG));
                authorsComponent.add(Component.literal(codeAuthors.get(0) + " & " + codeAuthors.get(1)));
            }
            else {
                authorsComponent.add(setComponent(AUTHORS_CODE_FLAG));
                authorsComponent.add(Component.literal(codeAuthors.toString().replace("[", "").replace("]", "")));
            }
        }
        if (!artAuthors.isEmpty() || !uiAuthors.isEmpty()) {
            if (artAuthors.equals(uiAuthors)) {
                if (artAuthors.size() == 2) {
                    authorsComponent.add(setComponent(AUTHORS_ART_UI_FLAG, artAuthors.get(0) + " & " + artAuthors.get(1)));
                }
                else {
                    authorsComponent.add(setComponent(AUTHORS_ART_UI_FLAG, artAuthors.toString().replace("[", "").replace("]", "")));
                }
            } else {
                if (!artAuthors.isEmpty()) {
                    if (artAuthors.size() == 2) {
                        authorsComponent.add(setComponent(AUTHORS_ART_FLAG, artAuthors.get(0) + " & " + artAuthors.get(1)));
                    }
                    else {
                            authorsComponent.add(setComponent(AUTHORS_ART_UI_FLAG, artAuthors.toString().replace("[", "").replace("]", "")));
                    }
                }
                if (!uiAuthors.isEmpty()) {
                    if (uiAuthors.size() == 2) {
                        authorsComponent.add(setComponent(AUTHORS_UI_FLAG, uiAuthors.get(0) + " & " + uiAuthors.get(1)));
                    }
                    else {
                        authorsComponent.add(setComponent(AUTHORS_UI_FLAG, uiAuthors.toString().replace("[", "").replace("]", "")));
                    }
                }
            }
        }
        return authorsComponent;
    }

    private void addSeparator(TooltipSection tooltipSection, String separator) {
        switch (tooltipSection) {
            case HEAD -> this.headLines.add(Component.literal(separator));
            case INNER -> this.innerLines.add(Component.literal(separator));
            case TAIL -> this.tailLines.add(Component.literal(separator));
        }
    }

    public CosmicTooltipBuilder addSeparators(TooltipSection tooltipSection, SeparatorType separatorType,
                                              @Nullable ChatFormatting... formatting) {
        String separator;
        if (formatting != null) {
            StringBuilder temp = new StringBuilder();
            for (ChatFormatting i : formatting) {
                temp.append(i);
            }
            separator = temp.append(switch (separatorType) {
                case LONG -> SEPARATOR;
                case SHORT -> SEPARATOR_SHORT;
            }).toString();
        } else {
            separator = switch (separatorType) {
                case LONG -> SEPARATOR;
                case SHORT -> SEPARATOR_SHORT;
            };
        }
        addSeparator(tooltipSection, separator);
        return this;
    }

    public CosmicTooltipBuilder addSeparators(TooltipSection tooltipSection, int length,
                                              @Nullable ChatFormatting... formatting) {
        String separator;
        if (formatting != null) {
            StringBuilder temp = new StringBuilder();
            for (ChatFormatting i : formatting) {
                temp.append(i);
            }
            temp.append("-".repeat(length));
            separator = temp.toString();
        } else {
            separator = "-".repeat(length);
        }
        addSeparator(tooltipSection, separator);
        return this;
    }

    public CosmicTooltipBuilder tooltipFinisher() {
        List<Component> normal = new LinkedList<>();
        List<Component> detailed = new LinkedList<>();
        List<Component> credit = new LinkedList<>();
        List<Component> all = new LinkedList<>();
        if (machineTypeComponent != null) normal.add(machineTypeComponent);
        if (headRows != 0) normal.addAll(headLines);
        if (tailRows != 0) normal.addAll(tailLines);
        if (parallelHatchSupport) normal.add(parallelHatchComponent);
        if (laserSupport) normal.add(setComponent(LASER));
        if (onlyAllowOneEnergy) normal.add(setComponent(ONE_ENERGY_INPUT));
        if (overclockTypeComponent != null) normal.add(overclockTypeComponent);
        if (innerRows == 0) {
            detailed.addAll(normal);
        } else {
            normal.add(HOLD_SHIFT);
            detailed.add(machineTypeComponent);
            if (headRows != 0) detailed.addAll(headLines);
            detailed.addAll(innerLines);
            if (tailRows != 0) detailed.addAll(tailLines);
            if (parallelHatchSupport) detailed.add(parallelHatchComponent);
            if (laserSupport) detailed.add(setComponent(LASER));
            if (onlyAllowOneEnergy) detailed.add(setComponent(ONE_ENERGY_INPUT));
            if (overclockTypeComponent != null) detailed.add(overclockTypeComponent);
        }
        all.addAll(detailed);
        credit.addAll(normal);
        if (!codeAuthors.isEmpty() || !artAuthors.isEmpty() || !uiAuthors.isEmpty()) {
            normal.add(HOLD_CTRL);
            detailed.add(HOLD_CTRL);
            credit.addAll(credit.size() - 1, authorsCompile());
            all.addAll(authorsCompile());
        }
        normalMultiblockMachineTooltips.put(machineDefinition.getItem(), normal);
        detailedMultiblockMachineTooltips.put(machineDefinition.getItem(), detailed);
        creditMultiblockMachineTooltips.put(machineDefinition.getItem(), credit);
        allMultiblockMachineTooltips.put(machineDefinition.getItem(), all);
        multiblockMachines = normalMultiblockMachineTooltips.keySet();
        return this;
    }

    public enum OverclockType {
        SILENT,
        NON_PERFECT,
        PERFECT,
        CUSTOM;
    }

    public enum ParallelInfo {
        VANILLA,
        COSMIC
    }

    public enum ContributeAspect {
        CODE,
        ART,
        UI
    }

    public enum SeparatorType {
        LONG,
        SHORT
    }

    public enum TooltipSection {
        HEAD,
        INNER,
        TAIL
    }
}
