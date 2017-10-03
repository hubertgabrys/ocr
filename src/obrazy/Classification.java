package obrazy;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
//</editor-fold>

/**
 *
 * @author Hubert
 */
public class Classification {

  //<editor-fold defaultstate="collapsed" desc="dbTraining">
  /**
   * Trening w celu utworzenia bazy danych.
   *
   * 1. Utorzenie listy typu String. 2. Przekazanie do listy nazw wszystkich
   * plików z rozszerzeniem .png z katalogu zawierającego graficzne pliki
   * treningowe. 3. Program pobiera i usuwa z listy pierwszy plik do typu
   * BufferedImage. 4. Dokonana zostaje segmentacja obrazu. 5. Ekstrakcja cech
   * wybraną metodą. 6. Wektor cech zapisywany macierzy która posłuży jako baza
   * danych. 7. Gdy lista jest pusta baza danych jest gotowa i zostaje zapisana
   * do pliku.
   */
  public static void dbTraining() {
    ObjectOutputStream oos = null;
    try {
      Deque<String> lista = new LinkedList<String>();
      lista.add("data/OCR/training/ArialNormal.png");
      lista.add("data/OCR/training/CourierNewNormal.png");
      lista.add("data/OCR/training/TimesNewRomanNormal.png");
      lista.add("data/OCR/training/VerdanaNormal.png");
      int[][] database2D = new int[lista.size() * 93][];
      int[][][] database3D = new int[lista.size() * 93][][];
      int[][][][] database4D = new int[lista.size() * 93][][][];
      int iterator = 0;
      while (!lista.isEmpty()) {
        BufferedImage[][] trainingImage = Segmentation.metoda1(ImageIO.read(new File(lista.removeFirst())));
        for (int i = 0; i < trainingImage.length; i++) {
          for (int j = 0; j < trainingImage[i].length; j++, iterator++) {
            database4D[iterator] = Extraction.byNrOfNeighbours(trainingImage[i][j]);
            database3D[iterator] = Extraction.byDiagonals(trainingImage[i][j]);
            database2D[iterator] = Extraction.bySquares(trainingImage[i][j]);
          }
        }
      }
      oos = new ObjectOutputStream(new FileOutputStream("data/OCR/database2D.dat"));
      oos.writeObject(database2D);
      oos.close();
      oos = new ObjectOutputStream(new FileOutputStream("data/OCR/database3D.dat"));
      oos.writeObject(database3D);
      oos.close();
      oos = new ObjectOutputStream(new FileOutputStream("data/OCR/database4D.dat"));
      oos.writeObject(database4D);
      oos.close();
    } catch (IOException ex) {
      Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        oos.close();
      } catch (IOException ex) {
        Logger.getLogger(Classification.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Porównanie wektora cech z bazą danych">
  /**
   * Metoda porównuje wektor cech znaku z bazą danych.
   *
   * @param vector1D Wektor cech znaku.
   * @param NN1 Jeśli prawda to zastosowany zostanie algorytm klasyfikacji 1NN,
   * jeśli fałsz to algorytm kNN.
   * @param euklides Jeśli prawda to zostanie zastosowana metryka euklidesowa,
   * jeśli fałsz to Manhattan.
   * @return Znak wyjściowy.
   */
  public static char compareVectorWithDatabase(int[] vector1D, Boolean NN1, Boolean euklides) throws IOException, ClassNotFoundException {
    char charOut;
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/OCR/database2D.dat"));
    int[][] database = (int[][]) ois.readObject();
    ois.close();
    //Metryki
    double[] metryki = new double[database.length];
    if (euklides) {//Metryka euklidesowa
      for (int i = 0; i < database.length; i++) {
        metryki[i] = metrEukl(vector1D, database[i]);
      }
    } else {//Metryka Manhattan
      for (int i = 0; i < database.length; i++) {
        metryki[i] = metrManh(vector1D, database[i]);
      }
    }
    //Metoda klasyfikacji
    if (NN1) {
      charOut = NN1(metryki);
    } else {
      charOut = kNN(metryki, 3);
    }

    return charOut;
  }

  public static char compareVectorWithDatabase(int[][] vector2D, Boolean NN1, Boolean euklides) throws IOException, ClassNotFoundException {
    char charOut;
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/OCR/database3D.dat"));
    int[][][] database = (int[][][]) ois.readObject();
    ois.close();
    //Metryki
    double[] metryki = new double[database.length];
    for (int i = 0; i < metryki.length; i++) {
      metryki[i] = 1e6;
    }
    if (euklides) {//Metryka euklidesowa
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if (vector2D.length == database[i].length) {
          metryki[i] = metrEukl(vector2D, database[i]);
        }
      }
    } else {//Metryka Manhattan
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if (vector2D.length == database[i].length) {
          metryki[i] = metrManh(vector2D, database[i]);
        }
      }
    }
    //Metoda klasyfikacji
    if (NN1) {
      charOut = NN1(metryki);
    } else {
      charOut = kNN(metryki, 3);
    }
    return charOut;
  }

  public static char compareVectorWithDatabase(int[][][] vector3D, Boolean NN1, Boolean euklides) throws IOException, ClassNotFoundException {
    char charOut;
    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/OCR/database4D.dat"));
    int[][][][] database = (int[][][][]) ois.readObject();
    ois.close();
    //Metryki
    double[] metryki = new double[database.length];
    for (int i = 0; i < metryki.length; i++) {
      metryki[i] = 1e6;
    }
    if (euklides) {//Metryka euklidesowa
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if ((vector3D[0].length == database[i][0].length) && (vector3D[1].length == database[i][1].length)) {
          metryki[i] = metrEukl(vector3D, database[i]);
        }
      }
    } else {//Metryka Manhattan
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if ((vector3D[0].length == database[i][0].length) && (vector3D[1].length == database[i][1].length)) {
          metryki[i] = metrManh(vector3D, database[i]);
        }
      }
    }
    //Metoda klasyfikacji
    if (NN1) {
      charOut = NN1(metryki);
    } else {
      charOut = kNN(metryki, 3);
    }

    return charOut;
  }
  
  /**
   * Metoda porównuje wektor cech znaku z bazą danych.
   *
   * @param vector1D Wektor cech znaku.
   * @param database2D Baza danych klasyfikatorów.
   * @param NN1 Jeśli prawda to zastosowany zostanie algorytm klasyfikacji 1NN,
   * jeśli fałsz to algorytm kNN.
   * @param euklides Jeśli prawda to zostanie zastosowana metryka euklidesowa,
   * jeśli fałsz to Manhattan.
   * @return Znak wyjściowy.
   */
  public static char compareVectorWithDatabase(int[] vector1D, int[][] database, Boolean NN1, Boolean euklides) throws IOException, ClassNotFoundException {
    char charOut;
    
    //Metryki
    double[] metryki = new double[database.length];
    if (euklides) {//Metryka euklidesowa
      for (int i = 0; i < database.length; i++) {
        metryki[i] = metrEukl(vector1D, database[i]);
      }
    } else {//Metryka Manhattan
      for (int i = 0; i < database.length; i++) {
        metryki[i] = metrManh(vector1D, database[i]);
      }
    }
    //Metoda klasyfikacji
    if (NN1) {
      charOut = NN1(metryki);
    } else {
      charOut = kNN(metryki, 3);
    }

    return charOut;
  }

  public static char compareVectorWithDatabase(int[][] vector2D, int[][][] database, Boolean NN1, Boolean euklides) throws IOException, ClassNotFoundException {
    char charOut;
    
    //Metryki
    double[] metryki = new double[database.length];
    for (int i = 0; i < metryki.length; i++) {
      metryki[i] = 1e6;
    }
    if (euklides) {//Metryka euklidesowa
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if (vector2D.length == database[i].length) {
          metryki[i] = metrEukl(vector2D, database[i]);
        }
      }
    } else {//Metryka Manhattan
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if (vector2D.length == database[i].length) {
          metryki[i] = metrManh(vector2D, database[i]);
        }
      }
    }
    //Metoda klasyfikacji
    if (NN1) {
      charOut = NN1(metryki);
    } else {
      charOut = kNN(metryki, 3);
    }

    return charOut;
  }

  public static char compareVectorWithDatabase(int[][][] vector3D, int[][][][] database, Boolean NN1, Boolean euklides) throws IOException, ClassNotFoundException {
    char charOut;
    
    //Metryki
    double[] metryki = new double[database.length];
    for (int i = 0; i < metryki.length; i++) {
      metryki[i] = 1e6;
    }
    if (euklides) {//Metryka euklidesowa
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if ((vector3D[0].length == database[i][0].length) && (vector3D[1].length == database[i][1].length)) {
          metryki[i] = metrEukl(vector3D, database[i]);
        }
      }
    } else {//Metryka Manhattan
      for (int i = 0; i < database.length; i++) {
        //Warunek wyklucza wektory o długości innnej niż rekordy w bazie danych
        if ((vector3D[0].length == database[i][0].length) && (vector3D[1].length == database[i][1].length)) {
          metryki[i] = metrManh(vector3D, database[i]);
        }
      }
    }
    //Metoda klasyfikacji
    if (NN1) {
      charOut = NN1(metryki);
    } else {
      charOut = kNN(metryki, 3);
    }

    return charOut;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Metryki">
  private static double metrEukl(int[] vector, int[] database) {
    double suma = 0;
    for (int i = 0; i < database.length; i++) {
      suma += Math.pow((vector[i] - database[i]), 2);
    }
    return Math.sqrt(suma);
  }

  private static int metrManh(int[] vector, int[] database) {
    int suma = 0;
    for (int i = 0; i < database.length; i++) {
      suma += Math.abs(vector[i] - database[i]);
    }
    return suma;
  }

  private static double metrEukl(int[][] vector, int[][] database) {
    double suma = 0;
    for (int i = 0; i < database.length; i++) {
      for (int j = 0; j < database[i].length; j++) {
        suma += Math.pow((vector[i][j] - database[i][j]), 2);
      }
    }
    return Math.sqrt(suma);
  }

  private static int metrManh(int[][] vector, int[][] database) {
    int suma = 0;
    for (int i = 0; i < database.length; i++) {
      for (int j = 0; j < database[i].length; j++) {
        suma += Math.abs(vector[i][j] - database[i][j]);
      }
    }
    return suma;
  }

  private static double metrEukl(int[][][] vector, int[][][] database) {
    double suma = 0;
    for (int i = 0; i < database.length; i++) {
      for (int j = 0; j < database[i].length; j++) {
        for (int k = 0; k < database[i][j].length; k++) {
          suma += Math.pow((vector[i][j][k] - database[i][j][k]), 2);
        }
      }
    }
    return Math.sqrt(suma);
  }

  private static int metrManh(int[][][] vector, int[][][] database) {
    int suma = 0;
    for (int i = 0; i < database.length; i++) {
      for (int j = 0; j < database[i].length; j++) {
        for (int k = 0; k < database[i][j].length; k++) {
          suma += Math.abs(vector[i][j][k] - database[i][j][k]);
        }
      }
    }
    return suma;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="1NN">
  /**
   * Znajduje minimalną wartość metryki i zwraca jej pozycję w tablicy.
   *
   * @param metryki Tablica z metrykami.
   * @return Znak.
   */
  private static char NN1(double[] metryki) {
    double[] szukacz = new double[2];
    szukacz[0] = metryki[0];
    szukacz[1] = 0;
    for (int i = 1; i < metryki.length; i++) {
      if (metryki[i] < szukacz[0]) {
        szukacz[0] = metryki[i];
        szukacz[1] = i;
      }
    }
    return findChar((int) szukacz[1]);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="kNN">
  /**
   * Znajduje k minimalnych wartości metryki. Sprawdza jakim znakom odpowiadają
   * i wybiera znak najczęściej występujący.
   *
   * @param metryki Tablica z metrykami.
   * @param k Liczba minimlanych wartości.
   * @return Znak.
   */
  private static char kNN(double[] metryki, int k) {
    //Tworzymy tablicę z minimami.
    double[][] minima = new double[k][2];
    //Szukacz znajduje minimalną wartość w tablicy. W komórce [0] wartość, a w komórce [1] jej indeks w tablicy.
    double[] szukacz = new double[2];
    //Jako minimalną wartość przypisuję pierwszą wartość z tablicy metryki.
    szukacz[0] = metryki[0];
    szukacz[1] = 0;
    //Przeszukuję całą tablicę metryki w poszukiwaniu minimalnej wartości.
    for (int i = 1; i < metryki.length; i++) {
      if (metryki[i] < szukacz[0]) {
        szukacz[0] = metryki[i];
        szukacz[1] = i;
      }
    }
    //Znalezioną minimalną wartość i jej indeks wpisuję do pierwszej komórki tablicy minima.
    minima[0][0] = szukacz[0];
    minima[0][1] = szukacz[1];
    //Szukam kolejnych minimalnych wartości k-1 razy tak aby zapełnić tablicę minima.
    for (int i = 1; i < k; i++) {
      //Jako minimalną wartość przypisuję pierwszą wartość z tablicy metryki.
      szukacz[0] = metryki[0];
      szukacz[1] = 0;
      //Przeszukuję całą tablicę metryki w poszukiwaniu minimalnej wartości większej od wartośći z pozycji minima[k-1];
      for (int j = 1; j < metryki.length; j++) {
        if ((metryki[j] < szukacz[0]) && metryki[j] > minima[i - 1][0]) {
          szukacz[0] = metryki[j];
          szukacz[1] = j;
        }
      }
      minima[i][0] = szukacz[0];
      minima[i][1] = szukacz[1];
    }
    //Wypisuję minima i znaki im odpowiadające
//    for (int i = 0; i < k; i++) {
//      System.out.print(minima[i][0] + " " + findChar((int) minima[i][1]) + ", ");
//    }
    int[] tally = new int[128];
    for (int i = 0; i < tally.length; i++) {
      tally[i] = 0;
    }
    for (int i = 0; i < minima.length; i++) {
      tally[findChar((int) minima[i][1])]++;
    }
    int maxIndex = 0;
    for (int i = 1; i < tally.length; i++) {
      if (tally[i] > tally[maxIndex]) {
        maxIndex = i;
      }
    }
    //System.out.println(" Najczęściej występuje: " + (char) maxIndex);
    return (char) maxIndex;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="findChar">
  /**
   * Metoda na podstawie indeksu w tabeli rozponaje znak.
   *
   * @param index Indeks.
   * @return Znak.
   */
  private static char findChar(int index) {
    char[] trainingLettersArray = new char[93];
    trainingLettersArray[0] = 'Q';
    trainingLettersArray[1] = 'W';
    trainingLettersArray[2] = 'E';
    trainingLettersArray[3] = 'R';
    trainingLettersArray[4] = 'T';
    trainingLettersArray[5] = 'Y';
    trainingLettersArray[6] = 'U';
    trainingLettersArray[7] = 'I';
    trainingLettersArray[8] = 'O';
    trainingLettersArray[9] = 'P';
    trainingLettersArray[10] = 'A';
    trainingLettersArray[11] = 'S';
    trainingLettersArray[12] = 'D';
    trainingLettersArray[13] = 'F';
    trainingLettersArray[14] = 'G';
    trainingLettersArray[15] = 'H';
    trainingLettersArray[16] = 'J';
    trainingLettersArray[17] = 'K';
    trainingLettersArray[18] = 'L';
    trainingLettersArray[19] = 'Z';
    trainingLettersArray[20] = 'X';
    trainingLettersArray[21] = 'C';
    trainingLettersArray[22] = 'V';
    trainingLettersArray[23] = 'B';
    trainingLettersArray[24] = 'N';
    trainingLettersArray[25] = 'M';
    trainingLettersArray[26] = 'q';
    trainingLettersArray[27] = 'w';
    trainingLettersArray[28] = 'e';
    trainingLettersArray[29] = 'r';
    trainingLettersArray[30] = 't';
    trainingLettersArray[31] = 'y';
    trainingLettersArray[32] = 'u';
    trainingLettersArray[33] = 'i';
    trainingLettersArray[34] = 'o';
    trainingLettersArray[35] = 'p';
    trainingLettersArray[36] = 'a';
    trainingLettersArray[37] = 's';
    trainingLettersArray[38] = 'd';
    trainingLettersArray[39] = 'f';
    trainingLettersArray[40] = 'g';
    trainingLettersArray[41] = 'h';
    trainingLettersArray[42] = 'j';
    trainingLettersArray[43] = 'k';
    trainingLettersArray[44] = 'l';
    trainingLettersArray[45] = 'z';
    trainingLettersArray[46] = 'x';
    trainingLettersArray[47] = 'c';
    trainingLettersArray[48] = 'v';
    trainingLettersArray[49] = 'b';
    trainingLettersArray[50] = 'n';
    trainingLettersArray[51] = 'm';
    trainingLettersArray[52] = '`';
    trainingLettersArray[53] = '1';
    trainingLettersArray[54] = '2';
    trainingLettersArray[55] = '3';
    trainingLettersArray[56] = '4';
    trainingLettersArray[57] = '5';
    trainingLettersArray[58] = '6';
    trainingLettersArray[59] = '7';
    trainingLettersArray[60] = '8';
    trainingLettersArray[61] = '9';
    trainingLettersArray[62] = '0';
    trainingLettersArray[63] = '-';
    trainingLettersArray[64] = '=';
    trainingLettersArray[65] = '[';
    trainingLettersArray[66] = ']';
    trainingLettersArray[67] = '\\';
    trainingLettersArray[68] = ';';
    trainingLettersArray[69] = '\'';
    trainingLettersArray[70] = ',';
    trainingLettersArray[71] = '.';
    trainingLettersArray[72] = '/';
    trainingLettersArray[73] = '~';
    trainingLettersArray[74] = '!';
    trainingLettersArray[75] = '@';
    trainingLettersArray[76] = '#';
    trainingLettersArray[77] = '$';
    trainingLettersArray[78] = '%';
    trainingLettersArray[79] = '^';
    trainingLettersArray[80] = '&';
    trainingLettersArray[81] = '*';
    trainingLettersArray[82] = '(';
    trainingLettersArray[83] = ')';
    trainingLettersArray[84] = '_';
    trainingLettersArray[85] = '+';
    trainingLettersArray[86] = '{';
    trainingLettersArray[87] = '}';
    trainingLettersArray[88] = '|';
    trainingLettersArray[89] = ':';
    trainingLettersArray[90] = '<';
    trainingLettersArray[91] = '>';
    trainingLettersArray[92] = '?';

    if (index > 92) {
      index %= 93;
    }
    return trainingLettersArray[index];
  }
  //</editor-fold>
}