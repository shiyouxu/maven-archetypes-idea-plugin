package org.google.code.archetypes;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents form object for propagating changes in the configuration.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesConfigurationPanel extends JPanel {
  private TextFieldWithBrowseButton repositoryHomeField = new TextFieldWithBrowseButton();
  private TextFieldWithBrowseButton archetypesFileLocationField = new TextFieldWithBrowseButton();

  public ArchetypesConfigurationPanel() {
    DataContext dataContext = DataManager.getInstance().getDataContext();
    Project project = DataKeys.PROJECT.getData(dataContext);

    FileChooserDescriptor descriptor1 = FileChooserDescriptorFactory.createSingleFolderDescriptor();

    repositoryHomeField.addBrowseFolderListener("Choose Project Root", "Select Location of Maven Repository", project, descriptor1);

    FileChooserDescriptor descriptor2 = FileChooserDescriptorFactory.createSingleFolderDescriptor();

    archetypesFileLocationField.addBrowseFolderListener("Choose Project Root", "Select Location of archetypes.xml file", null, descriptor2);

     // panel 1

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(new JLabel("Select Location of Maven Repository:"));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(repositoryHomeField);
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));

    // panel 2

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(new JLabel(("Select Location of archetypes.xml file:")));
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
    repositoryHomeField.setText(data.getRepositoryHome());

    archetypesFileLocationField.setText(data.getArchetypesFileLocation());
  }

  public void save(ArchetypesConfiguration data) {
    data.setRepositoryHome(repositoryHomeField.getText());

    data.setArchetypesFileLocation(archetypesFileLocationField.getText());
  }

  public boolean isModified(ArchetypesConfiguration data) {
    String text1 = repositoryHomeField.getText();
    String text2 = archetypesFileLocationField.getText();

    boolean isModified = (text1 != null ? !text1.equals(data.getRepositoryHome()) : data.getRepositoryHome() != null);

    isModified = isModified ||  (text2 != null ? !text2.equals(data.getArchetypesFileLocation()) : data.getArchetypesFileLocation() != null);

    return isModified;
  }

}
