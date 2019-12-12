package com.main;

import com.frame.WelcomeFrame;
import com.model.TankClient;


public class Main {

    public static void main(String[] args) {
        new WelcomeFrame().go();
        new TankClient();
    }
}
