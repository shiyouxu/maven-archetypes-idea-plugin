package org.google.code.archetypes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.maven.embedder.*;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.google.code.archetypes.data.Archetype;
import org.google.code.archetypes.data.Group;
import org.jetbrains.idea.maven.builder.MavenBuilderImpl;
import org.jetbrains.idea.maven.builder.MavenBuilderState;
import org.jetbrains.idea.maven.builder.executor.MavenBuildParameters;
import org.jetbrains.idea.maven.core.MavenCore;
import org.jetbrains.idea.maven.core.MavenCoreImpl;
import org.jetbrains.idea.maven.core.MavenCoreState;
import org.jetbrains.idea.maven.core.util.MavenEnv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class performs maven execution.
 *
 * @author Alexander Shvets
 * @version 1.0 11/17/2007
 */
public class MavenExecutor {
  private Project project;

  public MavenExecutor(Project project) {
    this.project = project;
  }

  public void execute(final String workingDir, Group archetypeGroup, Archetype archetype, String groupId, final String artifactId, String version) {
   List<String> repositories = archetypeGroup.getRepositories();

    StringBuffer sb = new StringBuffer();

    if (repositories.size() > 0) {
      for (int i = 0; i < repositories.size(); i++) {
        sb.append(repositories.get(i));

        if (i < repositories.size() - 1) {
          sb.append(" ");
        }
      }
    }

    String remoteRepositories = sb.toString();

     /*   MavenBuildParameters parameters = new MavenBuildParameters();
         MavenCoreState coreState = (project.getComponent(MavenCore.class)).getState().clone();
         MavenBuilderState builderState = (project.getComponent(MavenBuilder.class)).getState().clone();

         MavenEmbeddedExecutor mavenExecutor = new MavenEmbeddedExecutor(parameters, coreState, builderState);

         mavenExecutor.execute();
        */

     MavenCore mavenCore = new MavenCoreImpl();

     mavenCore.loadState(new MavenCoreState() {
       public MavenEmbedder createEmbedder() throws MavenEmbedderException {
         //return MavenEnv.createEmbedder(getMavenHome(), getMavenSettingsFile(), getClass().getClassLoader());

         Configuration configuration = new DefaultConfiguration();
         configuration.setMavenEmbedderLogger(new MavenEmbedderConsoleLogger());

         File userSettingsFile = MavenEnv.resolveUserSettingsFile(getMavenSettingsFile());
         if (userSettingsFile != null)
           configuration.setUserSettingsFile(userSettingsFile);

         File globalSettingsFile = MavenEnv.resolveGlobalSettingsFile(getMavenHome());
         if (globalSettingsFile != null)
           configuration.setGlobalSettingsFile(globalSettingsFile);

         configuration.setClassLoader(getClass().getClassLoader());

         ConfigurationValidationResult validationResult = MavenEmbedder.validateConfiguration(configuration);

         if (!validationResult.isValid())
           throw new MavenEmbedderException("Cannot create maven embedder.");

         System.setProperty("maven.home", getMavenHome());

         return new MavenEmbedder(configuration) {
           public MavenExecutionResult execute(MavenExecutionRequest request) {
             request.setBaseDirectory(new File(workingDir));
             //System.getProperties().put("user.dir", workingDir.getText());
             request.setPom(null);

             return super.execute(request);
           }
         };
       }
     });

     MavenBuilderImpl builder = new MavenBuilderImpl(project, mavenCore);

//    MavenCoreState mavenCoreState = mavenCore.getState();
//    mavenCoreState.setLocalRepository("c:/maven-repository");

     MavenBuilderState builderState = builder.getState();
     Map<String, String> mavenProperties = builderState.getMavenProperties();

     mavenProperties.put("archetypeGroupId", archetypeGroup.getGroupId());
     mavenProperties.put("archetypeArtifactId", archetype.getName());
     mavenProperties.put("archetypeVersion", archetype.getVersion());
     mavenProperties.put("groupId", groupId);
     mavenProperties.put("artifactId", artifactId);
     mavenProperties.put("version", version);
     mavenProperties.put("remoteRepositories", remoteRepositories);

     File projectDir = new File(workingDir + "/" + artifactId);

     if (projectDir.exists() && projectDir.list().length > 0) {
       Messages.showMessageDialog(
           "Output directory: " + workingDir + File.separatorChar + artifactId + "  should not exist!",
           "Warning",
           Messages.getInformationIcon()
       );
     }
     else {
       if (!builder.isRunning()) {
         List<String> goals = new ArrayList<String>();
         goals.add("archetype:create");

         MavenBuildParameters buildParameters = new MavenBuildParameters(workingDir, goals, null);

         buildParameters.setGoals(goals);

         builder.closeToolWindow();
         builder.run(buildParameters);
       }
     }
   }

}

/*
    ScriptExecutor executor = new ScriptExecutor();
    List params = new ArrayList();

    params.add(server.getInstallDir() + File.separator + scriptName);
    params.add(userName);
    params.add(dirName);
    params.add(fileName);
    params.add(tag);
    params.add(comment);
 */
    /*  int exitValue = 0;
       try {
         exitValue = executor.execute("C:/Env/maven-2.0.7/bin/mvn.bat ", "");
       } catch (Exception e) {
         e.printStackTrace();
       }

       if(exitValue == 0) {
         String answer = executor.getStandardOutput();
                 System.out.println(answer);
       }
       else {
         String answer = executor.getErrorOutput();
                 System.out.println(answer);

       }
    */

/*    try {
      // Runtime.getRuntime().exec( args);

      Process p = Runtime.getRuntime().exec(args);

      BufferedReader stdInput = new BufferedReader(new
          InputStreamReader(p.getInputStream()));

      BufferedReader stdError = new BufferedReader(new
          InputStreamReader(p.getErrorStream()));

      // read the output from the command

      System.out.println("Here is the standard output of the command:\n");
      String s = null;
      while ((s = stdInput.readLine()) != null) {
        System.out.println(s);
      }

      // read any errors from the attempted command

      System.out.println("Here is the standard error of the command (if any):\n");
      while ((s = stdError.readLine()) != null) {
        System.out.println(s);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
 */