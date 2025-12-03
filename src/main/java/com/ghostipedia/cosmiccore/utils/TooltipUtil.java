package com.ghostipedia.cosmiccore.utils;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class TooltipUtil {
    private static final String PREFIX = "frontiers";
    private static final String DOT = ".";
    private static final String POSTFIX = "tooltip";
    private static final String SEPARATOR = "------------------------------------------------------------";
    private static final String MACHINE_TYPE = PREFIX+DOT+Category.MULTI.text+DOT+"type"+POSTFIX;
    private static final String OVERCLOCK_TYPE = PREFIX+DOT+"overclock"+DOT+POSTFIX;

    public static ArrayList<Component> genTranslationKey(int rows, Category category, String machineName, String overclockType) {
        ArrayList<Component> components = new ArrayList<>();
        machineName.replace(" ", "_").toLowerCase();
        components.add(Component.translatable(MACHINE_TYPE, Component.translatable(PREFIX+DOT+Category.MULTI.text+DOT+machineName)));
        for (int i = 0; i < rows; i++) {
            components.add(Component.translatable(PREFIX+DOT+Category.MULTI.text+DOT+machineName+DOT+POSTFIX+DOT+i));
        }
        components.add(Component.translatable(OVERCLOCK_TYPE, Component.translatable(genOverclockKey(overclockType))));
        return components;
    }

    private static String genOverclockKey(String overclockType) {
        return PREFIX+DOT+"overclock"+DOT+overclockType.replace(" ", "_").toLowerCase();
    }

    public enum Category {
        ITEM("item"), BLOCK("block"), MACHINE("machine"), MULTI("multi");

        private String text;

        Category(String text) {
            this.text = text;
        }
    }

    public enum Overclock {
        NON_PERFECT("Imperfect"), PERFECT("Perfect");

        private String text;

        Overclock(String text) {
            this.text = text;
        }
    }
}
