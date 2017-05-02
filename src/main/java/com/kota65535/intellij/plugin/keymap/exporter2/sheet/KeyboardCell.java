package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import com.intellij.openapi.util.text.StringUtil;
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

    public KeyboardCell setBody(String content) {
        body.setCellValue(content);
        return this;
    }

    public KeyboardCell setBody(String content, XSSFColor color) {
        setBody(content);
        setColor(body, color);
        return this;
    }

    public KeyboardCell setBody(String content, XSSFColor color, String comment) {
        setBody(content);
        setColor(body, color);
        setComment(body, comment);
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

    public KeyboardCell setBodies(String first, XSSFColor color1, String second, XSSFColor color2) {
        setBodies(first, second);
        setColor(body, color1);
        setColor(secondBody, color2);
        return this;
    }

    public KeyboardCell setBodies(String first, XSSFColor color1, String comment1,
                                  String second, XSSFColor color2, String comment2) {
        setBodies(first, second);
        setColor(body, color1);
        setColor(secondBody, color2);
        setComment(body, comment1);
        setComment(secondBody, comment2);
        return this;
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

    private void setComment(Cell cell, String string) {
        if (StringUtil.isEmpty(string)) return;
        CreationHelper helper = sheet.getWorkbook().getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(100, 100, 100, 100,
                cell.getColumnIndex(), cell.getRowIndex(),
                cell.getColumnIndex()+7, cell.getRowIndex()+2);
        Comment comment = drawing.createCellComment(anchor);

        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short)18);
        RichTextString richTextString = helper.createRichTextString(string);
        richTextString.applyFont(font);
        comment.setString(richTextString);
        cell.setCellComment(comment);
    }

}
