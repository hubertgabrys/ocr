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
public class Transformation {

  private static double[] polar = new double[2];
  private static int[] cart = new int[2];
  private static int[] nodes = new int[4];
  private static int[] coordCart;
  private static double[] coordPol;

  /**
   * Obrót z kopii. Działa tylko dla kątów z I ćwiatki.
   *
   * @param in Obraz wejściowy.
   * @param angleD Kąt obrotu w stopniach.
   * @return Obraz wyjściowy.
   */
  public static BufferedImage obrotK(BufferedImage in, double angleD) {
    int height, width, newHeight, newWidth, red, green, blue;
    width = in.getWidth();
    height = in.getHeight();
    angleD %= 360;
    if (angleD < 0) {
      angleD += 360;
    }
    double angleR = (2 * Math.PI * angleD) / 360;
    newWidth = Transformation.newWidth(width, height, Math.abs(angleR));
    newHeight = Transformation.newHeight(width, height, Math.abs(angleR));
    BufferedImage out;
    out = new BufferedImage(newWidth, newHeight, in.getType());
    for (int i = 0; i < newWidth; i++) {
      for (int j = 0; j < newHeight; j++) {
        int x, y, a, b;
        double r = Transformation.cartToPol(i, j)[0];
        double theta = Transformation.cartToPol(i, j)[1];
        theta += angleR;
        if (angleR > 0 && angleR <= Math.PI / 2) {
          a = (int) (width * Math.sin(angleR) * Math.sin(angleR));
          b = (int) (-width * Math.sin(angleR) * Math.cos(angleR));
        } else if (angleR > Math.PI / 2 && angleR <= Math.PI) {
          a = (int) (width + height * Math.cos(Math.PI - angleR) * Math.sin(Math.PI - angleR));
          b = (int) (height * Math.cos(Math.PI - angleR) * Math.cos(Math.PI - angleR));
        } else if (angleR > Math.PI && angleR <= (1.5 * Math.PI)) {
          a = (int) (width - width * Math.sin(angleR - Math.PI) * Math.sin(angleR - Math.PI));
          b = (int) (height + width * Math.sin(angleR - Math.PI) * Math.cos(angleR - Math.PI));
        } else {
          a = (int) (-height * Math.cos(2 * Math.PI - angleR) * Math.sin(2 * Math.PI - angleR));
          b = (int) (height - height * Math.cos(2 * Math.PI - angleR) * Math.cos(2 * Math.PI - angleR));
        }
        x = Transformation.polToCart(r, theta)[0] + a;
        y = Transformation.polToCart(r, theta)[1] + b;

        if (x >= 0 && y >= 0 && x < width && y < height) {
          red = RGB.getR(in.getRGB(x, y));
          green = RGB.getG(in.getRGB(x, y));
          blue = RGB.getB(in.getRGB(x, y));
        } else {
          red = green = blue = 255;
        }
        out.setRGB(i, j, RGB.toRGB(red, green, blue));
      }
    }
    return out;
  }

  public static double[] cartToPol(int x, int y) {
    polar[0] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    polar[1] = Math.atan2(y, x);
    return polar;
  }

  public static int[] polToCart(double r, double theta) {
    cart[0] = (int) (r * Math.cos(theta));
    cart[1] = (int) (r * Math.sin(theta));
    return cart;
  }

  public static int xMin(int width, int height, double angleR) {
    return findNodes(width, height, angleR)[0];
  }

  public static int xMax(int width, int height, double angleR) {
    return findNodes(width, height, angleR)[1];
  }

  public static int yMin(int width, int height, double angleR) {
    return findNodes(width, height, angleR)[2];
  }

  public static int yMax(int width, int height, double angleR) {
    return findNodes(width, height, angleR)[3];
  }

  /**
   * Metoda zwraca szerokość obróconego obrazka.
   */
  public static int newWidth(int width, int height, double angleR) {
    return xMax(width, height, angleR) - xMin(width, height, angleR) + 1;
  }

  /**
   * Metoda zwraca wysokość obróconego obrazka.
   */
  public static int newHeight(int width, int height, double angleR) {

    return yMax(width, height, angleR) - yMin(width, height, angleR) + 1;
  }

  /**
   * Metoda znajduje wierzchołki obróconego obrazu. Na wejście podajemy
   * odpowiednio: wysokość obrazu, szerokośc obrazu, kąt rotacji w radianach.
   * Wynik zwracany jest w tabeli o 4 polach [xmin, xmax, ymin, ymax].
   */
  private static int[] findNodes(int width, int height, double angleR) {

    nodes[0] = nodes[1] = nodes[2] = nodes[3] = 0;
    coordPol = cartToPol(width - 1, 0);
    coordPol[1] = coordPol[1] - angleR;
    coordCart = polToCart(coordPol[0], coordPol[1]);
    if (coordCart[0] < nodes[0]) {
      nodes[0] = coordCart[0];
    }
    if (coordCart[1] < nodes[2]) {
      nodes[2] = coordCart[1];
    }
    if (coordCart[0] > nodes[1]) {
      nodes[1] = coordCart[0];
    }
    if (coordCart[1] > nodes[3]) {
      nodes[3] = coordCart[1];
    }

    coordPol = cartToPol(width - 1, height - 1);
    coordPol[1] = coordPol[1] - angleR;
    coordCart = polToCart(coordPol[0], coordPol[1]);
    if (coordCart[0] < nodes[0]) {
      nodes[0] = coordCart[0];
    }
    if (coordCart[1] < nodes[2]) {
      nodes[2] = coordCart[1];
    }
    if (coordCart[0] > nodes[1]) {
      nodes[1] = coordCart[0];
    }
    if (coordCart[1] > nodes[3]) {
      nodes[3] = coordCart[1];
    }

    coordPol = cartToPol(0, height - 1);
    coordPol[1] = coordPol[1] - angleR;
    coordCart = polToCart(coordPol[0], coordPol[1]);

    if (coordCart[0] < nodes[0]) {
      nodes[0] = coordCart[0];
    }
    if (coordCart[1] < nodes[2]) {
      nodes[2] = coordCart[1];
    }
    if (coordCart[0] > nodes[1]) {
      nodes[1] = coordCart[0];
    }
    if (coordCart[1] > nodes[3]) {
      nodes[3] = coordCart[1];
    }

    return nodes;
  }
}
