package org.google.code.archetypes;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.diagnostic.Logger;
import org.google.code.archetypes.data.Archetype;
import org.google.code.archetypes.data.Group;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class represents archetypes panel.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesToolWindowPanel extends JPanel {
  private static final Logger logger = Logger.getInstance(ArchetypesToolWindowPanel.class.getName());

  private TextFieldWithButton workingDir = new TextFieldWithButton("...") {
    private TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
    {
      FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();

      textFieldWithBrowseButton.addBrowseFolderListener("Choose Working Directory",
          "Select the location where new Project will be created", project, descriptor);
      textFieldWithBrowseButton.addFocusListener(new FocusAdapter() {

        public void focusLost(FocusEvent e) {
          System.out.print("");
        }
      });
    }
    public JTextField getTextField() {
      return textFieldWithBrowseButton.getTextField();
    }

    public JButton getButton() {
      return textFieldWithBrowseButton.getButton();
    }

    public JComponent getComponent() {
      return textFieldWithBrowseButton;
    }
  };

  private ArchetypesPanel archetypesPanel;

  private Project project;

  public ArchetypesToolWindowPanel(Project project) {
    this.project = project;

    archetypesPanel = new ArchetypesPanel(getConfiguration().getArchetypesReader()) {
      public TextFieldWithButton getWorkingDirComponent() {
        return workingDir;
      }

      public Console getConsole() {
        return null;
      }
    };

    JScrollPane scrollPane = new JScrollPane(archetypesPanel);

    this.setLayout(new BorderLayout());
    this.add(scrollPane, BorderLayout.NORTH);
  }

  private ArchetypesConfiguration getConfiguration() {
    return project.getComponent(ArchetypesConfiguration.class);
  }

/*  public void resetControls() {
    if(archetypesPanel != null) {
      archetypesPanel.resetControls();
    }
  }
  */
  
  public String getWorkingDirectory() {
    return archetypesPanel.getWorkingDirComponent().getTextField().getText();
  }

  public void setWorkingDirectory(String workingDirectory) {
    archetypesPanel.getWorkingDirComponent().getTextField().setText(workingDirectory);
  }

  public boolean isModified(ArchetypesConfiguration data) {
    String text = archetypesPanel.getWorkingDirComponent().getTextField().getText();

    return (text != null ? !text.equals(data.getWorkingDirectory()) : data.getWorkingDirectory() != null);
  }

  public void createArchetype() {
     Group archetypeGroup = archetypesPanel.getGroup();
     Archetype archetype = archetypesPanel.getArchetype();
     String groupId = archetypesPanel.getGroupId();
     final String artifactId = archetypesPanel.getArtifactId();
     String version = archetypesPanel.getVersion();

     String location = getConfiguration().getArchetypesFileLocation();

     if(location.equals(ArchetypesConfiguration.ARCHETYPES_FILE_NAME)) {
       logger.warn("Using internal \"" + ArchetypesConfiguration.ARCHETYPES_FILE_NAME + "\" file.");
     }
     else {
       logger.warn("Using external \"" + ArchetypesConfiguration.ARCHETYPES_FILE_NAME + "\" file (" + location + ").");
     }

     MavenExecutor mavenExecutor = new MavenExecutor(project);

     mavenExecutor.execute(archetypesPanel.getWorkingDirComponent().getTextField().getText(), archetypeGroup, archetype, groupId, artifactId, version);
  }

}
