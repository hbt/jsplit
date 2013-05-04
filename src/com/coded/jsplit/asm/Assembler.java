package com.coded.jsplit.asm;

/**
 * Assemble.java
 * permet de merger tous les fichiers splitted pour en faire qu'un
 * @author Hassen Ben Tanfous
 */

//imports
import java.io.*;
import com.coded.jsplit.msg.*;
import javax.swing.*;
import java.awt.*;
import com.coded.jsplit.split.*;

public class Assembler {
    private static final int BUFFER = 4096;

    private File[] tabFiles;

    private File fichierConf,
            dest;

    private Messagerie msg;

    private BufferedReader buffr;

    public Assembler() {
        msg.titre = "JSplit par Hassen Ben Tanfous";
    }

    /**
     * les fichiers splitter à assembler dans le fichier destination
     * @param tabSplitted File[]
     * @param destination File
     */
    public Assembler(File[] tabSplitted, File destination) {
        this();
        this.tabFiles = tabSplitted;
        dest = destination;
    }

    /**
     * pour les fichiers mergeConf
     * @param conf File
     * @param dest File
     */
    public Assembler(File conf, File dest) {
        this();
        this.fichierConf = conf;
        this.dest = dest;
        chargerFichiers();
    }

    public Assembler(String conf, String dest) {
        this(new File(conf), new File(dest));
    }

    /**
     * permet d'assembler tous les fichiers splitter pour en faire qu'un
     */
    public void assembler() {
        new AssembleFichiers().start();
    }

    /**
     * permet de charger le contenu du fichier merge Conf
     */
    private void chargerFichiers() {
        String lecture;
        int i = 0;
        try {

            buffr = new BufferedReader(new FileReader(fichierConf));
            lecture = buffr.readLine();
            tabFiles = new File[Integer.parseInt(lecture)];

            lecture = buffr.readLine();
            while (lecture != null) {
                tabFiles[i] = new File(lecture);
                i++;
                lecture = buffr.readLine();
            }
        } catch (FileNotFoundException ex) {
            msg.msge("Fichier introuvable");
        } catch (IOException ex) {
            msg.msge("Erreur lors de la lecture du fichier " +
                     Splitter.FICHIER_CONF);
        }
    }


    /**
     * AssembleFichiers.java
     * @author Hassen Ben Tanfous
     */
    private class AssembleFichiers extends Thread implements Runnable {
        private JFrame frame;

        private Container container;

        private JProgressBar progressBar;

        private JLabel lblInfos;

        private int
                count,
                progressValue,
                progressValMax;

        private byte[] data;

        private BufferedOutputStream buffos;

        private BufferedInputStream buffis;

        public AssembleFichiers() {
            data = new byte[BUFFER];
            instancierComposants();
            configurerComposants();
        }

        public void run() {
            for (int i = 0; i < tabFiles.length; i++) {
                progressValMax += tabFiles[i].length();
            }
            progressValue = 0;
            progressBar.setMinimum(0);
            progressBar.setMaximum(progressValMax);
            progressBar.setValue(progressValue);

            try {
                buffos = new BufferedOutputStream(new FileOutputStream(dest));

                for (int i = 0; i < tabFiles.length; i++) {
                    lblInfos.setText(tabFiles[i].getName());
                    buffis = new BufferedInputStream(new FileInputStream(
                            tabFiles[i]));
                    while ((count = buffis.read(data, 0, BUFFER)) != -1) {
                        buffos.write(data, 0, count);
                        progressValue += count;
                        progressBar.setValue(progressValue);
                    }
                    buffis.close();
                }
                buffos.close();
                msg.msgi("Assemblage complet");
            } catch (Exception e) {
                msg.msge("Erreur lors de la reconstruction des fichiers ");
            }

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
