package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Keyboard sheet class
 *
 * Created by tozawa on 2017/03/07.
 */
public class KeyboardSheet {

    XSSFSheet sheet;

    public KeyboardSheet(XSSFSheet spreadsheet) {
        sheet = spreadsheet;
    }

    /**
     * Get the cell of the given key.
     * @param key the key
     * @param shift whether shift key is pressed
     * @return cell
     */
    public KeyboardCell getKeyboardCell(String key, boolean shift) {
        List<Cell> cells = findAllStringCells(key);
        Cell labelCell = shift ? cells.get(1) : cells.get(0);
        Cell contentCell = getCell(labelCell.getRowIndex() + 1, labelCell.getColumnIndex());
        return new KeyboardCell(labelCell, contentCell);
    }

    /**
     * Set value for the cell of the key.
     * @param key the key
     * @param shift whether shift key is present
     * @param value the value
     */
    public void setKeyboardCell(String key, boolean shift, String value) {
        KeyboardCell keyboardCell = getKeyboardCell(key, shift);
        keyboardCell.getContent().setCellValue(value);
    }

    public void setKeyboardCellColor(String key, boolean shift, XSSFColor color) {
        KeyboardCell keyboardCell = getKeyboardCell(key, shift);
        Cell cell = keyboardCell.getContent();
        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        cell.setCellStyle(style);
    }

    /**
     * Set value and color for the cell of the key.
     * @param key the key
     * @param shift whether shift key is present
     * @param value the value
     * @param color the color
     */
    public void setKeyboardCellWithColor(String key, boolean shift, String value, XSSFColor color) {
        setKeyboardCell(key, shift, value);
        setKeyboardCellColor(key, shift, color);
    }


    /**
     * get the cell of the given position.
     * @param numRow row index
     * @param numColumn column index
     * @return a cell
     */
    private Cell getCell(int numRow, int numColumn) {
        return sheet.getRow(numRow).getCell(numColumn);
    }

    /**
     * find cells that have the given string value.
     * @param string value to search
     * @return a list of cells
     */
    private List<Cell> findAllStringCells(String string) {
        List<Cell> matchedCells = new ArrayList<>();
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                try {
                    String value = cell.getStringCellValue();
                    if (string.equals(value)) {
                        matchedCells.add(cell);
                    }
                } catch (IllegalStateException e) {
                    // Do nothing if not a string cell
                }
            }
        }
        return matchedCells;
    }

    /**
     * find the first cell that have the given string value.
     * @param string value to search
     * @return a cell
     */
    private Cell findFirstStringCell(String string) {
        List<Cell> result = findAllStringCells(string);
        return result.get(0);
    }
}
