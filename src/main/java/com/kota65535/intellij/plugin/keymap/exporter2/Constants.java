package com.kota65535.intellij.plugin.keymap.exporter2;

import com.google.common.collect.ImmutableMap;

import java.awt.*;
import java.util.Map;

/**
 * Created by tozawa on 2017/04/17.
 */
public class Constants {

    public static final Map<String, Color> GROUP_2_COLOR = ImmutableMap.<String, Color>builder()
            .put("EditorActions", new Color(11, 36, 251))
            .put("FileMenu", new Color(22, 131, 251))
            .put("EditMenu", new Color(45, 255, 254))
            .put("ViewMenu", new Color(41, 249, 130))
            .put("GoToMenu", new Color(41, 253, 47))
            .put("CodeMenu", new Color(132, 253, 49))
            .put("AnalyzeMenu", new Color(255, 253, 56))
            .put("RefactoringMenu", new Color(253, 127, 35))
            .put("BuildMenu", new Color(252, 13, 27))
            .put("RunMenu", new Color(251, 21, 128))
            .put("ToolsMenu", new Color(252, 40, 252))
            .put("VcsGroups", new Color(126, 37, 251))
            .put("WindowMenu", new Color(0, 0, 0))
            .put("HelpMenu", new Color(0, 0, 0))
            .build();

    public static final String KEYBOARD_WORKBOOK_NAME = "keymap.xlsx";
}
