package com.kota65535.intellij.plugin.keymap.exporter;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.mac.MacMessages;
import com.kota65535.intellij.plugin.keymap.exporter.xml.ActionGroupTree;
import org.w3c.dom.Document;

import static com.kota65535.intellij.plugin.keymap.exporter.Constants.GROUP_2_COLOR;
import static com.kota65535.intellij.plugin.keymap.exporter.Constants.KEYBOARD_WORKBOOK_NAME;

/**
 * Created by tozawa on 2017/03/04.
 */
public class ExportKeymapAction extends AnAction {

    private Project project;
    private KeymapManagerEx keymapManager = KeymapManagerEx.getInstanceEx();

    private static final Logger logger = Logger.getInstance(ExportKeymapAction.class);


    @Override
    public void actionPerformed(AnActionEvent e) {
        // DialogWrapper needs Project object to initialize.
        project = e.getRequiredData(CommonDataKeys.PROJECT);

        // Let users choose the keymap to be exported.
        ExportKeymapDialog d = new ExportKeymapDialog(project);
        if (!d.showAndGet()) {
            return;
        }

        // Save current keymap.
        Keymap currentKeymap = keymapManager.getActiveKeymap();

        // Temporarily set the selected keymap as active.
        keymapManager.setActiveKeymap(d.getSelectedKeymap());

        // Analyze the tree structure of actions and action groups.
        ActionGroupTree tree = new ActionGroupTree();
        Document document = tree.createTree(GROUP_2_COLOR);
        tree.print();

        // Restore active keymap.
        keymapManager.setActiveKeymap(currentKeymap);

        // Output the keymap xlsx file.
        KeyboardWorkbookWriter writer = new KeyboardWorkbookWriter(KEYBOARD_WORKBOOK_NAME, document,
                String.format("%s/%s", d.getSaveDir(), KEYBOARD_WORKBOOK_NAME));
        writer.write();

        MacMessages.getInstance().showOkMessageDialog("Keymap exporter", "Finished", "OK");
    }
}
