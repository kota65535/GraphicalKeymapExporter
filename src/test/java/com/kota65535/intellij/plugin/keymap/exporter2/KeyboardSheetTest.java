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
import static org.hamcrest.Matchers.nullValue;
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
        KeyboardCell cell = sut.getKeyboardCell("Q", false);
        assertThat(cell.getLabel().getRowIndex(), is(13));
        assertThat(cell.getLabel().getColumnIndex(), is(7));
        assertThat(cell.getBody().getRowIndex(), is(14));
        assertThat(cell.getBody().getColumnIndex(), is(7));

        cell = sut.getKeyboardCell("Q", true);
        assertThat(cell.getLabel().getRowIndex(), is(46));
        assertThat(cell.getLabel().getColumnIndex(), is(7));
        assertThat(cell.getBody().getRowIndex(), is(47));
        assertThat(cell.getBody().getColumnIndex(), is(7));
    }

    @Test
    public void test_getKeyboardCell_merged() {
        KeyboardCell cell = sut.getKeyboardCell("W", false);
        assertThat(cell.getLabel().getRowIndex(), is(13));
        assertThat(cell.getLabel().getColumnIndex(), is(11));
        assertThat(cell.getBody().getRowIndex(), is(14));
        assertThat(cell.getBody().getColumnIndex(), is(11));
        assertThat(cell.getSecondBody().getRowIndex(), is(16));
        assertThat(cell.getSecondBody().getColumnIndex(), is(11));

        cell = sut.getKeyboardCell("W", true);
        assertThat(cell.getLabel().getRowIndex(), is(46));
        assertThat(cell.getLabel().getColumnIndex(), is(11));
        assertThat(cell.getBody().getRowIndex(), is(47));
        assertThat(cell.getBody().getColumnIndex(), is(11));
        assertThat(cell.getSecondBody(), is(nullValue()));
        assertThat(cell.getSecondBody(), is(nullValue()));
    }

    @Test
    public void test_setKeyboardCell() {
        sut.setKeyboardCell("J", false, "aaa");
        KeyboardCell cell = sut.getKeyboardCell("J", false);
        assertThat(cell.getLabel().getRowIndex(), is(15));
        assertThat(cell.getLabel().getColumnIndex(), is(33));
        assertThat(cell.getBody().getRowIndex(), is(16));
        assertThat(cell.getBody().getColumnIndex(), is(33));
        assertThat(cell.getBody().getStringCellValue(), is("aaa"));
    }

    @Test
    public void test_setKeyboardCell_merged() {
        sut.setKeyboardCell("E", false, "aaa");
        KeyboardCell cell = sut.getKeyboardCell("E", false);
        assertThat(cell.getBody().getStringCellValue(), is("aaa"));
        assertThat(cell.getSecondBody(), is(nullValue()));

        sut.setKeyboardCell("E", false, "bbb", "ccc");
        cell = sut.getKeyboardCell("E", false);
        assertThat(cell.getBody().getStringCellValue(), is("bbb"));
        assertThat(cell.getSecondBody().getStringCellValue(), is("ccc"));

    }


//    @Test
//    public void test_setKeyboardCellColor() {
//        sut.setKeyboardCellColor("J", false, new XSSFColor(IndexedColors.BLUE));
//
//        KeyboardCell cell = sut.getKeyboardCell("J", false);
//        assertThat(cell.getLabel().getRowIndex(), is(15));
//        assertThat(cell.getLabel().getColumnIndex(), is(33));
//        assertThat(cell.getBody().getRowIndex(), is(16));
//        assertThat(cell.getBody().getColumnIndex(), is(33));
//        assertThat(cell.getBody().getCellStyle().getFillForegroundColor(), is(IndexedColors.BLUE.getIndex()));
//    }

//    @Test
//    public void test_searchBorderedCellToBottom() {
//        KeyboardCell keyboardCell = sut.getKeyboardCell("`", false);
//        Cell cell = sut.searchBorderedCellToBottom(keyboardCell.getBody());
//        assertThat(cell.getRowIndex(), is(12));
//    }
//
//    @Test
//    public void test_searchBorderedCellToRight() {
//        KeyboardCell keyboardCell = sut.getKeyboardCell("`", false);
//        Cell cell = sut.searchBorderedCellToRight(keyboardCell.getBody());
//        assertThat(cell.getColumnIndex(), is(4));
//    }

}
