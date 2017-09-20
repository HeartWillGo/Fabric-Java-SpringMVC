package com.heartgo.controller;

public class SingleEnd2End {
    private static End2end instance = new End2end();
    private SingleEnd2End (){}
    public static   End2end getEnd2EndInstance() {

        return instance;
    }
}
