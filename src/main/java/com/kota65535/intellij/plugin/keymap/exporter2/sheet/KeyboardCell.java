package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.stream.IntStream;

/**
 * Created by tozawa on 2017/04/16.
 */
@Getter
public class KeyboardCell {
    XSSFSheet sheet;
    Cell label;
    Cell body;
    Cell secondBody;

    public KeyboardCell(XSSFSheet sheet, Cell label) {
        this.sheet = sheet;
        this.label = label;
        body = sheet.getRow(label.getRowIndex()+1).getCell(label.getColumnIndex());
        if (isKeyboardCellDivided()) {
            CellRangeAddress address = sheet.getMergedRegion(getBodyRegionIndex());
            secondBody = sheet.getRow(address.getLastRow()+1).getCell(body.getColumnIndex());
        } else {
            secondBody = null;
        }
    }

    private int getBodyRegionIndex() {
        return IntStream.range(0, sheet.getMergedRegions().size())
                .filter( i -> {
                    CellRangeAddress addr = sheet.getMergedRegions().get(i);
                    return addr.getFirstRow() == body.getRowIndex()
                            && addr.getFirstColumn() == body.getColumnIndex();
                })
                .findFirst().orElse(-1);
    }

    private boolean isKeyboardCellDivided() {
        CellRangeAddress address = sheet.getMergedRegion(getBodyRegionIndex());
        Cell cell = sheet.getRow(address.getFirstRow()).getCell(address.getFirstColumn());
        if ( cell.getCellStyle().getBorderBottomEnum() != BorderStyle.DASHED) {
            return false;
        }
        return true;
    }

    private void setColor(Cell cell, XSSFColor color) {
        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
        cell.setCellStyle(style);
    }

    public KeyboardCell setBody(String content) {
        body = sheet.getRow(label.getRowIndex()+1).getCell(label.getColumnIndex());
        body.setCellValue(content);
        return this;
    }

    public KeyboardCell setBody(String content, XSSFColor color) {
        setBody(content);
        setColor(body, color);
        return this;
    }

    public KeyboardCell setBodies(String first, String second) {

        if (isKeyboardCellDivided()) {
            body.setCellValue(first);
            secondBody.setCellValue(second);
            return this;
        }

        int removedIndex = getBodyRegionIndex();
        CellRangeAddress address = sheet.getMergedRegion(removedIndex);

        CellRangeAddress firstAddress = new CellRangeAddress(
                address.getFirstRow(),
                (address.getFirstRow() + address.getLastRow()) / 2,
                address.getFirstColumn(),
                address.getLastColumn());
        CellRangeAddress secondAddress = new CellRangeAddress(
                (address.getFirstRow() + address.getLastRow()) / 2 + 1,
                address.getLastRow(),
                address.getFirstColumn(),
                address.getLastColumn());

        sheet.removeMergedRegion(removedIndex);

        sheet.addMergedRegion(firstAddress);
        sheet.addMergedRegion(secondAddress);

        secondBody = sheet.getRow((address.getFirstRow() + address.getLastRow()) / 2 + 1)
                .getCell(address.getFirstColumn());

        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.cloneStyleFrom(body.getCellStyle());
        style.setBorderBottom(BorderStyle.DASHED);
        body.setCellStyle(style);
        body.setCellValue(first);
        secondBody.setCellValue(second);
        return this;
    }

    public KeyboardCell setBodies(String first, String second, XSSFColor color1, XSSFColor color2) {
        setBodies(first, second);
        setColor(body, color1);
        setColor(secondBody, color2);
        return this;
    }
}
