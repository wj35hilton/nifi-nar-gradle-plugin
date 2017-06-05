package com.vg.data.gradle.plugins;

import java.util.concurrent.Callable;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.artifacts.publish.ArchivePublishArtifact;
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

public class NarPlugin implements Plugin<Project> {
    public static final String NAR_TASK_NAME = "nar";

    @Override
    public void apply(final Project project) {
        project.getPluginManager().apply(JavaPlugin.class);

        final NarPluginConvention pluginConvention = new NarPluginConvention(project);
        project.getConvention().getPlugins().put("nar", pluginConvention);


        project.getTasks().withType(Nar.class, new Action<Nar>() {
            public void execute(Nar task) {
                task.classpath(new Object[] {new Callable() {
                    public Object call() throws Exception {
                        FileCollection runtimeClasspath = project.getConvention().getPlugin(JavaPluginConvention.class)
                                .getSourceSets().getByName(SourceSet.MAIN_SOURCE_SET_NAME).getRuntimeClasspath();
                        return runtimeClasspath;
                    }
                }});
            }
        });


        Nar nar = project.getTasks().create(NAR_TASK_NAME, Nar.class);
        nar.setGroup(BasePlugin.BUILD_GROUP);
        // TODO: nar id here????
        nar.setNarId(project.getName());
        ArchivePublishArtifact narArtifact = new ArchivePublishArtifact(nar);
        project.getExtensions().getByType(DefaultArtifactPublicationSet.class).addCandidate(narArtifact);
    }

    // TODO: build should not include nifi-api-x.x.x.jar
    // maybe adding 'provided.....' properties ala:
    // https://github.com/gradle/gradle/blob/master/subprojects/plugins/src/main/java/org/gradle/api/plugins/WarPlugin.java
    // Will do it.

    // TODO: need to prevent creation of empty jar ... war plugin does this (how?)
}
