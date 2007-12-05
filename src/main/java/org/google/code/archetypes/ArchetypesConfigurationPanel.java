package org.google.code.archetypes;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * This class represents form object for propagating changes in the configuration.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesConfigurationPanel extends JPanel {
  private JCheckBox useExternalArchetypesFileBox = new JCheckBox();
  private TextFieldWithBrowseButton archetypesFileLocationField = new TextFieldWithBrowseButton();

  public ArchetypesConfigurationPanel() {
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();

    archetypesFileLocationField.addBrowseFolderListener("Choose archetypes.xml file location", "Select the location of archetypes.xml file", null, descriptor);

    useExternalArchetypesFileBox.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent event) {
        JCheckBox checkBox = (JCheckBox)event.getSource();

        archetypesFileLocationField.setEnabled(checkBox.isSelected());
      }
    });

    archetypesFileLocationField.setEnabled(useExternalArchetypesFileBox.isSelected());

     // panel 1

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(new JLabel("Use external archetypes.xml file:"));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(useExternalArchetypesFileBox);
    panel1.add(new JPanel());
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));

    // panel 2

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(new JLabel(("Select the location of archetypes.xml file:")));
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(archetypesFileLocationField);
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(Box.createRigidArea(new Dimension(0, 10)));
    this.add(panel1);
    this.add(Box.createRigidArea(new Dimension(0, 10)));
    this.add(panel2);
    this.add(Box.createRigidArea(new Dimension(0, 10)));
  }

  /**
   * Gets the root component of the form.
   *
   * @return root component of the form
   */
  public JComponent getRootComponent() {
    return this;
  }

  public void load(ArchetypesConfiguration data) {
    useExternalArchetypesFileBox.setSelected((data.getUseExternalArchetypesFile() != null) ? data.getUseExternalArchetypesFile() : false);

    archetypesFileLocationField.setText(data.getArchetypesFileLocation());
  }

  public void save(ArchetypesConfiguration data) {
    data.setUseExternalArchetypesFile(useExternalArchetypesFileBox.isSelected());

    data.setArchetypesFileLocation(archetypesFileLocationField.getText());
  }

  public boolean isModified(ArchetypesConfiguration data) {
    boolean isSelected = useExternalArchetypesFileBox.isSelected();
    String text = archetypesFileLocationField.getText();

    boolean isModified = (data.getUseExternalArchetypesFile() != null && isSelected != data.getUseExternalArchetypesFile());

    isModified = isModified ||  (text != null ? !text.equals(data.getArchetypesFileLocation()) : data.getArchetypesFileLocation() != null);

    return isModified;
  }

}
