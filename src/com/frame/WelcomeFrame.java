package com.frame;

import javax.swing.*;

import com.music.Play;
import com.utils.*;

import java.awt.*;

public class WelcomeFrame extends JFrame {

    public static final int Fram_width = 800;
    public static final int Fram_length = 600;
    public void go() {
        this.setBounds(500,300,Fram_width,Fram_length);
        this.getContentPane().setLayout(null);

        JLabel jLabel=new JLabel();
        Tools.cgJLabelImg(jLabel,WelcomeFrame.class.getResource("/Images/welcome.png"));
        this.add(jLabel);
        this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,0));
        this.setType(Type.UTILITY);
        this.setVisible(true);
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.setVisible(false);
       // new Play("D:\\Java\\TankBattle\\music\\bgm.mp3").start();
    }
}
