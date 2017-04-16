package com.kota65535.intellij.plugin.keymap.exporter2;

import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardCell;
import com.kota65535.intellij.plugin.keymap.exporter2.sheet.KeyboardSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by tozawa on 2017/04/16.
 */
public class KeyboardSheetTest {
    public KeyboardSheet sut;

    @Before
    public void setup() throws Exception {
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("keymap.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        sut = new KeyboardSheet(workbook.getSheetAt(0));
    }

    @Test
    public void test_getCell() throws Exception {
        Method method = KeyboardSheet.class.getDeclaredMethod("getCell", int.class, int.class);
        method.setAccessible(true);
        Cell cell = (Cell) method.invoke(sut, 1, 3);
        assertThat(cell.getStringCellValue(), is("ESC"));
        assertThat(cell.getRowIndex(), is(1));
        assertThat(cell.getColumnIndex(), is(3));
    }

    @Test
    public void test_getKeyboardCell() {
        KeyboardCell cell = sut.getKeyboardCell("Y", false);
        assertThat(cell.getLabel().getRowIndex(), is(11));
        assertThat(cell.getLabel().getColumnIndex(), is(27));
        assertThat(cell.getContent().getRowIndex(), is(12));
        assertThat(cell.getContent().getColumnIndex(), is(27));

        cell = sut.getKeyboardCell("Y", true);
        assertThat(cell.getLabel().getRowIndex(), is(38));
        assertThat(cell.getLabel().getColumnIndex(), is(27));
        assertThat(cell.getContent().getRowIndex(), is(39));
        assertThat(cell.getContent().getColumnIndex(), is(27));
    }

    @Test
    public void test_setKeyboardCell() {
        sut.setKeyboardCell("J", false, "aaa");
        KeyboardCell cell = sut.getKeyboardCell("J", false);
        assertThat(cell.getLabel().getRowIndex(), is(15));
        assertThat(cell.getLabel().getColumnIndex(), is(33));
        assertThat(cell.getContent().getRowIndex(), is(16));
        assertThat(cell.getContent().getColumnIndex(), is(33));
        assertThat(cell.getContent().getStringCellValue(), is("aaa"));
    }

    @Test
    public void test_setKeyboardCellColor() {
        sut.setKeyboardCellColor("J", false, new XSSFColor(IndexedColors.BLUE));

        KeyboardCell cell = sut.getKeyboardCell("J", false);
        assertThat(cell.getLabel().getRowIndex(), is(15));
        assertThat(cell.getLabel().getColumnIndex(), is(33));
        assertThat(cell.getContent().getRowIndex(), is(16));
        assertThat(cell.getContent().getColumnIndex(), is(33));
        assertThat(cell.getContent().getCellStyle().getFillForegroundColor(), is(IndexedColors.BLUE.getIndex()));
    }

}
