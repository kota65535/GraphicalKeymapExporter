package com.kota65535;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.IntStream.range;

/**
 * Created by tozawa on 2017/03/07.
 */
public class KeyboardSheet {

    XSSFSheet sheet;

    public KeyboardSheet(XSSFSheet spreadsheet) {
        sheet = spreadsheet;
    }

    /**
     * Get cell of given key.
     * @param key the key
     * @param shift whether shift key is pressed
     * @return
     */
    public Cell getKeyboardCell(String key, boolean shift) {
        List<Cell> cells = findAllStringCells(key);
        Cell charCell = shift ? cells.get(1) : cells.get(0);
        return getCell(charCell.getRowIndex() + 1, charCell.getColumnIndex());
    }

    /**
     * Set value for the cell of the key.
     * @param key the key
     * @param shift whether shift key is present
     * @param value the value
     */
    public void setKeyboardCell(String key, boolean shift, String value) {
        Cell cell = getKeyboardCell(key, shift);
        cell.setCellValue(value);
    }

    /**
     * Set value and color for the cell of the key.
     * @param key the key
     * @param shift whether shift key is present
     * @param value the value
     * @param color the color
     */
    public void setKeyboardCell(String key, boolean shift, String value, Color color) {
        // Set value
        Cell cell = getKeyboardCell(key, shift);
        cell.setCellValue(value);
        // Set color
        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        XSSFColor myColor = new XSSFColor(color);
        style.setFillForegroundColor(myColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(style);
    }

    private Cell getCell(int numRow, int numColumn) {
        Iterator<Row> rows = sheet.rowIterator();
        range(0, numRow).forEach(i -> rows.next());
        Row row = rows.next();
        Iterator<Cell> cells = row.cellIterator();
        range(0, numColumn).forEach(i -> cells.next());
        return cells.next();
    }

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

    private Cell findFirstStringCell(String string) {
        List<Cell> result = findAllStringCells(string);
        return result.get(0);
    }
}
