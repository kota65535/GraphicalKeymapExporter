package com.kota65535.intellij.plugin.keymap.exporter2.sheet;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by tozawa on 2017/04/16.
 */
@Data
@AllArgsConstructor
public class KeyboardCell {
    Cell label;
    Cell content;
}
