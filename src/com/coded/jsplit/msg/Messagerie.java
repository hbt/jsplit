package com.coded.jsplit.msg;

import javax.swing.*;

public class Messagerie {
    public static String titre;

    public static void msge(String message) {
        msg(message, JOptionPane.ERROR_MESSAGE);
    }

    public static void msg(String message) {
        msg(message, JOptionPane.PLAIN_MESSAGE);
    }

    public static void msgi(String message) {
        msg(message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void msgq(String message) {
        msg(message, JOptionPane.QUESTION_MESSAGE);
    }

    public static void msgw(String message) {
        msg(message, JOptionPane.WARNING_MESSAGE);
    }

    public static void msg(String msg, int type) {
        JOptionPane.showMessageDialog(null, msg, titre, type);
    }
}
