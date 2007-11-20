package org.google.code.archetypes;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
  public final static String COMPONENT_NAME = "Archetypes.Configuration";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

  private ArchetypesConfigurationPanel panel;

  // properties to persist

  private String repositoryHome;
  private String archetypesFileLocation;

  public boolean isModified() {
    return panel != null && panel.isModified(this);
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
  }

  public void disposeComponent() {
  }

  public String getDisplayName() {
    return "Archetypes";
  }

  public Icon getIcon() {
//    return CONFIG_ICON;
    return null;
  }

  public String getHelpTopic() {
    return null;
  }

  public JComponent createComponent() {
    if (panel == null) {
      panel = new ArchetypesConfigurationPanel();
    }

    return panel.getRootComponent();
  }

  /**
   * Stores settings from panel to configuration bean.
   *
   * @throws ConfigurationException
   */
  public void apply() throws ConfigurationException {
    if (panel != null) {
      panel.save(this);
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

  public String getRepositoryHome() {
    return repositoryHome;
  }

  public void setRepositoryHome(String repositoryHome) {
    this.repositoryHome = repositoryHome;
  }

  public String getArchetypesFileLocation() {
    return archetypesFileLocation;
  }

  public void setArchetypesFileLocation(String archetypesFileLocation) {
    this.archetypesFileLocation = archetypesFileLocation;
  }

  public ArchetypesConfiguration getState() {
    return this;
  }

  public void loadState(ArchetypesConfiguration state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}
