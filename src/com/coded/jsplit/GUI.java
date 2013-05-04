package com.coded.jsplit;

/**
 * GUI.java
 * interface graphique du programme JSplit
 * Date: 05/01/2005
 * @author Hassen Ben Tanfous
 *
 * @version 1.1
 */

//imports
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import com.coded.jsplit.msg.*;
import com.coded.jsplit.split.*;
import com.coded.jsplit.asm.*;
import com.coded.jsplit.outils.*;
import com.coded.jsplit.lang.*;

public class GUI extends JFrame implements Langues {

    /** tailles disponibles pour le comboBox */
    public static final String[] TAILLES = {"500 Ko", "1 Mb", "5 Mb", "10 Mb",
                                           "Autres"};

    public static final long LTAILLES[] = {500000, 1000000, 5000000, 10000000};

    public static final String[] LANGAGES = {Langues.FRANCAIS, Langues.ENGLISH};


    private Container container;

    private JPanel paneConf, //panneau configuration
            paneSplit,
            paneMerge,
            paneHelp;

    private JRadioButton radioParTaille, //configuration par taille
            radioParNbFichiers; //configuration par nombre de fichiers

    private JComboBox comboTailles; //taille disponibles pour configuration par taille

    private JTextField txtConf; //field pour taille (Autres) ou nbFichiers

    private JComboBox comboLangues; //francais or english

    private JCheckBox checkFolder; //pour splitter les fichiers dans un dossier portant leurs noms

    private JTextPane txtSplitSelected; //fichiers sélectionnés split (nom, taille, nbSplit)
    private JScrollPane scrollSplitSelected; //déroulement split

    private JFileChooser jfcSplitSelection, //pour sélectionner les fichiers à splitter
            jfcSplitDestination; //sélectionner où se retrouveront les fichiers

    private JButton boutonSplit, //split les fichiers
            boutonSplitSelection; //selectionne les fichiers

    private JTextPane txtMergeSelected; //fichiers sélectionnés (nom, taille)
    private JScrollPane scrollMergeSelected;

    private JLabel lblMergeNbFichiers; //nombre de fichiers sélectionnés

    private JButton boutonMerge, //permet d'assembler les fichiers splittés
            boutonMergeSelection; //sélection des fichiers à assembler

    private JFileChooser jfcMergeSelection, //sélection des fichiers à assembler
            jfcMergeDestination; //sauvegarde du fichier finale

    private JTabbedPane tabPane;

    private Messagerie msg;

    private File[] tabSplitSources, //fichiers sources
            tabSplitDest, //fichiers destination
            tabMergeSources;

    private Splitter split;
    private Assembler assem;

    private long tailleSplit; //split taille 500 ko, 1Mb etc


    public GUI() {
        instancierComposants();
        configurerComposants();
    }

    private void instancierComposants() {
        //container
        container = getContentPane();

        tabPane = new JTabbedPane();

        //partie configuration
        paneConf = new JPanel();

        radioParTaille = new JRadioButton("Par taille");
        radioParNbFichiers = new JRadioButton("Par Nb fichiers");

        comboTailles = new JComboBox(TAILLES);
        txtConf = new JTextField();
        comboLangues = new JComboBox(LANGAGES);

        //partie split
        paneSplit = new JPanel();

        checkFolder = new JCheckBox("Dossier: ");
        txtSplitSelected = new JTextPane();
        scrollSplitSelected = new JScrollPane(txtSplitSelected);

        jfcSplitSelection = new JFileChooser("");
        jfcSplitDestination = new JFileChooser("");

        boutonSplit = new JButton("Diviser");
        boutonSplitSelection = new JButton("Selectionner");

        //partie merge
        paneMerge = new JPanel();

        txtMergeSelected = new JTextPane();
        scrollMergeSelected = new JScrollPane(txtMergeSelected);

        boutonMerge = new JButton("Assembler");
        boutonMergeSelection = new JButton("Selectionner");

        jfcMergeSelection = new JFileChooser("");
        jfcMergeDestination = new JFileChooser("");

        lblMergeNbFichiers = new JLabel("Nb fichiers: ");
    }

    private void configurerComposants() {
        msg.titre = "JSplit - Hassen Ben Tanfous";

        //partie configuration
        paneConf.setLayout(null);

        radioParTaille.setSelected(false);
        radioParNbFichiers.setSelected(true);
        comboTailles.setVisible(false);

        //listener
        comboTailles.addActionListener(alPaneConf);
        radioParNbFichiers.addActionListener(alPaneConf);
        radioParTaille.addActionListener(alPaneConf);
        comboLangues.addActionListener(alPaneConf);

        ajouterComposant(paneConf, radioParNbFichiers, 10, 10, 130, 20);
        ajouterComposant(paneConf, radioParTaille, 150, 10, 100, 20);

        ajouterComposant(paneConf, comboTailles, 10, 50, 100, 20);
        ajouterComposant(paneConf, txtConf, 10, 100, 100, 20);

        ajouterComposant(paneConf, comboLangues, 300, 10, 100, 20);

        //partie Split
        paneSplit.setLayout(null);

        txtSplitSelected.setFont(new Font("Arial", Font.PLAIN, 11));
        txtSplitSelected.setEnabled(false);
//        txtSplitSelected.setTabSize(5);

        jfcSplitSelection.setMultiSelectionEnabled(true);

        jfcSplitDestination.setMultiSelectionEnabled(false);
        jfcSplitDestination.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        boutonSplit.setEnabled(false);
        boutonSplit.setBackground(Color.RED);

        //listener
        boutonSplitSelection.addActionListener(alPaneSplit);
        boutonSplit.addActionListener(alPaneSplit);

        //ajout au container
        ajouterComposant(paneSplit, scrollSplitSelected, 10, 10, 300, 250);
        ajouterComposant(paneSplit, checkFolder, 310, 20, 100, 20);

        ajouterComposant(paneSplit, boutonSplitSelection, 310, 80, 150, 20);
        ajouterComposant(paneSplit, boutonSplit, 310, 140, 150, 20);

        //partie assemblage
        paneMerge.setLayout(null);

        txtMergeSelected.setFont(new Font("Arial", Font.PLAIN, 11));
        txtMergeSelected.setEnabled(false);
//        txtMergeSelected.setTabSize(5);

        jfcMergeSelection.setMultiSelectionEnabled(true);

        jfcMergeDestination.setMultiSelectionEnabled(false);

        boutonMerge.setEnabled(false);
        boutonMerge.setBackground(Color.RED);

        //listener
        boutonMergeSelection.addActionListener(alPaneMerge);
        boutonMerge.addActionListener(alPaneMerge);

        ajouterComposant(paneMerge, scrollMergeSelected, 10, 10, 300, 250);
        ajouterComposant(paneMerge, boutonMergeSelection, 310, 80, 150, 20);
        ajouterComposant(paneMerge, boutonMerge, 310, 140, 150, 20);
        ajouterComposant(paneMerge, lblMergeNbFichiers, 10, 270, 100, 20);

        tabPane.add(paneConf, "Configuration");
        tabPane.add(paneSplit, "Diviser");
        tabPane.add(paneMerge, "Assembler");
        container.add(tabPane);

        setLangue(Langues.FRANCAIS);

        setTitle("JSplit par Hassen Ben Tanfous");
        setSize(500, 350);
        setLocation(new Point(250, 150));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }


    private void ajouterComposant(Container c, Component comp, int x,
                                  int y, int x1, int y1) {
        comp.setBounds(x, y, x1, y1);
        c.add(comp);
    }


    private ActionListener alPaneSplit = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int choix; //choix de jfc
            File tempDossier, //dossier si checkFolder.isSelected();
                    dossier; //selected folder
            long tailleFichiers = 0; //compteur pour somme taille fichiers

            if (e.getSource() == boutonSplitSelection) {
                choix = jfcSplitSelection.showOpenDialog(null);
                if (choix == JFileChooser.APPROVE_OPTION) {
                    tabSplitSources = jfcSplitSelection.getSelectedFiles();
                    tabSplitDest = new File[tabSplitSources.length];

                    boutonSplit.setEnabled(true);
                    afficherInfosFichiersSplit();
                }

            } else if (e.getSource() == boutonSplit) {
                choix = jfcSplitDestination.showSaveDialog(null);

                if (choix == JFileChooser.APPROVE_OPTION) {
                    dossier = jfcSplitDestination.getSelectedFile();

                    //split dans des dossiers
                    if (checkFolder.isSelected()) {
                        for (int i = 0; i < tabSplitSources.length; i++) {
                            tailleFichiers += tabSplitSources[i].length();
                            tempDossier = new File(dossier.getAbsolutePath() +
                                    dossier.separator +
                                    tabSplitSources[i].getName().
                                    substring(0,
                                              tabSplitSources[i].getName().
                                              lastIndexOf('.')));
                            //création des dossiers
                            tempDossier.mkdirs();
                            //chemin de destination du fichier ex: D:\
                            tabSplitDest[i] = new File(tempDossier.
                                    getAbsolutePath() + tempDossier.separator);
                        }
                    } else {
                        for (int i = 0; i < tabSplitSources.length; i++) {
                            tailleFichiers += tabSplitSources[i].length();
                            //chemin simple de type Here or TO
                            tabSplitDest[i] = new File(dossier.getAbsolutePath() +
                                    dossier.separator);
                        }
                    }

                    split = new Splitter(tabSplitSources, tabSplitDest);
                    split.setTailleFichiers(tailleFichiers);

                    //taille du split
                    if (comboTailles.getSelectedIndex() ==
                        comboTailles.getItemCount() - 1) {
                        split.setTaille(Integer.parseInt(txtConf.getText()));
                    } else {
                        split.setTaille(LTAILLES[comboTailles.getSelectedIndex()]);
                    }

                    if (txtConf.isEnabled() && radioParNbFichiers.isSelected()) {
                        split.setParNbFichiers(Integer.parseInt(txtConf.getText()));
                    }

                    split.split();
                }
            }
        }
    };

    /**
     * gestion des évènements du panneau de configuration
     */
    private ActionListener alPaneConf = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == radioParNbFichiers &&
                radioParNbFichiers.isSelected()) {

                radioParTaille.setSelected(false);
                txtConf.setEnabled(true);
                comboTailles.setEnabled(false);

            } else if (e.getSource() == radioParTaille &&
                       radioParTaille.isSelected()) {

                radioParNbFichiers.setSelected(false);
                comboTailles.setVisible(true);
                txtConf.setText("");
                txtConf.setEnabled(false);

            } else if (e.getSource() == comboTailles) {
                //sélection autres
                if (comboTailles.getSelectedIndex() == TAILLES.length - 1) {
                    txtConf.setText("en octets");
                    txtConf.setEnabled(true);
                }
            } else if (e.getSource() == comboLangues) {
                //française
                if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
                    setLangue(Langues.FRANCAIS);
                } else if (comboLangues.getSelectedItem() == Langues.ENGLISH) {
                    //anglais
                    setLangue(Langues.ENGLISH);
                }
            }
        }
    };

    private ActionListener alPaneMerge = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int choix;
            File fichierDest;

            if (e.getSource() == boutonMergeSelection) {
                choix = jfcMergeSelection.showOpenDialog(null);

                if (choix == JFileChooser.APPROVE_OPTION) {
                    tabMergeSources = jfcMergeSelection.getSelectedFiles();
                    boutonMerge.setEnabled(true);

                    afficherInfosFichiersMerge();
                    if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
                        lblMergeNbFichiers.setText("Nb fichiers: " +
                                tabMergeSources.length);
                    } else if (comboLangues.getSelectedItem() ==
                               Langues.ENGLISH) {
                        lblMergeNbFichiers.setText("File count: " +
                                tabMergeSources.length);
                    }
                }
            }

            else if (e.getSource() == boutonMerge) {
                choix = jfcMergeDestination.showSaveDialog(null);

                if (choix == JFileChooser.APPROVE_OPTION) {
                    fichierDest = jfcMergeDestination.getSelectedFile();

                    if (tabMergeSources[0].getName().equalsIgnoreCase(Splitter.
                            FICHIER_CONF)) {
                        assem = new Assembler(tabMergeSources[0], fichierDest);
                    } else {
                        assem = new Assembler(tabMergeSources, fichierDest);
                    }

                    assem.assembler();
                }
            }
        }
    };

    /**
     * affiche les informations (nom, taille, nombre de fichiers prévus)
     * @param selectedFiles File[]
     */
    private void afficherInfosFichiersSplit() {
        String contenu = "";
        final int TAILLE_MAX = 20;
        if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
            contenu = "";
            contenu += remplirEspaces("FICHIERS",TAILLE_MAX) + "\t" + "TAILLE" + "    " +
                    "NB_FICHIERS\n";
        } else if (comboLangues.getSelectedItem() == Langues.ENGLISH) {
            contenu = "";
            contenu += remplirEspaces("FILES", TAILLE_MAX)+ "\t" + "LENGTH" + "\t" +
                    "NB_FILES\n";
        }

        for (int i = 0; i < tabSplitSources.length; i++) {
            if (tabSplitSources[i].getName().length() > TAILLE_MAX) {
                contenu += tabSplitSources[i].getName().substring(0, TAILLE_MAX) +
                        "\t";
            } else {
                contenu += remplirEspaces(tabSplitSources[i].getName(), TAILLE_MAX) + "\t";
            }

            contenu += tabSplitSources[i].length() + "\t";
            contenu += calculerNbFichiersPrevus(tabSplitSources[i]);
            contenu += "\n";
        }

        txtSplitSelected.setText(contenu);
    }

    /**
     * le résultat du splitting en nombre de fichiers qui seront créer
     * @param fichier File
     * @return int le nombre de fichier prévus
     */
    private int calculerNbFichiersPrevus(File fichier) {

        int nbFichiers = 0;
        if (txtConf.isEnabled() && radioParNbFichiers.isSelected()) {
            try {
                nbFichiers = Integer.parseInt(txtConf.getText());
            } catch (Exception e) {
                if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
                    msg.msge("Nombre de fichiers incorrect");
                } else if (comboLangues.getSelectedItem() == Langues.ENGLISH) {
                    msg.msge("Number of files incorrect");
                }
            }
        } else if (txtConf.isEnabled() &&
                   comboTailles.getSelectedItem() == TAILLES[TAILLES.length - 1] &&
                   radioParTaille.isSelected()) {
            try {
                nbFichiers = (int) Math.abs(fichier.length() /
                                            Integer.parseInt(txtConf.getText()));
            } catch (Exception e) {
                if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
                    msg.msge("Nombre de fichiers incorrect");
                } else if (comboLangues.getSelectedItem() == Langues.ENGLISH) {
                    msg.msge("Number of files incorrect");
                }
            }
        } else if (radioParTaille.isEnabled() && !txtConf.isEnabled()) {
            nbFichiers = (int) fichier.length() /
                         (int) LTAILLES[comboTailles.getSelectedIndex()];
        }

        return nbFichiers;
    }

    /**
     * affiche les informations sur les fichiers dans la partie Assembleur
     */
    private void afficherInfosFichiersMerge() {
        BufferedReader lecteur;
        String contenu = "",
                         ligne;
        File temp;
        final int TAILLE_MAX = 20;
        if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
            contenu = "";
            contenu += remplirEspaces ("FICHIER", TAILLE_MAX) + "\t" + "TAILLE\n";
        } else if (comboLangues.getSelectedItem() == Langues.ENGLISH) {
            contenu = "";
            contenu += remplirEspaces("Files", TAILLE_MAX) + "\t\t" + "LENGTH\n";
        }

        //vérifie si le fichier est le fichier de configuration Splitter.FICHIER_CONF
        //lecture du fichier et affichage
        if (tabMergeSources[0].getName().equalsIgnoreCase(Splitter.FICHIER_CONF)) {
            try {
                lecteur = new BufferedReader(new FileReader(tabMergeSources[0]));
                lecteur.readLine(); //sauter la ligne avec le nombre de fichiers
                ligne = lecteur.readLine();
                while (ligne != null) {
                    temp = new File(ligne);

                    if (temp.getName().length() > TAILLE_MAX) {
                        contenu += temp.getName().substring(0, TAILLE_MAX) +
                                "\t";
                    } else {
                        contenu += temp.getName() + "\t";
                    }

                    contenu += temp.length() + "\n";

                    ligne = lecteur.readLine();
                }

                lecteur.close();
            } catch (FileNotFoundException ex) {
                if (comboLangues.getSelectedItem() == Langues.FRANCAIS) {
                    msg.msge("Fichier " + Splitter.FICHIER_CONF +
                             " introuvable");
                } else if (comboLangues.getSelectedItem() == Langues.ENGLISH) {
                    msg.msge("File " + Splitter.FICHIER_CONF + " not found");
                }
            } catch (IOException ex) {
            }

        } else {
            for (int i = 0; i < tabMergeSources.length; i++) {
                if (tabMergeSources[i].getName().length() > TAILLE_MAX) {
                    contenu +=
                            tabMergeSources[i].getName().substring(0,
                            TAILLE_MAX) +
                            "\t";
                } else {
                    contenu += remplirEspaces(tabMergeSources[i].getName(), TAILLE_MAX) + "\t";
                }

                contenu += tabMergeSources[i].length() + "\t\n";
            }
        }

        txtMergeSelected.setText(contenu);
    }

    private String remplirEspaces (String str, int tailleVoulu) {
        while (str.length() < tailleVoulu) {
            str += " ";
        }
        return str;
    }


    /**
     * détermine la langue du programme
     * @param langue String
     */
    public void setLangue(String langue) {
        if (langue.equalsIgnoreCase(Langues.FRANCAIS)) {
            tabPane.setTitleAt(0, "Configuration");
            tabPane.setTitleAt(1, "Diviser");
            tabPane.setTitleAt(2, "Assembler");

            radioParTaille.setText("Par taille");
            radioParNbFichiers.setText("Par nb fichiers");

            checkFolder.setText("Dossier");
            checkFolder.setToolTipText(
                    "Crée un dossier avec le nom du fichier contenant la division");

            jfcSplitSelection.setDialogTitle("Sectionner vos fichiers");
            jfcSplitSelection.setApproveButtonText("Selectionner");

            jfcSplitDestination.setDialogTitle(
                    "Sélectionner la destination de vos fichiers");
            jfcSplitDestination.setApproveButtonText("Selectionner");

            boutonSplit.setText("Diviser");
            boutonSplitSelection.setText("Selectionner");

            boutonMerge.setText("Assembler");
            boutonMergeSelection.setText("Selectionner");

            jfcMergeSelection.setDialogTitle(
                    "Selectionner le fichier conf ou vos fichiers divisés");
            jfcMergeSelection.setApproveButtonText("Selectionner");

            jfcMergeDestination.setDialogTitle(
                    "Entrez le nom du fichier pour l'assemblage");
            jfcMergeDestination.setApproveButtonText("Assembler");

            lblMergeNbFichiers.setText("Nb Fichiers: ");
        } else if (langue.equalsIgnoreCase(Langues.ENGLISH)) {
            tabPane.setTitleAt(0, "Configuration");
            tabPane.setTitleAt(1, "Split");
            tabPane.setTitleAt(2, "Merge");

            radioParTaille.setText("By size");
            radioParNbFichiers.setText("By file number");

            checkFolder.setText("Folder");
            checkFolder.setToolTipText(
                    "Create a folder with the filename containing the result");

            jfcSplitSelection.setDialogTitle("Select your files");
            jfcSplitSelection.setApproveButtonText("Select");

            jfcSplitDestination.setDialogTitle(
                    "Select the destination of your files");
            jfcSplitDestination.setApproveButtonText("Select");

            boutonSplit.setText("Split");
            boutonSplitSelection.setText("Select");

            boutonMerge.setText("Assemble");
            boutonMergeSelection.setText("Select");

            jfcMergeSelection.setDialogTitle(
                    "Select the file conf or your splitted files");
            jfcMergeSelection.setApproveButtonText("Select");

            jfcMergeDestination.setDialogTitle(
                    "Enter the filename to assemble");
            jfcMergeDestination.setApproveButtonText("Assemble");

            lblMergeNbFichiers.setText("File count: ");
        }
    }
}
