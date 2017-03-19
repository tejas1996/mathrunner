package com.vish.imagetotext.ocr.sample;

/**
 * Created by Tejus dond on 18 03 2017.
 */
public class anss {
    double aa,bb;
    public anss solquad(int a,int b,int c){

        double temp;
        temp = Math.sqrt((b*b)-(4*a*c));
        System.out.println(temp);
        aa = (-b + temp)/(2*a);
        bb = (-b - temp)/(2*a);
        return this;
    }



}
