package com.softwebdevelopers.ecommerce.common;

public class ECOMMessage {
    public static String HTTP_METHOD_NOT_SUPPORTED = "Method Is Not Supported For This Request";
    public static String MEDIA_TYPE_NOT_SUPPORTED = "Media Type Is Not Supported";
    public static String METHOD_ARGUMENT_NOT_VALID = "Method Argument Is Not Valid";

    public static String EMPTY_RESULT = "No results";
    public static String RECORD_DOES_NOT_EXIST = "Record Does Not Exists";
    public static String UNPROCESSABLE_ENTITY_FOUND = "Entity Not Supported";
    public static String RECORD_ALREADY_EXIST = "Record Already Exists";
    public static String RECORD_CONFLICT_OCCURRED = "Record Conflict Occurred";
    public static String RECORD_EXISTS = "Record Exists";
    public static String RECORD_DELETED = "Record Deleted Successfully";
    public static String PAYMENT_FAILED = "Payment is Failed";
    public static String BAD_REQUEST = "Record is Proceeded with Bad Request.";
    public static String CREATION_FAILED = "Creation Failed.";
    public static String UPDATE_FAILED = "Update Failed.";

    //COUPONS
    public static String COUPON_NOT_VALID = "Coupon code is not valid.";

    public static String CONSTRAINT_VIOLATION = "Violation Of Constraint";
    public static String DATA_INTEGRITY_VIOLATION = "Violation Of Data Integrity";
    public static String DATABASE_CONNECTION_ERROR = "Database Connection Error";
    public static String EMAIL_CONFIG_ERROR = "Email Configuration Error";

    public static String EMAIL_NOT_VERIFIED = "Please verify the email.";
    public static String BAD_CREDENTIALS = "Username/Password is invalid.";

    // EMAIL SENDING MESSAGES
    public static String EMAIL_SUBJECT_REGISTRATION_TOKEN = ECOMConstants.APPLICATION_NAME + " - Registration Confirmation";
    public static String EMAIL_BODY_REGISTRATION_TOKEN = "We are excited to have you get started with " + ECOMConstants.APPLICATION_NAME + ". First, you need to confirm your account. Just press the button below.";


    public static String EMAIL_SUBJECT_RESEND_REGISTRATION_TOKEN = ECOMConstants.APPLICATION_NAME + " - Registration Confirmation: Resend Token";
    public static String EMAIL_BODY_RESEND_REGISTRATION_TOKEN = "You have requested for new Token to activate the account. Just press the button below.";

    public static String EMAIL_SUBJECT_FORGOT_PASSWORD_TOKEN = ECOMConstants.APPLICATION_NAME + " - Forgot Password";
    public static String EMAIL_BODY_FORGOT_PASSWORD_TOKEN = "You have requested for new Token to change the account password. Just press the button below.";
}
