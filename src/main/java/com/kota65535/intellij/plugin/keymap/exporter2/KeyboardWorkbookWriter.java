package com.kota65535.intellij.plugin.keymap.exporter2;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.kota65535.intellij.plugin.keymap.exporter2.ExportKeymapAction;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardWorkbook;

import javax.swing.*;
import java.io.InputStream;
import java.util.Arrays;
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
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            workbook = new KeyboardWorkbook(is);
        } catch (Exception ex) {
            System.out.println("Failed to load " + fileName);
            ex.printStackTrace();
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
