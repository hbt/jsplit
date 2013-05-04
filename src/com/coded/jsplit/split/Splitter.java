package com.coded.jsplit.split;

/**
 * Split.java
 * permet de splitter un fichiers en plusieurs
 * permet de créer un fichier "conf" pour assembler de nouveau le fichier et son contenu
 * Date: 27/12/2005
 * @author Hassen Ben Tanfous
 *
 * @version 1.1
 */

//imports
import java.io.*;
import javax.swing.*;
import java.awt.*;
import com.coded.jsplit.msg.*;

public class Splitter {
    public static final int BUFFER = 4096;
    public static final String FICHIER_CONF = "mergeConf";

    private File[] tabSources,
            tabDest;

    private File fichier;

    private Messagerie msg;

    private long tailleSplit,
            tailleFichiers; //taille de tous les fichiers

    /** contient toutes les tailles pour chaque fichier */
    private long[] tabTailleSplitParNbFichiers;

    private int nbFichiers;

    public Splitter() {
        msg.titre = "JSplit par Hassen Ben Tanfous";
    }

    /**
     * permet de Splitter les fichiers sources vers leurs destinations
     * @param tabSources File[]
     * @param tabDestinations File[]
     */
    public Splitter(File[] tabSources, File[] tabDestinations) {
        this();
        this.tabSources = tabSources;
        this.tabDest = tabDestinations;
    }

    public Splitter(File fichier) {
        this();
        this.fichier = fichier;
    }

    public Splitter(String fichier) {
        this(new File(fichier));
    }

    /**
     * permet de splitter le fichier en plusieurs
     */
    public void split() {
        new SplitFichiers().start();
    }

    // taille du split 500 ko, 1Mb or else
    public void setTaille(long taille) {
        this.tailleSplit = taille;
    }

    public long getTaille() {
        return tailleSplit;
    }

    /**
     * somme de la taille de tous les fichiers
     * @param taille long
     */
    public void setTailleFichiers(long taille) {
        this.tailleFichiers = taille;
    }

    public long getTailleFichiers() {
        return tailleFichiers;
    }

    public void setParNbFichiers(int nbFichiers) {
        this.nbFichiers = nbFichiers;
        calculerNouvelleTailleSplit();
    }

    public int getNbFichiers() {
        return nbFichiers;
    }

    /**
     * permet de calculer la taille split par nombre de fichier pour chacun
     * des fichiers sources
     */
    private void calculerNouvelleTailleSplit() {
        tabTailleSplitParNbFichiers = new long[tabSources.length];
        for (int i = 0; i < tabSources.length; i++) {
            tabTailleSplitParNbFichiers[i] = tabSources[i].length() /
                                             nbFichiers;
        }
    }

    /**
     * SplitFichiers.java
     * permet d'exécuter le splitting dans un Thread pour permettre à l'utilisateur
     * d'effectuer d'autres opérations
     * Date: 27/12/2005
     * @author Hassen Ben Tanfous
     *
     * @version 1.1
     */
    private class SplitFichiers extends Thread implements Runnable {
        private JFrame frame;

        private Container container;

        private JProgressBar progressBar;

        private JLabel lblInfos;

        private BufferedInputStream buffis;

        private BufferedOutputStream buffos;

        private BufferedWriter buffw;

        private int count,
                progressValue,
                progressValMax,
                cpt, //compteur
                nbSplit; //nb de fichiers splitter

        private byte[] data;

        private String fichier,
                dossierParent,
                contenu; //contenu fichier mergeConf

        public SplitFichiers() {
            data = new byte[BUFFER];
            fichier = "";
            dossierParent = "";
            instancierComposants();
            configurerComposants();
        }

        public void run() {
            progressValMax = (int) tailleFichiers;

            progressBar.setMinimum(0);
            progressBar.setMaximum(progressValMax);
            progressBar.setValue(progressValue);

            for (int i = 0; i < tabSources.length; i++) {
                contenu = "";
                if (nbFichiers != 0) {
                    tailleSplit = tabTailleSplitParNbFichiers[i];
                }
                lblInfos.setText(tabSources[i].getName());
                nbSplit = 0;
                cpt = 0;
                try {
                    buffis = new BufferedInputStream(new FileInputStream(
                            tabSources[i]));
                    dossierParent = tabDest[i].getAbsolutePath();
                    buffw = new BufferedWriter(new FileWriter(dossierParent +
                            tabSources[i].separator + FICHIER_CONF));

                    fichier = dossierParent + tabSources[i].separator +
                              tabSources[i].getName().substring(0,
                            tabSources[i].getName().lastIndexOf('.') + 1) +
                              nbSplit +
                              tabSources[i].getName().substring(tabSources[
                            i].
                            getName().lastIndexOf('.'));
                    contenu += fichier + "\n";

                    buffos = new BufferedOutputStream(new FileOutputStream(
                            fichier));
                    lblInfos.setText(fichier);
                    while ((count = buffis.read(data, 0, BUFFER)) != -1) {
                        cpt += count;
                        progressValue += count;
                        buffos.write(data, 0, count);
                        progressBar.setValue(progressValue);

                        if (cpt > tailleSplit) {
                            cpt = 0;
                            buffos.close();
                            nbSplit++;
                            fichier = dossierParent + tabSources[i].separator +
                                      tabSources[i].getName().substring(0,
                                    tabSources[i].getName().lastIndexOf('.') +
                                    1) +
                                      nbSplit +
                                      tabSources[i].getName().substring(
                                              tabSources[
                                              i].
                                              getName().lastIndexOf('.'));

                            contenu += fichier + "\n";
                            lblInfos.setText(fichier);
                            buffos = new BufferedOutputStream(new
                                    FileOutputStream(fichier));
                        }
                    }
                    buffw.write(nbSplit + 1 + "\n");
                    buffw.write(contenu);
                    buffw.close();
                    buffis.close();
                } catch (FileNotFoundException ex) {
                    msg.msge("fichier introuvable " + tabSources[i].getName());
                } catch (IOException ex) {
                    msg.msge("Erreur lors du split");
                }
            }
            try {
                buffos.close();
            } catch (IOException ex1) {
            }

            msg.msgi("Split complet");
            frame.setVisible(false);
            stop();
        }

        private void instancierComposants() {
            frame = new JFrame("JSplit par Hassen Ben Tanfous");
            container = frame.getContentPane();

            progressBar = new JProgressBar();
            lblInfos = new JLabel();
        }

        private void configurerComposants() {
            container.setLayout(null);
            ajouterComposant(container, lblInfos, 125, 10, 300, 25);
            ajouterComposant(container, progressBar, 5, 50, 475, 25);

            frame.setSize(500, 125);
            frame.setLocation(new Point(300, 300));
            frame.setVisible(true);
        }

        private void ajouterComposant(Container c, Component comp, int x,
                                      int y, int x1, int y1) {
            comp.setBounds(x, y, x1, y1);
            c.add(comp);
        }
    }
}
