# ohtu-miniprojekti2021


![workflow](https://github.com/nothros/ohtu-miniprojekti2021/actions/workflows/main.yml/badge.svg)
 [![codecov](https://codecov.io/gh/nothros/ohtu-miniprojekti2021/branch/main/graph/badge.svg?token=T5B64DEFPH)](https://codecov.io/gh/nothros/ohtu-miniprojekti2021)


Tämä on Helsingin Yliopiston ohjelmistotuotanto-kurssin miniprojekti. Sovellukseen on mahdollista tallentaa lukuvinkkejä (Kirja, Blogi tai Podcast) ja viiteitä käyttäjän syöttämiin kursseihin sekä erilaisia omia tageja. Sovellus käyttää SQLite-tietokantaa. Graafinen käyttöliittymä on toteutettu JavaFX:llä.

## Käyttöohjeet

Linux: ```$gradle run``` tai ``` ./gradlew run``` 

Windows: ``` $gradlew.bat run ```

**JAR-tiedoston luonti** ``` ./gradlew shadowJar``` 

**Testit** ``` $gradle test``` tai ``` ./gradlew test```

**Checkstyle** ``` $gradle checkstyleMain``` tai ``` ./gradlew checkstyleMain```

**jacoco** ``` $gradle jacocoTestReport``` tai ``` ./gradlew jacocoTestReport```

# Dokumentaatio
## Definition of done

- Automaattisessa yksikkötestauksessa edellytämme 70% rivi- ja haaraumakattavuutta.
- Testien tulee olla relevantteja ja selkeästi luettavia. Niiden pitää mennä läpi lokaalisti ja GitHub Actionissa.
- Ylläpidämme koodin luettavuutta Checkstyle:n avulla. 
- Luokka- ja metodijaon tulee olla järkevä.
- Ohjelma ei hajoa sitä käytettäessä

## Backlog

[Backlog](https://docs.google.com/spreadsheets/d/1Mqu61MkBKXb47hqxVo3GOnbK3Os7-lqfA4JLydHTWgk/edit?usp=sharing)

## Loppuraportti

[Loppuraportti](https://github.com/nothros/ohtu-miniprojekti2021/blob/main/documents/Ohtu2021-loppuraportti-kurssikirjahylly.pdf)
## User Manual

[User Manual](https://github.com/nothros/ohtu-miniprojekti2021/blob/main/documents/User%20Manual.md)

# Riippuvuudet

Riippuvuudet lisätään ```build.gradle```-tiedostoon. [Lisätietoa](https://docs.gradle.org/current/userguide/dependency_management_for_java_projects.html).

Mukana:
- [JDBC](https://github.com/xerial/sqlite-jdbc)
- [JavaFX](https://openjfx.io/)
- [TestFX](https://github.com/TestFX/TestFX)
- [Cucumber](https://cucumber.io/)


## Tiedostorakenne
```
.
├── app
│   ├── build.gradle
│   ├── src
│   │   ├── main
│   │   │   └── java
│   │   │       └── bookcase
│   │   │           ├── App.java
│   │   │           ├── dao
│   │   │           │   ├── CourseDAO.java
│   │   │           │   ├── DAO.java
│   │   │           │   ├── LibraryObjectDAO.java
│   │   │           │   └── TagDAO.java
│   │   │           ├── domain
│   │   │           │   └── LibraryObject.java
│   │   │           ├── logic
│   │   │           │   └── LibraryService.java
│   │   │           └── ui
│   │   │               └── AppUi.java
│   │   └── test
│   │       ├── java
│   │       │   └── bookcase
│   │       │       ├── dao
│   │       │       │   ├── CourseDaoTest.java
│   │       │       │   ├── LibraryObjectDaoTest.java
│   │       │       │   └── TagDaoTest.java
│   │       │       ├── logic
│   │       │       │   └── ServiceTest.java
│   │       │       └── ui
│   │       │           ├── RunCucumberTest.java
│   │       │           ├── StepDefinitions.java
│   │       │           └── TestFXBase.java
│   │       └── resources
│   │           ├── bookcase
│   │           │   ├── addblogpost.feature
│   │           │   ├── addbook.feature
│   │           │   └── addpodcast.feature
│   │           └── cucumber.properties
│   └── test.db
├── codecov.yml
├── config
│   └── checkstyle
│       ├── checkstyle.xml
│       └── suppressions.xml
├── documents
│   └── User Manual.md
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── LICENSE
├── README.md
└── settings.gra
```
