package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
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
        if (isBodyDivided()) {
            secondBody = sheet.getRow(label.getRowIndex()+3).getCell(label.getColumnIndex());
        } else {
            secondBody = null;
        }

    }

    private boolean isBodyDivided() {
        return getBodyRangeAddressIndex() == -1;
    }

    private int getBodyRangeAddressIndex() {
        return IntStream.range(0, sheet.getMergedRegions().size())
                .filter( i -> {
                    CellRangeAddress addr = sheet.getMergedRegions().get(i);
                    return addr.getFirstRow() == body.getRowIndex()
                            && addr.getLastRow() == body.getRowIndex()+3
                            && addr.getFirstColumn() == body.getColumnIndex()
                            && addr.getLastColumn() == body.getColumnIndex()+3;
                })
                .findFirst().orElse(-1);
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
        int removedIndex = getBodyRangeAddressIndex();

        if (removedIndex > -1) {
            sheet.removeMergedRegion(removedIndex);
        }

        sheet.addMergedRegion(new CellRangeAddress(body.getRowIndex(), body.getRowIndex()+1,
                body.getColumnIndex(), body.getColumnIndex()+3));
        sheet.addMergedRegion(new CellRangeAddress(body.getRowIndex()+2, body.getRowIndex()+3,
                body.getColumnIndex(), body.getColumnIndex()+3));

        secondBody = sheet.getRow(label.getRowIndex()+3).getCell(label.getColumnIndex());

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
