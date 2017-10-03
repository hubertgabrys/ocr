/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obrazy;

import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author Hubert
 */
public class Vector {
  private String name;
  private Deque<int[]> lista = new LinkedList<int[]>();
  public void setName(String name){
    name = this.name;
  }
  public void addToDeque(int[] in) {
    lista.add(in);
  }
  //int[][] array = new int[94][][];
  
}
