/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import obrazy.Binarization;
import obrazy.RGB;

/**
 *
 * @author Hubert
 */
public class InputImage extends JFrame{
  private JLabel labelImage;
  private ImageIcon imageIcon;

  InputImage(JFrame frame, File file) throws IOException {
    setLayout(new FlowLayout());
    setTitle("Input Image");
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

    BufferedImage in = ImageIO.read(file); 
    //Konwersja to odcieni szarości z normalizacją histogramu
    in = RGB.szaryOpt(in);
    in = RGB.szaryHist(in);
    //Test
    //Binarization.metodaOtsu(in);
    imageIcon = new ImageIcon((Image)in);
    labelImage = new JLabel(imageIcon);
    add(labelImage);

    pack();
  }
}
