package com.kota65535.intellij.plugin.keymap.exporter2;

import com.google.common.collect.Sets;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.keymap.ex.KeymapManagerEx;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import javax.swing.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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

    Document document;
    String outputFileName;



    public KeyboardWorkbookWriter(String inputFileName, Document document, String outputFileName) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(inputFileName);
        try {
            workbook = new KeyboardWorkbook(is);
        } catch (Exception ex) {
            System.out.println("Failed to load " + inputFileName);
            ex.printStackTrace();
        }
        this.document = document;
        this.outputFileName = outputFileName;
    }



    private Map<String, Set<String>> getKey2Actions() {
        Map<String, Set<String>> key2Actions = new HashMap<>();
        NodeList nodeList = document.getElementsByTagName("action");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element elem = (Element) nodeList.item(i);
            String keyStroke = elem.getAttribute("key");
            if (key2Actions.get(keyStroke) != null) {
                Set<String> newList = key2Actions.get(keyStroke);
                newList.add(elem.getAttribute("id"));
                key2Actions.put(keyStroke, newList);
            } else {
                key2Actions.put(keyStroke, Sets.newHashSet(elem.getAttribute("id")));
            }
        }

        return key2Actions;
    }


    private Map<String, Set<String>> getKey2MainActions(int threshold) {
        Map<String, Set<String>> key2Actions = getKey2Actions();

        return key2Actions.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            if (entry.getValue().size() > threshold) {
                                Set<String> actionSet = entry.getValue();
                                return actionSet.stream()
                                        .filter(a -> isGroupOf(a, "MainMenu"))
                                        .collect(Collectors.toSet());
                            } else {
                                return entry.getValue();
                            }
                        }));
    }


    private boolean isGroupOf(String childId, String ancestorId) {

        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = String.format("/root//group[@id='%s']//action[@id='%s']", ancestorId, childId);
        try {
            Node widgetNode = (Node) xpath.evaluate(expression, document, XPathConstants.NODE);
            if (widgetNode != null) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public void write() {
        Map<String, Set<String>> key2MainAct = getKey2MainActions(2);

        key2MainAct.forEach((key, value) -> {
            List<String> list = new ArrayList<>(value);
            if (value.size() == 2) {
                workbook.setKeyboardCell(key, list.get(0), list.get(1));
            } else if (value.size() == 1) {
                workbook.setKeyboardCell(key, list.get(0));
            }
        });

        try {
            workbook.save(outputFileName);
            logger.info(String.format("Successfully write file %s", outputFileName));
        } catch (IOException ex) {
            logger.error(String.format("Failed to write file", outputFileName), ex);
        }
    }
}


//        GROUP_2_COLOR.entrySet().forEach( entry -> {
//            List<AnAction> actions = groupToAllChildren.get(entry.getKey());
//            actions.forEach(action -> {
//                Arrays.stream(action.getShortcutSet().getShortcuts()).forEach(shortcut -> {
//                    if (shortcut instanceof KeyboardShortcut) {
//                        KeyboardShortcut keyboardShortcut = (KeyboardShortcut)shortcut;
//                        logger.info(String.format("group: %s, color: %s, text: %s, key: %s",
//                                entry.getKey(),
//                                entry.getValue().name(),
//                                action.getTemplatePresentation().getText(),
//                                KeymapUtil.getKeystrokeText(keyboardShortcut.getFirstKeyStroke())));
//                        workbook.setKeyboardCell(keyboardShortcut.getFirstKeyStroke(),
//                                action.getTemplatePresentation().getText());
//                    }
//                    else if (shortcut instanceof KeyboardModifierGestureShortcut) {
//                        KeyboardModifierGestureShortcut keyboardShortcut = (KeyboardModifierGestureShortcut)shortcut;
//                        logger.info(String.format("group: %s, color: %s, text: %s, key: %s",
//                                entry.getKey(),
//                                entry.getValue().name(),
//                                action.getTemplatePresentation().getText(),
//                                KeymapUtil.getKeystrokeText(keyboardShortcut.getStroke())));
//                    }
//                });
//            });
//        });
//        try {
//            workbook.save(outputFileName);
//            logger.info(String.format("Successfully write file %s", outputFileName));
//        } catch (IOException ex) {
//            logger.error(String.format("Failed to write file", outputFileName), ex);
//        }


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
//}
