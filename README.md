# Ryuuri (tiralab - Helsinki 2021)

![Github Actions](https://github.com/Luukuton/Ryuuri-tiralab2021/workflows/Java%20CI%20with%20Gradle/badge.svg) [![codecov](https://codecov.io/gh/Luukuton/Ryuuri-tiralab2021/branch/main/graph/badge.svg?token=JU6DOB0RLA)](https://codecov.io/gh/Luukuton/Ryuuri-tiralab2021)

The name, Ryuuri (流離), is Japanese and means _"to wander in a foreign country far from home"_.

![preview of the application](documentation/images/preview.gif)

## Weekly reports

[Week 4](documentation/weekly_reports/week4.md)

[Week 3](documentation/weekly_reports/week3.md)

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

and benchmarks with: 
```
gradlew clean benchmark
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


### Examples of some dungeons

1. 200x200 px, 40 %, 2 steps, x & y scaled by 2, seed: 7482077468331371336
2. 200x200 px, 47 %, 5 steps, x & y scaled by 2, seed: 8684197241994208105

![preview of the application](documentation/images/200x200_47%25_2steps_2x2scale_7482077468331371336.png)
![preview of the application](documentation/images/200x200_40%25_5steps_2x2scale_8684197241994208105.png)