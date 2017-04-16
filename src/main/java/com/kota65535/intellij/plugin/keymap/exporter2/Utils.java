package com.kota65535.intellij.plugin.keymap.exporter2;

import org.jetbrains.annotations.NonNls;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EnumSet;

/**
 * Created by tozawa on 2017/03/08.
 */
public class Utils {

    @NonNls private static final String CANCEL_KEY_TEXT = "Cancel";
    @NonNls private static final String BREAK_KEY_TEXT = "Break";

    public static String getKey(KeyStroke stroke) {
        String keyText = KeyEvent.getKeyText(stroke.getKeyCode());
        // [vova] this is dirty fix for bug #35092
        if(CANCEL_KEY_TEXT.equals(keyText)){
            keyText = BREAK_KEY_TEXT;
        }
        return keyText.trim();
    }

    public static EnumSet<Modifier> getModifiers(KeyStroke stroke) {
        int modifiers = stroke.getModifiers();
        EnumSet<Modifier> result = EnumSet.noneOf(Modifier.class);
        if ((modifiers & InputEvent.META_MASK) != 0) {
            result.add(Modifier.META);
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            result.add(Modifier.CTRL);
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            result.add(Modifier.ALT);
        }
        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            result.add(Modifier.SHIFT);
        }
        return result;
    }

}
