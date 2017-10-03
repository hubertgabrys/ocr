/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ocr;

import java.awt.FlowLayout;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Hubert
 */
public class OutputText extends JDialog {
  JTextField textField;
  JTextArea ta;
    
  OutputText(JFrame frame, char[][] outputText) throws FileNotFoundException {
    super(frame, "OutputText", true);
    setLayout(new FlowLayout());
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setLocation(200,200);
    String[] text = new String[outputText.length];
    for (int i = 0; i < outputText.length; i++) {
      text[i] = new String(outputText[i]);
    }
    String outputTextFinal = "";
    for (int i = 0; i < text.length; i++) {
      outputTextFinal += (text[i]+"\r\n");
    }
    PrintWriter out = new PrintWriter("data/OCR/outputText.txt");
    out.println(outputTextFinal);
    out.close();
    ta = new JTextArea(outputTextFinal);
    ta.setEditable(false);
    add(ta);

    pack();
  }
}