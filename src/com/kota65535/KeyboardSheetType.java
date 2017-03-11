package com.kota65535;

/**
 * Created by tozawa on 2017/03/08.
 */
public enum KeyboardSheetType {
    NoMod(0),
    Ctrl(1),
    Alt(2),
    AltCtrl(3),
    ;

    private int page;

    KeyboardSheetType(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
