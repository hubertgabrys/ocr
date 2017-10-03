/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obrazy;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class RGB {

  /**
   * Metoda przekształca obraz do tablicy dwuwymiarowej zamieniając czarne
   * piksele na 1, a białe na 0
   *
   * @param in obraz wejściowy
   * @return tablica dwuwymiarowa
   */
  public static int[][] toArray(BufferedImage in) {
    int[] black = {0, 0, 0, 0};
    int[] white = {0, 255, 255, 255};
    int[][] array = new int[in.getWidth()][in.getHeight()];
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        if (Arrays.equals(RGB.getArray(in.getRGB(i, j)), black)) {
          array[i][j] = 1;
        }
        if (Arrays.equals(RGB.getArray(in.getRGB(i, j)), white)) {
          array[i][j] = 0;
        }
      }
    }
    return array;
  }

  /**
   * Metoda zamienia tablicę dwuwymiarową na obraz BufferedImage. 0 zamienia na
   * biały, a 1 na czarny
   *
   * @param in
   * @return
   */
  public static BufferedImage toBF(int[][] in) {
    BufferedImage out = new BufferedImage(in.length, in[0].length, 5);
    for (int i = 0; i < in.length; i++) {
      for (int j = 0; j < in[i].length; j++) {
        if (in[i][j] == 0) {
          out.setRGB(i, j, RGB.toRGB(255, 255, 255));
        }
        if (in[i][j] == 1) {
          out.setRGB(i, j, RGB.toRGB(0, 0, 0));
        }
        if (in[i][j] == 2) {
          out.setRGB(i, j, RGB.toRGB(255, 0, 0));
        }
        if (in[i][j] == 3) {
          out.setRGB(i, j, RGB.toRGB(0, 255, 0));
        }
        if (in[i][j] == 4) {
          out.setRGB(i, j, RGB.toRGB(0, 0, 255));
        }
      }
    }
    return out;
  }

  public static int[] getArray(int in) {
    int[] array = new int[4];
    array[0] = 0;
    array[1] = getR(in);
    array[2] = getG(in);
    array[3] = getB(in);
    return array;
  }

  public static int getR(int in) {
    return (int) (in >> 16) & 0xff;
  }

  public static int getG(int in) {
    return (int) (in >> 8) & 0xff;
  }

  public static int getB(int in) {
    return (int) (in) & 0xff;
  }

  public static int toRGB(int r, int g, int b) {
    return (int) ((((r << 8) | g) << 8) | b);
  }

  public static int limitsR(int r) {
    if (r < 0) {
      r = 0;
    }
    if (r > 255) {
      r = 255;
    }
    return r;
  }

  public static int limitsG(int g) {
    if (g < 0) {
      g = 0;
    }
    if (g > 255) {
      g = 255;
    }
    return g;
  }

  public static int limitsB(int b) {
    if (b < 0) {
      b = 0;
    }
    if (b > 255) {
      b = 255;
    }
    return b;
  }

  public static BufferedImage negatyw(BufferedImage in) throws IOException {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        int r = 255 - RGB.getR(in.getRGB(i, j));
        int g = 255 - RGB.getG(in.getRGB(i, j));
        int b = 255 - RGB.getB(in.getRGB(i, j));
        out.setRGB(i, j, RGB.toRGB(r, g, b));
      }
    }
    return out;
  }

  public static BufferedImage szary(BufferedImage in) throws IOException {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        int r = RGB.getR(in.getRGB(i, j));
        int g = RGB.getG(in.getRGB(i, j));
        int b = RGB.getB(in.getRGB(i, j));
        int x = (r + g + b) / 3;
        out.setRGB(i, j, RGB.toRGB(x, x, x));
      }
    }

    return out;
  }

  public static BufferedImage szaryOpt(BufferedImage in) throws IOException {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        int r = RGB.getR(in.getRGB(i, j));
        int g = RGB.getG(in.getRGB(i, j));
        int b = RGB.getB(in.getRGB(i, j));
        int x = (int) (0.3 * r + 0.59 * g + 0.11 * b);
        out.setRGB(i, j, RGB.toRGB(x, x, x));
      }
    }

    return out;
  }

  public static BufferedImage normalnyHist(BufferedImage in) throws IOException {

    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    int minRedPix = 255;
    int minGreenPix = 255;
    int minBluePix = 255;
    int maxRedPix = 0;
    int maxGreenPix = 0;
    int maxBluePix = 0;
    //znalezienie minimalnych i maksymalnych wartości na poszczególnych kanałach
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        if (RGB.getR(in.getRGB(i, j)) > maxRedPix) {
          maxRedPix = RGB.getR(in.getRGB(i, j));
        }
        if (RGB.getG(in.getRGB(i, j)) > maxGreenPix) {
          maxGreenPix = RGB.getG(in.getRGB(i, j));
        }
        if (RGB.getB(in.getRGB(i, j)) > maxBluePix) {
          maxBluePix = RGB.getB(in.getRGB(i, j));
        }
        if (RGB.getR(in.getRGB(i, j)) < minRedPix) {
          minRedPix = RGB.getR(in.getRGB(i, j));
        }
        if (RGB.getG(in.getRGB(i, j)) < minGreenPix) {
          minGreenPix = RGB.getG(in.getRGB(i, j));
        }
        if (RGB.getB(in.getRGB(i, j)) < minBluePix) {
          minBluePix = RGB.getB(in.getRGB(i, j));
        }
      }
    }
    //normalizacja histogramu
    int r, g, b;
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        r = 255 * (RGB.getR(in.getRGB(i, j)) - minRedPix) / (maxRedPix - minRedPix);
        g = 255 * (RGB.getG(in.getRGB(i, j)) - minGreenPix) / (maxGreenPix - minGreenPix);
        b = 255 * (RGB.getB(in.getRGB(i, j)) - minBluePix) / (maxBluePix - minBluePix);
        out.setRGB(i, j, RGB.toRGB(r, g, b));
      }
    }
    /*
     * Wypisanie minimalnych i maksymalnych wartości na poszczególnych kanałach
     * System.out.println("minRedPix: "+minRedPix);
     * System.out.println("minGreenPix: "+minGreenPix);
     * System.out.println("minBluePix: "+minBluePix);
     * System.out.println("maxRedPix: "+maxRedPix);
     * System.out.println("maxGreenPix: "+maxGreenPix);
     * System.out.println("maxBluePix: "+maxBluePix);
     */

    //zapis znormalizowanego obrazka

    return out;
  }

  public static BufferedImage szaryHist(BufferedImage in) throws IOException {

    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    int minRedPix = 255;
    int minGreenPix = 255;
    int minBluePix = 255;
    int maxRedPix = 0;
    int maxGreenPix = 0;
    int maxBluePix = 0;
    //znalezienie minimalnych i maksymalnych wartości na poszczególnych kanałach
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        if (RGB.getR(in.getRGB(i, j)) > maxRedPix) {
          maxRedPix = RGB.getR(in.getRGB(i, j));
        }
        if (RGB.getG(in.getRGB(i, j)) > maxGreenPix) {
          maxGreenPix = RGB.getG(in.getRGB(i, j));
        }
        if (RGB.getB(in.getRGB(i, j)) > maxBluePix) {
          maxBluePix = RGB.getB(in.getRGB(i, j));
        }
        if (RGB.getR(in.getRGB(i, j)) < minRedPix) {
          minRedPix = RGB.getR(in.getRGB(i, j));
        }
        if (RGB.getG(in.getRGB(i, j)) < minGreenPix) {
          minGreenPix = RGB.getG(in.getRGB(i, j));
        }
        if (RGB.getB(in.getRGB(i, j)) < minBluePix) {
          minBluePix = RGB.getB(in.getRGB(i, j));
        }
      }
    }
    //normalizacja histogramu
    int r, g, b;
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        r = 255 * (RGB.getR(in.getRGB(i, j)) - minRedPix) / (maxRedPix - minRedPix);
        g = 255 * (RGB.getG(in.getRGB(i, j)) - minGreenPix) / (maxGreenPix - minGreenPix);
        b = 255 * (RGB.getB(in.getRGB(i, j)) - minBluePix) / (maxBluePix - minBluePix);
        int x = (r + g + b) / 3;
        out.setRGB(i, j, RGB.toRGB(x, x, x));
      }
    }
    //zapis znormalizowanego obrazka
    return out;
  }

  /**
   * Średnia arytmetyczna pikseli z obrazka.
   *
   * @param in
   * @return
   */
  public static int getGlobAMean(BufferedImage in) {
    int suma = 0;
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        suma += getR(in.getRGB(i, j));
      }
    }
    return (int) (suma / (in.getWidth() * in.getHeight()));
  }

  /**
   * Mediana pikseli z obrazka.
   *
   * @param in
   * @return
   */
  public static int getGlobMedian(BufferedImage in) {
    int[] array = new int[in.getWidth() * in.getHeight()];
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        array[i * in.getHeight() + j] = getR(in.getRGB(i, j));
      }
    }
    Arrays.sort(array);

    return array[(int) (array.length / 2)];
  }
}