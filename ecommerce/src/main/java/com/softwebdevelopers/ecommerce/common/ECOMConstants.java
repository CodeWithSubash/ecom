package com.softwebdevelopers.ecommerce.common;

public class ECOMConstants {

    public static String APPLICATION_NAME = "Ecommerce";
    public static String HTTP_PROTOCOL = "http";

    public static final String BASE_URL = "/api";
    public static String EMAIL_SENDER_ID = "noreply@softecommerce.com";
    public static String ANONYMOUS_USER = "anonymousUser";

    public static int OTP_LENGTH = 6;
    public static int LINK_LENGTH = 32;
    public static int PASSWORD_LENGTH = 6;
    public static String INVOICE_CHAR = "INV";

    public static String ORDER_NO_DETAIL = "NO_DETAIL";
    public static String ORDER_WITH_DETAIL = "WITH_DETAIL";

    public static String EMAIL_TEXT = "EMAIL";
    public static String USERNAME_TEXT = "USERNAME";

    //	CONSTANTS USED FOR AUDIT ACTIVITY RECORD LOG
    public static String SIGNIN = "SIGNIN";
    public static String CREATED = "CREATED";
    public static String UPDATED = "UPDATED";
    public static String DELETED = "DELETED";

    public static String NEW_EMAIL = "NEW EMAIL";
    public static String RESEND_EMAIL = "RESEND EMAIL";
    public static String PASSWORD_CHANGED_EMAIL = "PASSWORD CHANGED EMAIL";
    public static String FORGOT_PASSWORD_TOKEN_EMAIL = "FORGOT PASSWORD TOKEN EMAIL";
    public static String INVALID_TOKEN = "Invalid Token";
    public static String TOKEN_EXPIRED = "Token Expired";
    public static String ALREADY_VERIFIED = "Already Verified";

    public static Message REGISTRATION_SUCCESS = new Message(EMessageType.SUCCESS, "Registration Successful");
    public static Message INVALID_TOKEN1 = new Message(EMessageType.INFO, "Invalid Token");
    public static Message TOKEN_EXPIRED1 = new Message(EMessageType.INFO, "Token Expired");
    public static Message PROCEED_TO_LOGIN = new Message(EMessageType.SUCCESS, "User Activated successfully, You can proceed to Login");
    public static Message NEW_MAIL_SENT = new Message(EMessageType.SUCCESS, "New mail for new token sent");
    public static Message INVALID_OTP = new Message(EMessageType.INFO, "Invalid OTP");
    public static Message OTP_EXPIRED = new Message(EMessageType.INFO, "OTP Expired");
    public static Message PASSWORD_CHANGE_SUCCESS = new Message(EMessageType.SUCCESS, "Password Change Successful");
    public static Message PASSWORD_REPASSWORD_NOT_MATCHED = new Message(EMessageType.WARN, "Password and confirm password didn't matched.");
    public static Message PASSWORD_NEW_PASSWORD_CANT_SAME = new Message(EMessageType.WARN, "Current password and new password can't be same. Please try with different password.");

    public static class EmailTemplates {
        public static String USER_ACTIVATION_TEMPLATE = "activation-template.html";
        public static String FORGOT_PASSWORD_TOKEN_TEMPLATE = "forgot-password-token-template.html";
        public static String FORGOT_PASSWORD_CHANGED_TEMPLATE = "changed-password-template.html";
        public static String APPOINTMENT_BOOKED_TEMPLATE = "appointment-booked.html";
        public static String THANK_YOU_EMAIL_TEMPLATE = "thankyou-email-template.html";
        public static String PHASE_TWO_EMAIL_TEMPLATE = "document-email-template.html";
    }

    public static class ThankYouEmailConstants {

        public static String NEW_USER_REGISTRATION = "NEW_USER_REGISTRATION";
        public static String PASSWORD_CHANGED = "PASSWORD_CHANGED";
    }
}
