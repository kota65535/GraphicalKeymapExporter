package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import com.intellij.openapi.diagnostic.Logger;
import com.kota65535.intellij.plugin.keymap.exporter2.Modifier;
import com.kota65535.intellij.plugin.keymap.exporter2.Utils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tozawa on 2017/03/07.
 */
public class KeyboardWorkbook {

    XSSFWorkbook workbook;
    Map<KeyboardSheetType, KeyboardSheet> sheetMap = new HashMap<>();

    Logger logger = Logger.getInstance(KeyboardWorkbook.class);

    public KeyboardWorkbook(InputStream fis) throws IOException {
        workbook = new XSSFWorkbook(fis);

        Arrays.stream(KeyboardSheetType.values())
                .forEach( type -> sheetMap.put(type, new KeyboardSheet(workbook.getSheetAt(type.getPage()))) );
    }


    public KeyboardSheet getSheet(KeyboardSheetType type) {
        return sheetMap.get(type);
    }

    public KeyboardSheet getSheet(KeyStroke stroke) {
        String key = Utils.getKey(stroke);
        EnumSet<Modifier> mods = Utils.getModifiers(stroke);
        KeyboardSheet sheet;
        if ( mods.containsAll(Arrays.asList(Modifier.CTRL, Modifier.ALT, Modifier.META))) {
            sheet = getSheet(KeyboardSheetType.CtrlAltMeta);
        }
        else if ( mods.containsAll(Arrays.asList(Modifier.ALT, Modifier.META))) {
            sheet = getSheet(KeyboardSheetType.AltMeta);
        }
        else if ( mods.containsAll(Arrays.asList(Modifier.CTRL, Modifier.META))) {
            sheet = getSheet(KeyboardSheetType.CtrlMeta);
        }
        else if ( mods.containsAll(Arrays.asList(Modifier.CTRL, Modifier.ALT))) {
            sheet = getSheet(KeyboardSheetType.CtrlAlt);
        }
        else if ( mods.contains(Modifier.META)) {
            sheet = getSheet(KeyboardSheetType.Meta);
        }
        else if ( mods.contains(Modifier.ALT)) {
            sheet = getSheet(KeyboardSheetType.Alt);
        }
        else if ( mods.contains(Modifier.CTRL)) {
            sheet = getSheet(KeyboardSheetType.Ctrl);
        }
        else {
            sheet = getSheet(KeyboardSheetType.NoMod);
        }
        return sheet;
    }

    /**
     *
     * @param stroke
     * @return
     */
    public KeyboardCell getKeyboardCell(KeyStroke stroke) {
        KeyboardSheet sheet = getSheet(stroke);
        return sheet.getKeyboardCell(Utils.getKey(stroke), Utils.getModifiers(stroke).contains(Modifier.SHIFT));
    }

    /**
     *
     * @param stroke
     * @param value
     */
    public void setKeyboardCell(KeyStroke stroke, String value) {
        KeyboardSheet sheet = getSheet(stroke);
        sheet.setKeyboardCell(Utils.getKey(stroke), Utils.getModifiers(stroke).contains(Modifier.SHIFT), value);
    }

    public void setKeyboardCell(String strokeText, String value) {
        sheet.setKeyboardCell(strokeText, Key.getModifiers(strokeText).contains(Modifier.SHIFT), value);
    }


    public void save(String fileName) throws IOException {
        FileOutputStream os = new FileOutputStream(fileName);
        workbook.write(os);
    }

    public static void main(String[] args) throws IOException {
        InputStream is = KeyboardWorkbook.class.getClassLoader().getResourceAsStream("keymap.xlsx");
        KeyboardWorkbook book = new KeyboardWorkbook(is);
    }

}
