package org.google.code.archetypes;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.diagnostic.Logger;
import org.google.code.archetypes.data.Archetype;
import org.google.code.archetypes.data.Group;
import org.google.code.archetypes.model.ArchetypeModel;
import org.google.code.archetypes.model.GroupModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * This class represents archetypes panel.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesToolWindowPanel extends JPanel {
  private static final Logger logger = Logger.getInstance(ArchetypesToolWindowPanel.class.getName());

  private TextFieldWithBrowseButton workingDir = new TextFieldWithBrowseButton();

  private JComboBox groupCombo = new JComboBox();
  private JComboBox archetypeCombo = new JComboBox();
  private JLabel archetypeVersionLabel = new JLabel();

  private JTextField groupIdField = new JTextField(15);
  private JTextField artifactIdField = new JTextField(15);
  private JTextField versionField = new JTextField(15);

  private Project project;

  public ArchetypesToolWindowPanel(Project project) {
    this.project = project;

    groupIdField.setText("org.test");
    artifactIdField.setText("test");
    versionField.setText("1.0");

    JPanel topPanel = fillPanel();
    JScrollPane scrollPane = new JScrollPane(topPanel);

    this.setLayout(new BorderLayout());
    this.add(scrollPane, BorderLayout.NORTH);
  }

  private ArchetypesConfiguration getConfiguration() {
    return project.getComponent(ArchetypesConfiguration.class);
  }

  public void resetControls() {
    groupCombo.setModel(new GroupModel(getConfiguration().getArchetypesReader().getGroups()));

    groupCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        String groupName = (String) groupCombo.getSelectedItem();

        Group group = getGroup(groupName);

        archetypeVersionLabel.setText(group.getVersion());
        List archetypes = group.getArchetypes();

        archetypeCombo.setModel(new ArchetypeModel(archetypes));
        archetypeCombo.setSelectedIndex(0);
      }
    });

    groupCombo.setSelectedIndex(0);
  }


  private JPanel fillPanel() {
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
    workingDir.addBrowseFolderListener("Choose Working Directory",
        "Select the location where new Project will be created", project, descriptor);
    workingDir.addFocusListener(new FocusAdapter() {

      public void focusLost(FocusEvent e) {
        System.out.print("");
      }
    });

    // panel 0

    JPanel panel0 = new JPanel();
    panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));

    panel0.add(Box.createRigidArea(new Dimension(10, 0)));
    panel0.add(new JLabel("Working Directory: "));
    panel0.add(workingDir);

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(new JLabel("Archetype Info: "));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(new JPanel());
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));

   JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(new JLabel("Group ID: "));
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(groupCombo);
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));

   JPanel panel3 = new JPanel();
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(new JLabel("Artifact ID:  "));
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(archetypeCombo);
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(new JLabel("Version:  "));
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(archetypeVersionLabel);
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(new JPanel());
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel5 = new JPanel();
    panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));

    panel5.add(Box.createRigidArea(new Dimension(10, 0)));
    panel5.add(new JLabel("New Project Info: "));
    panel5.add(Box.createRigidArea(new Dimension(10, 0)));
    panel5.add(new JPanel());
    panel5.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel6 = new JPanel();
    panel6.setLayout(new BoxLayout(panel6, BoxLayout.X_AXIS));

    panel6.add(Box.createRigidArea(new Dimension(10, 0)));
    panel6.add(new JLabel("Group ID:  "));
    panel6.add(Box.createRigidArea(new Dimension(10, 0)));
    panel6.add(groupIdField);
    panel6.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel7 = new JPanel();
    panel7.setLayout(new BoxLayout(panel7, BoxLayout.X_AXIS));

    panel7.add(Box.createRigidArea(new Dimension(10, 0)));
    panel7.add(new JLabel("Artifact ID: "));
    panel7.add(Box.createRigidArea(new Dimension(10, 0)));
    panel7.add(artifactIdField);
    panel7.add(Box.createRigidArea(new Dimension(10, 0)));

    JPanel panel8 = new JPanel();
    panel8.setLayout(new BoxLayout(panel8, BoxLayout.X_AXIS));

    panel8.add(Box.createRigidArea(new Dimension(10, 0)));
    panel8.add(new JLabel("Version:    "));
    panel8.add(Box.createRigidArea(new Dimension(10, 0)));
    panel8.add(versionField);
    panel8.add(Box.createRigidArea(new Dimension(10, 0)));

    // top panel
    JPanel topPanel = new JPanel();

    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel0);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel1);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
   topPanel.add(panel2);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
   topPanel.add(panel3);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel4);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel5);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel6);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel7);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    topPanel.add(panel8);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
  // topPanel.add(panel9);
    topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    return topPanel;
  }

  public List<Group> getGroups() {
    return getConfiguration().getArchetypesReader().getGroups();
  }

  private Group getGroup(String groupName) {
    Group group = null;

    List<Group> groups = getConfiguration().getArchetypesReader().getGroups();

    for (int i = 0; i < groups.size() && group == null; i++) {
      Group g = groups.get(i);

      if (g.getName().equals(groupName)) {
        group = g;
      }
    }

    return group;
  }

  public String getWorkingDirectory() {
    return workingDir.getText();
  }

  public void setWorkingDirectory(String workingDirectory) {
    this.workingDir.setText(workingDirectory);
  }

  public boolean isModified(ArchetypesConfiguration data) {
    String text = workingDir.getText();

    return (text != null ? !text.equals(data.getWorkingDirectory()) : data.getWorkingDirectory() != null);
  }

  private Archetype getArchetype(String groupName, String archetypeName) {
    Archetype archetype = null;

    Group group = getGroup(groupName);

    List archetypes = group.getArchetypes();

    for (int i = 0; i < archetypes.size() && archetype == null; i++) {
      Archetype a = (Archetype) archetypes.get(i);

      if (a.getName().equals(archetypeName)) {
        archetype = a;
      }
    }

    return archetype;
  }

  public void createArchetype() {
     Group archetypeGroup = getGroup((String) groupCombo.getSelectedItem());
     Archetype archetype = getArchetype((String) groupCombo.getSelectedItem(), (String) archetypeCombo.getSelectedItem());
     String groupId = groupIdField.getText();
     final String artifactId = artifactIdField.getText();
     String version = versionField.getText();

     String location = getConfiguration().getArchetypesFileLocation();

     if(location.equals(ArchetypesConfiguration.ARCHETYPES_FILE_NAME)) {
       logger.warn("Using internal \"" + ArchetypesConfiguration.ARCHETYPES_FILE_NAME + "\" file.");
     }
     else {
       logger.warn("Using external \"" + ArchetypesConfiguration.ARCHETYPES_FILE_NAME + "\" file (" + location + ").");
     }

     MavenExecutor mavenExecutor = new MavenExecutor(project);

     mavenExecutor.execute(workingDir.getText(), archetypeGroup, archetype, groupId, artifactId, version);
  }

}
