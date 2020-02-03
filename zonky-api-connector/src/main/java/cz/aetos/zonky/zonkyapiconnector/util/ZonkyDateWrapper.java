package cz.aetos.zonky.zonkyapiconnector.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ZonkyDateWrapper {

    private static final Log logger = LogFactory.getLog(ZonkyDateWrapper.class);

    public enum Type {REQUEST, RESPONSE}

    final private Type type;

    private String date;

    private String responsePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    private String requestPattern = "yyyy-MM-dd'T'HH:mm:ss";

    public ZonkyDateWrapper(Type type, Date date) {
        this.responsePattern = responsePattern;
        this.requestPattern = requestPattern;
        this.type = type;
        this.date = getDateString(date, type == Type.REQUEST ? requestPattern: responsePattern);
    }

    public ZonkyDateWrapper(Type type, String date) {
        this.responsePattern = responsePattern;
        this.requestPattern = requestPattern;
        this.type = type;
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public String getRequestDate() {
        return Type.REQUEST == type ? date : ZonkyAPIUtil.convertDateFormat(date, responsePattern, requestPattern);
    }

    public String getResponseDate() {
        return Type.RESPONSE == type ? date : ZonkyAPIUtil.convertDateFormat(date, requestPattern, responsePattern);
    }

    public String getDate() {
        return Type.REQUEST == type ? getRequestDate(): getResponseDate();
    }

    private static String getDateString(Date date, String datePattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        return simpleDateFormat.format(date);
    }

    public void addSecondsToDate(int seconds) {
        try {
            Date date = getSimpleDateFormat().parse(this.date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, seconds);
            this.date = getSimpleDateFormat().format(calendar.getTime());
        } catch (ParseException e) {
            logger.error("Error occurred during parsing date from api", e);
        }
    }

    private SimpleDateFormat getSimpleDateFormat() {
        return type == Type.REQUEST ? new SimpleDateFormat(requestPattern): new SimpleDateFormat(responsePattern);
    }

}
