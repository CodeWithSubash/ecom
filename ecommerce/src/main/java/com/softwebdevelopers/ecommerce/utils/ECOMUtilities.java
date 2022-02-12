package com.softwebdevelopers.ecommerce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwebdevelopers.ecommerce.common.ECOMConstants;
import com.softwebdevelopers.ecommerce.common.Preconditions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ECOMUtilities {

    static ObjectMapper mapper = new ObjectMapper();

    public static String getJSONString(Object object, boolean prettyPrint) {
        if (object == null)
            return null;
        try {
            if (prettyPrint)
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            else
                return mapper.writeValueAsString(object);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validateEmail(String addr) {
        if (addr == null || addr.length() == 0) {
            return false;
        }
        return Pattern.matches(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", addr);
    }

    public static int booleanToInteger(Boolean bool) {
        if (bool == null)
            return 0;
        if (bool)
            return 1;
        return 0;
    }

    public static boolean integerToBoolean(int n) {
        if (n == 0)
            return false;
        if (n == 1)
            return true;
        return false;
    }

    public static <E extends Enum<E>> boolean enumContains(Class<E> enumerator, String value) {
        return Arrays.stream(enumerator.getEnumConstants()).anyMatch(e -> e.name().equals(value));
    }

    public static String convertUnixTimeToStringDate(Long unix_seconds) {
        if (unix_seconds != null) {
            // convert seconds to milliseconds
            Date date = new Date(unix_seconds * 1000L);
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		   jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            String java_date = jdf.format(date);
            return java_date;
        } else {
            return null;
        }
    }

    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[(data.length) / 3];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressBytes(byte[] data) {
        Inflater inflator = new Inflater();
        inflator.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[(data.length) / 3];

        try {
            while (!inflator.finished()) {
                int count = inflator.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {

        } catch (DataFormatException e) {

        }
        return outputStream.toByteArray();
    }

    public static java.sql.Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new java.sql.Date(cal.getTime().getTime());
    }

    public static Long calculateExpiryUnixTime(Long expiryTimeInMinutes) {
        return Instant.now().getEpochSecond() + expiryTimeInMinutes;
    }

    public static Long getUnixTime() {
        return Instant.now().getEpochSecond();
    }

    public static String generateOTP() {
        String numbers = "0123456789";

        Random rndm_method = new Random();

        char[] otp = new char[ECOMConstants.OTP_LENGTH];

        for (int i = 0; i < ECOMConstants.OTP_LENGTH; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }

    public static String generatePassword() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "[{()}]@%$&#" + "abcdefghijklmnopqrstuvxyz";

        Random rndm_method = new Random();

        char[] otp = new char[ECOMConstants.PASSWORD_LENGTH];

        for (int i = 0; i < ECOMConstants.PASSWORD_LENGTH; i++) {
            otp[i] = AlphaNumericString.charAt(rndm_method.nextInt(AlphaNumericString.length()));
        }
        return String.valueOf(otp);
    }

    public static String generateGUIDLink() {
        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(ECOMConstants.LINK_LENGTH);

        for (int i = 0; i < ECOMConstants.LINK_LENGTH; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public static Time convertStringToSqlDate1(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long ms = sdf.parse(date).getTime();
        Time t = new Time(ms);
        return t;
    }

    public static Time stringToSqlTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long ms = sdf.parse(time).getTime();
        Time t = new Time(ms);
        return t;
    }

    public static boolean deleteSingleFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("File deleted successfully");
                return true;
            } else {
                System.out.println("Fail to delete file");
                return false;
            }
        }
        return false;
    }

    public static boolean deleteMultipleFiles(String foldername) {
        File folder = new File(foldername);
        if (folder.exists() && folder.isDirectory()) {

            for (File f : folder.listFiles()) {
                if (f.delete()) {
                    System.out.println("'" + f.getName() + "' deleted successfully");
                } else {
                    System.out.println("Fail to delete '" + f.getName() + "'");
                }
            }
            return true;
        }
        return false;
    }

    public static boolean deleteFolderWithFiles(String foldername) {
        if (deleteMultipleFiles(foldername)) {
            File folder = new File(foldername);
            if (folder.exists() && folder.isDirectory()) {
                if (folder.delete()) {
                    System.out.println("Folder deleted successfully");
                    return true;
                } else {
                    System.out.println("Fail to delete folder");
                    return false;
                }
            }
        }
        return true;
    }

    // util.Date to LocalDateTime via Instant
    public static LocalDateTime convertToLocalDateTime(String strDate, String dateFormat) {
        if(Preconditions.checkBlank(strDate))
            return null;
        if(Preconditions.checkBlank(dateFormat))
            dateFormat = "yyyy-MM-dd HH:mm:ss";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDateTime.parse(strDate, formatter);
    }

    // util.Date to LocalTime via Instant
    public static LocalTime convertToLocalTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    public static LocalTime convertToLocalTimeViaSqlDate(long dateToConvert) {
        return new java.sql.Time(dateToConvert).toLocalTime();
    }

    // util.Date to LocalDate via Instant
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // util.Date to LocalDate via sql.Date
    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    // util.Date to LocalDateTime via Instant
    public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // util.Date to LocalDateTime via sql.Timestamp
    public static LocalDateTime convertToLocalDateTimeViaSqlTimestamp(Date dateToConvert) {
        return new java.sql.Timestamp(dateToConvert.getTime()).toLocalDateTime();
    }

    // LocalDate to util.Date via Instant
    public static Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    // LocalDate to util.Date via sql.Date
    public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    // LocalDate to util.Date via Instant
    public static Date convertToDateTimeViaInstant(LocalDate dateToConvert, LocalTime timeToConvert) {
        // the conversion based on your system timezone
//			Instant instant = dateToConvert.atTime(timeToConvert).atZone(ZoneId.systemDefault()).toInstant();
//			Date d = DateTimeUtils.toDate(instant);

        return java.util.Date.from(dateToConvert.atTime(timeToConvert).atZone(ZoneId.systemDefault()).toInstant());
    }

    // Generic function to convert set to list
    public static <T> List<T> convertSetToList(Set<T> set) {
        // create a list from Set
        List<T> list = new ArrayList<>(set);
        return list;
    }

    public static String convertToPascalSpaceCase(String str) {

        String[] temp = str.split(" ");

        String[] result = new String[temp.length];

        for (int i = 0; i < temp.length; i++) {
            result[i] = temp[i].substring(0, 1).toUpperCase()
                    .concat(temp[i].substring(1, temp[i].length()).toLowerCase());
        }

        return String.join(" ", result);
    }
}
