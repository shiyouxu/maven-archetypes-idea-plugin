package org.google.code.archetypes;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;

/**
 * This class represents configuration component for Maven Archetype Tool Window.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
@State(
    name = ArchetypesConfiguration.COMPONENT_NAME,
    storages = {@Storage(id = "archetypes", file = "$PROJECT_FILE$")}
)
public final class ArchetypesConfiguration
    implements ProjectComponent, Configurable, PersistentStateComponent<ArchetypesConfiguration> {
  private static final Logger logger = Logger.getInstance(ArchetypesConfiguration.class.getName());

  public final static String COMPONENT_NAME = "Archetypes.Configuration";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

  public final static String ARCHETYPES_FILE_NAME = "archetypes.xml";

  private ArchetypesReader archetypesReader = new ArchetypesReader();

  private ArchetypesConfigurationPanel panel;

  // properties to persist

  private Boolean useExternalArchetypesFile = Boolean.FALSE;
  private String archetypesFileLocation;
  private String workingDirectory;

  private Project project;

  public ArchetypesConfiguration() {
  }

  public ArchetypesConfiguration(Project project) {
    this.project = project;
  }

  public boolean isModified() {
    ArchetypesToolWindowPanel panel2 = project.getComponent(ArchetypesToolWindow.class).getPanel();

    setWorkingDirectory(panel2.getWorkingDirectory());    

    return panel != null && panel.isModified(this) ||
               (panel2.isModified(this));
  }

  public void projectOpened() {
  }

  public void projectClosed() {
  }

  @NotNull
  public String getComponentName() {
    return COMPONENT_NAME;
  }

  public void initComponent() {
    if (panel == null) {
      panel = new ArchetypesConfigurationPanel();
    }
  }

  public void disposeComponent() {
    panel = null;
  }

  public String getDisplayName() {
    return "Archetypes";
  }

  public JComponent createComponent() {
    initComponent();

    return panel.getRootComponent();
  }

  public Icon getIcon() {
//    return CONFIG_ICON;
    return null;
  }

  public String getHelpTopic() {
    return null;
  }

  /**
   * Stores settings from panel to configuration bean.
   *
   * @throws ConfigurationException
   */
  public void apply() throws ConfigurationException {
    if (panel != null) {
      panel.save(this);

      loadArchetypesFile();
    }
  }

  /**
   * Restores panel values from configuration.
   */
  public void reset() {
    if (panel != null) {
      panel.load(this);
    }
  }

  /**
   * Disposes UI resource.
   */
  public void disposeUIResources() {
    panel = null;
  }

  public Boolean getUseExternalArchetypesFile() {
    return useExternalArchetypesFile;
  }

  public void setUseExternalArchetypesFile(Boolean useExternalArchetypesFile) {
    this.useExternalArchetypesFile = useExternalArchetypesFile;
  }

  public String getArchetypesFileLocation() {
    return archetypesFileLocation;
  }

  public void setArchetypesFileLocation(String archetypesFileLocation) {
    this.archetypesFileLocation = archetypesFileLocation;
  }

  public String getWorkingDirectory() {
    return workingDirectory;
  }

  public void setWorkingDirectory(String workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  public ArchetypesConfiguration getState() {
    ArchetypesToolWindowPanel panel = project.getComponent(ArchetypesToolWindow.class).getPanel();

    setWorkingDirectory(panel.getWorkingDirectory());

    return this;
  }

  public void loadState(ArchetypesConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);

    loadArchetypesFile();

    ArchetypesToolWindow archetypesToolWindow = project.getComponent(ArchetypesToolWindow.class);

    ArchetypesToolWindowPanel archetypesToolWindowPanel = archetypesToolWindow.getPanel();

    archetypesToolWindowPanel.setWorkingDirectory(workingDirectory);
  }

  public void loadArchetypesFile() {
    archetypesFileLocation = getArchetypesFileName();

    try {
      archetypesReader.readConfigFile(archetypesFileLocation);
    }
    catch (Exception e) {
      try {
        useExternalArchetypesFile = Boolean.FALSE;
        archetypesFileLocation = ARCHETYPES_FILE_NAME;
        archetypesReader.readConfigFile(ARCHETYPES_FILE_NAME);
      } catch (Exception e2) {
        logger.error(e2.getMessage());
      }
    }
  }

  private String getArchetypesFileName() {
    String fileName = ARCHETYPES_FILE_NAME;

    if (getUseExternalArchetypesFile()) {
      String location = getArchetypesFileLocation();

      File file = new File(location);

      if (file.exists()) {
        if (file.isDirectory()) {
          location = location + File.separatorChar + ARCHETYPES_FILE_NAME;

          file = new File(location);
        }
      }

      if (file.exists()) {
        fileName = location;
      }
    }

    return fileName;
  }

  public ArchetypesReader getArchetypesReader() {
    return archetypesReader;
  }

}
