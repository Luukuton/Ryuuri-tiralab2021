# Testing

All testing is done with JUnit 5 using its tagging system to differentiate benchmarks and unit tests from each other.

## Unit tests

Performed with:
```
gradlew test
```
Unit tests are basically instantaneous and make sure that the algorithm and image utility both are working correctly. 
The JavaFX GUI has been excluded from these.

The tests are made mainly with the help of pre-generated data and seeds for the random number generator. 
ImageUtil class is tested with two images found in the test resources.

## Test coverage

### JaCoCo

![JaCoCo all](images/jacoco_all.png)

![JaCoCo CelluralMapHandler](images/jacococo_CelluralMapHandler.png)

![JaCoCo ImageUtil](images/jacoco_ImageUtil.png)

### Dynamic chart by CodeCov

> The top section represents the entire project. Proceeding with folders and finally individual files. The size and color of each slice is representing the number of statements and the coverage, respectively.

![CodeCove icicle](https://codecov.io/gh/Luukuton/Ryuuri-tiralab2021/branch/main/graphs/icicle.svg)

## Benchmarking

Performed with:
```
gradlew clean benchmark
```

In contrast with unit testing, benchmarking can take up a lot of time. 
Currently, on system with __Ryzen 1700X @ 3.8GHz and 16GB DDR4 @ 3200MHz CL14__:

```
Benchmark STANDARD_OUT
    Time (100x100px / 100 000 steps): 22822ms
    Time (1000x1000px / 1000 steps / Logic V1): 22344ms
    Time (1000x1000px / 1000 steps / Logic V2): 21405ms
    Time (1000x1000px / 1000 steps / Logic V2 / traversable): 21452ms
```
