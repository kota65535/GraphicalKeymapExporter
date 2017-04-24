package com.kota65535.intellij.plugin.keymap.exporter2;

import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardCell;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardSheet;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardSheetType;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by tozawa on 2017/04/16.
 */
public class KeyboardWorkbookTest {
    KeyboardWorkbook sut;

    @Before
    public void setup() throws Exception {
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("keymap.xlsx");
        sut = new KeyboardWorkbook(fis);
    }

    @Test
    public void test_getSheet() {
        sut.getSheet(KeyboardSheetType.Ctrl);
    }

    @Test
    public void test_save() throws Exception {
        KeyboardSheet sheet = sut.getSheet(KeyboardSheetType.NoMod);
        sheet.setKeyboardCellWithColor("A", false, "unko",
                new XSSFColor(IndexedColors.PINK));
        sut.save("keymap2.xlsx");
    }

    @Test
    public void test_getKeyboardCell() {
        KeyboardCell cell = sut.getKeyboardCell("J");
        assertThat(cell.getLabel().getRowIndex(), is(15));
        assertThat(cell.getLabel().getColumnIndex(), is(33));
        assertThat(cell.getBody().getRowIndex(), is(16));
        assertThat(cell.getBody().getColumnIndex(), is(33));

        cell = sut.getKeyboardCell("shift J");
        assertThat(cell.getLabel().getRowIndex(), is(42));
        assertThat(cell.getLabel().getColumnIndex(), is(33));
        assertThat(cell.getBody().getRowIndex(), is(43));
        assertThat(cell.getBody().getColumnIndex(), is(33));
    }

    @Test
    public void test_setKeyboardCell() {
        sut.setKeyboardCell("8", "jjj");
        KeyboardCell cell = sut.getKeyboardCell("8");
//        assertThat(cell.getLabel().getRowIndex(), is(15));
//        assertThat(cell.getLabel().getColumnIndex(), is(33));
//        assertThat(cell.getBody().getRowIndex(), is(16));
//        assertThat(cell.getBody().getColumnIndex(), is(33));
        assertThat(cell.getBody().getStringCellValue(), is("jjj"));
    }


}
