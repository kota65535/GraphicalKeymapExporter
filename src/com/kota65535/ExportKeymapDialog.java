package com.kota65535;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExportKeymapDialog extends DialogWrapper {
    private JPanel myRootPanel;
    private JComboBox keymapComboBox;
    private final Project myProject;
    private KeymapManagerEx manager = KeymapManagerEx.getInstanceEx();

    public ExportKeymapDialog(@Nullable Project project) {
        super(project, true);

        myProject = project;

        // List all keymaps
        Arrays.stream(manager.getAllKeymaps()).forEach( m -> keymapComboBox.addItem(m));

        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        return myRootPanel;
    }

    @Override
    protected void doOKAction() {
        if (getOKAction().isEnabled()) {
            close(OK_EXIT_CODE);
        }
    }


    public Keymap getSelectedKeymap() {
        return (Keymap) keymapComboBox.getSelectedItem();
    }

//    private void createKeymap() {
//        Keymap selectedKeymap =
////         Arrays.stream(manager.getAllKeymaps())
////                .filter( map -> map.getName().equals(selectedKeymapName))
////                .findFirst().orElse(null);
////        if (selectedKeymap == null) {
////            return;
////        }
//
//        // Get a list of action IDs
//        List<String> actionIds = Arrays.stream(selectedKeymap.getActionIds())
//                .collect(Collectors.toList());
//
//        //
//        actionIds.forEach(id -> {
//            List<Shortcut> shortcuts = Arrays.stream(selectedKeymap.getShortcuts(id))
//                    .collect(Collectors.toList());
//            if (!shortcuts.isEmpty()) {
//                shortcuts.forEach(s -> {
//                    if (s instanceof KeyboardShortcut) {
//                        KeyStroke stroke = ((KeyboardShortcut) s).getFirstKeyStroke();
//                        AnAction action = ActionManager.getInstance().getAction(id);
//                        String description;
//                        if (action != null) {
//                            description = action.getTemplatePresentation().getDescription();
//                        } else {
//                            description = id;
//                        }
//                        String shortcutText = KeymapUtil.getFirstKeyboardShortcutText(id);
//                        System.out.println(String.format("%s:%s, %s", id, shortcutText, description));
//                    }
//                });
//            }
//        });
//    }
}
