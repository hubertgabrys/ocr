/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obrazy;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Hubert
 */
public class Morphology {

  /**
   * Metoda dokonuje erozji obrazu
   *
   * @param in Obraz wejściowy
   * @return Obraz wyjściowy
   */
  public static BufferedImage erosion(BufferedImage in) {
    BufferedImage out = powiekszCzarnymi(in, 1);
    int[][] array = RGB.toArray(out);
    int[][] array2 = new int[array.length][array[0].length];
    for (int i = 1; i < array.length - 1; i++) {
      for (int j = 1; j < array[i].length - 1; j++) {
        array2[i][j] = array[i][j];
        if (array[i][j] == 1) {
          for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
              if (array[k][l] == 0) {
                array2[i][j] = 0;
              }
            }
          }
        }
      }
    }
    return crop(RGB.toBF(array2), 1);
  }

  /**
   * Metoda dokonuje dylatacji obrazu
   *
   * @param in Obraz wejściowy
   * @return Obraz wyjściowy
   */
  public static BufferedImage dilation(BufferedImage in) {
    BufferedImage out = powiekszBialymi(in, 1);
    int[][] array = RGB.toArray(out);
    int[][] array2 = new int[array.length][array[0].length];
    for (int i = 1; i < array.length - 1; i++) {
      for (int j = 1; j < array[i].length - 1; j++) {
        array2[i][j] = array[i][j];
        if (array[i][j] == 0) {
          for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
              if (array[k][l] == 1) {
                array2[i][j] = 1;
              }
            }
          }
        }
      }
    }
    return crop(RGB.toBF(array2), 1);
  }

  /**
   * Metoda obcina krawędzie obrazka
   *
   * @param in Obraz wejściowy
   * @param ile Szerokość odbcięcia w px
   * @return Obraz wyjściowy
   */
  public static BufferedImage crop(BufferedImage in, int ile) {
    int width = in.getWidth() - (2 * ile);
    int height = in.getHeight() - (2 * ile);
    BufferedImage out = new BufferedImage(width, height, in.getType());
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        out.setRGB(i, j, in.getRGB(i + ile, j + ile));
      }
    }
    return out;
  }

  /**
   * Metoda obcina krawędzie tablicy
   *
   * @param in Tablica wejściowa
   * @param ile Szerokość obcięcia
   * @return Tabela wyjściowa
   */
  public static int[][] crop(int[][] in, int ile) {
    int width = in.length - (2 * ile);
    int height = in[0].length - (2 * ile);
    int[][] out = new int[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        out[i][j] = in[i + ile][j + ile];
      }
    }
    return out;
  }

  /**
   * Metoda otacza obraz ramką z czarnych pikseli.
   *
   * @param in obraz wejściowy
   * @param ile szerokość ramki w pikselach
   * @return obraz wyjściowy
   */
  private static BufferedImage powiekszCzarnymi(BufferedImage in, int ile) {
    int black = RGB.toRGB(0, 0, 0);

    int outWidth = in.getWidth() + 2 * ile;
    int outHeight = in.getHeight() + 2 * ile;

    int inWidth = in.getWidth();
    int inHeight = in.getHeight();

    BufferedImage out = new BufferedImage(outWidth, outHeight, in.getType());

    for (int i = 0; i < outWidth; i++) {
      int ri = i - ile;
      for (int j = 0; j < outHeight; j++) {
        int rj = j - ile;
        if (ri >= 0 && rj >= 0 && ri < inWidth && rj < inHeight) {
          out.setRGB(i, j, in.getRGB(ri, rj));
        } else {
          out.setRGB(i, j, black);
        }
      }
    }
    return out;
  }

  /**
   * Metoda otacza obraz ramką z białych pikseli.
   *
   * @param in obraz wejściowy
   * @param ile szerokość ramki w pikselach
   * @return obraz wyjściowy
   */
  public static BufferedImage powiekszBialymi(BufferedImage in, int ile) {
    int white = RGB.toRGB(255, 255, 255);

    int outWidth = in.getWidth() + 2 * ile;
    int outHeight = in.getHeight() + 2 * ile;

    int inWidth = in.getWidth();
    int inHeight = in.getHeight();

    BufferedImage out = new BufferedImage(outWidth, outHeight, in.getType());

    for (int i = 0; i < outWidth; i++) {
      int ri = i - ile;
      for (int j = 0; j < outHeight; j++) {
        int rj = j - ile;
        if (ri >= 0 && rj >= 0 && ri < inWidth && rj < inHeight) {
          out.setRGB(i, j, in.getRGB(ri, rj));
        } else {
          out.setRGB(i, j, white);
        }
      }
    }
    return out;
  }

  public static int[][] powiekszBialymi(int[][] in, int ile) {
    int outWidth = in.length + 2 * ile;
    int outHeight = in[0].length + 2 * ile;
    int inWidth = in.length;
    int inHeight = in[0].length;
    int[][] out = new int[outWidth][outHeight];
    for (int i = 0; i < outWidth; i++) {
      int ri = i - ile;
      for (int j = 0; j < outHeight; j++) {
        int rj = j - ile;
        if (ri >= 0 && rj >= 0 && ri < inWidth && rj < inHeight) {
          out[i][j] = in[ri][rj];
        } else {
          out[i][j] = 0;
        }
      }
    }
    return out;
  }

  /**
   * Metoda dokonuje ścieniania obrazu maską.
   *
   * @param in obraz wejściowy
   * @return obraz wyjściowy
   */
  public static BufferedImage thinningByMask(BufferedImage in) {
    in = powiekszBialymi(in, 1);
    int width = in.getWidth();
    int height = in.getHeight();

    int[] black = {0, 0, 0, 0};
    int[] white = {0, 255, 255, 255};
    int[] red = {0, 255, 0, 0};

    //Ustalenie maski.
    int[][][] mask0 = {{red, white, red}, {red, black, red}, {black, black, black}};
    int[][][] mask90 = {{red, red, black}, {white, black, black}, {red, red, black}};
    int[][][] mask180 = {{black, black, black}, {red, black, red}, {red, white, red}};
    int[][][] mask270 = {{black, red, red}, {black, black, white}, {black, red, red}};

    BufferedImage out = new BufferedImage(width, height, in.getType());
    //cały obraz wyjściowy malujemy na biało
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        out.setRGB(i, j, RGB.toRGB(white[1], white[2], white[3]));
      }
    }

    int[][][] pixelSurr;//przechowuje otoczenie piksela
    boolean changed;
    do {
      changed = false;
      for (int i = 1; i < width - 1; i++) {
        for (int j = 1; j < height - 1; j++) {
          if (Arrays.equals(RGB.getArray(in.getRGB(i, j)), black)) {//operacje
            //wykonaywane będą tylko dla czarnych pikseli obrazka wejściowego

            //Pobranie otoczenia piksela.
            pixelSurr = Filter.getPixelSurr(3, 3, in, i, j);

            //Porównanie z maską.
            if (compareWithMask(mask0, pixelSurr)) {
              //zmieniamy wartość
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
              changed = true;
            } else if (compareWithMask(mask90, pixelSurr)) {
              //zmieniamy wartość
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
              changed = true;
            } else if (compareWithMask(mask180, pixelSurr)) {
              //zmieniamy wartość
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
              changed = true;
            } else if (compareWithMask(mask270, pixelSurr)) {
              //zmieniamy wartość
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
              changed = true;
            } else {
              //zostawiamy tą samą
              out.setRGB(i, j, RGB.toRGB(0, 0, 0));
            }
          }
        }
      }

      //przepisujemy kopię na wejście
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          in.setRGB(i, j, out.getRGB(i, j));
        }
      }
    } while (changed);
    return out;
  }

  /**
   * Metoda dokonuje porównania maski z otoczeniem piksela
   *
   * @param mask maska (piksele w masce oznaczone jako czerwone nie są
   * porównywane.
   * @param pixelSurr otoczenie piksela w formie tablicy dwuwymiarowej
   * @return prawda jeśli obraz zgadza się z maską, fałsz jeśli obraz nie zgadza
   * się z maską.
   */
  private static boolean compareWithMask(int[][][] mask, int[][][] pixelSurr) {
    boolean theSame;
    int licznik = 0;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (Arrays.equals(mask[i][j], pixelSurr[i][j])) {
          licznik++;
        }
      }
    }
    if (licznik == 5) {
      theSame = true;
    } else {
      theSame = false;
    }

    return theSame;
  }

  /**
   * Maskę sprawdzarka zakłada się na zasadzie sumy wartości. Na początku
   * analizy waga piksela środkowego maski wynosi 0. Jeżeli danej komórce maski
   * odpowiada piksel o wartości 1, 2, 3 lub 4, to do wagi piksela środkowego
   * dodaje się wagę analizowanej komórki (nie jest ona wymnażana razy wartość
   * piksela). W celu poprawnego działania algorytmu należy obramować oryginalny
   * obraz ramką o grubości 1 px złożoną z białych pikseli.
   *
   * @param array tablica wejściowa utworzona zgodnie z algorytmem KMM
   * @return waga środkowego piksela maski
   */
  private static int sprawdzarka(int i, int j, int[][] array) {
    int waga = 0;
    int[][] sprawdzarka = {{128, 64, 32}, {1, 0, 16}, {2, 4, 8}};
    int k, m;
    for (k = i - 1, m = 0; k <= i + 1; k++, m++) {
      int l, n;
      for (l = j - 1, n = 0; l <= j + 1; l++, n++) {
        if (array[k][l] != 0) {
          waga += sprawdzarka[m][n];
        }
      }
    }
    return waga;
  }

  /**
   * Metoda sprawdza czy podana wartość znajduje się w podanej tablicy
   *
   * @param waga szukana wartość
   * @param tablica tablica
   * @return jeśli znaleziono to metoda zwraca true, jeśli nie - false.
   */
  private static boolean isIn(int waga, int[] tablica) {
    boolean znaleziono = false;
    int i = 0;

    do {
      if (waga == tablica[i]) {
        znaleziono = true;
      }
      i++;
    } while (!znaleziono && i < tablica.length);
    return znaleziono;
  }

  /**
   * Metoda dokonuje ścieniania algorytmem KMM
   *
   * @param in obraz wejściowy
   * @return obraz wyjściowy
   */
  public static BufferedImage thinningByKMM(BufferedImage in) {
    int[] czwórki = {3, 6, 7, 12, 14, 15, 24, 28, 30, 48, 56, 60, 96, 112, 120,
      129, 131, 135, 192, 193, 195, 224, 225, 240};

    int[] wycięcia = {3, 5, 7, 12, 13, 14, 15, 20, 21, 22, 23, 28, 29, 30, 31,
      48, 52, 53, 54, 55, 56, 60, 61, 62, 63, 65, 67, 69, 71,
      77, 79, 80, 81, 83, 84, 85, 86, 87, 88, 89, 91, 92, 93,
      94, 95, 97, 99, 101, 103, 109, 111, 112, 113, 115, 116,
      117, 118, 119, 120, 121, 123, 124, 125, 126, 127, 131,
      133, 135, 141, 143, 149, 151, 157, 159, 181, 183, 189,
      191, 192, 193, 195, 197, 199, 205, 207, 208, 209, 211,
      212, 213, 214, 215, 216, 217, 219, 220, 221, 222, 223,
      224, 225, 227, 229, 231, 237, 239, 240, 241, 243, 244,
      245, 246, 247, 248, 249, 251, 252, 253, 254, 255};

    int[] black = {0, 0, 0, 0};
    int[] white = {0, 255, 255, 255};

    in = powiekszBialymi(in, 1);

    int width = in.getWidth();
    int height = in.getHeight();

    int[][] array = new int[width][height];

    /*
     * skanujemy obrazek i przekazujemy do tablicy wartości: 1 - czarny, 0 -
     * biały
     */
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (Arrays.equals(RGB.getArray(in.getRGB(i, j)), black)) {
          array[i][j] = 1;
        }
        if (Arrays.equals(RGB.getArray(in.getRGB(i, j)), white)) {
          array[i][j] = 0;
        }
      }
    }
    boolean changed;
    do {
      changed = false;

      /*
       * Piksele 1, które posiadają sąsiadów o oznaczeniu 0 po bokach, u góry
       * lub u dołu, oznaczamy jako 2.
       */
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (array[i][j] == 1) {
            if (array[i - 1][j] == 0 || array[i + 1][j] == 0 || array[i][j + 1] == 0
                    || array[i][j - 1] == 0) {
              array[i][j] = 2;
            }
          }
        }
      }

      /*
       * Pozostałe piksele 1, które posiadają sąsiadów o oznaczeniu 0 na rogach,
       * oznaczamy jako 3.
       */
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (array[i][j] == 1) {
            if (array[i - 1][j - 1] == 0 || array[i - 1][j + 1] == 0
                    || array[i + 1][j + 1] == 0 || array[i + 1][j - 1] == 0) {
              array[i][j] = 3;
            }
          }
        }
      }

      /*
       * Dla pikseli oznaczonych jako 2 za pomocą maski sprawdzarka obliczamy
       * ich wagę. Jeśli waga znajduje się na liście czwórki oznaczenie piksela
       * zamieniamy z 2 na 4.
       */
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (array[i][j] == 2) {
            if (isIn(sprawdzarka(i, j, array), czwórki)) {
              array[i][j] = 4;
            }
          }
        }
      }

      /*
       * Dla wszystkich pikseli oznaczonych jako 4 wyliczamy wagę za pomocą
       * maski sprawdzarka. Jeśli waga znajduje się na liście wycięcia,
       * zamieniamy piksel na 0, zaś w przeciwnym razie zamieniamy go na 1.
       */
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (array[i][j] == 4) {
            if (isIn(sprawdzarka(i, j, array), wycięcia)) {
              array[i][j] = 0;
              changed = true;
            } else {
              array[i][j] = 1;
            }
          }
        }
      }

      /*
       * Dla wszystkich pikseli oznaczonych jako 2 wyliczamy wagę za pomocą
       * maski sprawdzarka. Jeśli waga znajduje się na liście wycięcia,
       * zamieniamy piksel na 0, zaś w przeciwnym razie zamieniamy go na 1.
       */
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (array[i][j] == 2) {
            if (isIn(sprawdzarka(i, j, array), wycięcia)) {
              array[i][j] = 0;
              changed = true;
            } else {
              array[i][j] = 1;
            }
          }
        }
      }

      /*
       * Dla wszystkich pikseli oznaczonych jako 3 wyliczamy wagę za pomocą
       * maski sprawdzarka. Jeśli waga znajduje się na liście wycięcia,
       * zamieniamy piksel na 0, zaś w przeciwnym razie zamieniamy go na 1.
       */
      for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
          if (array[i][j] == 3) {
            if (isIn(sprawdzarka(i, j, array), wycięcia)) {
              array[i][j] = 0;
              changed = true;
            } else {
              array[i][j] = 1;
            }
          }
        }
      }
    } while (changed);

    BufferedImage out = new BufferedImage(width - 2, height - 2, in.getType());

    /*
     * Przepisanie tablicy do BufferedImage
     */
    for (int i = 1; i < width - 1; i++) {
      for (int j = 1; j < height - 1; j++) {
        if (array[i][j] == 0) {
          out.setRGB(i - 1, j - 1, RGB.toRGB(255, 255, 255));
        }
        if (array[i][j] == 1) {
          out.setRGB(i - 1, j - 1, RGB.toRGB(0, 0, 0));
        }
      }
    }
    return out;
  }
}