package obrazy;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author Hubert
 */
public class Noise {

  //<editor-fold defaultstate="collapsed" desc="Zaszumienie sól i pieprz">
  /**
   * Metoda zaszumia obraz szumem typu sól i pieprz.
   * @param in Obraz do zaszumienia.
   * @param procent Procent zaszumienia obrazu.
   * @return Zaszumiony obraz.
   */
  public static BufferedImage solIpieprz(BufferedImage in, int procent) {
    int width = in.getWidth();
    int height = in.getHeight();
    int czarny = 0x00000000;
    int bialy = 0x00ffffff;
    int szum;
    
    BufferedImage out = new BufferedImage(width, height, in.getType());
    
    Random generator = new Random();
    Random generator2 = new Random();
    
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (generator.nextInt(100) < procent) {
          if (generator2.nextBoolean())
            szum = czarny;
          else
            szum = bialy;
          out.setRGB(i, j, szum);
        }
        else
          out.setRGB(i,j,in.getRGB(i,j));
      }
    }
    return out;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Zaszumienie równomierne (to samo na każdy kanał)">
  /**
   * Metoda zaszumia obraz szumem równomiernym. Dla danego piksela każdy kanał
   * jest zmieniany o tą samą wartość z sumy przedziałów <-30,-21> i <21,30>.
   * @param in Obraz do zaszumienia.
   * @param procent Procent zaszumienia obrazu.
   * @return Zaszumiony obraz.
   */
  public static BufferedImage rownomiernyT(BufferedImage in, int procent) {
    int width = in.getWidth();
    int height = in.getHeight();
    int war, r, g, b;
    
    BufferedImage out = new BufferedImage(width, height, in.getType());
    
    Random generator = new Random();
    Random generator2 = new Random();
    
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (generator.nextInt(100) < procent) {
          war = generator2.nextInt(10)+21;
          if (generator.nextBoolean())
            war = -war;
          r = RGB.getR(in.getRGB(i,j)) + war;
          g = RGB.getG(in.getRGB(i,j)) + war;
          b = RGB.getB(in.getRGB(i,j)) + war;
          r = RGB.limitsR(r);
          g = RGB.limitsG(g);
          b = RGB.limitsB(b);
          out.setRGB(i,j,RGB.toRGB(r, g, b));
        }
        else
          out.setRGB(i,j,in.getRGB(i,j));
      }
    }
    return out;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Zaszumienie równomierne">
  /**
   * Metoda zaszumia obraz szumem równomiernym. Dla danego piksela każdy kanał
   * jest zmieniany o losową wartość z sumy przedziałów <-30,-21> i <21,30>.
   * @param in Obraz do zaszumienia.
   * @param procent Procent zaszumienia obrazu.
   * @return Zaszumiony obraz.
   */
  public static BufferedImage rownomiernyN(BufferedImage in, int procent) {
    int width = in.getWidth();
    int height = in.getHeight();
    int warR, warG, warB, r, g, b;
    
    BufferedImage out = new BufferedImage(width, height, in.getType());
    
    Random generator = new Random();
    Random generator2 = new Random();
    
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (generator.nextInt(100) < procent) {
          warR = generator2.nextInt(10)+21;
          warG = generator2.nextInt(10)+21;
          warB = generator2.nextInt(10)+21;
          if (generator.nextBoolean()) {
            warR = -warR;
            warG = -warG;
            warB = -warB;
          }
          r = RGB.getR(in.getRGB(i,j)) + warR;
          g = RGB.getG(in.getRGB(i,j)) + warG;
          b = RGB.getB(in.getRGB(i,j)) + warB;
          r = RGB.limitsR(r);
          g = RGB.limitsG(g);
          b = RGB.limitsB(b);
          out.setRGB(i,j,RGB.toRGB(r, g, b));
        }
        else
          out.setRGB(i,j,in.getRGB(i,j));
      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Odszumienie przez średnią">
  /**
   * Metoda dokonuje odszumiania obrazu obliczając nową wartość piksela na
   * podstawie wartości średniej otoczenia piksela oddzielnie dla każdego
   * kanału.
   * @param in Obraz wejściowy.
   * @param radius radius maski jaką wyliczana jest średnia lokalna.
   * @return Obraz odszumiony.
   */
  public static BufferedImage odszumianieS(BufferedImage in,int radius) {
    int width = in.getWidth();
    int height = in.getHeight();
    
    BufferedImage out = new BufferedImage(width, height, in.getType());
    
    //Ustalenie maski.
    int [][] mask = new int [2*radius+1][2*radius+1];
    for (int x = 0; x < 2*radius+1; x++) {
      for (int y = 0; y < 2*radius+1; y++) {
        mask[x][y] = 1;
      }
    }
    
    //Odszumienie.
    int [][][] pixelSurr;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        //Pobranie wartości otoczenia piksela.
        pixelSurr = Filter.getPixelSurr((2*radius+1), (2*radius+1), in, i, j);
        //Konwolucja otoczenia piksela z maską i wyznaczenie wartości piksela.
        //out.setRGB(i, j, Filter.conv(pixelSurr, mask)); DO POPRAWY!
      }
    }
    return out;
  }
  //</editor-fold>
  
  //<editor-fold defaultstate="collapsed" desc="Odszumienie filtrem medianowym">
  /**
   * Metoda dokonuje odszumiana obrazu filtrem medianowym.
   * @param in Obraz wejściowy.
   * @param radius Promień otoczenia piksela.
   * @return Obraz wynikowy.
   */
  public static BufferedImage odszumianieM(BufferedImage in,int radius) {
    BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
    for (int i = 0; i < in.getWidth(); i++) {
      for (int j = 0; j < in.getHeight(); j++) {
        out.setRGB(i, j, Filter.median(Filter.getPixelSurr((2*radius+1), (2*radius+1), in, i, j)));
      }
    }
    return out;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="Odszumienie filtrem medianowym ulepszonym">
  /**
   * Odszumianie przez medianę ulepszoną.
   * @param in Obraz wejściowy.
   * @param radius Promień otoczenia piksela.
   * @param percent Procent skrajnych wartości dla jakich wyznaczana będzie
   * mediana.
   * @return Obraz wynikowy
   */
  public static BufferedImage odszumianieMU(BufferedImage in,int radius,
          int percent) {
    int width = in.getWidth();
    int height = in.getHeight();
    
    BufferedImage out = new BufferedImage(width, height, in.getType());
    
    //Odszumienie.
    int pixelValue;
    int [][][] pixelSurr;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        //Pobranie wartości piksela.
        pixelValue = in.getRGB(i,j);
        //Pobranie wartości otoczenia piksela.
        pixelSurr = Filter.getPixelSurr((2*radius+1), (2*radius+1), in, i, j);
        //Wyznaczenie wartości piksela.
        //out.setRGB(i, j, Filter.median(pixelValue, pixelSurr, percent)); DO POPRAWY!
        
      }
    }
    return out;
  }
  //</editor-fold>
}