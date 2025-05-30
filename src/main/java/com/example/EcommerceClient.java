package com.example;

public class EcommerceClient {
     public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
                GestorEcommerce ec = new GestorEcommerce();
                ec.setVisible(true);
        });
    }
}
