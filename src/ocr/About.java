/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Hubert
 */
public class About extends JDialog {
  JLabel label;
    
  About(JFrame frame) {
    super(frame, "About", true);

    setLayout(new FlowLayout());
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setLocation(100,100);
    label = new JLabel("Author: Hubert Gabryś");
    add(label);

    pack();
  }
}
