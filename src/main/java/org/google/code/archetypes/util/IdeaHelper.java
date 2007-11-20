// IdeaHelper.java

package org.google.code.archetypes.util;

import java.net.URL;
import java.io.File;

import javax.swing.ImageIcon;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;

/**
 * This class contains useful routines for working with IDEA API.
 *
 * @author Alexander Shvets
 * @version 1.0 11/30/2003
 */
public class IdeaHelper {
  private static IdeaHelper ourInstance = new IdeaHelper();

  public static IdeaHelper getInstance() {
    return ourInstance;
  }

  private IdeaHelper() {
  }

  /**
   * Gets the project.
   *
   * @param event the event
   * @return the project
   */
  public Project getProject(AnActionEvent event) {
    DataContext dataContext = event.getDataContext();

    return DataKeys.PROJECT.getData(dataContext);
  }

  /**
   * Gets the tool window.
   *
   * @param project      the project
   * @param toolWindowId ID for tool window
   * @return the tool window
   */
  public ToolWindow getToolWindow(Project project, String toolWindowId) {
    ToolWindowManager toolWindowManager =
        ToolWindowManager.getInstance(project);

    return toolWindowManager.getToolWindow(toolWindowId);
  }

  /**
   * Gets the icon.
   *
   * @param name  the resource name
   * @param clazz the class name
   * @return the icon
   */
  public ImageIcon getIcon(String name, Class clazz) {
    URL url = clazz.getResource(name);

    return new ImageIcon(url);
  }

  /**
   * Gets the class name inside the project.
   *
   * @param fileName the file name
   * @param project  the project
   * @return the class name
   */
  public String getClassName(String fileName, Project project) {
    PsiManager psiManager = PsiManager.getInstance(project);
    LocalFileSystem localFileSystem = LocalFileSystem.getInstance();

    String vfsPathName = fileName.replace(File.separatorChar, '/');

    VirtualFile virtualFile = localFileSystem.findFileByPath(vfsPathName);

    if (virtualFile == null) {
      return fileName;
    }

    PsiDirectory psiDir = psiManager.findDirectory(virtualFile);

    if (psiDir == null) {
      return fileName;
    }

    PsiPackage psiPackage = psiDir.getPackage();

    if (psiPackage == null) {
      return fileName;
    }

    return psiPackage.getQualifiedName() + "." +
        virtualFile.getNameWithoutExtension();
  }

}
