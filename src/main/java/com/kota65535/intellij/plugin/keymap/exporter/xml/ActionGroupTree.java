package com.kota65535.intellij.plugin.keymap.exporter.xml;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.keymap.KeymapUtil;
import org.w3c.dom.*;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.io.StringWriter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by tozawa on 2017/04/23.
 */
public class ActionGroupTree {

    private ActionManager actionManager = ActionManager.getInstance();
    List<ActionGroup> actionGroups;
    List<AnAction> actions;
    Document document;
    Element rootElement;

    public ActionGroupTree() {

        // Get all action groups.
        actionGroups = Arrays.stream(actionManager.getActionIds(""))
                .filter(aid -> actionManager.isGroup(aid))
                .map(aid -> (ActionGroup) actionManager.getAction(aid))
                .collect(Collectors.toList());

        // Get all actions (excluding groups).
        actions = Arrays.stream(actionManager.getActionIds(""))
                .filter(aid -> !actionManager.isGroup(aid))
                .map(aid -> actionManager.getAction(aid))
                .collect(Collectors.toList());

        try {
            document = initXMLDocument("root");
            rootElement = document.getDocumentElement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Document createTree(Map<String, Color> group2color) {
        // create group elements with child actions
        actionGroups.forEach(this::createGroupElement);
        // create actions without any parent group
        appendOrphanActionElements();
        // add color attribute to each group
        group2color.forEach((key, value) -> addColorAttribute(key, Integer.toString(value.getRGB())));
        // add color attribute to orphan actions
        addColorAttributeToOrphans("Activate.*Window", 0x33CCFF);
        return document;
    }

    public void print() {
        try {
            System.err.println(createXMLString(document));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void addColorAttribute(String groupId, String color) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = String.format("/root//group[@id='%s']//action", groupId);
        try {
            NodeList nodes = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
            for( int i = 0 ; i < nodes.getLength() ; ++i ) {
                Element elem = (Element) nodes.item(i);
                elem.setAttribute("color", color);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Document initXMLDocument(String root) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        DOMImplementation dom = builder.getDOMImplementation();
        return dom.createDocument("", root, null);
    }

    private static String createXMLString(Document document) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");

        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }

    private void createGroupElement(@Nonnull ActionGroup group) {
        // The XML element of this group.
        Element groupElement = findOrCreateElement(actionManager.getId(group), "group", true);

        AnAction[] actions = group.getChildren(null);

        // Child action groups
        List<ActionGroup> childActionGroups = Arrays.stream(actions)
                .filter(a -> a instanceof ActionGroup)
                .filter(a -> actionManager.getId(a) != null)
                .map(a -> (ActionGroup) a)
                .collect(Collectors.toList());

        // Append all child groups.
        childActionGroups.forEach(ag -> {
            Element childGroupElement = findOrCreateElement(actionManager.getId(ag), "group", false);
            groupElement.appendChild(childGroupElement);
        });

        // Child actions
        List<AnAction> childActions = Arrays.stream(actions)
                .filter(a -> !(a instanceof ActionGroup))
                .filter(a -> actionManager.getId(a) != null)
                .collect(Collectors.toList());

        // Append all child actions to group
        childActions.forEach(a -> groupElement.appendChild(createActionElement(a)));
    }

    private Element findOrCreateElement(String id, String tagName, boolean shouldAppendToRoot) {
        Element element = document.getElementById(id);
        if (element != null) {
            // If the element already exists, simply return it.
            return element;
        } else {
            // If not exist, create new element.
            element = document.createElement(tagName);
            element.setAttribute("id", id);
            element.setIdAttribute("id", true);
            if (shouldAppendToRoot) {
                rootElement.appendChild(element);
            }
            return element;
        }
    }

    private void appendOrphanActionElements() {
        List<String> actionIds = actions.stream()
                .map( a -> actionManager.getId(a))
                .collect(Collectors.toList());

        NodeList nodeList = document.getElementsByTagName("action");
        for (int i=0 ; i < nodeList.getLength() ; i++) {
            Element elem = (Element) nodeList.item(i);
            String actionId = elem.getAttribute("id");
            actionIds.remove(actionId);
        }
        actionIds.forEach( aid -> rootElement.appendChild(
                createActionElement(actionManager.getAction(aid))));
        // add color attribute for orphan actions
    }

    private void addColorAttributeToOrphans(String regex, int color) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = String.format("/root//action");
        try {
            NodeList nodes = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
            Pattern pattern = Pattern.compile(regex);
            for( int i = 0 ; i < nodes.getLength() ; ++i ) {
                Element elem = (Element) nodes.item(i);
                Matcher m = pattern.matcher(elem.getAttribute("id"));
                if (m.find()) {
                    elem.setAttribute("color", String.valueOf(color));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Element createActionElement(AnAction action) {
        String id = actionManager.getId(action);
        if (id == null) {
            return null;
        }
        Element element = document.createElement("action");
        element.setAttribute("id", id);
        element.setIdAttribute("id", true);
        String shortcut = KeymapUtil.getFirstKeyboardShortcutText(action);
        if (! shortcut.isEmpty()) {
            element.setAttribute("key", shortcut);
        }
        String text = action.getTemplatePresentation().getText();
        if (text != null) {
            text = text.replace("...", "");
        }
        element.setAttribute("text", text);
        element.setAttribute("description", action.getTemplatePresentation().getDescription());
        return element;
    }
}
