package org.hello.item01.flyweightpattern.after;

import java.util.HashMap;
import java.util.Map;

public class FontFactory {
    public static final String REGEX = ":";
    private Map<String, Font> cache = new HashMap<>();

    public Font getFont(String font) {
        if (cache.containsKey(font)) {
            return cache.get(font);
        }
        return createFont(font);
    }

    private Font createFont(String font) {
        String[] split = font.split(REGEX);
        Font newFont = new Font(split[0], Integer.parseInt(split[1]));
        cache.put(font, newFont);
        return newFont;
    }
}
