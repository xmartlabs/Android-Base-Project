# BigBang

[![CircleCI](https://circleci.com/gh/xmartlabs/Android-Base-Project.svg?style=svg)](https://circleci.com/gh/xmartlabs/Android-Base-Project)
[![codebeat badge](https://codebeat.co/badges/af8770f0-d2bf-47d1-a504-6dee56b99312)](https://codebeat.co/projects/github-com-xmartlabs-android-base-project-master)
[![Download](https://api.bintray.com/packages/xmartlabs/Android-Base-Project/Core/images/download.svg)](https://bintray.com/xmartlabs/Android-Base-Project/Core/_latestVersion)
[![Download](https://api.bintray.com/packages/xmartlabs/Android-Base-Project/DbFlow/images/download.svg)](https://bintray.com/xmartlabs/Android-Base-Project/DbFlow/_latestVersion)
[![Download](https://api.bintray.com/packages/xmartlabs/Android-Base-Project/Retrofit/images/download.svg)](https://bintray.com/xmartlabs/Android-Base-Project/Retrofit/_latestVersion)
[![Download](https://api.bintray.com/packages/xmartlabs/Android-Base-Project/Ui/images/download.svg)](https://bintray.com/xmartlabs/Android-Base-Project/Ui/_latestVersion)


This is [XMARTLABS](https://xmartlabs.com) Android base project. Architecturally composed of a set of libraries for building modern and scalable applications from the ground up.
<br>The BigBang of our Android projects.

Our architecture divides the typical layers of an Android application into simple coherent modules, which are completely independent from one another:
* UI
* Services
* Database
* Crashlytics

In order to keep modules independent, we decoupled the behavior required for each layer in another module, which we call `core`. This not only allows you to choose which modules to use, but also gives you independence from any service or database oriented libraries we use.

Learn more about the project on the [Wiki](https://github.com/xmartlabs/Android-Base-Project/wiki) or by reading the Javadocs.

## Getting started

The first step is to include the Core library into your project as a Gradle compile dependency:

```groovy
compile 'com.xmartlabs.base:core:0.1.10'
```

Then, add jCenter to the buildscript repositories:

```groovy
buildscript {
    repositories {
        jcenter()
    }
}
```

Also, you'll need to add the following repositories:

```groovy
repositories {
    flatDir {
        dirs 'libs'
    }
    maven { url "https://jitpack.io" }
    maven { url 'https://maven.fabric.io/public' }
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

Then, include any of the following compile dependencies to add an specific module to your app:
```groovy
compile 'com.xmartlabs.base:dbflow:0.1.4'
compile 'com.xmartlabs.base:retrofit:0.1.2'
compile 'com.xmartlabs.base:ui:0.1.1'
```

The dbflow module is `database` related, while the `retrofit` module is service related.

## Architecture

![Architecture](architecture.png)

There are four main components. The `core` component exposes the 
interfaces needed to implement a certain type of service, like database or network related.
Thus, the `core` component should always be included in any project that makes use of this
architecture.

A component should encapsulate and provide a single functionality. If the component belongs to a certain category of services (like networking or database), then the functionality
should be specified and described in the core component.

## Project template

In order to start using the modules, you'll need to set up a few things, which are well
explained in their respective Wiki. Therefore, to start a new project, we provide a
template, which consists of the minimal setup required to get you going.
You will find that in the `template` module.

## Style guide

We strictly follow [our style guide](https://github.com/xmartlabs/Android-Style-Guide), which extends from the [Google Java Style Guide](http://google.github.io/styleguide/javaguide.html) but with some modifications. Please remember to follow it when contributing to this project.

## Bugs and Feedback

When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible. Please also make sure your code compiles and try to cover your code with tests.
<br><b>We welcome contributors!</b>

For bugs, issues, feature requests, questions and discussions please use the [Github Issues](https://github.com/xmartlabs/Android-Base-Project/issues).

## Authors

* [Santiago Castro](https://github.com/bryant1410)
* [Miguel Revetria](https://github.com/m-revetria)
* [Matías Irland](https://github.com/matir91)
* [Michael Ellis](https://github.com/michaelEllisUy)
* [Diego Medina](https://github.com/diegomedina248)
* [Santiago Casas](https://github.com/chacaa)

## Change Log

This can be found in the [CHANGELOG.md](CHANGELOG.md) file.



