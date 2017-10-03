package obrazy;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
//</editor-fold>

/**
 *
 * @author Hubert
 */
public class Segmentation {

  //<editor-fold defaultstate="collapsed" desc="metoda2">
  /**
   * Metoda dokonuje segmentacji liter z tekstu.
   *
   * @param in Obraz wejściowy
   * @return Lista przechowująca litery w formacie BufferedImage
   */
  public static List metoda2(BufferedImage in) {
    /*
     * Czarnych sąsiadów znalezionego piksela dodaje się do tak zwanej listy
     * buforowej. W taki sam sposób analizuje się kolejne piksele z listy
     * buforowej, aż zostanie ona opróżniona. Podczas analizy należy zapamiętać
     * współrzędne skrajnych czarnych pikseli analizowanego obszaru. Gdy lista
     * buforowa będzie pusta, należy skopiować piksele 3 z analizowanego obszaru
     * do nowego obrazka, w ten sposób wyodrębniając literę. Podczas powyższego
     * kroku 3 i białe piksele są zamieniane na 2. Analiza dokonywana jest w
     * powyższy sposób, aż na analizowanym obrazie będą tylko piksele 2.
     */
    List<BufferedImage> out = new LinkedList<BufferedImage>();
    int width = in.getWidth();
    int height = in.getHeight();
    int[] black = {0, 0, 0, 0};
    int[] white = {0, 255, 255, 255};
    //Przepisujemy obraz do tablicy. Białe piksele zamieniamy na 0, czarne na 1
    int[][] array = new int[width][height];
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
    /*
     * Obraz analizowany jest od góry wierszami, do momentu, w którym znaleziony
     * zostaje pierwszy czarny piksel. Białe piksele podczas tej analizy
     * oznaczane są jako 2. Znaleziony czarny piksel oznacza się jako 3.
     */
    Deque<int[]> listaBuforowa = new LinkedList<int[]>();
    //pierwsza współrzędna pozioma, druga pionowa
    int[] coordinates;
    //Wierzchołki: 0 - lewy, 1 - górny, 2 - prawy, 3 - dolny
    int[] vertices = new int[4];
    boolean sthChanged = true;
    while (sthChanged) {
      sthChanged = false;
      width = in.getWidth();
      height = in.getHeight();
      vertices[0] = width - 1;
      vertices[1] = height - 1;
      vertices[2] = 0;
      vertices[3] = 0;
      for (int j = 0; j < height; j++) {
        for (int i = 0; i < width; i++) {
          if (array[i][j] == 0) {
            //białe piksele oznaczam jako 2
            array[i][j] = 2;
          }
          if (array[i][j] == 1) {
            //znaleziono czarny piksel, więc zostanie wykonana kolejna iteracja
            sthChanged = true;
            //czarne piksele ozanczam jako 3
            array[i][j] = 3;
            //Czarnych sąsiadów znalezionego piksela dodaję do listy buforowej
            for (int l = j - 1; l <= j + 1; l++) {
              for (int k = i - 1; k <= i + 1; k++) {
                if (array[k][l] == 1) {
                  //Znaleziony czarny piksel oznaczam jako 3
                  array[k][l] = 3;
                  //wrzucam współrzędne czarnch sąsiadów do listy buforowej
                  coordinates = new int[2];
                  coordinates[0] = k;
                  coordinates[1] = l;
                  listaBuforowa.add(coordinates);
                  //sprawdzam czy współrzędne wierzchołka są ekstremalne
                  if (k < vertices[0]) {
                    vertices[0] = k;
                  }
                  if (l < vertices[1]) {
                    vertices[1] = l;
                  }
                  if (k > vertices[2]) {
                    vertices[2] = k;
                  }
                  if (l > vertices[3]) {
                    vertices[3] = l;
                  }
                }
              }
            }
            /*
             * wychodzę z podwójnej pętli 'for' szukającej czarnych pikseli w
             * obrazie
             */
            j = height;
            i = width;
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
          //Czarnych sąsiadów piksela dodaje się do listy buforowej.
          for (int l = j - 1; l <= j + 1; l++) {
            for (int k = i - 1; k <= i + 1; k++) {
              if (array[k][l] == 1) {
                //Znaleziony czarny piksel oznaczam jako 3
                array[k][l] = 3;
                //wrzucam współrzędne czarnch sąsiadów do listy buforowej
                coordinates = new int[2];
                coordinates[0] = k;
                coordinates[1] = l;
                listaBuforowa.add(coordinates);
                //sprawdzam czy współrzędne wierzchołka są ekstremalne
                if (k < vertices[0]) {
                  vertices[0] = k;
                }
                if (l < vertices[1]) {
                  vertices[1] = l;
                }
                if (k > vertices[2]) {
                  vertices[2] = k;
                }
                if (l > vertices[3]) {
                  vertices[3] = l;
                }
              }
            }
          }
          //Usuwam piksel z listy buforowej
          listaBuforowa.removeFirst();
        }
        /*
         * Teraz cała jedna litera powinna być oznaczona trójkami. Zapisuję ją
         * do obiektu typu BufferedImage, a następnie zamieniam jej piksele na
         * dwójki.
         */
        width = vertices[2] - vertices[0] + 1;
        height = vertices[3] - vertices[1] + 1;
        BufferedImage litera = new BufferedImage(width, height, in.getType());
        for (int i = 0; i < width; i++) {
          for (int j = 0; j < height; j++) {
            //malujemy całą literę na biało
            litera.setRGB(i, j, RGB.toRGB(255, 255, 255));
            if (array[vertices[0] + i][vertices[1] + j] == 3) {
              //trójki zamieniamy na czarne piksele
              litera.setRGB(i, j, RGB.toRGB(0, 0, 0));
              //trójki zamieniamy na dwójki
              array[vertices[0] + i][vertices[1] + j] = 2;
            }
          }
        }
        out.add(litera);
      }
    }
    System.out.println("Ile liter? " + out.size());
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="metoda1">
  /**
   * Metoda dokonuje wyodrębnienia liter z tekstu jako osobnych obrazów.
   *
   * @param in Obraz wejściowy
   * @return Tablica 2D przechowująca wyodrębnione litery (jako obrazy)
   */
  public static BufferedImage[][] metoda1(BufferedImage in) {
    BufferedImage[][] out = wyodrębnienieLiter(wyodrębnienieWierszy(in));
    
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Wyodrębnienie liter">
  /**
   * Metoda wyodrębnia litery z wierszy
   *
   * @param in Tablica przechowująca wiersze
   * @return Tablica dwuwymiarowa przechowująca litery
   */
  private static BufferedImage[][] wyodrębnienieLiter(BufferedImage[] in) {
    BufferedImage[][] out = new BufferedImage[in.length][];
    for (int wiersz = 0; wiersz < in.length; wiersz++) {
      //tym policzymy sobie liczbę kolumn w danej literze
      int liczbaKolumn = 1;
      //wczytamy nr kolumny od której zaczyna się litera
      int kolumnaPierwsza = 0;
      //posłuży do ustalenia rozmiaru tablicy przechowującej litery
      int liczbaLiter = 0;
      int black = 0;
      //szerokość wejściowego wiersza
      int width = in[wiersz].getWidth();
      //wysokość wejściowego wiersza
      int height = in[wiersz].getHeight();
      //tablica przechowująca litery
      BufferedImage[] litery = new BufferedImage[width];
      //zakładam, że poprzednia kolumna nie zawierała czarnych pikseli
      boolean isBlack = false;
      for (int j = 0; j < width; j++) {
        for (int i = 0; i < height; i++) {
          //jeśli poprzednia kolumna nie zawierała czarnych pikseli
          if (!isBlack) {
            //jeśli w danej kolumnie natrafimy na czarny piksel
            if (RGB.getR(in[wiersz].getRGB(j, i)) == black) {
              isBlack = true;
              liczbaKolumn++;
              kolumnaPierwsza = j;
              liczbaLiter++;
              //przeskakujemy do kolejnej kolumny
              j++;
              i = 0;
            }
          }
          //jeśli poprzednia kolumna zawierała czarne piksele
          if (isBlack) {
            //jeśli w danej kolumnie natrafimy na czarny piksel
            if (RGB.getR(in[wiersz].getRGB(j, i)) == black) {
              isBlack = true;
              liczbaKolumn++;
              //przeskakujemy do kolejnej kolumny
              j++;
              i = 0;
            }
          }
        }
        /*
         * jeśli w poprzedniej kolumnie był czarny piksel, a w bieżącej nie
         * został taki znaleziony
         */
        if (isBlack) {
          isBlack = false;
          litery[liczbaLiter - 1] = new BufferedImage(liczbaKolumn,
                  height, in[wiersz].getType());
          //przepisujemy wartości z obrazu wejściowego do litery
          for (int kolumna = 0; kolumna < liczbaKolumn; kolumna++) {
            for (int linia = 0; linia < height; linia++) {
              litery[liczbaLiter - 1].setRGB(kolumna, linia,
                      in[wiersz].getRGB(kolumnaPierwsza + kolumna, linia));
            }
          }
          //zerujemy liczbę kolumn w literze
          liczbaKolumn = 0;
        }
      }
      //przepisanie na nową tablicę
      BufferedImage[] litery2 = new BufferedImage[liczbaLiter];
      System.arraycopy(litery, 0, litery2, 0, liczbaLiter);
      out[wiersz] = litery2;
      //wycięcie białych przestrzeni nad i pod literami i dookoła
      for (int k = 0; k < out[wiersz].length; k++) {
        BufferedImage in2 = litery2[k];
        //szerokość wejściowej litery
        width = in2.getWidth();
        //wysokość wejściowej litery
        height = in2.getHeight();
        int left = width - 1;
        int top = height - 1;
        int right = 0;
        int bottom = 0;
        for (int m = 0; m < width; m++) {
          for (int n = 0; n < height; n++) {
            if (RGB.getR(in2.getRGB(m, n)) == 0) {
              //Znaleziono czarny piksel
              //sprawdzam czy współrzędne wierzchołka są ekstremalne
              if (m < left) {
                left = m;
              }
              if (n < top) {
                top = n;
              }
              if (m > right) {
                right = m;
              }
              if (n > bottom) {
                bottom = n;
              }
            }
          }
        }
        width = right - left + 1;
        height = bottom - top + 1;
        BufferedImage litera = new BufferedImage(width, height, in2.getType());
        for (int i = 0; i < width; i++) {
          for (int j = 0; j < height; j++) {
            //malujemy całą literę na biało
            litera.setRGB(i, j, RGB.toRGB(255, 255, 255));
            if (RGB.getR(in2.getRGB(left + i, top + j)) == 0) {
              litera.setRGB(i, j, RGB.toRGB(0, 0, 0));
            }
          }
        }
        out[wiersz][k] = litera;
      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Wyodrębnienie wierszy">
  /**
   * Metoda wyodrębnia wiersze z obrazu wejściowego
   *
   * @param in Obraz wejściowy
   * @return Tablica przechowująca wiersze
   */
  private static BufferedImage[] wyodrębnienieWierszy(BufferedImage in) {
    //tym policzymy sobie liczbę linii w danym wierszu
    int liczbaLinii = 1;
    //wczytamy nr linii od którego zaczyna się wiersz
    int liniaPierwsza = 0;
    //posłuży do ustalenia rozmiaru tablicy przechowującej wiersze
    int liczbaWierszy = 0;
    int black = 0;
    int width = in.getWidth();
    int height = in.getHeight();
    //tablica przechowująca wiersze
    BufferedImage[] wiersze = new BufferedImage[height];
    //zakładam, że poprzednia linia nie zawierała czarnych pikseli
    boolean isBlack = false;
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
        //jeśli poprzednia linia nie zawierała czarnych pikseli
        if (!isBlack) {
          //jeśli w danej linii natrafimy na czarny piksel
          if (RGB.getR(in.getRGB(i, j)) == black) {
            isBlack = true;
            liczbaLinii++;
            liniaPierwsza = j;
            liczbaWierszy++;
            //przeskakujemy do kolejnej linii
            j++;
            i = 0;
          }
        }
        //jeśli poprzednia linia zawierała czarne piksele
        if (isBlack) {
          //jeśli w danej linii natrafimy na czarny piksel
          if (RGB.getR(in.getRGB(i, j)) == black) {
            isBlack = true;
            liczbaLinii++;
            //przeskakujemy do kolejnej linii
            j++;
            i = 0;
          }
        }
      }
      /*
       * jeśli w poprzedniej linii był czarny piksel, a w bieżącej nie został
       * taki znaleziony
       */
      //
      if (isBlack) {
        isBlack = false;
        wiersze[liczbaWierszy - 1] = new BufferedImage(width,
                liczbaLinii, in.getType());
        //przepisujemy wartości z obrazu wejściowego do wiersza
        for (int k = 0; k < liczbaLinii; k++) {
          for (int l = 0; l < width; l++) {
            wiersze[liczbaWierszy - 1].setRGB(l, k, in.getRGB(l,
                    liniaPierwsza + k));
          }
        }
        //zerujemy liczbę linii w wierszu
        liczbaLinii = 0;
      }
    }
    //przepisanie na nową tablicę
    BufferedImage[] wiersze2 = new BufferedImage[liczbaWierszy];
    System.arraycopy(wiersze, 0, wiersze2, 0, liczbaWierszy);
    return wiersze2;
  }
  //</editor-fold>
}