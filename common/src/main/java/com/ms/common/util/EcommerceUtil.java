package com.ms.common.util;

import java.util.Random;

public class EcommerceUtil {

    public static int getRandomNumber(int interval1, int interval2){
        Random random = new Random();
        double v = random.nextDouble();
        return (int) (v*interval2) + interval1;
    }

    public static boolean isPaymentSuccess(int chance){
        int randomNumber = getRandomNumber(0, 100);
        System.out.println(randomNumber);
        if(chance <0 || chance > 100) return false;
        return chance > randomNumber;
    }

    public static boolean isStockSuccess(int chance){
        int randomNumber = getRandomNumber(0, 100);
        System.out.println(randomNumber);
        if(chance <0 || chance > 100) return false;
        return chance > randomNumber;
    }
}
