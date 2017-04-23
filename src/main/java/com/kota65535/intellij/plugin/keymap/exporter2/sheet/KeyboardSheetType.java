package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

/**
 * Created by tozawa on 2017/03/08.
 */
public enum KeyboardSheetType {
    NoMod(0),
    Ctrl(1),
    Alt(2),
    Meta(3),
    CtrlAlt(4),
    CtrlMeta(5),
    AltMeta(6),
    CtrlAltMeta(7)
    ;

    private int page;

    KeyboardSheetType(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
