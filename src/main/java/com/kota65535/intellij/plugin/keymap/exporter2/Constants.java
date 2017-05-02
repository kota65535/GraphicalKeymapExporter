package com.kota65535.intellij.plugin.keymap.exporter2;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import org.apache.commons.collections4.OrderedMap;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by tozawa on 2017/04/17.
 */
public class Constants {

    public static final Map<String, Color> GROUP_2_COLOR = ImmutableMap.<String, Color>builder()
            .put("EditorActions", new Color(0xFFFF66))
            .put("EditMenu", new Color(0xFFFF66))
            .put("FileMenu", new Color(22, 131, 251))
            .put("ViewMenu", new Color(0x33CCFF))
            .put("WindowMenu", new Color(0x33CCFF))
            .put("GoToMenu", new Color(0x99CCFF))
            .put("Bookmarks", new Color(0x99CCFF))
            .put("CodeMenu", new Color(0xFF9966))
            .put("AnalyzeMenu", new Color(0xFF6699))
            .put("RefactoringMenu", new Color(0xFF6699))
            .put("BuildMenu", new Color(0x99FF66))
            .put("RunMenu", new Color(0x99FF66))
            .put("ToolsMenu", new Color(252, 40, 252))
            .put("VcsGroups", new Color(0x66FF99))
            .put("ChangesViewPopupMenu", new Color(0x66FF99))
            .put("Diff.KeymapGroup", new Color(0x66FF99))

            .put("HelpMenu", new Color(255,255, 255))
            .build();

    public static final String KEYBOARD_WORKBOOK_NAME = "keymap.xlsx";
}
