package com.vg.data.gradle.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.gradle.api.Action;
import org.gradle.api.file.CopySpec;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.copy.DefaultCopySpec;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.bundling.Jar;

@CacheableTask
public class Nar extends Jar {
    public static final String NAR_EXTENSION = "nar";

    private static final Spec<File> IS_FILE = new Spec<File>() {
        @Override
        public boolean isSatisfiedBy(File element) {
            return element.isFile();
        }
    };

    private FileCollection classpath;
    private final DefaultCopySpec webInf;

    public Nar() {
        setExtension(NAR_EXTENSION);

        webInf = (DefaultCopySpec) getRootSpec().addChildBeforeSpec(getMainSpec()).into("META-INF");
        webInf.into("bundled-dependencies", new Action<CopySpec>() {
            public void execute(CopySpec it) {
                it.from(new Callable<Iterable<File>>() {
                        public Iterable<File> call() {
                            FileCollection classpath = getClasspath();
                            return classpath != null ? classpath.filter(IS_FILE) : Collections.<File>emptyList();
                        }
                    });

            }

        });
    }

    @Optional
    @Classpath
    public FileCollection getClasspath() {
        return classpath;
    }

    public void classpath(Object... classpath) {
        FileCollection oldClasspath = getClasspath();
        this.classpath = getProject().files(oldClasspath != null ? oldClasspath : new ArrayList(), classpath);
    }

    public void setNarId(String narId) {
        Map<String, String> m= new HashMap<>();
        m.put("Nar-Id", narId);
        getManifest().attributes(m);
    }
}
