package org.google.code.archetypes;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.peer.PeerFactory;
import com.intellij.ui.content.Content;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * The tool window for Maven Archetypes plugin.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
@State(
  name = ArchetypesToolWindow.COMPONENT_NAME,
  storages = {@Storage(id = "archetypes", file = "$PROJECT_FILE$")}
)
public class ArchetypesToolWindow
          implements ProjectComponent, PersistentStateComponent<ArchetypesToolWindow> {
  private static final Logger logger = Logger.getInstance(ArchetypesToolWindow.class.getName());

  public static final String COMPONENT_NAME = "Archetypes.ToolWindow";
  public static final String TOOL_WINDOW_ID = "Archetypes";

  // properties to persist

  private String projectRootPath;

  private ArchetypesToolWindowPanel panel;

  private Project project;

  public ArchetypesToolWindow() {}

  public ArchetypesToolWindow(Project project) {
    this.project = project;
  }

  public void projectOpened() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

    ToolWindow toolWindow =
        toolWindowManager.registerToolWindow(TOOL_WINDOW_ID, true, ToolWindowAnchor.RIGHT);

    PeerFactory peerFactory = PeerFactory.getInstance();

    Content content = peerFactory.getContentFactory().createContent(panel, "", false);

    toolWindow.getContentManager().addContent(content);
    //toolWindow.setIcon(IconLoader.getIcon("icon.png", ArchetypesToolWindow.class));
  }

  public void projectClosed() {
    ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);

    toolWindowManager.unregisterToolWindow(TOOL_WINDOW_ID);
  }

  public void initComponent() {
    try {
      panel = new ArchetypesToolWindowPanel(project);

      panel.load(this);
    }
    catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  public void disposeComponent() {
    panel = null;
  }

  @NotNull
  public String getComponentName() {
    return COMPONENT_NAME;
  }

  public String getProjectRootPath() {
    return projectRootPath;
  }

  public void setProjectRootPath(String projectRootPath) {
    this.projectRootPath = projectRootPath;
  }

  public ArchetypesToolWindow getState() {
    panel.save(this);

    return this;
  }

  public void loadState(ArchetypesToolWindow state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}
