package com.kota65535.intellij.plugin.keymap.exporter2;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardWorkbook;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kota65535.intellij.plugin.keymap.exporter2.Constants.GROUP_2_COLOR;

/**
 * Created by tozawa on 2017/03/08.
 */
public class KeyboardWorkbookWriter {
    private KeyboardWorkbook workbook;
    private KeymapManagerEx keymapManager = KeymapManagerEx.getInstanceEx();
    private ActionManager actionManager = ActionManager.getInstance();
    private Keymap keymap;
    private static final Logger logger = Logger.getInstance(ExportKeymapAction.class);

    List<String> actionGroupIds;
    List<String> actionIds;
    Map<String, List<AnAction>> groupToAllChildren;

    public KeyboardWorkbookWriter(String fileName, Keymap keymap) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            workbook = new KeyboardWorkbook(is);
        } catch (Exception ex) {
            System.out.println("Failed to load " + fileName);
            ex.printStackTrace();
        }
        this.keymap = keymap;

        // Get all action group IDs.
        actionGroupIds = Arrays.stream(actionManager.getActionIds(""))
                .filter( aid -> actionManager.isGroup(aid))
                .collect(Collectors.toList());

        // Get all action IDs.
        actionIds = Arrays.stream(actionManager.getActionIds(""))
                .filter( aid -> ! actionManager.isGroup(aid))
                .collect(Collectors.toList());

//        List<AnAction> actions = actionIds.stream()
//                .map( id -> actionManager.getAction(id))
//                .collect(Collectors.toList());
//
//        List<ActionGroup> actionGroups = actionGroupIds.stream()
//                .map( id -> (ActionGroup)actionManager.getAction(id))
//                .collect(Collectors.toList());

        // Make map of group IDs associated with their all children.
        groupToAllChildren = GROUP_2_COLOR.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> {
                            ActionGroup group = (ActionGroup)actionManager.getAction(e.getKey());
                            List<AnAction> as = getAllChildActions(group);
                            return as;
                        }
                ));
    }

    public void write() {
        groupToAllChildren.entrySet().stream()
                .

    }

    private List<AnAction> getAllChildActions(@Nonnull ActionGroup group) {
        AnAction[] actions = group.getChildren(null);

        List<AnAction> childActions = Arrays.stream(actions)
                .filter( a -> ! (a instanceof ActionGroup))
                .collect(Collectors.toList());

        List<ActionGroup> childActionGroups = Arrays.stream(actions)
                .filter( a -> a instanceof ActionGroup)
                .map( a -> (ActionGroup)a)
                .collect(Collectors.toList());

        if ( childActionGroups.size() > 0) {
            List<AnAction> recursiveChildActions = childActionGroups.stream()
                    .flatMap( ag -> this.getAllChildActions(ag).stream())
                    .collect(Collectors.toList());
            childActions.addAll(recursiveChildActions);
        }

        return childActions;
    }


//    /**
//     * generate KeyStroke-to-Action map
//     */
//    public void makeActionToColorMap() {
//
//
//        // Make action-to-action-group map.
//        actionIds.stream()
//                .collect(Collectors.toMap(
//                        k -> k,
//                        v -> actionManager.getAction(v).
//                ))
//
//
//        // Make action-to-color map
////        actionIds.forEach(action -> {
////            KeyStroke stroke = KeymapUtil.getKeyStroke(action.getShortcutSet());
////            String description = action.getTemplatePresentation().getDescription();
////            workbook.setKeyboardCell(stroke, description);
////        });
//    }
//
//    private String searchActionGroup(String childActionId) {
//        List<ActionGroup> actionGroups = actionGroupIds.stream()
//                .map( id -> (ActionGroup)actionManager.getAction(id))
//                .collect(Collectors.toList());
//
//        actionGroups.stream()
//                .flatMap( ag -> ag.getChildren()
//    }



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
