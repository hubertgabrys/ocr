/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obrazy;

import java.awt.image.BufferedImage;

/**
 *
 * @author Hubert
 */
public class Filter {

  //<editor-fold defaultstate="collapsed" desc="Filtr medianowy ulepszony">
  /**
   * Metoda wyznacza medianę z zadanego otoczenia piksela, jeżeli wartość
   * piksela zawiera się w zadanym procencie skrajnych wartości. Np. dla 0%
   * przepisze dla wszystkich pikseli wartość bez zmian, a dla 100% dla wszyst-
   * kich pikseli wyliczy medianę z otoczenia.
   *
   * @param i Współrzędna pozioma piksela.
   * @param j Współrzędna pionowa piksela
   * @param in Wczytywany obraz.
   * @param pixelSurr Otoczenie piksela.
   * @param percent Procent dla jakiego będzie liczona mediana.
   * @return Wartość piksela.
   */
  public static int median(int pixelValue, int[][] pixelSurr,
          int percent) {
    //Wyznaczenie liczby elementów otoczenia piksela.
    int licznik = 0;
    for (int[] x : pixelSurr) {
      for (int y : x) {
        if (y >= 0) {
          licznik++;
        }
      }
    }
    double[] array = new double[licznik];

    //Przekazanie otoczenia piksela do tablicy jednowymiarowej.
    licznik = 0;
    for (int[] x : pixelSurr) {
      for (int y : x) {
        if (y >= 0) {
          array[licznik] = y;
          licznik++;
        }
      }
    }

    /*
     * Stworzenie tablicy dwuwymiarowej. Pierwszy wymiar oznacza numer kanału,
     * drugi wymiar oznacza element otoczenia piksela. Następnie dla każdego
     * kanału znajdujemy wartość RGB.
     */
    int[][] array2 = new int[3][array.length];
    for (int k = 0; k < array.length; k++) {
      array2[0][k] = RGB.getR((int) array[k]);
      array2[1][k] = RGB.getG((int) array[k]);
      array2[2][k] = RGB.getB((int) array[k]);
    }

    //Posortowanie wartości odrębnie dla każdego kanału.
    Sort.quicksort(array2[0], 0, array2[0].length - 1);
    Sort.quicksort(array2[1], 0, array2[1].length - 1);
    Sort.quicksort(array2[2], 0, array2[2].length - 1);

    //Wyznaczenie median na poszczególnych kanałach.
    int r, g, b;
    r = array2[0][array2[0].length / 2 + 1];
    g = array2[1][array2[1].length / 2 + 1];
    b = array2[2][array2[2].length / 2 + 1];

    //Porównanie wyznaczonych wartości RGB z wartościami z obrazka.
    int limit = (int) (array.length * percent / 100 * 0.5);
    //Warunek zabezpiecza co najmniej po dwa elementy z obu stron tablicy.
    if (limit < 2) {
      limit = 2;
    }
    int red = RGB.getR(pixelValue);
    int green = RGB.getG(pixelValue);
    int blue = RGB.getB(pixelValue);
    if (red < array2[0][limit] || red > array2[0][array.length - limit - 1])
      ; else {
      r = red;
    }
    if (green < array2[1][limit] || green > array2[1][array.length - limit - 1])
      ; else {
      g = green;
    }
    if (blue < array2[2][limit] || blue > array2[2][array.length - limit - 1])
      ; else {
      b = blue;
    }
    
    return RGB.toRGB(r, g, b);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Filtr medianowy">
  /**
   * Metoda wyznacza medianę z zadanego otoczenia piksela.
   *
   * @param pixelSurr Otoczenie piksela.
   * @return Wartość piksela.
   */
  public static int median(int[][][] pixelSurr3D) {
    //Zamiana otoczenia piksela z 3D na 2D
    int[][] pixelSurr = new int[pixelSurr3D.length][pixelSurr3D[0].length];
    for (int i = 0; i < pixelSurr3D.length; i++) {
      for (int j = 0; j < pixelSurr3D[i].length; j++) {
        pixelSurr[i][j] = RGB.toRGB(pixelSurr3D[i][j][1], pixelSurr3D[i][j][2], pixelSurr3D[i][j][3]);
      }
    }
    //Wyznaczenie liczby elementów otoczenia piksela.
    int licznik = 0;
    for (int[] x : pixelSurr) {
      for (int y : x) {
        if (y >= 0) {
          licznik++;
        }
      }
    }
    double[] array = new double[licznik];

    //Przekazanie otoczenia piksela do tablicy jednowymiarowej.
    licznik = 0;
    for (int[] x : pixelSurr) {
      for (int y : x) {
        if (y >= 0) {
          array[licznik] = y;
          licznik++;
        }
      }
    }

    /*
     * Stworzenie tablicy dwuwymiarowej. Pierwszy wymiar oznacza numer kanału,
     * drugi wymiar oznacza element otoczenia piksela. Następnie dla każdego
     * kanału znajdujemy wartość RGB.
     */
    int[][] array2 = new int[3][array.length];
    for (int j = 0; j < array.length; j++) {
      array2[0][j] = RGB.getR((int) array[j]);
      array2[1][j] = RGB.getG((int) array[j]);
      array2[2][j] = RGB.getB((int) array[j]);
    }

    //Posortowanie wartości odrębnie dla każdego kanału.
    Sort.quicksort(array2[0], 0, array2[0].length - 1);
    Sort.quicksort(array2[1], 0, array2[1].length - 1);
    Sort.quicksort(array2[2], 0, array2[2].length - 1);

    //Wyznaczenie median na poszczególnych kanałach.
    int r, g, b;
    r = array2[0][array2[0].length / 2 + 1];
    g = array2[1][array2[1].length / 2 + 1];
    b = array2[2][array2[2].length / 2 + 1];
    
    return RGB.toRGB(r, g, b);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Filtr splotowy1">
  /**
   * Metoda dokonuje konwolucji otoczenia piksela z maską.
   *
   * @param pixelSurr Otoczenie piksela.
   * @param mask Maska.
   * @return Wartość piksela.
   */
  public static int conv(int[][] pixelSurr, int[][] mask) {
    //Wykonanie konwolucji.
    double[][] conv = new double[pixelSurr.length][pixelSurr[0].length];
    for (int i = 0; i < pixelSurr.length; i++) {
      for (int j = 0; j < pixelSurr[0].length; j++) {
        conv[i][j] = pixelSurr[i][j] * mask[i][j];
      }
    }

    //Wyznaczenie wartości piksela.
    int licznikR, licznikG, licznikB, mianownik, r, g, b;
    licznikR = licznikG = licznikB = mianownik = 0;
    for (int i = 0; i < conv.length; i++) {
      for (int j = 0; j < conv[0].length; j++) {
        //Piksele należące do otoczenia piksela, leżące poza obrazem mają typ
        //double.
        if (Math.floor(conv[i][j]) == conv[i][j]) {
          licznikR += RGB.getR((int) conv[i][j]);
          licznikG += RGB.getG((int) conv[i][j]);
          licznikB += RGB.getB((int) conv[i][j]);
          mianownik += mask[i][j];
        }
      }
    }
    r = licznikR / mianownik;
    g = licznikG / mianownik;
    b = licznikB / mianownik;
    
    return RGB.toRGB(r, g, b);
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Filtr splotowy2">
  public static BufferedImage splot(BufferedImage bf, int x) {
    BufferedImage in = bf;
    int filtr = x;
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    int maska[][] = new int[3][3];
    
    //Menu wyboru maski.
    switch (filtr) {
      case 1:
        maska[0][0] = 1;
        maska[0][1] = 1;
        maska[0][2] = 1;
        maska[1][0] = 1;
        maska[1][1] = 1;
        maska[1][2] = 1;
        maska[2][0] = 1;
        maska[2][1] = 1;
        maska[2][2] = 1;
        break;
      case 2:
        maska[0][0] = 1;
        maska[0][1] = 1;
        maska[0][2] = 1;
        maska[1][0] = 1;
        maska[1][1] = 2;
        maska[1][2] = 1;
        maska[2][0] = 1;
        maska[2][1] = 1;
        maska[2][2] = 1;
        break;
      case 3:
        maska[0][0] = 1;
        maska[0][1] = 2;
        maska[0][2] = 1;
        maska[1][0] = 2;
        maska[1][1] = 4;
        maska[1][2] = 2;
        maska[2][0] = 1;
        maska[2][1] = 2;
        maska[2][2] = 1;
        break;
      case 4:
        maska[0][0] = 0;
        maska[0][1] = -1;
        maska[0][2] = 0;
        maska[1][0] = -1;
        maska[1][1] = 5;
        maska[1][2] = -1;
        maska[2][0] = 0;
        maska[2][1] = -1;
        maska[2][2] = 0;
        break;
      case 5:
        maska[0][0] = -1;
        maska[0][1] = -1;
        maska[0][2] = -1;
        maska[1][0] = -1;
        maska[1][1] = 9;
        maska[1][2] = -1;
        maska[2][0] = -1;
        maska[2][1] = -1;
        maska[2][2] = -1;
        break;
      case 6:
        maska[0][0] = 1;
        maska[0][1] = -2;
        maska[0][2] = 1;
        maska[1][0] = -2;
        maska[1][1] = 5;
        maska[1][2] = -2;
        maska[2][0] = 1;
        maska[2][1] = -2;
        maska[2][2] = 1;
        break;
    }
    
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        
        int licznikR = 0;
        int licznikG = 0;
        int licznikB = 0;
        int mianownik = 0;
        
        //Zabezpieczenie przed wyjściem poza zakres
        int k = i - 1;
        int endK = i + 1;
        while (k < 0) {
          k = i;
        }
        while (endK >= in.getWidth()) {
          endK = i;
        }
        
        //Wyznaczenie nowej wartości piksela
        for (; k <= endK; k++) {
          int w = j - 1;
          int endW = j + 1;
          while (w < 0) {
            w = j;
          }
          while (endW >= in.getHeight()) {
            endW = j;
          }
          for (; w <= endW; w++) {
            licznikR += maska[k - i + 1][w - j + 1] * getR(in.getRGB(k, w));
            licznikG += maska[k - i + 1][w - j + 1] * getG(in.getRGB(k, w));
            licznikB += maska[k - i + 1][w - j + 1] * getB(in.getRGB(k, w));
            mianownik += maska[k - i + 1][w - j + 1];
          }
        }
        int r = (int) (licznikR / mianownik);
        int g = (int) (licznikG / mianownik);
        int b = (int) (licznikB / mianownik);
        
        //Zabezpieczenie przed przekroczeniem na kanałach wartości 0 i 255.
        if (r < 0) {
          r = 0;
        }
        if (g < 0) {
          g = 0;
        }
        if (b < 0) {
          b = 0;
        }
        if (r > 255) {
          r = 255;
        }
        if (g > 255) {
          g = 255;
        }
        if (b > 255) {
          b = 255;
        }
        
        out.setRGB(i, j, toRGB(r, g, b));
      }
    }
    return out;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="getPixelSurr">
  /**
   * Metoda pobiera z obrazu do tablicy wartości RGB piksela a jego otoczenia.
   *
   * @param x Wymiar poprzeczny otoczenia. Koniecznie nieparzysty!
   * @param y Wymiar pionowy otoczenia. Koniecznie nieparzysty!
   * @param in Obraz wejściowy.
   * @param a Współrzędna poprzeczna piksela.
   * @param b Współrzędna pionowa piksela.
   * @return Otoczenie piksela w postaci tablicy dwuwymiarowej. Pierwsza
   * współrzędna odpowiada za wiersz, druga za kolumnę.
   */
  public static int[][][] getPixelSurr(int x, int y, BufferedImage in, int i,
          int j) {
    if ((x | y) % 2 == 0) {
      System.out.println("Wymiary maski muszą być nieparzyste!");
    }
    
    //Dla wszystkich elementów otoczenia ustalamy jako początkową wartość -1
    //ponieważ dla wszystkich pikseli obrazu wartość jest > 0.
    //To pozwoli później wyznaczyć w tablicy konwolucji elementy leżące poza
    //obrazem.
    int[][][] pixelSurr = new int[x][y][4];
    for (int a = 0; a < x; a++) {
      for (int b = 0; b < y; b++) {
        pixelSurr[a][b][0] = 0;//alfa
        pixelSurr[a][b][1] = -1;//red
        pixelSurr[a][b][2] = -1;//green
        pixelSurr[a][b][3] = -1;//blue
      }
    }
    
    
    //Zabezpieczenie przed wyjściem poza zakres dla kolumn.
    int promienX = (int) (x / 2);
    int promienY = (int) (y / 2);
    int k = i - promienX;
    while (k < 0) {
      k = 0;
    }
    int endWidth = i + promienX;
    while (endWidth >= in.getWidth()) {
      endWidth = in.getWidth() - 1;
    }
    
    //Pobranie wartości RGB otoczenia piksela do tablicy.
    for (; k <= endWidth; k++) {
      //Zabezpieczenie przed wyjściem poza zakres dla wierszy. Nie przenosić,
      //bo nie ma inicjalizacji w pętli for a źle się iteruje.
      int l = j - promienY;
      while (l < 0) {
        l = 0;
      }
      int endHeight = j + promienY;
      while (endHeight >= in.getHeight()) {
        endHeight = in.getHeight() - 1;
      }
      for (; l <= endHeight; l++) {
        pixelSurr[k - i + promienX][l - j + promienY] = RGB.getArray(in.getRGB(k, l));
      }
    }
    return pixelSurr;
  }
  //</editor-fold>
  
    private static int getR(int in) {
    return (int) ((in << 8) >> 24) & 0xff;
  }

  private static int getG(int in) {
    return (int) ((in << 16) >> 24) & 0xff;
  }

  private static int getB(int in) {
    return (int) ((in << 24) >> 24) & 0xff;
  }

  private static int toRGB(int r, int g, int b) {
    return (int) ((((r << 8) | g) << 8) | b);
  }
  
}
