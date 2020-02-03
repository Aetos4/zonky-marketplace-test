package cz.aetos.zonky.zonkyapiconnector.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ZonkyAPIUtil {

    static String convertDateFormat(String responseDate, String fromDatePattern, String toDatePattern) {
        LocalDateTime datetime = LocalDateTime.parse(responseDate, DateTimeFormatter.ofPattern(fromDatePattern));
        return datetime.format(DateTimeFormatter.ofPattern(toDatePattern));
    }

}
