package org.google.code.archetypes;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.openapi.wm.ToolWindowAnchor;
import org.google.code.idea.common.ToolWindowComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The tool window for Maven Archetypes plugin.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class ArchetypesToolWindow extends ToolWindowComponent
    implements ProjectComponent {
  public static final String ACTION_GROUP_ID = "archetypes.ToolWindow";

  public static final String COMPONENT_NAME = "Archetypes.ToolWindow";
  public static final String TOOL_WINDOW_ID = "Archetypes";

  public ArchetypesToolWindow(Project project) {
    super(project, TOOL_WINDOW_ID);
  }

  public void projectOpened() {
     createConsole();

     //setConsoleVisible(true);
  }

  public void projectClosed() {
    //setConsoleVisible(false);

    closeConsole();
  }

 public void initComponent() {
    create();
  }
  public void disposeComponent() {
    dispose();
  }

  @NotNull
  public String getComponentName() {
    return COMPONENT_NAME;
  }

  public ArchetypesToolWindowPanel getPanel() {
    return (ArchetypesToolWindowPanel)getContentPanel();
  }

  private ArchetypesConfiguration getConfiguration() {
    return getProject().getComponent(ArchetypesConfiguration.class);
  }

  protected void customizeToolWindow(ToolWindow toolWindow) {
    toolWindow.setType(ToolWindowType.DOCKED, null);

    toolWindow.setAnchor(ToolWindowAnchor.LEFT, EMPTY_RUNNABLE);
  }

  protected JComponent createToolbar() {
    ActionManager actionManager = ActionManager.getInstance();

    ActionGroup actionGroup =
        (ActionGroup) actionManager.getAction(ACTION_GROUP_ID);

    ActionToolbar toolBar =
        actionManager.createActionToolbar(TOOL_WINDOW_ID, actionGroup, false);

    return toolBar.getComponent();
  }

  protected void createContentPanel() {
    contentPanel = new ArchetypesToolWindowPanel(getProject());

    contentPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
  }

 protected void initMainPanel() {
    mainPanel.add(createToolbar(), BorderLayout.WEST);
  }

  protected void initContentPanel() {
    getConfiguration().loadArchetypesFile();
    
  //  ((ArchetypesToolWindowPanel)contentPanel).resetControls();    
  }

  public void createArchetype() {
    String workingDir = getPanel().getWorkingDirectory();

    if(workingDir == null || workingDir.trim().length() == 0) {
      Messages.showMessageDialog(
          "You have to specify Working Directory",
          "Warning",
          Messages.getInformationIcon()
      );
    }
    else {
      ArchetypesToolWindowPanel panel = ((ArchetypesToolWindowPanel)contentPanel);

      panel.createArchetype();
    }
   }

}