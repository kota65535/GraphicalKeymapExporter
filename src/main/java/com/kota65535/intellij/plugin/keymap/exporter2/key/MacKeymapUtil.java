/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kota65535.intellij.plugin.keymap.exporter2.key;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Utility class to display action shortcuts in Mac menus
 *
 * @author Konstantin Bulenkov
 */
@SuppressWarnings("UnusedDeclaration")
public class MacKeymapUtil {

    public static EnumSet<Modifier> getModifiers(String keyText) {
        EnumSet<Modifier> result = EnumSet.noneOf(Modifier.class);
        if ((keyText.contains(MacKey.SHIFT.getOriginalString()))) {
            result.add(Modifier.SHIFT);
        }
        if ((keyText.contains(MacKey.CONTROL.getOriginalString()))) {
            result.add(Modifier.CTRL);
        }
        if ((keyText.contains(MacKey.OPTION.getOriginalString()))) {
            result.add(Modifier.ALT);
        }
        if ((keyText.contains(MacKey.COMMAND.getOriginalString()))) {
            result.add(Modifier.META);
        }
        return result;
    }

    public static String normalizeKeyText(String keyText) {
        String result = keyText;
        EnumSet<MacKey> candidates = Arrays.stream(MacKey.values())
                .filter( v -> v.getNormalizedString() != null)
                .collect(Collectors.toCollection( () -> EnumSet.noneOf(MacKey.class)));
        for (MacKey v : candidates) {
            result = result.replace(v.getOriginalString(), v.getNormalizedString());
        }
        System.err.printf("%s -> %s\n", keyText, result);

        return result;
    }

    public static String stripModifiers(String keyText) {
        String result = new String(keyText);
        EnumSet<MacKey> mods = EnumSet.of(MacKey.SHIFT, MacKey.CONTROL, MacKey.OPTION, MacKey.COMMAND);
        for(MacKey v : mods) {
            result = result.replace(v.getOriginalString(), "");
        }
        return result;
    }

}
