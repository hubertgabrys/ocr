/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Hubert
 */
public class OutputImage extends JFrame{
  private JLabel labelImage;
  private ImageIcon imageIcon;

  OutputImage(JFrame frame, BufferedImage in) throws IOException {
    setLayout(new FlowLayout());
    setTitle("Output Image");
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    
    imageIcon = new ImageIcon((Image)in);
    labelImage = new JLabel(imageIcon);
    add(labelImage);

    pack();
  }
}