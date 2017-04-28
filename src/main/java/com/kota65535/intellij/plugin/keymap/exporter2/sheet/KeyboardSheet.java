package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
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

    Logger logger = Logger.getInstance(KeyboardSheet.class);

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
        if (StringUtil.isEmpty(key)) {
            return null;
        }
        List<Cell> cells = findAllStringCells(key, true);
        if (cells.isEmpty()) {
            logger.warn(String.format("Key: '%s' with shift=%s not found", key, shift));
            return null;
        }
        Cell labelCell = shift ? cells.get(1) : cells.get(0);
        return new KeyboardCell(sheet, labelCell);
    }

    /**
     * Set value for the cell of the key.
     * @param key the key
     * @param shift whether shift key is present
     * @param value the value
     */
    public void setKeyboardCell(String key, boolean shift, String value) {
        KeyboardCell keyboardCell = getKeyboardCell(key, shift);
        if (keyboardCell == null) {
            logger.warn(String.format("Cannot set key: '%s' with shift=%s", key, shift));
        } else {
            keyboardCell.setBody(value);
        }
    }

    public void setKeyboardCell(String key, boolean shift, String first, String second) {
        KeyboardCell keyboardCell = getKeyboardCell(key, shift);
        if (keyboardCell == null) {
            logger.warn(String.format("Cannot set key: '%s' with shift=%s", key, shift));
        } else {
            keyboardCell.setBodies(first, second);
        }
    }

//    public void setKeyboardCellColor(String key, boolean shift, XSSFColor color1, XSSFColor color2) {
//        KeyboardCell keyboardCell = getKeyboardCell(key, shift);
//        if (keyboardCell == null) {
//            logger.warn(String.format("Cannot set key color: '%s' with shift=%s", key, shift));
//        } else {
//            keyboardCell.setBodies()
//            Cell cell = keyboardCell.getBody();
//            XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
//            style.cloneStyleFrom(cell.getCellStyle());
//            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            style.setFillForegroundColor(color1);
//            cell.setCellStyle(style);
//            cell = keyboardCell.getSecondBody();
//            style = sheet.getWorkbook().createCellStyle();
//            style.cloneStyleFrom(cell.getCellStyle());
//            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//            style.setFillForegroundColor(color2);
//            cell.setCellStyle(style);
//        }
//    }
//
//    /**
//     * Set value and color for the cell of the key.
//     * @param key the key
//     * @param shift whether shift key is present
//     * @param value the value
//     * @param color the color
//     */
//    public void setKeyboardCellWithColor(String key, boolean shift, String value, XSSFColor color) {
//        setKeyboardCell(key, shift, value);
//        setKeyboardCellColor(key, shift, color);
//    }


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
    private List<Cell> findAllStringCells(String string, boolean ignoreCase) {
        List<Cell> matchedCells = new ArrayList<>();
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                try {
                    String value = cell.getStringCellValue();
                    if (ignoreCase) {
                        if (string.toUpperCase().equals(value.toUpperCase())) {
                            matchedCells.add(cell);
                        }
                    } else {
                        if (string.equals(value)) {
                            matchedCells.add(cell);
                        }
                    }
                } catch (IllegalStateException e) {
                    // Do nothing if not a string cell
                }
            }
        }
        return matchedCells;
    }

    public Cell searchBorderedCellToBottom(Cell currentCell) {
        int currentRowIndex = currentCell.getRowIndex();
        int columnIndex = currentCell.getColumnIndex();
        while (true) {
            currentRowIndex++;
            Cell cell = getCell(currentRowIndex, columnIndex);
            if (cell == null) {
                break;
            }
            if (cell.getCellStyle().getBorderBottomEnum() != BorderStyle.NONE) {
                return cell;
            }
        }
        return null;
    }

    public Cell searchBorderedCellToRight(Cell currentCell) {
        int currentColumnIndex = currentCell.getColumnIndex();
        int rowIndex = currentCell.getRowIndex();
        while (true) {
            currentColumnIndex++;
            Cell cell = getCell(rowIndex, currentColumnIndex);
            if (cell == null) {
                break;
            }
            if (cell.getCellStyle().getBorderRightEnum() != BorderStyle.NONE) {
                return cell;
            }
        }

        return null;
    }


    /**
     * find the first cell that have the given string value.
     * @param string value to search
     * @return a cell
     */
    private Cell findFirstStringCell(String string) {
        List<Cell> result = findAllStringCells(string, true);
        return result.get(0);
    }

}
