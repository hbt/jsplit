package com.coded.jsplit.outils;

//imports
import java.io.*;

public class Outils {
    /**
     * permet de trier la sélection de l'utilisateur
     * @param tabSources File[]
     */
    public static void trier(File[] tabSources) {
        boolean fini = true;
        int j = 0;
        File temp = null;
        while (fini) {
            fini = false;
            for (int i = 0; i < tabSources.length - 1 - j; i++) {
                if (getNumero(tabSources[i]) >
                    getNumero(tabSources[i + 1])) {
                    temp = tabSources[i];
                    tabSources[i] = tabSources[i + 1];
                    tabSources[i + 1] = temp;
                    fini = true;
                }
            }
            j++;
        }
    }

    private static int getNumero(File fichier) {
        String nom = fichier.getName();
        nom = nom.substring(0, nom.lastIndexOf('.'));
        return Integer.parseInt(nom.substring(nom.lastIndexOf('.') +
                                              1));
    }
}
