package ocr;

// <editor-fold defaultstate="collapsed" desc="IMPORT">
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
//import laborki.Lab02;
import obrazy.*;
// </editor-fold>

public class OCR extends JFrame {

  public OCR() {
    initComponents();
  }

  // <editor-fold defaultstate="collapsed" desc="USTAWIENIA POCZĄTKOWE">
  private void initComponents() {

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // <editor-fold defaultstate="collapsed" desc="Menu i zakładki">

    //Tytuł okna programu
    setTitle("Program do OCR na zaliczenie z AIPO 2012");

    //Pasek menu
    menubar = new JMenuBar();

    //Elementy paska menu
    fileM = new JMenu("Plik");
    setupM = new JMenu("Ustawienia");
    helpM = new JMenu("Pomoc");

    //Elementy elementów paska menu.
    open = new JMenuItem("Otwórz plik");
    exit = new JMenuItem("Zakończ");

    help = new JMenuItem("Pomoc");
    about = new JMenuItem("Autor");

    //Robimy zakładki
    jTabbedPane1 = new JTabbedPane();
    getContentPane().add(jTabbedPane1);

    transformationPanel = new JPanel(new GridBagLayout());
    binarizationPanel = new JPanel(new GridBagLayout());
    filterPanel = new JPanel(new GridBagLayout());
    OCRPanel = new JPanel(new GridBagLayout());

    //Ustawiamy menu i dodajemy jego elementy
    setJMenuBar(menubar);
    menubar.add(fileM);
    menubar.add(setupM);
    menubar.add(helpM);

    fileM.add(open);
    fileM.add(exit);

    // <editor-fold defaultstate="collapsed" desc="Setup configuration">


    fMedianCheckBoxMenuItem = new JCheckBoxMenuItem();
    fUsredCheckBoxMenuItem = new JCheckBoxMenuItem();
    erozjaCheckBoxMenuItem = new JCheckBoxMenuItem();
    dylatacjaCheckBoxMenuItem = new JCheckBoxMenuItem();
    jSeparator1 = new JPopupMenu.Separator();
    globBinCheckBoxMenuItem = new JCheckBoxMenuItem();
    locBinCheckBoxMenuItem = new JCheckBoxMenuItem();
    mixBinCheckBoxMenuItem = new JCheckBoxMenuItem();
    jSeparator2 = new JPopupMenu.Separator();
    ekstrNeighbCheckBoxMenuItem = new JCheckBoxMenuItem();
    ekstrDiagCheckBoxMenuItem = new JCheckBoxMenuItem();
    ekstrSqCheckBoxMenuItem = new JCheckBoxMenuItem();
    jSeparator3 = new JPopupMenu.Separator();
    class1NNCheckBoxMenuItem = new JCheckBoxMenuItem();
    classkNNCheckBoxMenuItem = new JCheckBoxMenuItem();
    jSeparator4 = new JPopupMenu.Separator();
    metrEukCheckBoxMenuItem = new JCheckBoxMenuItem();
    metrManhCheckBoxMenuItem = new JCheckBoxMenuItem();



    //Filtr medianowy
    fMedianCheckBoxMenuItem.setSelected(false);
    fMedianCheckBoxMenuItem.setText("Filtr medianowy");
    fMedianCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fMedianCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(fMedianCheckBoxMenuItem);

    //Filtr splotowy
    fUsredCheckBoxMenuItem.setSelected(false);
    fUsredCheckBoxMenuItem.setText("Filtr uśredniający");
    fUsredCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        fUsredCheckBoxMenuItemActionPerformed(evt);
      }
    });
    //setupM.add(fUsredCheckBoxMenuItem);

    //Erozja
    erozjaCheckBoxMenuItem.setSelected(false);
    erozjaCheckBoxMenuItem.setText("Erozja");
    erozjaCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        erozjaCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(erozjaCheckBoxMenuItem);

    //Dylatacja
    dylatacjaCheckBoxMenuItem.setSelected(false);
    dylatacjaCheckBoxMenuItem.setText("Dylatacja");
    dylatacjaCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dylatacjaCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(dylatacjaCheckBoxMenuItem);
    setupM.add(jSeparator1);

    //Progowanie globalne
    globBinCheckBoxMenuItem.setSelected(false);
    globBinCheckBoxMenuItem.setText("Binaryzacja globalna");
    globBinCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        globBinCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(globBinCheckBoxMenuItem);

    //Progowanie lokalne
    locBinCheckBoxMenuItem.setSelected(false);
    locBinCheckBoxMenuItem.setText("Binaryzacja lokalna");
    locBinCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        locBinCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(locBinCheckBoxMenuItem);

    //Progowanie mieszane
    mixBinCheckBoxMenuItem.setSelected(false);
    mixBinCheckBoxMenuItem.setText("Binaryzacja mieszana");
    mixBinCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mixBinCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(mixBinCheckBoxMenuItem);
    setupM.add(jSeparator2);

    //Ekstrakcja przez liczbę sąsiadów
    ekstrNeighbCheckBoxMenuItem.setSelected(false);
    ekstrNeighbCheckBoxMenuItem.setText("Ekstrakcja przez liczbę sąsiadów");
    ekstrNeighbCheckBoxMenuItem.setEnabled(false);
    ekstrNeighbCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ekstrNeighbCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(ekstrNeighbCheckBoxMenuItem);

    //Ekstrakcja przez przekątne
    ekstrDiagCheckBoxMenuItem.setSelected(false);
    ekstrDiagCheckBoxMenuItem.setText("Ekstrakcja przez przekątne");
    ekstrDiagCheckBoxMenuItem.setEnabled(false);
    ekstrDiagCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ekstrDiagCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(ekstrDiagCheckBoxMenuItem);

    //Ekstrakcja przez podział na prostokąty
    ekstrSqCheckBoxMenuItem.setSelected(false);
    ekstrSqCheckBoxMenuItem.setText("Ekstrakcja przez podział na prostokąty");
    ekstrSqCheckBoxMenuItem.setEnabled(false);
    ekstrSqCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ekstrSqCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(ekstrSqCheckBoxMenuItem);
    setupM.add(jSeparator3);

    //Klasyfikacja 1NN
    class1NNCheckBoxMenuItem.setSelected(false);
    class1NNCheckBoxMenuItem.setText("Klasyfikacja 1NN");
    class1NNCheckBoxMenuItem.setEnabled(false);
    class1NNCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jCheckBoxMenuItem14ActionPerformed(evt);
      }
    });
    setupM.add(class1NNCheckBoxMenuItem);

    //Klasyfikacja kNN
    classkNNCheckBoxMenuItem.setSelected(false);
    classkNNCheckBoxMenuItem.setText("Klasyfikacja kNN");
    classkNNCheckBoxMenuItem.setEnabled(false);
    classkNNCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jCheckBoxMenuItem15ActionPerformed(evt);
      }
    });
    setupM.add(classkNNCheckBoxMenuItem);
    setupM.add(jSeparator4);

    //Metryka Euklidesowa
    metrEukCheckBoxMenuItem.setSelected(false);
    metrEukCheckBoxMenuItem.setText("Metryka Euklidesowa");
    metrEukCheckBoxMenuItem.setEnabled(false);
    metrEukCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        metrEukCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(metrEukCheckBoxMenuItem);

    //Metryka Manhattan
    metrManhCheckBoxMenuItem.setSelected(false);
    metrManhCheckBoxMenuItem.setText("Metryka Manhattan");
    metrManhCheckBoxMenuItem.setEnabled(false);
    metrManhCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        metrManhCheckBoxMenuItemActionPerformed(evt);
      }
    });
    setupM.add(metrManhCheckBoxMenuItem);
    // </editor-fold>

    helpM.add(help);
    helpM.add(about);

    //Dodaję zakładki
    jTabbedPane1.addTab("Obrót", transformationPanel);
    jTabbedPane1.addTab("Binaryzacja", binarizationPanel);
    jTabbedPane1.addTab("Filtry", filterPanel);
    jTabbedPane1.addTab("OCR", OCRPanel);

    //<editor-fold defaultstate="collapsed" desc="Obrót Panel">
    JLabel obrotLabel1 = new JLabel("Obróc obraz o ");
    obrotTextField = new JTextField("0");
    obrotTextField.setEditable(true);
    JLabel obrotLabel2 = new JLabel(" stopni.");
    JLabel space1 = new JLabel(" ");

    transformationProcessButton = new JButton("Obróć");
    transformationProcessButton.setEnabled(false);
    transformationProcessButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          transformationProcessButtonActionPerformed(evt);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    transformationPanel.updateUI();
    transformationPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 1;
    transformationPanel.add(obrotLabel1, c);

    c.gridx = 1;
    c.gridy = 1;
    transformationPanel.add(obrotTextField, c);

    c.gridx = 2;
    c.gridy = 1;
    transformationPanel.add(obrotLabel2, c);

    c.gridx = 1;
    c.gridy = 2;
    transformationPanel.add(space1, c);

    c.gridx = 1;
    c.gridy = 3;
    transformationPanel.add(transformationProcessButton, c);

    transformationPanel.repaint();
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OCR Panel">

    JLabel ekstrakcjaLabel = new JLabel("Metoda ekstrakcji cech");
    ekstrakcjaTextField = new JTextField("Nie wybrano");
    ekstrakcjaTextField.setEditable(false);
    space1 = new JLabel(" ");

    JLabel klasyfikacjaLabel = new JLabel("Metoda klasyfikacji danych");
    klasyfikacjaTextField = new JTextField("Nie wybrano");
    klasyfikacjaTextField.setEditable(false);
    JLabel space2 = new JLabel(" ");

    JLabel metrykaLabel = new JLabel("Metryka");
    metrykaTextField = new JTextField("Nie wybrano");
    metrykaTextField.setEditable(false);
    JLabel space3 = new JLabel(" ");

    OCRProcessButton = new JButton("Oblicz");
    OCRProcessButton.setEnabled(false);
    OCRProcessButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          OCRProcessButtonActionPerformed(evt);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    OCRPanel.updateUI();
    OCRPanel.removeAll();

    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 1;
    OCRPanel.add(ekstrakcjaLabel, c);

    c.gridx = 0;
    c.gridy = 2;
    OCRPanel.add(ekstrakcjaTextField, c);

    c.gridx = 0;
    c.gridy = 3;
    OCRPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 4;
    OCRPanel.add(klasyfikacjaLabel, c);

    c.gridx = 0;
    c.gridy = 5;
    OCRPanel.add(klasyfikacjaTextField, c);

    c.gridx = 0;
    c.gridy = 6;
    OCRPanel.add(space2, c);

    c.gridx = 0;
    c.gridy = 7;
    OCRPanel.add(metrykaLabel, c);

    c.gridx = 0;
    c.gridy = 8;
    OCRPanel.add(metrykaTextField, c);

    c.gridx = 0;
    c.gridy = 9;
    OCRPanel.add(space3, c);

    c.gridx = 0;
    c.gridy = 10;
    OCRPanel.add(OCRProcessButton, c);

    OCRPanel.repaint();


    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Deklaracja wydarzeń z menu">

    //Wczytanie obrazka
    open.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          openActionPerformed(evt);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    //Wyjście z programu
    exit.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exitActionPerformed(evt);
      }
    });

    about.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutActionPerformed(evt);
      }
    });

    help.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          try {
            helpActionPerformed(evt);
          } catch (URISyntaxException ex) {
            Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
          }
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>

    setSize(400, 400);
    setupM.setEnabled(false);

  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Filtry">
  /**
   * Filtr medianowy
   *
   * @param evt
   */
  private void fMedianCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //<editor-fold defaultstate="collapsed" desc="Layout">
    erozjaCheckBoxMenuItem.setSelected(false);
    dylatacjaCheckBoxMenuItem.setSelected(false);
    fMedianCheckBoxMenuItem.setSelected(true);
    fUsredCheckBoxMenuItem.setSelected(false);

    JLabel titleLabel = new JLabel();

    titleLabel.setText("Filtr medianowy o masce 3x3");
    JLabel space1 = new JLabel(" ");
    JButton processButton = new JButton("Filtruj");

    filterPanel.updateUI();
    filterPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    filterPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 1;
    filterPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 2;
    filterPanel.add(processButton, c);

    filterPanel.repaint();
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inIm = Noise.odszumianieM(inIm, 1);
        System.out.println("Dokonano filtracji medianowej maską 3x3.");
        try {
          new OutputImage(OCR.this, inIm).setVisible(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }

  /**
   * Filtr uśredniający
   *
   * @param evt
   */
  private void fUsredCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //<editor-fold defaultstate="collapsed" desc="Layout">
    erozjaCheckBoxMenuItem.setSelected(false);
    dylatacjaCheckBoxMenuItem.setSelected(false);
    fMedianCheckBoxMenuItem.setSelected(false);
    fUsredCheckBoxMenuItem.setSelected(true);

    JLabel titleLabel = new JLabel();

    titleLabel.setText("Filtr splotowy uśredniający");
    JLabel space1 = new JLabel(" ");
    JButton processButton = new JButton("Filtruj");

    filterPanel.updateUI();
    filterPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    filterPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 1;
    filterPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 2;
    filterPanel.add(processButton, c);

    filterPanel.repaint();
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inIm = Filter.splot(inIm, 1);
        System.out.println("Dokonano filtracji splotowej uśredniającej.");
        try {
          new OutputImage(OCR.this, inIm).setVisible(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }

  /**
   * Erozja
   *
   * @param evt
   */
  private void erozjaCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //<editor-fold defaultstate="collapsed" desc="Layout">
    erozjaCheckBoxMenuItem.setSelected(true);
    dylatacjaCheckBoxMenuItem.setSelected(false);
    fMedianCheckBoxMenuItem.setSelected(false);
    fUsredCheckBoxMenuItem.setSelected(false);


    JLabel titleLabel = new JLabel();

    titleLabel.setText("Erozja");
    JLabel space1 = new JLabel(" ");
    JButton processButton = new JButton("Dokonaj erozji");

    filterPanel.updateUI();
    filterPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    //filterPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 1;
    filterPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 2;
    filterPanel.add(processButton, c);

    filterPanel.repaint();
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inIm = Morphology.erosion(inIm);
        System.out.println("Dokonano erozji.");
        try {
          new OutputImage(OCR.this, inIm).setVisible(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }

  /**
   * Dylatacja
   *
   * @param evt
   */
  private void dylatacjaCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    //<editor-fold defaultstate="collapsed" desc="Layout">
    erozjaCheckBoxMenuItem.setSelected(false);
    dylatacjaCheckBoxMenuItem.setSelected(true);
    fMedianCheckBoxMenuItem.setSelected(false);
    fUsredCheckBoxMenuItem.setSelected(false);


    JLabel titleLabel = new JLabel();

    titleLabel.setText("Dylatacja");
    JLabel space1 = new JLabel(" ");
    JButton processButton = new JButton("Dokonaj dylatacji");

    filterPanel.updateUI();
    filterPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    //filterPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 1;
    filterPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 2;
    filterPanel.add(processButton, c);

    filterPanel.repaint();
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        inIm = Morphology.dilation(inIm);
        System.out.println("Dokonano dylatacji.");
        try {
          new OutputImage(OCR.this, inIm).setVisible(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }

  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Binaryzacja">
  /**
   * Binaryzacja globalna
   *
   * @param evt
   */
  private void globBinCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    // <editor-fold defaultstate="collapsed" desc="Layout">
    globBinCheckBoxMenuItem.setSelected(true);
    locBinCheckBoxMenuItem.setSelected(false);
    mixBinCheckBoxMenuItem.setSelected(false);

    JLabel titleLabel = new JLabel("BINARYZACJA GLOBALNA");
    JLabel space1 = new JLabel(" ");

    JTextField median = new JTextField("Mediana wynosi " + RGB.getGlobMedian(inIm));
    median.setEditable(false);
    JTextField arithmeticMean = new JTextField("Średnia arytmetyczna wynosi " + RGB.getGlobAMean(inIm));
    arithmeticMean.setEditable(false);
    int otsu = Binarization.metodaOtsu(inIm);
    JTextField otsuTextField = new JTextField("Próg wyznaczony metodą Otsu " + otsu);
    otsuTextField.setEditable(false);
    JLabel space2 = new JLabel(" ");

    JLabel binThresholdLabel = new JLabel("Ustaw próg binaryzacji [0-255]");
    final JTextField binThresholdTextField = new JTextField("" + otsu);
    JLabel space3 = new JLabel(" ");

    final JButton processButton = new JButton("Oblicz");

    binarizationPanel.updateUI();
    binarizationPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    binarizationPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 1;
    binarizationPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 2;
    binarizationPanel.add(median, c);

    c.gridx = 0;
    c.gridy = 3;
    binarizationPanel.add(arithmeticMean, c);

    c.gridx = 0;
    c.gridy = 4;
    binarizationPanel.add(otsuTextField, c);

    c.gridx = 0;
    c.gridy = 5;
    binarizationPanel.add(space2, c);

    c.gridx = 0;
    c.gridy = 6;
    binarizationPanel.add(binThresholdLabel, c);

    c.gridx = 0;
    c.gridy = 7;
    binarizationPanel.add(binThresholdTextField, c);

    c.gridx = 0;
    c.gridy = 8;
    binarizationPanel.add(space3, c);

    c.gridx = 0;
    c.gridy = 9;
    binarizationPanel.add(processButton, c);

    binarizationPanel.repaint();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          inIm = Binarization.byGlobalValue(inIm, Integer.parseInt(binThresholdTextField.getText()));
          System.out.println("Dokonano binaryzacji globalnej o progu " + binThresholdTextField.getText() + ".");
          new OutputImage(OCR.this, inIm).setVisible(true);
          ekstrNeighbCheckBoxMenuItem.setEnabled(true);
          ekstrDiagCheckBoxMenuItem.setEnabled(true);
          ekstrSqCheckBoxMenuItem.setEnabled(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }

  /**
   * Binaryzacja lokalna
   *
   * @param evt
   */
  private void locBinCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    // <editor-fold defaultstate="collapsed" desc="Layout">
    globBinCheckBoxMenuItem.setSelected(false);
    locBinCheckBoxMenuItem.setSelected(true);
    mixBinCheckBoxMenuItem.setSelected(false);

    JLabel titleLabel = new JLabel("BINARYZACJA LOKALNA");
    JLabel space1 = new JLabel(" ");

    JLabel methodL = new JLabel("Kryterium binaryzacji lokalnej");
    final JRadioButton medianRB = new JRadioButton("Mediana");
    medianRB.setSelected(true);
    final JRadioButton meanRB = new JRadioButton("Średnia arytmetyczna");
    JLabel space2 = new JLabel(" ");

    JLabel surrSizeL = new JLabel("Promień otoczenia piksela");
    final JTextField surrSizeTF = new JTextField("1");
    JLabel space3 = new JLabel(" ");

    final JButton processButton = new JButton("Oblicz");

    medianRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        medianRB.setSelected(true);
        meanRB.setSelected(false);
      }
    });

    meanRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        medianRB.setSelected(false);
        meanRB.setSelected(true);
      }
    });

    binarizationPanel.updateUI();
    binarizationPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    binarizationPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 5;
    binarizationPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 6;
    binarizationPanel.add(methodL, c);

    c.gridx = 0;
    c.gridy = 7;
    binarizationPanel.add(medianRB, c);

    c.gridx = 0;
    c.gridy = 8;
    binarizationPanel.add(meanRB, c);

    c.gridx = 0;
    c.gridy = 9;
    binarizationPanel.add(space2, c);

    c.gridx = 0;
    c.gridy = 10;
    binarizationPanel.add(surrSizeL, c);

    c.gridx = 0;
    c.gridy = 11;
    binarizationPanel.add(surrSizeTF, c);

    c.gridx = 0;
    c.gridy = 12;
    binarizationPanel.add(space3, c);

    c.gridx = 0;
    c.gridy = 13;
    binarizationPanel.add(processButton, c);

    binarizationPanel.repaint();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          inIm = Binarization.byLocalSurr(inIm, Integer.parseInt(surrSizeTF.getText()), medianRB.isSelected(), meanRB.isSelected());
          System.out.println("Dokonano binaryzacji lokalnej.");
          new OutputImage(OCR.this, inIm).setVisible(true);
          ekstrNeighbCheckBoxMenuItem.setEnabled(true);
          ekstrDiagCheckBoxMenuItem.setEnabled(true);
          ekstrSqCheckBoxMenuItem.setEnabled(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }

  /**
   * Binaryzacja mieszana
   *
   * @param evt
   */
  private void mixBinCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {

    // <editor-fold defaultstate="collapsed" desc="Layout">
    globBinCheckBoxMenuItem.setSelected(false);
    locBinCheckBoxMenuItem.setSelected(false);
    mixBinCheckBoxMenuItem.setSelected(true);

    JLabel titleLabel = new JLabel("BINARYZACJA MIESZANA");
    JLabel space1 = new JLabel(" ");

    JLabel methodL = new JLabel("Kryterium binaryzacji");
    final JRadioButton medianRB = new JRadioButton("Mediana");
    medianRB.setSelected(true);
    final JRadioButton meanRB = new JRadioButton("Średnia arytmetyczna");
    JLabel space2 = new JLabel(" ");

    JLabel surrSizeL = new JLabel("Promień otoczenia piksela");
    final JTextField surrSizeTF = new JTextField("7");
    JLabel space3 = new JLabel(" ");

    JLabel devValueL = new JLabel("Dopuszczalne odchylenie od wartości globalnej");
    final JTextField devValueTF = new JTextField("25");
    JLabel space4 = new JLabel(" ");

    final JButton processButton = new JButton("Oblicz");

    medianRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        medianRB.setSelected(true);
        meanRB.setSelected(false);
      }
    });

    meanRB.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        medianRB.setSelected(false);
        meanRB.setSelected(true);
      }
    });

    binarizationPanel.updateUI();
    binarizationPanel.removeAll();

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    c.gridx = 0;
    c.gridy = 0;
    binarizationPanel.add(titleLabel, c);

    c.gridx = 0;
    c.gridy = 1;
    binarizationPanel.add(space1, c);

    c.gridx = 0;
    c.gridy = 2;
    binarizationPanel.add(methodL, c);

    c.gridx = 0;
    c.gridy = 3;
    binarizationPanel.add(medianRB, c);

    c.gridx = 0;
    c.gridy = 4;
    binarizationPanel.add(meanRB, c);

    c.gridx = 0;
    c.gridy = 5;
    binarizationPanel.add(space2, c);

    c.gridx = 0;
    c.gridy = 6;
    binarizationPanel.add(surrSizeL, c);

    c.gridx = 0;
    c.gridy = 7;
    binarizationPanel.add(surrSizeTF, c);

    c.gridx = 0;
    c.gridy = 8;
    binarizationPanel.add(space3, c);

    c.gridx = 0;
    c.gridy = 9;
    binarizationPanel.add(devValueL, c);

    c.gridx = 0;
    c.gridy = 10;
    binarizationPanel.add(devValueTF, c);

    c.gridx = 0;
    c.gridy = 11;
    binarizationPanel.add(space4, c);

    c.gridx = 0;
    c.gridy = 12;
    binarizationPanel.add(processButton, c);

    binarizationPanel.repaint();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Obsługa wydarzenia">
    processButton.addActionListener(new java.awt.event.ActionListener() {

      public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
          inIm = Binarization.mixed(inIm, Integer.parseInt(surrSizeTF.getText()), Integer.parseInt(devValueTF.getText()), medianRB.isSelected(), meanRB.isSelected());
          System.out.println("Dokonano binaryzacji mieszanej.");
          new OutputImage(OCR.this, inIm).setVisible(true);
          ekstrNeighbCheckBoxMenuItem.setEnabled(true);
          ekstrDiagCheckBoxMenuItem.setEnabled(true);
          ekstrSqCheckBoxMenuItem.setEnabled(true);
        } catch (IOException ex) {
          Logger.getLogger(OCR.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    // </editor-fold>
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Ekstrakcja">
  /**
   * Ekstrakcja przez liczbę sąsiadów
   *
   * @param evt
   */
  private void ekstrNeighbCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    ekstrNeighbCheckBoxMenuItem.setSelected(true);
    ekstrDiagCheckBoxMenuItem.setSelected(false);
    ekstrSqCheckBoxMenuItem.setSelected(false);
    class1NNCheckBoxMenuItem.setEnabled(true);
    classkNNCheckBoxMenuItem.setEnabled(true);
    ekstrakcjaTextField.setText("Przez liczbę sąsiadów");
  }

  /**
   * Ekstrakcja przez przekątne
   *
   * @param evt
   */
  private void ekstrDiagCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    ekstrNeighbCheckBoxMenuItem.setSelected(false);
    ekstrDiagCheckBoxMenuItem.setSelected(true);
    ekstrSqCheckBoxMenuItem.setSelected(false);
    class1NNCheckBoxMenuItem.setEnabled(true);
    classkNNCheckBoxMenuItem.setEnabled(true);
    ekstrakcjaTextField.setText("Przez przekątne");
  }

  /**
   * Ekstrakcja przez podział na prostokąty
   *
   * @param evt
   */
  private void ekstrSqCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    ekstrNeighbCheckBoxMenuItem.setSelected(false);
    ekstrDiagCheckBoxMenuItem.setSelected(false);
    ekstrSqCheckBoxMenuItem.setSelected(true);
    class1NNCheckBoxMenuItem.setEnabled(true);
    classkNNCheckBoxMenuItem.setEnabled(true);
    ekstrakcjaTextField.setText("Przez podział na prostokąty");
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Klasyfikacja">
  /**
   * Klasyfikacja 1NN
   *
   * @param evt
   */
  private void jCheckBoxMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    class1NNCheckBoxMenuItem.setSelected(true);
    classkNNCheckBoxMenuItem.setSelected(false);
    metrEukCheckBoxMenuItem.setEnabled(true);
    metrManhCheckBoxMenuItem.setEnabled(true);
    klasyfikacjaTextField.setText("1NN");
  }

  /**
   * Klasyfikacja kNN
   *
   * @param evt
   */
  private void jCheckBoxMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    class1NNCheckBoxMenuItem.setSelected(false);
    classkNNCheckBoxMenuItem.setSelected(true);
    metrEukCheckBoxMenuItem.setEnabled(true);
    metrManhCheckBoxMenuItem.setEnabled(true);
    klasyfikacjaTextField.setText("kNN");
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Metryka">
  /**
   * Metryka Euklidesowa
   *
   * @param evt
   */
  private void metrEukCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    // TODO add your handling code here:
    metrEukCheckBoxMenuItem.setSelected(true);
    metrManhCheckBoxMenuItem.setSelected(false);
    metrykaTextField.setText("Euklidesowa");
    OCRProcessButton.setEnabled(true);
  }

  /**
   * Metryka Manhattan
   *
   * @param evt
   */
  private void metrManhCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    metrEukCheckBoxMenuItem.setSelected(false);
    metrManhCheckBoxMenuItem.setSelected(true);
    metrykaTextField.setText("Manhattan");
    OCRProcessButton.setEnabled(true);
  }
  // </editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Obrót">
  private void transformationProcessButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
    inIm = Transformation.obrotK(inIm, Double.parseDouble(obrotTextField.getText()));
    System.out.println("Obrócono obraz o " + Double.parseDouble(obrotTextField.getText()) + " stopni.");
    new OutputImage(OCR.this, inIm).setVisible(true);
  }
  //</editor-fold>

  // <editor-fold defaultstate="collapsed" desc="OCR">
  /**
   * Metoda dokonuje OCR wg ustalonych parametrów.
   *
   * @param evt
   */
  private void OCRProcessButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException, ClassNotFoundException {
    //Sprawdzenie czy istnieją pliki z bazami danych.
    if (!(new File("data/OCR/database2D.dat").exists() && new File("data/OCR/database3D.dat").exists()
            && new File("data/OCR/database4D.dat").exists())) {
      System.out.println("Nie znaleziono plików baz danych. Rozpoczęto trening.");
      Classification.dbTraining();
      System.out.println("Wytrenowano bazy danych i zapisano do plików.");
    } else {
      System.out.println("Znaleziono pliki baz danych.");
    }
    /*
     * 1. Segmentacja obrazu przeznaczonego do OCR. 2. Należy zapamiętać wiersz
     * i kolumnę. 3. Ekstrakcja wektora cech z pierwszego znaku. 4. Porównanie
     * wektora z bazą danych. W razie niepowodzenia pominąć znak. 5. Zapis
     * rozpoznanego znaku do tablicy 2D typu char.
     */
    BufferedImage[][] inputImage = Segmentation.metoda1(inIm);
    //Ustalenie wymiaru tablicy przechowującej znaki.
    char[][] outputText;
    outputText = new char[inputImage.length][];
    for (int i = 0; i < inputImage.length; i++) {
      outputText[i] = new char[inputImage[i].length];
    }
    for (int i = 0; i < inputImage.length; i++) {
      for (int j = 0; j < inputImage[i].length; j++) {
        //W tym miejscu następuje porównanie wektora cech znaku z bazą danych.
        if (ekstrNeighbCheckBoxMenuItem.isSelected()) {
          outputText[i][j] = Classification.compareVectorWithDatabase(
                  Extraction.byNrOfNeighbours(inputImage[i][j]), class1NNCheckBoxMenuItem.isSelected(), metrEukCheckBoxMenuItem.isSelected());
        } else if (ekstrDiagCheckBoxMenuItem.isSelected()) {
          outputText[i][j] = Classification.compareVectorWithDatabase(
                  Extraction.byDiagonals(inputImage[i][j]), class1NNCheckBoxMenuItem.isSelected(), metrEukCheckBoxMenuItem.isSelected());
        } else if (ekstrSqCheckBoxMenuItem.isSelected()) {
          outputText[i][j] = Classification.compareVectorWithDatabase(
                  Extraction.bySquares(inputImage[i][j]), class1NNCheckBoxMenuItem.isSelected(), metrEukCheckBoxMenuItem.isSelected());
        }
      }
    }
    for (int i = 0; i < outputText.length; i++) {
      for (int j = 0; j < outputText[i].length; j++) {
        System.out.print(outputText[i][j]);
      }
      System.out.println();
    }

    new OutputText(OCR.this, outputText).setVisible(true);

  }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Obsługa wydarzeń z menu">

  /**
   * Wczytanie obrazka.
   *
   * @param evt
   * @throws IOException
   */
  private void openActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new java.io.File("data/OCR/inputImages"));

    int returnVal = chooser.showOpenDialog(open);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      new InputImage(OCR.this, chooser.getSelectedFile()).setVisible(true);
      inIm = ImageIO.read(chooser.getSelectedFile());
      //Konwersja to odcieni szarości z normalizacją histogramu
      inIm = RGB.szaryOpt(inIm);
      inIm = RGB.szaryHist(inIm);
      setupM.setEnabled(true);
      globBinCheckBoxMenuItem.setSelected(false);
      locBinCheckBoxMenuItem.setSelected(false);
      mixBinCheckBoxMenuItem.setSelected(false);
      ekstrNeighbCheckBoxMenuItem.setSelected(false);
      ekstrDiagCheckBoxMenuItem.setSelected(false);
      ekstrSqCheckBoxMenuItem.setSelected(false);
      class1NNCheckBoxMenuItem.setSelected(false);
      classkNNCheckBoxMenuItem.setSelected(false);
      metrEukCheckBoxMenuItem.setSelected(false);
      metrManhCheckBoxMenuItem.setSelected(false);
      ekstrNeighbCheckBoxMenuItem.setEnabled(false);
      ekstrDiagCheckBoxMenuItem.setEnabled(false);
      ekstrSqCheckBoxMenuItem.setEnabled(false);
      class1NNCheckBoxMenuItem.setEnabled(false);
      classkNNCheckBoxMenuItem.setEnabled(false);
      metrEukCheckBoxMenuItem.setEnabled(false);
      metrManhCheckBoxMenuItem.setEnabled(false);
      binarizationPanel.updateUI();
      binarizationPanel.removeAll();
      transformationProcessButton.setEnabled(true);
      System.out.println("Wczytano obraz wejściowy.");
    }
    if (returnVal == JFileChooser.CANCEL_OPTION) 
      ;

    //Resets the file chooser for the next time it's shown.
    chooser.setSelectedFile(null);
  }

  /**
   * Zamknięcie programu poleceniem 'exit'
   *
   * @param evt
   */
  private void exitActionPerformed(java.awt.event.ActionEvent evt) {
    System.out.println("Zakończono program.");
    System.exit(0);
  }

  /**
   * Polecenie 'about'
   *
   * @param evt
   */
  private void aboutActionPerformed(java.awt.event.ActionEvent evt) {
    new About(OCR.this).setVisible(true);
  }

  /**
   * Polecenie 'help'
   *
   * @param evt
   */
  private void helpActionPerformed(java.awt.event.ActionEvent evt) throws IOException, URISyntaxException {
    //new Help(OCR.this).setVisible(true);
    Desktop desktop;
    if (Desktop.isDesktopSupported()) {
      desktop = Desktop.getDesktop();
      desktop.open(new File("data/OCR/help.html"));
    }
  }
  // </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Variables declatarion">
  // Variables declaration
  //MENU
  private JMenuBar menubar;
  private JMenu fileM;
  private JMenu helpM;
  private JMenu setupM;
  private JMenuItem open;
  private JMenuItem exit;
  private javax.swing.JCheckBoxMenuItem globBinCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem ekstrNeighbCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem ekstrDiagCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem ekstrSqCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem class1NNCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem classkNNCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem metrEukCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem metrManhCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem locBinCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem mixBinCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem fMedianCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem fUsredCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem erozjaCheckBoxMenuItem;
  private javax.swing.JCheckBoxMenuItem dylatacjaCheckBoxMenuItem;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JPopupMenu.Separator jSeparator3;
  private javax.swing.JPopupMenu.Separator jSeparator4;
  private JMenuItem about;
  private JMenuItem help;
  private javax.swing.JTabbedPane jTabbedPane1;
  private javax.swing.JPanel transformationPanel;
  private javax.swing.JPanel filterPanel;
  private javax.swing.JPanel binarizationPanel;
  private javax.swing.JPanel OCRPanel;
  //OCR PANEL
  private JTextField ekstrakcjaTextField;
  private JTextField klasyfikacjaTextField;
  private JTextField metrykaTextField;
  private JButton OCRProcessButton;
  //Obrót panel
  private JTextField obrotTextField;
  private JButton transformationProcessButton;
  //MISC
  private BufferedImage inIm;
  // End of variables declaration
  // </editor-fold>
}