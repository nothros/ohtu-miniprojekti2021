# ohtu-miniprojekti2021
Käyttö:
Linux: ```$gradle run``` tai ``` $./gradlew run ```

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

# Gradle


## Riippuvuuksia
Riippuvuudet lisätään ```build.gradle```-tiedostoon.[Lisätietoa](https://docs.gradle.org/current/userguide/dependency_management_for_java_projects.html).


Mukana:
- [JDBC](https://github.com/xerial/sqlite-jdbc).
