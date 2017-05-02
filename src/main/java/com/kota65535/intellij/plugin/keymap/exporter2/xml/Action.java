package com.kota65535.intellij.plugin.keymap.exporter2.xml;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.util.List;

/**
 * Created by tozawa on 2017/04/30.
 */
@Data
public class Action {
    private String actionId;
    private String text;
    private String description;
    private int color;

    public Action(String actionId, String text, String description, String color) {
        this.actionId = actionId;
        this.text = text;
        this.description = description;
        try {
            this.color = Integer.parseInt(color);
        } catch (NumberFormatException ex) {
            this.color = 0xFFFFFF;
        }

    }
}
