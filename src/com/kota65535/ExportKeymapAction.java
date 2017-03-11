package com.kota65535;

import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tozawa on 2017/03/04.
 */
public class ExportKeymapAction extends AnAction {

    private Project project;

    private static final Logger logger = Logger.getInstance(ExportKeymapAction.class);


    private Map<String, Color> groupColorMap = ImmutableMap.<String, Color>builder()
            .put("FileMenu", Color.BLUE)
            .put("EditMenu", Color.YELLOW)
            .put("ViewMenu", Color.YELLOW)
            .put("GoToMenu", Color.CYAN)
            .put("CodeMenu", Color.ORANGE)
            .put("AnalyzeMenu", Color.PINK)
            .put("RefactoringMenu", Color.RED)
            .put("BuildMenu", Color.GREEN)
            .put("RunMenu", Color.GREEN)
            .put("ToolsMenu", Color.PINK)
            .put("VCSGroups", Color.BLUE)
            .put("WindowMenu", Color.BLUE)
            .build();

    private Map<String, Color> actionColorMap = new HashMap<>();

    private final String KEYBOARD_WORKBOOK_NAME = "keymap.xlsx";

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getRequiredData(CommonDataKeys.PROJECT);

//        makeActionColorMap();

        ExportKeymapDialog d = new ExportKeymapDialog(project);
        if (!d.showAndGet()) {
            return;
        }

        KeyboardWorkbookWriter writer = new KeyboardWorkbookWriter(KEYBOARD_WORKBOOK_NAME, d.getSelectedKeymap());
        writer.getClass();
    }


//    private void makeActionColorMap() {
//        // Get all action group IDs
//        List<String> actionGroupIds = Arrays.stream(actionManager.getActionIds(""))
//                .filter( aid -> actionManager.isGroup(aid))
//                .collect(Collectors.toList());
//
//        // Map all child actions to the color associated with the group
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
