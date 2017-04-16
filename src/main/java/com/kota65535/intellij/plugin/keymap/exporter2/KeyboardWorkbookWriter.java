package com.kota65535.intellij.plugin.keymap.exporter2;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.ActionManagerEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.util.SystemInfo;
import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tozawa on 2017/03/08.
 */
public class KeyboardWorkbookWriter {
    private KeyboardWorkbook workbook;
    private KeymapManagerEx keymapManager = KeymapManagerEx.getInstanceEx();
    private ActionManager actionManager = ActionManager.getInstance();
    private Keymap keymap;
    private static final Logger logger = Logger.getInstance(ExportKeymapAction.class);


    public KeyboardWorkbookWriter(String fileName, Keymap keymap) {
        try {
            workbook = new KeyboardWorkbook(ClassLoader.getSystemClassLoader().getResourceAsStream(fileName));
        } catch (IOException exception) {
            logger.error("Cannot read file %s", fileName);
            return;
        }
        this.keymap = keymap;

        // Get all action group IDs
        List<String> actionGroupIds = Arrays.stream(actionManager.getActionIds(""))
                .filter( aid -> actionManager.isGroup(aid))
                .collect(Collectors.toList());
        // Get all action IDs
        List<String> actionIds = Arrays.stream(actionManager.getActionIds(""))
                .collect(Collectors.toList());

        // Get all actions
        List<AnAction> actions = Arrays.stream(actionManager.getActionIds(""))
                .map( aid -> actionManager.getAction(aid))
                .collect(Collectors.toList());

        // Make action-to-color map
        actions.forEach( action -> {
            KeyStroke stroke = KeymapUtil.getKeyStroke(action.getShortcutSet());
            String description = action.getTemplatePresentation().getDescription();
            workbook.setKeyboardCell(stroke, description);
        });

    }



//    public void makeAction2Shortcut
//    // Get a list of action IDs
//    List<String> actionIds = Arrays.stream(selectedKeymap.getActionIds())
//            .collect(Collectors.toList());
//
//    //
//        actionIds.forEach(id -> {
//        List<Shortcut> shortcuts = Arrays.stream(selectedKeymap.getShortcuts(id))
//                .collect(Collectors.toList());
//        if (!shortcuts.isEmpty()) {
//            shortcuts.forEach(s -> {
//                if (s instanceof KeyboardShortcut) {
//                    KeyStroke stroke = ((KeyboardShortcut) s).getFirstKeyStroke();
//                    AnAction action = ActionManager.getInstance().getAction(id);
//                    String description;
//                    if (action != null) {
//                        description = action.getTemplatePresentation().getDescription();
//                    } else {
//                        description = id;
//                    }
//                    String shortcutText = KeymapUtil.getFirstKeyboardShortcutText(id);
//                    System.out.println(String.format("%s:%s, %s", id, shortcutText, description));
//                }
//            });
//        }
//    });
}
