package org.google.code.archetypes.model;

import org.google.code.archetypes.data.Group;

import java.util.*;
import javax.swing.*;

/**
 * This class represents seing list model for archetype group object.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class GroupModel extends AbstractListModel
    implements ComboBoxModel {
  protected Object selectedObject;

  protected List list;

  public GroupModel(List list) {
    this.list = list;
  }

  public int getSize() {
    return list.size();
  }

  public Object getElementAt(int index) {
    if (index >= 0 && index < list.size()) {
      return ((Group) list.get(index)).getName();
    } else {
      return null;
    }
  }

  // implements ComboBoxModel

  public Object getSelectedItem() {
    return selectedObject;
  }

  public void setSelectedItem(Object anObject) {
    if ((selectedObject != null && !selectedObject.equals(anObject)) ||
        selectedObject == null && anObject != null) {
      selectedObject = anObject;
      fireContentsChanged(this, -1, -1);
    }
  }

}
