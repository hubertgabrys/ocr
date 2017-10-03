package obrazy;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
//</editor-fold>

/**
 *
 * @author Hubert
 */
public class Extraction {

  //<editor-fold defaultstate="collapsed" desc="By Squares">
  /**
   * Metoda dokonuje ekstrakcji cech z liter. obraz skaluje się i dzieli na
   * prostokąty równej wielkości. Zlicza się ilość czarnych pikseli w każdym
   * prostokącie tworząc w ten sposób wektor cech charakterystycznych.
   *
   * @param in Obraz wejściowy
   * @return Wektor cech
   */
  public static int[] bySquares(BufferedImage in) {
    /*
     * PRZYGOTOWANIE LITERY DO EKSTRAKCJI CECH
     */
    BufferedImage outBF = prepToExtraction(in);
    /*
     * Obraz zostanie podzielony na kwadraty. W każdym kwadracie zostaną
     * zliczone czarne piksele. Ilość czarnych pikseli w danym kwadracie będzie
     * stanowić wektor cech. Pierwiastek z liczby kwadratów musi być liczbą
     * naturalną
     */
    //na ile kwadratów podzielić obraz?
    int liczbaKwadratow = 9;
    //konwersja obrazu do tablicy
    int[][] array = RGB.toArray(outBF);
    //znalezienie czarnych pikseli w każdym z kwadratów
    int sqWidth = (int) (array.length / Math.sqrt(liczbaKwadratow));
    int margines = (int) (array.length - (Math.sqrt(liczbaKwadratow)
            * sqWidth));
    
    /*
     * wymalowanie obrazka do sprawka.
     */
    //    liczbaKwadratow = 4;
    //    for (int i = 0; i < Math.sqrt(liczbaKwadratow); i++) {
    //      for (int j = 0; j < Math.sqrt(liczbaKwadratow); j++) {
    //        //System.out.println("KWADRAT: x: "+i+", y: "+j);
    //        for (int k = i*sqWidth; k < (i+1)*sqWidth+margines; k++) {
    //          for (int l = j*sqWidth; l < (j+1)*sqWidth+margines; l++) {
    //            if (array[k][l] == 1 && i == 0 && j == 0) {
    //              //System.out.println("Piksel: x: "+k+", y: "+l);
    //              array[k][l] = 2;
    //            }
    //            if (array[k][l] == 1 && i == 1 && j == 0) {
    //              //System.out.println("Piksel: x: "+k+", y: "+l);
    //              array[k][l] = 3;
    //            }
    //            if (array[k][l] == 1 && i == 0 && j == 1) {
    //              //System.out.println("Piksel: x: "+k+", y: "+l);
    //              array[k][l] = 4;
    //            }
    //          }
    //        }
    //        System.out.println();
    //      }
    //    }
    //    //konwersja tablicy do obrazu
    //    outBF = RGB.toBF(array);
    /*
     * Znalezienie wektora cech.
     */
    int licznik;
    int iteracja = 0;
    int[] wektor = new int[9];
    for (int i = 0; i < Math.sqrt(liczbaKwadratow); i++) {
      for (int j = 0; j < Math.sqrt(liczbaKwadratow); j++) {
        licznik = 0;
        for (int k = i * sqWidth; k < (i + 1) * sqWidth + margines; k++) {
          for (int l = j * sqWidth; l < (j + 1) * sqWidth + margines; l++) {
            if (array[k][l] == 1) {
              licznik++;
            }
          }
        }
        wektor[iteracja++] = licznik;
      }
    }
    
    return wektor;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="By diagonals">
  /**
   * Metoda dokonuje ekstrakcji cech z litery. obraz dzieli się za pomocą linii
   * pionowych, poziomych i ukośnych na równe części. Za punkty
   * charakterystyczne uznaje się przecięcia linii z obrazem.
   *
   * @param in Obraz wejściowy
   * @return Wektor cech
   */
  public static int[][] byDiagonals(BufferedImage in) {
    /*
     * PRZYGOTOWANIE LITERY DO EKSTRAKCJI CECH
     */
    BufferedImage out = prepToExtraction(in);
    /*
     * obraz dzieli się za pomocą linii pionowych, poziomych lub ukośnych na
     * równe części. Za punkty charakterystyczne uznaje się przecięcia linii z
     * obrazem.
     */
    //konwersja obrazu do tablicy. 0 - biały, 1 - czarny
    int[][] array = RGB.toArray(out);
    /*
     * Maską będzie tablica o rozmiarach obrazu wejściowego. Osie symetrii
     * oznaczone będą dwójkami. Pozostałe komórki będą miały wartość zero.
     * Miejsca przecięcia osi z obrazem (punkty charakterystyczne) będą miały
     * wartość 3.
     */
    int[][] maska = new int[array.length][array[0].length];
    for (int i = 0; i < maska.length; i++) {
      for (int j = 0; j < maska[0].length; j++) {
        if (i == maska.length / 2 || j == maska[0].length / 2
                || j == i * (maska[0].length - 1) / (maska.length - 1)
                || j == i * (1 - maska[0].length) / (maska.length - 1)
                + maska[0].length - 1) {
          maska[i][j] = 2;
        } else {
          maska[i][j] = 0;
        }
      }
    }
    
    //dodaję maskę i obraz
    for (int i = 0; i < maska.length; i++) {
      for (int j = 0; j < maska[0].length; j++) {
        array[i][j] += maska[i][j];
      }
    }
    
    //redukcja ilości punktów charakterystycznych
    array = reduction(array, 3);
    
    Deque<Integer> listChar = new LinkedList<Integer>();
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array[0].length; j++) {
        if (array[i][j] == 3) {
          listChar.add(i);
          listChar.add(j);
        }
      }
    }
    
    /*
     * Wektor cech wyeksportuję w formacie. Trzeba wykomentować, jeśli chce się
     * eksporotwać obrazek
     */
    
    int[][] wektor = new int[listChar.size() / 2][2];
    int i = 0;
    while (listChar.size() != 0) {
      wektor[i][0] = listChar.remove();
      wektor[i][1] = listChar.remove();
      i++;
    }
    
    //    /*
    //     * Tym sobie zrobię obrazki do sprawka
    //     */
    //    System.out.println("PUNKTY PRZECIĘCIA:");
    //    while (listChar.size() != 0)
    //      System.out.println("x: "+listChar.remove()+", y: "+listChar.remove());
    //    System.out.println();
    //
    //    //Przepisujemy tablicę na obraz
    //    for (int i = 0; i < array.length; i++) {
    //      for (int j = 0; j < array[0].length; j++) {
    //        if (array[i][j] == 0)
    //          out.setRGB(i, j, RGB.toRGB(255, 255, 255));
    //        if (array[i][j] == 1)
    //          out.setRGB(i, j, RGB.toRGB(0, 0, 0));
    //        if (array[i][j] == 2)
    //          out.setRGB(i, j, RGB.toRGB(255, 0, 0));
    //        if (array[i][j] == 3)
    //          out.setRGB(i, j, RGB.toRGB(0, 255, 0));
    //        if (array[i][j] == 4)
    //          out.setRGB(i, j, RGB.toRGB(0, 0, 255));
    //      }
    //    }
    return wektor;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Preparation to extraction">
  /**
   * Przygotowanie litery do ekstrakcji cech.
   * @param in Obraz wejściowy.
   * @return Obraz wyjściowy.
   */
  private static BufferedImage prepToExtraction(BufferedImage in) {
    //obraz wyjściowy
    BufferedImage out;
    //długość krawędzi obrazu wyjściowego w px
    int size;
    //Ścieniam literę
    out = Morphology.thinningByKMM(in);
    //to zatrzyma operacje przygotowujące literę
    int[] stop = {0, 0, 0, 0};
    //ilość iteracji potrzebnych do przygotowania litery
    int licznik = 0;
    do {
      licznik++;
      //Dopasowanie litery
      out = fit(out);
      //Skalowanie litery
      size = 53;
      out = scale(out, size);
      //Dylatacja
      out = Morphology.dilation(out);
      //Dylatacja
      out = Morphology.dilation(out);
      //Ścienianie
      out = Morphology.thinningByKMM(out);
      //Obcinam białe brzegi
      out = Morphology.crop(out, 1);
      //Sprawdzam czy na każdej krawędzi jest przynajmniej jeden czarny piksel
      int[] black = {0, 0, 0, 0};
      for (int i = 0; i < out.getWidth(); i++) {
        if (Arrays.equals(RGB.getArray(out.getRGB(i, 0)), black)) {
          stop[0] = 1;
        }
        if (Arrays.equals(RGB.getArray(out.getRGB(i, out.getHeight() - 1)),
                black)) {
          stop[1] = 1;
        }
      }
      for (int i = 0; i < out.getHeight(); i++) {
        if (Arrays.equals(RGB.getArray(out.getRGB(0, i)), black)) {
          stop[2] = 1;
        }
        if (Arrays.equals(RGB.getArray(out.getRGB(out.getWidth() - 1, i)),
                black)) {
          stop[3] = 1;
        }
      }
    } while (((stop[0] + stop[1] + stop[2] + stop[3]) != 4) && licznik < 5);
    
    //    System.out.println("Ilość iteracji przygotowujących literę do ekstrakcji:"
    //            + " " + licznik);
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="By number of neighbours">
  /**
   * Metoda do ekstrakcji cech z liter. za punkty charakterystyczne uznaje się
   * te, które mają mniej lub więcej niż 2 sąsiadów oraz skrajne punkty litery.
   * Na poniższym obrazku na zielono zaznaczono punkty spełniające warunek
   * sąsiedztwa, a na czerwono punkty spełniające warunek skrajności.
   *
   * @param in obraz wejściowy
   * @return wektor cech
   */
  public static int[][][] byNrOfNeighbours(BufferedImage in) {
    /*
     * PRZYGOTOWANIE LITERY DO EKSTRAKCJI CECH
     */
    BufferedImage out = prepToExtraction(in);
    
    /*
     * Białe piksele oznaczam jako 0, czarne jako 1, skrajne jako 2, mające
     * mniej lub więcej niż dwóch sąsiadów jako 3. Najpierw ekstrakcja skrajnych
     * pikseli.
     */
    
    Deque<Integer> lista = new LinkedList<Integer>();
    //konwersja obrazu do tablicy
    int[][] arrayRed = RGB.toArray(out);
    int[][] arrayGreen = RGB.toArray(out);
    
    /*
     * ZNALEZIENIE ZIELONYCH
     */
    //Znajduję współrzędne pikseli mających mniej lub więcej niż 2 sąsiadów
    arrayGreen = Morphology.powiekszBialymi(arrayGreen, 1);
    for (int i = 1; i < arrayGreen.length - 1; i++) {
      for (int j = 1; j < arrayGreen[0].length; j++) {
        if (arrayGreen[i][j] == 1 && (arrayGreen[i - 1][j - 1]
                + arrayGreen[i - 1][j] + arrayGreen[i - 1][j + 1]
                + arrayGreen[i][j - 1]
                + arrayGreen[i][j + 1] + arrayGreen[i + 1][j - 1]
                + arrayGreen[i + 1][j]
                + arrayGreen[i + 1][j + 1] != 2)) {
          lista.add(i);
          lista.add(j);
        }
      }
    }
    //Piksele z listy oznaczam jako 3
    while (lista.size() != 0) {
      arrayGreen[lista.removeFirst()][lista.removeFirst()] = 3;
    }
    arrayGreen = Morphology.crop(arrayGreen, 1);
    
    /*
     * ZNALEZIENIE CZERWONYCH
     */
    
    //Skrajne piksele oznaczam jako 2
    for (int i = 0; i < arrayRed.length; i++) {
      for (int j = 0; j < arrayRed[0].length; j++) {
        if (arrayRed[i][j] == 1 && (i == 0 || i == arrayRed.length - 1 || j == 0
                || j == arrayRed[0].length - 1)) {
          arrayRed[i][j] = 2;
        }
      }
    }
    
    /*
     * jeśli dwa punkty leżą w bardzo bliskiej odległości - skleja się je ze
     * sobą uśredniając ich współrzędne. Korzystam z metody reduction.
     */
    arrayGreen = reduction(arrayGreen, 3);
    arrayRed = reduction(arrayRed, 2);
    
    Deque<Integer> listRed = new LinkedList<Integer>();
    for (int i = 0; i < arrayRed.length; i++) {
      for (int j = 0; j < arrayRed[0].length; j++) {
        if (arrayRed[i][j] == 2) {
          listRed.add(i);
          listRed.add(j);
        }
      }
    }
    Deque<Integer> listGreen = new LinkedList<Integer>();
    for (int i = 0; i < arrayGreen.length; i++) {
      for (int j = 0; j < arrayGreen[0].length; j++) {
        if (arrayGreen[i][j] == 3) {
          listGreen.add(i);
          listGreen.add(j);
        }
      }
    }
    
    /*
     * Wektor cech
     */
    int[][] vectorGreen = new int[listGreen.size() / 2][2];
    int i = 0;
    while (listGreen.size() != 0) {
      vectorGreen[i][0] = listGreen.remove();
      vectorGreen[i][1] = listGreen.remove();
      i++;
    }
    int[][] vectorRed = new int[listRed.size() / 2][2];
    int j = 0;
    while (listRed.size() != 0) {
      vectorRed[j][0] = listRed.remove();
      vectorRed[j][1] = listRed.remove();
      j++;
    }
    int[][][] vector = new int[2][][];
    vector[0] = vectorRed;
    vector[1] = vectorGreen;
    
    //    /*
    //     * Eksport obrazka
    //     */
    //    //tym sobie wybiorę co się wyeksportuje jako obrazek
    //    //array = arrayGreen;
    //    array = arrayRed;
    //
    //    //Przepisujemy tablicę na obraz
    //    for (int i = 0; i < array.length; i++) {
    //      for (int j = 0; j < array[0].length; j++) {
    //        if (array[i][j] == 0)
    //          out.setRGB(i, j, RGB.toRGB(255, 255, 255));
    //        if (array[i][j] == 1)
    //          out.setRGB(i, j, RGB.toRGB(0, 0, 0));
    //        if (array[i][j] == 2)
    //          out.setRGB(i, j, RGB.toRGB(255, 0, 0));
    //        if (array[i][j] == 3)
    //          out.setRGB(i, j, RGB.toRGB(0, 255, 0));
    //        if (array[i][j] == 4)
    //          out.setRGB(i, j, RGB.toRGB(0, 0, 255));
    //      }
    //    }
    //
    //    System.out.println("SKRAJNE PIKSELE:");
    //    while (listRed.size() != 0)
    //      System.out.println("x: "+listRed.remove()+", y: "+listRed.remove());
    //
    //    System.out.println("MNIEJ LUB WIĘCEJ NIŻ DWÓCH SĄSIADÓW:");
    //    while (listGreen.size() != 0)
    //      System.out.println("x: "+listGreen.remove()+", y: "+listGreen.remove());
    //    System.out.println();
    
    return vector;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Skaluj do kwadratu.">
  /**
   * Metoda skaluje obrazek do kształtu kwadratowego
   *
   * @param in Obraz wejściowy
   * @param size Długość krawędzi w px
   * @return Obraz przeskalowany
   */
  private static BufferedImage scale(BufferedImage in, int size) {
    //Obraz wejśćiowy zamieniam na tablicę 0 i 1
    int width = in.getWidth();
    int height = in.getHeight();
    int[][] arrayIn = RGB.toArray(in);
    int[][] arrayOut = new int[size][size];
    //Obraz wyjśćiowy zamieniam na tablicę 0
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        arrayOut[i][j] = 0;
      }
    }
    int iOut;
    int jOut;
    for (int i = 0; i < height; i++) {
      iOut = (int) (i * ((double) (size - 1)) / ((double) (height - 1)));
      for (int j = 0; j < width; j++) {
        jOut = (int) (j * ((double) (size - 1)) / ((double) (width - 1)));
        if (arrayOut[jOut][iOut] != 1) {
          arrayOut[jOut][iOut] = arrayIn[j][i];
        }
      }
    }
    
    BufferedImage out = new BufferedImage(size, size, in.getType());
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (arrayOut[i][j] == 0) {
          out.setRGB(i, j, RGB.toRGB(255, 255, 255));
        } else if (arrayOut[i][j] == 1) {
          out.setRGB(i, j, RGB.toRGB(0, 0, 0));
        } else if (arrayOut[i][j] == 2) {
          out.setRGB(i, j, RGB.toRGB(0, 0, 255));
        }
      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Obcięcie białych przestrzeni wokół litery">
  /**
   * Metoda obcina białe przestrzenie dookoła litery
   *
   * @param in Obraz wejściowy
   * @return Obraz wyjściowy
   */
  private static BufferedImage fit(BufferedImage in) {
    int width = in.getWidth();
    int height = in.getHeight();
    int left = width - 1;
    int top = height - 1;
    int right = 0;
    int bottom = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (RGB.getR(in.getRGB(i, j)) == 0) {
          if (i < left) {
            left = i;
          }
          if (j < top) {
            top = j;
          }
          if (i > right) {
            right = i;
          }
          if (j > bottom) {
            bottom = j;
          }
        }
      }
    }
    width = right - left + 1;
    height = bottom - top + 1;
    BufferedImage out = new BufferedImage(width, height, in.getType());
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        //malujemy całą literę na biało
        out.setRGB(i, j, RGB.toRGB(255, 255, 255));
        if (RGB.getR(in.getRGB(left + i, top + j)) == 0) {
          out.setRGB(i, j, RGB.toRGB(0, 0, 0));
        }
      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Redukcja ilości punktów w metodzie sąsiadów.">
  /**
   * jeśli dwa punkty leżą w bardzo bliskiej odległości - skleja się je ze sobą
   * uśredniając ich współrzędne.
   *
   * @param in tablica wejściowa
   * @param liczba liczba do redukcji
   * @return tablica wyjściowa
   */
  private static int[][] reduction(int[][] in, int liczba) {
    
    /*
     * Promien decyduje o obszarze z jakiego będą redukowane piksele. Np.
     * promien = 3 oznacza, że piksele z kwadratu o boku 7 (2*3+1) będą
     * przybliżone jednym pikselem
     */
    int promien = 3;
    //Robimy obramówkę z białych pikseli
    int[][] out = Morphology.powiekszBialymi(in, promien);
    Deque<int[]> listaBuforowa = new LinkedList<int[]>();
    //pierwsza współrzędna pozioma, druga pionowa
    int[] coord;
    boolean sthChanged = true;
    while (sthChanged) {
      sthChanged = false;
      for (int i = promien; i < out.length - promien; i++) {
        for (int j = promien; j < out[0].length - promien; j++) {
          if (out[i][j] == liczba) {
            //znal.  piksel, więc zostanie wykonana kolejna iteracja
            sthChanged = true;
            //piksele ozanczam jako 13
            out[i][j] = 13;
            //sąsiadów znalezionego piksela dodaję do listy buforowej
            for (int l = j - promien; l <= j + promien; l++) {
              for (int k = i - promien; k <= i + promien; k++) {
                if (out[k][l] == liczba) {
                  //Znaleziony piksel oznaczam jako 13
                  out[k][l] = 13;
                  //wrzucam współrzędne sąsiadów do listy buforowej
                  coord = new int[2];
                  coord[0] = k;
                  coord[1] = l;
                  listaBuforowa.add(coord);
                }
              }
            }
            /*
             * wychodzę z podwójnej pętli 'for' szukającej czarnych pikseli w
             * obrazie
             */
            i = out.length;
            j = out[0].length;
          }
        }
      }
      if (listaBuforowa.size() != 0) {
        /*
         * Przeszukujemy dalej otoczenie danego piksela.
         */
        while (listaBuforowa.size() != 0) {
          int i = listaBuforowa.getFirst()[0];
          int j = listaBuforowa.getFirst()[1];
          //sąsiadów piksela dodaje się do listy buforowej.
          for (int l = j - promien; l <= j + promien; l++) {
            for (int k = i - promien; k <= i + promien; k++) {
              if (out[k][l] == liczba) {
                //Znaleziony piksel oznaczam jako 4
                out[k][l] = 13;
                //wrzucam współrzędne czarnch sąsiadów do listy buforowej
                coord = new int[2];
                coord[0] = k;
                coord[1] = l;
                listaBuforowa.add(coord);
              }
            }
          }
          //Usuwam piksel z listy buforowej
          listaBuforowa.removeFirst();
        }
      }
      int wspPozioma = 0;
      int wspPionowa = 0;
      int mianownik = 0;
      for (int i = promien; i < out.length - promien; i++) {
        for (int j = promien; j < out[i].length - promien; j++) {
          if (out[i][j] == 13) {
            wspPionowa += i;
            wspPozioma += j;
            mianownik++;
            out[i][j] = 1;
          }
        }
      }
      if (mianownik > 0) {
        out[(int) Math.round((double) wspPionowa
                / mianownik)][(int) Math.round((double) wspPozioma
                / mianownik)] = 26;
      }
    }
    
    for (int i = promien; i < out.length - promien; i++) {
      for (int j = promien; j < out[0].length - promien; j++) {
        if (out[i][j] == 26) {
          out[i][j] = liczba;
        }
      }
    }
    return Morphology.crop(out, promien);
  }
  //</editor-fold>
}