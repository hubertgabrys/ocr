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
public class Maska {
  //Zostawić tylko elMaski3 i elMaski2

  private static int[] elMaski3 = new int[8];
  private static int[] elMaski3R = new int[8];
  private static int[] elMaski3G = new int[8];
  private static int[] elMaski3B = new int[8];
  private static int[] elMaski2 = new int[4];
  private static int[] elMaski2R = new int[4];
  private static int[] elMaski2G = new int[4];
  private static int[] elMaski2B = new int[4];

  public static int[] elMaski3(int i, int j, int width, int height, BufferedImage in) {
    if (i - 1 >= 0 && j - 1 >= 0) {
      elMaski3[0] = in.getRGB(i - 1, j - 1);
    } else {
      elMaski3[0] = 0;
    }
    if (j - 1 >= 0) {
      elMaski3[1] = in.getRGB(i, j - 1);
    } else {
      elMaski3[1] = 0;
    }
    if (i + 1 < width && j - 1 >= 0) {
      elMaski3[2] = in.getRGB(i + 1, j - 1);
    } else {
      elMaski3[2] = 0;
    }
    if (i + 1 < width) {
      elMaski3[3] = in.getRGB(i + 1, j);
    } else {
      elMaski3[3] = 0;
    }
    if (i + 1 < width && j + 1 < height) {
      elMaski3[4] = in.getRGB(i + 1, j + 1);
    } else {
      elMaski3[4] = 0;
    }
    if (j + 1 < height) {
      elMaski3[5] = in.getRGB(i, j + 1);
    } else {
      elMaski3[5] = 0;
    }
    if (i - 1 >= 0 && j + 1 < height) {
      elMaski3[6] = in.getRGB(i - 1, j + 1);
    } else {
      elMaski3[6] = 0;
    }
    if (i - 1 >= 0) {
      elMaski3[7] = in.getRGB(i - 1, j);
    } else {
      elMaski3[7] = 0;
    }

    return elMaski3;
  }

  public static int[] elMaski3R(int i, int j, int width, int height, BufferedImage in) {
    elMaski3(i, j, width, height, in);
    for (int k = 0; k < 8; k++) {
      elMaski3R[k] = RGB.getR(elMaski3[k]);
    }
    return elMaski3R;
  }

  public static int[] elMaski3G(int i, int j, int width, int height, BufferedImage in) {
    elMaski3(i, j, width, height, in);
    for (int k = 0; k < 8; k++) {
      elMaski3G[k] = RGB.getG(elMaski3[k]);
    }
    return elMaski3G;
  }

  public static int[] elMaski3B(int i, int j, int width, int height, BufferedImage in) {
    elMaski3(i, j, width, height, in);
    for (int k = 0; k < 8; k++) {
      elMaski3B[k] = RGB.getB(elMaski3[k]);
    }
    return elMaski3B;
  }

  public static int[] elMaski2(int i, int j, int width, int height, BufferedImage in) {
    elMaski2[0] = in.getRGB(i, j);
    if (i + 1 < width) {
      elMaski2[1] = in.getRGB(i + 1, j);
    } else {
      elMaski2[1] = 0;
    }
    if (i + 1 < width && j + 1 < height) {
      elMaski2[2] = in.getRGB(i + 1, j + 1);
    } else {
      elMaski2[2] = 0;
    }
    if (j + 1 < height) {
      elMaski2[3] = in.getRGB(i, j + 1);
    } else {
      elMaski2[3] = 0;
    }

    return elMaski2;
  }

  public static int[] elMaski2R(int i, int j, int width, int height, BufferedImage in) {
    elMaski2(i, j, width, height, in);
    for (int k = 0; k < 4; k++) {
      elMaski2R[k] = RGB.getR(elMaski2[k]);
    }
    return elMaski2R;
  }

  public static int[] elMaski2G(int i, int j, int width, int height, BufferedImage in) {
    elMaski2(i, j, width, height, in);
    for (int k = 0; k < 4; k++) {
      elMaski2G[k] = RGB.getG(elMaski2[k]);
    }
    return elMaski2G;
  }

  public static int[] elMaski2B(int i, int j, int width, int height, BufferedImage in) {
    elMaski2(i, j, width, height, in);
    for (int k = 0; k < 4; k++) {
      elMaski2B[k] = RGB.getB(elMaski2[k]);
    }
    return elMaski2B;
  }
}
