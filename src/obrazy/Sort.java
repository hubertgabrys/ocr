/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package obrazy;

/**
 *
 * @author Hubert
 */
public class Sort {
  //optymalne sortowanie - możesz wykorzystać je przy filtrach medianowych
  public static void quicksort(int tab[],int dol,int gora) {

    int i = dol, j = gora;
    int polowa = tab[dol + (gora-dol)/2];

    while (i <= j) {
      while (tab[i] < polowa) {
        i++;
      }
      while (tab[j] > polowa) {
        j--;
      }
      if (i <= j) {
        int tmp = tab[i];
        tab[i] = tab[j];
        tab[j] = tmp;
        i++;
        j--;
      }
    }
    if (dol < j)
      quicksort(tab,dol, j);
    if (i < gora)
      quicksort(tab,i, gora);
  }
}
