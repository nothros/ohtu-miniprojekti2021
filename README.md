# ohtu-miniprojekti2021 ![workflow](https://github.com/nothros/ohtu-miniprojekti2021/actions/workflows/main.yml/badge.svg)
[![codecov](https://codecov.io/gh/nothros/ohtu-miniprojekti2021/branch/main/graph/badge.svg?token=T5B64DEFPH)](https://codecov.io/gh/nothros/ohtu-miniprojekti2021)
Käyttö:


Linux: ```$gradle run```

Windows: ``` $gradlew.bat run ```

## Tiedostorakenne
```
.
├── app
│   ├
│   ├── build.gradle
│   └── src                          	<<<< Sovelluslogiikka
│       ├── main
│       │   ├── java
│       │   │   └── kurssikirjahylly
│       │   │       └── App.java
│       │   └── resources
│       └── test			            <<<< Testitiedostot
│           ├── java
│           │   └── kurssikirjahylly
│           │       └── AppTest.java
│           └── resources
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── README.md
└── settings.gradle
```

## Backlog

[Backlog](https://docs.google.com/spreadsheets/d/1Mqu61MkBKXb47hqxVo3GOnbK3Os7-lqfA4JLydHTWgk/edit?usp=sharing)

# Gradle


## Riippuvuuksia
Riippuvuudet lisätään ```build.gradle```-tiedostoon.[Lisätietoa](https://docs.gradle.org/current/userguide/dependency_management_for_java_projects.html).


Mukana:
- [JDBC](https://github.com/xerial/sqlite-jdbc)
- [JavaFX](https://openjfx.io/)
- [TestFX](https://github.com/TestFX/TestFX)
