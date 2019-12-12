package com.utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Tools {
    public static void cgJLabelImg(JLabel jLabel, URL imgUrl){
        ImageIcon icon = new ImageIcon(imgUrl);
        int picWidth = icon.getIconWidth(),pinHeight = icon.getIconHeight();
        icon.setImage(icon.getImage().getScaledInstance(picWidth,pinHeight, Image.SCALE_DEFAULT));
        jLabel.setBounds(0,0,picWidth,pinHeight);
        jLabel.setIcon(icon);



    }


}
