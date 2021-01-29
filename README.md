# Ryuuri (tiralab - Helsinki 2021)

![Github Actions](https://github.com/Luukuton/Ryuuri-tiralab2021/workflows/Java%20CI%20with%20Gradle/badge.svg)

The name, Ryuuri (流離), is Japanese and means _"to wander in a foreign country far from home"_.

<TODO: Preview images here!>

## Weekly reports

[Week 2](documentation/weekly_reports/week2.md)

[Week 1](documentation/weekly_reports/week1.md)

## Documentation

[User Guide](documentation/user_guide.md)

[Specifications](documentation/specifications.md)

[Implementation](documentation/implementation.md)

[Testing](documentation/testing.md)

## Running

The application can be run with:
```
gradlew run
```

## Building a JAR
For Linux & Windows on Windows:
```
gradlew shadowJar -Dplatform=linux.x86_64
```

## Testing

Tests can be performed with:
```
gradlew test
```

Code coverage can be created with:
```
gradlew test jacocoTestReport
```

Code coverage can be viewed by opening _build/reports/jacoco/test/html/index.html_ in a browser.

## JavaDoc

JavaDoc files can be created with:
```
gradlew javadoc
```

They can be viewed by opening _build/docs/javadoc/index.html_ in a browser.

## CheckStyle

Checks defined in [checkstyle.xml](config/checkstyle/checkstyle.xml) can be executed with:
```
gradlew checkstyleMain
```

Results can be viewed by opening _target/site/checkstyle.html_ in a browser.

## Dependencies
* Java 11+
* Gradle 6.8.1+
