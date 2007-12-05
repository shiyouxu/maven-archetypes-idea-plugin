package org.google.code.archetypes.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import org.google.code.archetypes.ArchetypesToolWindow;
import org.google.code.archetypes.common.IdeaAction;

import java.awt.*;

/**
 * This action generates archetype.
 *
 * @author Alexander Shvets
 * @version 1.0 11/24/2007
 */
public class CreateArchetypeAction extends IdeaAction {

  public void update(AnActionEvent event) {
    update(event, ArchetypesToolWindow.TOOL_WINDOW_ID);
  }

  public void actionPerformed(final AnActionEvent event) {
    final Project project = helper.getProject(event);

    final Runnable runnable = new Runnable() {
      public void run() {
        Window window = WindowManager.getInstance().suggestParentWindow(project);

        Cursor cursor = window.getCursor();

        window.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        ArchetypesToolWindow toolWindow = project.getComponent(ArchetypesToolWindow.class);

        toolWindow.createArchetype();

        window.setCursor(cursor);
      }
    };

    actionPerformed(event, ArchetypesToolWindow.TOOL_WINDOW_ID, runnable);
  }

}