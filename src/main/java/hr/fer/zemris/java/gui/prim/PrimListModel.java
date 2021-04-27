package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>ListModel</code> that generates prim numbers.
 */
public class PrimListModel implements ListModel<Integer> {

  /**
   * List of generated prim numbers;
   */
  private List<Integer> prims;

  /**
   * List of <code>ListDataListener</code>.
   */
  private List<ListDataListener> listeners;

  /**
   * Creates an instance of the class.
   */
  public PrimListModel() {
    prims = new ArrayList<>();
    listeners = new ArrayList<>();
  }

  /**
   * Return the number of objects in a list.
   * @return number of objects in a list
   */
  @Override
  public int getSize() {
    return prims.size();
  }


  /**
   * Returns an element in the list on a position that matches the passed index.
   * @param index position in a list
   * @return an element in the list on passed position
   */
  @Override
  public Integer getElementAt(int index) {
    return prims.get(index);
  }

  /**
   * Adds passed <code>ListDataListener</code> to internal list of listeners.
   * @param l <code>ListDataListener</code>
   */
  @Override
  public void addListDataListener(ListDataListener l) {
    if(l == null)
      throw new NullPointerException("Lister can't be null");

    listeners.add(l);
  }

  /**
   * Removes passed <code>ListDataListener</code> if it exists.
   * @param l <code>ListDataListener</code> that need to be removed
   */
  @Override
  public void removeListDataListener(ListDataListener l) {
    if(l == null) return;

    listeners.remove(l);
  }

  /**
   * Generates next prim number.
   */
  public void next() {

    if(prims.size() == 0) {
      prims.add(1);
    } else {
      int last = prims.get(prims.size()-1);
      outer:
      while(true) {
        last++;
        for(int div = 2; div < Math.sqrt(last); div++) {
          if(last % div == 0) continue outer;
        }
        break;
      }
      prims.add(last);
    }
    notifyListeners();
  }

  /**
   * Notifies all the listeners that a new prim number was added.
   */
  private void notifyListeners() {
    for(ListDataListener l : listeners) {
      l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getSize()-1, getSize()-1));
    }
  }
}
