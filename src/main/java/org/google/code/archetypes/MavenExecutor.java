package org.google.code.archetypes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import org.google.code.archetypes.data.Group;
import org.google.code.archetypes.data.Archetype;
import org.jetbrains.idea.maven.core.MavenCoreSettings;
import org.jetbrains.idea.maven.core.MavenCoreImpl;
import org.jetbrains.idea.maven.core.MavenCore;
import org.jetbrains.idea.maven.core.util.MavenEnv;
import org.jetbrains.idea.maven.project.MavenException;
import org.jetbrains.idea.maven.runner.executor.MavenRunnerParameters;
import org.jetbrains.idea.maven.runner.MavenRunnerSettings;
import org.jetbrains.idea.maven.runner.MavenRunnerImpl;
import org.apache.maven.embedder.*;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.execution.MavenExecutionRequest;

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

     MavenCore mavenCore = new MavenCoreImpl();

     mavenCore.loadState(new MavenCoreSettings() {
       public MavenEmbedder createEmbedder() throws MavenException {
         //return MavenEnv.createEmbedder(getMavenHome(), getEffectiveLocalRepository(), getMavenSettingsFile(), getClass().getClassLoader());

         DefaultConfiguration configuration = new DefaultConfiguration();
         configuration.setLocalRepository(getEffectiveLocalRepository());

         MavenEmbedderConsoleLogger l = new MavenEmbedderConsoleLogger();
         l.setThreshold(2);
         configuration.setMavenEmbedderLogger(l);

         File userSettingsFile = MavenEnv.resolveUserSettingsFile(getMavenSettingsFile());

         if(userSettingsFile != null) {
           configuration.setUserSettingsFile(userSettingsFile);
         }
         File globalSettingsFile = MavenEnv.resolveGlobalSettingsFile(getMavenHome());
         if(globalSettingsFile != null) {
           configuration.setGlobalSettingsFile(globalSettingsFile);
         }

         configuration.setClassLoader(getClass().getClassLoader());
         ConfigurationValidationResult result = MavenEmbedder.validateConfiguration(configuration);
         if(!result.isValid()) {
           //noinspection RedundantArrayCreation
           throw new MavenException(Arrays.asList(new Exception[] {
             result.getGlobalSettingsException(),
             result.getUserSettingsException()
           }));
         }

         System.setProperty("maven.home", getMavenHome());

         try {
           return new MavenEmbedder(configuration) {
          public MavenExecutionResult execute(MavenExecutionRequest request) {
             request.setBaseDirectory(new File(workingDir));
             request.setPom(null);

             return super.execute(request);
           }
           };
         }
         catch(MavenEmbedderException e) {
           throw new MavenException(e);
         }
       }
     });

    MavenRunnerImpl builder = new MavenRunnerImpl(project, mavenCore);

     MavenRunnerSettings builderState = builder.getState();
     Map<String, String> mavenProperties = builderState.getMavenProperties();

     mavenProperties.put("archetypeGroupId", archetypeGroup.getGroupId());
     mavenProperties.put("archetypeArtifactId", archetype.getName());
     mavenProperties.put("archetypeVersion", archetype.getVersion());
     mavenProperties.put("groupId", groupId);
     mavenProperties.put("artifactId", artifactId);
     mavenProperties.put("version", version);
     mavenProperties.put("remoteRepositories", remoteRepositories);
    // mavenProperties.put("user.dir", workingDir);

     File projectDir = new File(workingDir + "/" + artifactId);

     if (projectDir.exists() && projectDir.list().length > 0) {
       WarningDialog dialog = new WarningDialog(
           project,
           workingDir + File.separatorChar + artifactId,
           "Output directory: " + workingDir + File.separatorChar + artifactId + "  shouldn't exist!",
           "Warning",
            Messages.getInformationIcon());
       dialog.show();
     }
     else {
       if (!builder.isRunning()) {
         List<String> goals = new ArrayList<String>();
         goals.add("archetype:create");

         MavenRunnerParameters buildParameters = new MavenRunnerParameters(workingDir, goals, null);

         buildParameters.setGoals(goals);

         builder.closeToolWindow();
         builder.run(buildParameters);
       }
     }
   }

}
