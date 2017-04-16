package com.kota65535.intellij.plugin.keymap.exporter2;

import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Map;

/**
 * Created by tozawa on 2017/04/17.
 */
public class Constants {

    public static final Map<String, IndexedColors> GROUP_2_COLOR = ImmutableMap.<String, IndexedColors>builder()
            .put("FileMenu", IndexedColors.BLUE)
            .put("EditMenu", IndexedColors.YELLOW)
            .put("ViewMenu", IndexedColors.YELLOW)
            .put("GoToMenu", IndexedColors.AQUA)
            .put("CodeMenu", IndexedColors.ORANGE)
            .put("AnalyzeMenu", IndexedColors.PINK)
            .put("RefactoringMenu", IndexedColors.RED)
            .put("BuildMenu", IndexedColors.GREEN)
            .put("RunMenu", IndexedColors.GREEN)
            .put("ToolsMenu", IndexedColors.PINK)
            .put("VCSGroups", IndexedColors.BLUE)
            .put("WindowMenu", IndexedColors.BLUE)
            .build();

    public static final String KEYBOARD_WORKBOOK_NAME = "keymap.xlsx";
}
