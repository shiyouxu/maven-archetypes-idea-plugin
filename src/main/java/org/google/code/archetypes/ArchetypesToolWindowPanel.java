package org.google.code.archetypes;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.google.code.archetypes.data.Archetype;
import org.google.code.archetypes.data.Group;
import org.google.code.archetypes.model.ArchetypeModel;
import org.google.code.archetypes.model.GroupModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * This class represents archetypes panel.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesToolWindowPanel extends JPanel {
  private TextFieldWithBrowseButton workingDir = new TextFieldWithBrowseButton();

  private JComboBox groupCombo = new JComboBox();
  private JComboBox archetypeCombo = new JComboBox();

  private JTextField groupIdField = new JTextField(15);
  private JTextField artifactIdField = new JTextField(15);
  private JTextField versionField = new JTextField(15);

  JButton createButton = new JButton("Create archetype...");

  private ArchetypesReader archetypesReader;
  private Project project;

  public ArchetypesToolWindowPanel(ArchetypesReader archetypesReader, Project project) {
    this.archetypesReader = archetypesReader;
    this.project = project;

    groupCombo.setModel(new GroupModel(archetypesReader.getGroups()));

    groupCombo.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        String groupName = (String) groupCombo.getSelectedItem();

        Group group = getGroup(groupName);

        List archetypes = group.getArchetypes();

        archetypeCombo.setModel(new ArchetypeModel(archetypes));
        archetypeCombo.setSelectedIndex(0);
      }

    });

    groupCombo.setSelectedIndex(0);

    groupIdField.setText("org.test");
    artifactIdField.setText("test");
    versionField.setText("1.0");

    createButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        submit();
      }
    });

    JPanel topPanel = fillPanel();

    this.setLayout(new BorderLayout());
    this.add(topPanel, BorderLayout.NORTH);
  }

  private JPanel fillPanel() {
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
    workingDir.addBrowseFolderListener("Choose Working Directory",
        "Select the location where new Project will be created", project, descriptor);

    // panel 0

    JPanel panel0 = new JPanel();
    panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));

    panel0.add(Box.createRigidArea(new Dimension(10, 0)));
    panel0.add(new JLabel("Working Directory: "));
    panel0.add(workingDir);

    // panel 1

    JPanel panel1 = new JPanel();
    panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));

    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(new JLabel("Group: "));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(groupCombo);
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(new JLabel("Archetype:  "));
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));
    panel1.add(archetypeCombo);
    panel1.add(Box.createRigidArea(new Dimension(10, 0)));

    // panel 2

    JPanel panel2 = new JPanel();
    panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));

    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(new JLabel("Group ID:  "));
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));
    panel2.add(groupIdField);
    panel2.add(Box.createRigidArea(new Dimension(10, 0)));

    // panel 3

    JPanel panel3 = new JPanel();
    panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));

    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(new JLabel("Artifact ID: "));
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));
    panel3.add(artifactIdField);
    panel3.add(Box.createRigidArea(new Dimension(10, 0)));

    // panel 4

    JPanel panel4 = new JPanel();
    panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));

    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(new JLabel("Version:    "));
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));
    panel4.add(versionField);
    panel4.add(Box.createRigidArea(new Dimension(10, 0)));

    // panel 5

    JPanel panel5 = new JPanel();
    panel5.setLayout(new BoxLayout(panel5, BoxLayout.X_AXIS));

    panel5.add(Box.createRigidArea(new Dimension(10, 0)));
    panel5.add(createButton);
    panel5.add(Box.createRigidArea(new Dimension(10, 0)));

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

    return topPanel;
  }

  public void load(ArchetypesToolWindow data) {
    workingDir.setText(data.getProjectRootPath());
  }

  public void save(ArchetypesToolWindow data) {
    data.setProjectRootPath(workingDir.getText());
  }

  private Group getGroup(String groupName) {
    Group group = null;

    List groups = archetypesReader.getGroups();

    for (int i = 0; i < groups.size() && group == null; i++) {
      Group g = (Group) groups.get(i);

      if (g.getName().equals(groupName)) {
        group = g;
      }
    }

    return group;
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

  private void submit() {
    Group archetypeGroup = getGroup((String) groupCombo.getSelectedItem());
    Archetype archetype = getArchetype((String) groupCombo.getSelectedItem(), (String) archetypeCombo.getSelectedItem());
    String groupId = groupIdField.getText();
    final String artifactId = artifactIdField.getText();
    String version = versionField.getText();

    MavenExecutor mavenExecutor = new MavenExecutor(project);

    mavenExecutor.execute(workingDir.getText(), archetypeGroup, archetype, groupId, artifactId, version);
  }
 
}
