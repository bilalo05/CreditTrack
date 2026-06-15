package com.example.creditservice;

public class Constants {
    public static  final  String ipAddress ="https://www.mycreditsv.com";
    private static final String ROOT_URL =
            "https://www.mycreditsv.com/android/v1/";

    public static final String URL_Add = ROOT_URL + "AddNew.php";
    public static final String URL_LOGIN = ROOT_URL + "UserLogin.php";
    public static final String URL_DEBTORS = ROOT_URL + "GetDebtors.php";
    // debt auto_update
    public static final String URL_DEBT = ROOT_URL + "Api.php";

    //update debt
    public static final String URL_DEBT_UPDATE = ROOT_URL + "UpdateProduct.php";

    //Add Token
    public static final String URL_Add_TOKEN = ROOT_URL + "fcmInsert.php";

    //send notification
    public static  final String URL_NOTIFICATION = ROOT_URL + "SendNotification.php";


}
