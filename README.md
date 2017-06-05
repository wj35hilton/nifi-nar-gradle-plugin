# nifi-nar-gradle-plugin

## Usage

In your build.gradle file:

```
buildscript {
  repositories {
    mavenLocal()
    maven {
      // for local testing
      url "file:///Users/whilton/work/videology/repo"
    }
  }

  dependencies {
    classpath 'com.vg:nifi-nar-gradle-plugin:1.0'
  }
}

apply plugin: 'nar'
```

## TODO

* nar task description

    clean - Deletes the build directory.

    jar - Assembles a jar archive containing the main classes.

    nar

    testClasses - Assembles test classes.

* provided jars (e.g. nifi-api-x.x.x.jar) should not be included

* 1.2.0 MANIFEST.MF changes
