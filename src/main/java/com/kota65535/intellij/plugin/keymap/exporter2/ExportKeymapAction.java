package com.kota65535.intellij.plugin.keymap.exporter2;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;
import com.kota65535.intellij.plugin.keymap.exporter2.xml.ActionGroupTree;
import org.w3c.dom.Document;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kota65535.intellij.plugin.keymap.exporter2.Constants.GROUP_2_COLOR;
import static com.kota65535.intellij.plugin.keymap.exporter2.Constants.KEYBOARD_WORKBOOK_NAME;

/**
 * Created by tozawa on 2017/03/04.
 */
public class ExportKeymapAction extends AnAction {

    private Project project;

    private static final Logger logger = Logger.getInstance(ExportKeymapAction.class);

    private Map<String, Color> actionColorMap = new HashMap<>();

    @Override
    public void actionPerformed(AnActionEvent e) {
        // DialogWrapper needs Project object to initialize.
        project = e.getRequiredData(CommonDataKeys.PROJECT);

        // Let users choose the keymap to be exported.
        ExportKeymapDialog d = new ExportKeymapDialog(project);
        if (!d.showAndGet()) {
            return;
        }

        ActionGroupTree tree = new ActionGroupTree();
        Document document = tree.createActionGroupTree(GROUP_2_COLOR);
        tree.write();

        KeyboardWorkbookWriter writer = new KeyboardWorkbookWriter(KEYBOARD_WORKBOOK_NAME, document,
                "/Users/tozawa/keymap3.xlsx");
        writer.write();
    }




//    private void makeActionColorMap() {
//        // Get all action group IDs
//        List<String> actionGroupIds = Arrays.stream(actionManager.getActionIds(""))
//                .filter( aid -> actionManager.isGroup(aid))
//                .collect(Collectors.toList());
//
//        // Map all child actionIds to the color associated with the group
//        actionGroupIds.forEach( agid -> {
//            if (groupColorMap.containsKey(agid)) {
//                Color color = groupColorMap.get(agid);
//                mapActionToColorRecusively(((ActionGroup)(actionManager.getAction(agid))), color);
//            }
//        });
//    }
//
//    private void mapActionToColorRecusively(ActionGroup actionGroup, Color color) {
//        for (AnAction action: actionGroup.getChildren(null)) {
//            String actionId =  actionManager.getId(action);
//            if (actionId != null) {
//                if (actionManager.isGroup(actionId)) {
//                    mapActionToColorRecusively((ActionGroup)action, color);
//                } else {
//                    actionColorMap.put(actionId, color);
//                }
//            }
//        }
//    }

}
