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
 * @author Hubert
 */
public class Binarization {

  //<editor-fold defaultstate="collapsed" desc="Progowanie mieszane">
  /**
   * Metoda dokonuje progowania mieszanego. Jako argumenty należy podać kolejno
   * BufferedImage w odcieniach szarości, szerokość otoczenia i wielkość
   * odbiegania od średniej.
   *
   * @author Hubert
   * @param in Obraz wejściowy
   * @param otoczenie Szerokość otoczenia lokalnego
   * @param odbieganie Wielkość odbiegania od progu
   */
  public static BufferedImage progMiesz(BufferedImage in, int threshold, int otoczenie, int odbieganie) {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());

    //Wyliczenie średniej globalnej
    int suma = 0;
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        suma += RGB.getR(in.getRGB(i, j));
      }
    }
    int sredniaGlobalna = (int) (suma / (in.getWidth() * in.getHeight()));

    //Wyliczenie średniej lokalnej i byGlobalValue mieszane
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        int u, d, l, r = (int) (otoczenie / 2);
        u = d = l = r;
        int licznik = 0;
        suma = 0;

        int k = i - l;
        int endK = i + r;

        //Zabezpieczenie przed wyjściem poza zakres
        while (k < 0) {
          k = i - (--l);
        }
        while (endK >= in.getWidth()) {
          endK = i + (--r);
        }

        //Wyznaczenie średniej lokalnej
        for (; k <= endK; k++) {
          int w = j - u;
          int endW = j + d;
          while (w < 0) {
            w = j - (--u);
          }
          while (endW >= in.getHeight()) {
            endW = j + (--d);
          }
          for (; w <= endW; w++) {
            suma += RGB.getR(in.getRGB(k, w));
            licznik++;
          }
        }
        int sredniaLokalna = (int) (suma / licznik);

        //Binaryzacja
        if (Math.abs(sredniaGlobalna - sredniaLokalna) > odbieganie) {
          if (RGB.getR(in.getRGB(i, j)) < sredniaGlobalna) {
            out.setRGB(i, j, RGB.toRGB(0, 0, 0));
          } else {
            out.setRGB(i, j, RGB.toRGB(255, 255, 255));
          }
        } else {
          if (RGB.getR(in.getRGB(i, j)) < sredniaLokalna) {
            out.setRGB(i, j, RGB.toRGB(0, 0, 0));
          } else {
            out.setRGB(i, j, RGB.toRGB(255, 255, 255));
          }
        }

      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Progowanie średnią lokalną">
  /**
   * Metoda dokonuje progowania średnią lokalną. Jako argumenty należy przekazać
   * kolejno BufferedImage w odcieniach szarości oraz szerokość otoczenia
   * piksela.
   *
   * @param in Obraz wejściowy
   * @param otoczenie Otoczenie piksela
   * @return
   */
  public static BufferedImage byLocalSurr(BufferedImage in, int otoczenie, boolean median, boolean mean) {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    int pole = (1 + 2 * otoczenie) * (1 + 2 * otoczenie);
    if (median) {
      for (int i = 0; i < in.getWidth(); i++) {
        for (int j = 0; j < in.getHeight(); j++) {
          int u, d, l, r = (int) (otoczenie);
          u = d = l = r;
          int licznik = 0;

          int k = i - l;
          int endK = i + r;

          //Zabezpieczenie przed wyjściem poza zakres
          while (k < 0) {
            k = i - (--l);
          }
          while (endK >= in.getWidth()) {
            endK = i + (--r);
          }

          //Wyznaczenie lokalnej mediany
          int[] array = new int[pole];
          for (; k <= endK; k++) {
            int w = j - u;
            int endW = j + d;
            while (w < 0) {
              w = j - (--u);
            }
            while (endW >= in.getHeight()) {
              endW = j + (--d);
            }
            for (; w <= endW; w++) {
              array[licznik] = RGB.getR(in.getRGB(k, w));
              licznik++;
            }
          }
          Arrays.sort(array);
          int mediana = array[array.length / 2];

          //Binaryzacja
          if (RGB.getR(in.getRGB(i, j)) < mediana) {
            out.setRGB(i, j, RGB.toRGB(0, 0, 0));
          } else {
            out.setRGB(i, j, RGB.toRGB(255, 255, 255));
          }
        }
      }
    } else if (mean) {
      for (int i = 0; i < in.getWidth(); i++) {
        for (int j = 0; j < in.getHeight(); j++) {
          int u, d, l, r = (int) (otoczenie);
          u = d = l = r;
          int licznik = 0;
          int suma = 0;

          int k = i - l;
          int endK = i + r;

          //Zabezpieczenie przed wyjściem poza zakres
          while (k < 0) {
            k = i - (--l);
          }
          while (endK >= in.getWidth()) {
            endK = i + (--r);
          }

          //Wyznaczenie średniej lokalnej
          for (; k <= endK; k++) {
            int w = j - u;
            int endW = j + d;
            while (w < 0) {
              w = j - (--u);
            }
            while (endW >= in.getHeight()) {
              endW = j + (--d);
            }
            for (; w <= endW; w++) {
              suma += RGB.getR(in.getRGB(k, w));
              licznik++;
            }
          }
          int sredniaLokalna = (int) (suma / licznik);

          //Binaryzacja
          if (RGB.getR(in.getRGB(i, j)) < sredniaLokalna) {
            out.setRGB(i, j, RGB.toRGB(0, 0, 0));
          } else {
            out.setRGB(i, j, RGB.toRGB(255, 255, 255));
          }
        }
      }
    }

    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Progowanie mieszane">
  /**
   * Metoda dokonuje progowania mieszanego.
   *
   * @param in Obraz wejściowy
   * @param otoczenie Otoczenie piksela
   * @param deviation Wartość odbiegania od średniej globalnej
   * @param median wybrano medianę?
   * @param mean wybrano średnią arytmetyczną?
   * @return
   */
  public static BufferedImage mixed(BufferedImage in, int otoczenie, int deviation, boolean median, boolean mean) throws IOException {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    int pole = (1 + 2 * otoczenie) * (1 + 2 * otoczenie);
    if (median) {
      int medianaGlobalna = RGB.getGlobMedian(in);
//      System.out.println("Jest global median");
      for (int i = 0; i < in.getWidth(); i++) {
        for (int j = 0; j < in.getHeight(); j++) {
          int u, d, l, r = (int) (otoczenie);
          u = d = l = r;
          int licznik = 0;

          int k = i - l;
          int endK = i + r;

          //Zabezpieczenie przed wyjściem poza zakres
          while (k < 0) {
            k = i - (--l);
          }
          while (endK >= in.getWidth()) {
            endK = i + (--r);
          }

          //Wyznaczenie lokalnej mediany
          int[] array = new int[pole];
          for (; k <= endK; k++) {
            int w = j - u;
            int endW = j + d;
            while (w < 0) {
              w = j - (--u);
            }
            while (endW >= in.getHeight()) {
              endW = j + (--d);
            }
            for (; w <= endW; w++) {
              array[licznik] = RGB.getR(in.getRGB(k, w));
              licznik++;
            }
          }
          Arrays.sort(array);
          int mediana = array[array.length / 2];

          //Binaryzacja
          if (Math.abs(medianaGlobalna - mediana) > deviation) {
            if (RGB.getR(in.getRGB(i, j)) < medianaGlobalna) {
              out.setRGB(i, j, RGB.toRGB(0, 0, 0));
            } else {
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
            }
          } else {
            if (RGB.getR(in.getRGB(i, j)) < mediana) {
              out.setRGB(i, j, RGB.toRGB(0, 0, 0));
            } else {
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
            }
          }
        }
      }
    } else if (mean) {
      int sredniaGlobalna = RGB.getGlobAMean(in);
      for (int i = 0; i < in.getWidth(); i++) {
        for (int j = 0; j < in.getHeight(); j++) {
          int u, d, l, r = (int) (otoczenie);
          u = d = l = r;
          int licznik = 0;
          int suma = 0;

          int k = i - l;
          int endK = i + r;

          //Zabezpieczenie przed wyjściem poza zakres
          while (k < 0) {
            k = i - (--l);
          }
          while (endK >= in.getWidth()) {
            endK = i + (--r);
          }

          //Wyznaczenie średniej lokalnej
          for (; k <= endK; k++) {
            int w = j - u;
            int endW = j + d;
            while (w < 0) {
              w = j - (--u);
            }
            while (endW >= in.getHeight()) {
              endW = j + (--d);
            }
            for (; w <= endW; w++) {
              suma += RGB.getR(in.getRGB(k, w));
              licznik++;
            }
          }
          int sredniaLokalna = (int) (suma / licznik);

          //Binaryzacja
          if (Math.abs(sredniaGlobalna - sredniaLokalna) > deviation) {
            if (RGB.getR(in.getRGB(i, j)) < sredniaGlobalna) {
              out.setRGB(i, j, RGB.toRGB(0, 0, 0));
            } else {
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
            }
          } else {
            if (RGB.getR(in.getRGB(i, j)) < sredniaLokalna) {
              out.setRGB(i, j, RGB.toRGB(0, 0, 0));
            } else {
              out.setRGB(i, j, RGB.toRGB(255, 255, 255));
            }
          }
        }
      }
    }

    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Progowanie globalne">
  /**
   * Metoda dokonuje progowania wartość globalną. Jako argument należy przekazać
   * BufferedImage w odcieniach szarości.
   *
   * @param in Obraz wejściowy
   * @param threshold Próg binaryzacji
   * @return Obraz wyjściowy
   * @throws IOException
   */
  public static BufferedImage byGlobalValue(BufferedImage in, int threshold) throws IOException {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    //Progowanie
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        if (RGB.getR(in.getRGB(i, j)) < threshold) {
          out.setRGB(i, j, RGB.toRGB(0, 0, 0));
        } else {
          out.setRGB(i, j, RGB.toRGB(255, 255, 255));
        }
      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Metoda Otsu">
  /**
   * Metoda znajduje próg binaryzacji metodą Otsu. Algorytm na podstawie
   * http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
   *
   * @param in Obraz wejściowy
   * @return Próg binaryzacji [0-255]
   */
  public static int metodaOtsu(BufferedImage in) {
    //Wyliczenie histogramu
    int grayscale = 256;
    int[][] histogram = new int[grayscale][2];
    for (int i = 0; i < grayscale; i++) {
      histogram[i][0] = 0;
    }
    for (int i = 0; i < in.getHeight(); i++) {
      for (int j = 0; j < in.getWidth(); j++) {
        histogram[RGB.getR(in.getRGB(j, i))][0]++;
      }
    }
    //Zsumowanie wartości histogramu
    histogram[0][1] = histogram[0][0];
    for (int i = 1; i < grayscale; i++) {
      histogram[i][1] = histogram[i - 1][1] + histogram[i][0];
    }
    //    for (int i = 0; i < grayscale; i++) {
    //      System.out.println(i + ": " + histogram[i][0] + "   " + histogram[i][1]);
    //    }
    //ilość pikseli w obrazie
    double iloscPikseli = in.getHeight() * in.getWidth();
    //    System.out.println("Ilość pikseli: " + iloscPikseli);
    double weightB, meanB, varianceB;
    double weightF, meanF, varianceF;
    double[] wcv = new double[grayscale];
    double licznik;
    int licznik2 = 0;
    for (int i = 0; i < grayscale; i++) {
      if (i == 0) {
        weightB = 0;
        varianceB = 0;
        weightF = 1;
        //obliczenie meanF
        licznik = 0;
        for (int j = i; j < grayscale; j++) {
          licznik += j * histogram[j][0];
        }
        meanF = licznik / (iloscPikseli);
        //obliczenie varianceF
        licznik = 0;
        for (int j = i; j < grayscale; j++) {
          licznik += Math.pow((j - meanF), 2) * histogram[j][0];
        }
        varianceF = licznik / (iloscPikseli);
      } else {
        //obliczenie weightB
        weightB = histogram[i - 1][1] / iloscPikseli;
        //obliczenie meanB
        licznik = 0;
        for (int j = 0; j < i; j++) {
          licznik += j * histogram[j][0];
        }
        meanB = licznik / (histogram[i - 1][1]);
        //obliczenie varianceB
        licznik = 0;
        for (int j = 0; j < i; j++) {
          licznik += Math.pow((j - meanB), 2) * histogram[j][0];
        }
        varianceB = licznik / (histogram[i - 1][1]);
        //obliczenie weightF
        weightF = (iloscPikseli - histogram[i - 1][1]) / iloscPikseli;
        //obliczenie meanF
        licznik = 0;
        for (int j = i; j < grayscale; j++) {
          licznik += j * histogram[j][0];
        }
        meanF = licznik / (iloscPikseli - histogram[i - 1][1]);
        //obliczenie varianceF
        licznik = 0;
        for (int j = i; j < grayscale; j++) {
          licznik += Math.pow((j - meanF), 2) * histogram[j][0];
        }
        varianceF = licznik / (iloscPikseli - histogram[i - 1][1]);
      }
      wcv[i] = weightB * varianceB + weightF * varianceF;
      //      if (histogram[i][0] != 0) {
      //        System.out.println(i);
      //        System.out.println("weightB: " + weightB);
      //        System.out.println("meanB: " + meanB);
      //        System.out.println("varianceB: " + varianceB);
      //        System.out.println("weightF: " + weightF);
      //        System.out.println("meanF: " + meanF);
      //        System.out.println("varianceF: " + varianceF);
      //        System.out.println("within class variance: " + wcv[i]);
      //        System.out.println();
      //      }
    }
    /*
     * Szukacz na pozycji [0] zwraca wartość minialną, a na [1] pozycję w
     * tablicy.
     */
    double[] szukacz = new double[2];
    szukacz[0] = wcv[0];
    szukacz[1] = 0;
    for (int i = 1; i < wcv.length; i++) {
      //      System.out.println("i: "+i+", wcv: "+wcv[i]);
      if (wcv[i] < szukacz[0]) {
        szukacz[0] = wcv[i];
        szukacz[1] = i;
      }
    }
    
    //    System.out.println("Szukacz[0]: "+szukacz[0]);
    //    System.out.println("Szukacz[1]: "+szukacz[1]);
    return (int) szukacz[1];
  }
  //</editor-fold>
}