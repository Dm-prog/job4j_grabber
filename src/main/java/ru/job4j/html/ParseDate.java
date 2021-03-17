package ru.job4j.html;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParseDate {

    private final static Map<String, Integer> MONTH_MAPPER = new HashMap<>() {
        {
            put("янв", 1);
            put("фев", 2);
            put("мар", 3);
            put("апр", 4);
            put("май", 5);
            put("июн", 6);
            put("июл", 7);
            put("авг", 8);
            put("сен", 9);
            put("окт", 10);
            put("ноя", 11);
            put("дек", 12);
        }
    };

    LocalDate parse(String date) {

        String[] parts = date.split(" ");
        if (parts[0].replace(",", "").equals("вчера")) {
            return LocalDate.now().minusDays(1);
        } else if (parts[0].replace(",", "").equals("сегодня")) {
            return LocalDate.now();
        }
        int day = Integer.parseInt(parts[0]);
        int month = MONTH_MAPPER.get(parts[1]);
        int year = 2000 + Integer.parseInt(parts[2].replace(",", ""));
        return LocalDate.of(year, month, day);
    }
}
