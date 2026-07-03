package com.ms.payment_service.util;

import java.util.Random;

public class PaymentUtil {


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



}
