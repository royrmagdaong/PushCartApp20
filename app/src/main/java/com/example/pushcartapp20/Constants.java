package com.example.pushcartapp20;

public class Constants {
    public static String IPADDRESS = "192.168.0.105";
    public static String ROOT_URL = "http://"+IPADDRESS+"/pushcart/v1/";

    public static String URL_SENDTOCASHIER = ROOT_URL + "sendToCashier.php";
    public static String URL_GETQUEUE = ROOT_URL+ "getQueue.php";
    public static String URL_LOGIN = ROOT_URL+ "userLogin.php";
    public static String URL_GETALLPRODUCTS = ROOT_URL+ "getProducts.php";
    public static String URL_GETSAMPLE = ROOT_URL+ "getSample.php";

    public static int CART_NUMBER = 3;
    public static String ADMINPASSWORD = "123321";

    public static boolean OFFLINE_MODE = false;
    public static boolean SENIOR_PWD = false;


}
