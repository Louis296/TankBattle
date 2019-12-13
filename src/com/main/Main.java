package com.main;

import com.frame.WelcomeFrame;
import com.model.TankClient;
import com.music.*;


public class Main {

    public static void main(String[] args) {
        new WelcomeFrame().go();
        new TankClient();
        new Play("D:\\Java\\TankBattle\\music\\bgm.mp3").start();
    }
}
