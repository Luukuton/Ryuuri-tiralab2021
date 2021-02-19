# Testing

All testing is done with JUnit 5 using its tags (@Tag) and parameters (@ParameterizedTest) 
to differentiate unit tests and different benchmarks from each other.

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

![JaCoCo CelluralMapHandler](images/jacococo_CelluralMapHandlerBit.png)

![JaCoCo CelluralMapHandler](images/jacococo_CelluralMapHandlerAbstract.png)

![JaCoCo ImageUtil](images/jacoco_ImageUtil.png)

### Dynamic chart by CodeCov

> The top section represents the entire project. Proceeding with folders and finally individual files. The size and color of each slice is representing the number of statements and the coverage, respectively.

![CodeCove icicle](https://codecov.io/gh/Luukuton/Ryuuri-tiralab2021/branch/main/graphs/icicle.svg)

## Benchmarking

In contrast with unit testing, benchmarking can take up a lot of time.
Currently, on system with __Ryzen 1700X @ 3.8GHz and 16GB DDR4 @ 3200MHz CL14__:

int[][] algorithm benchmarked with `gradlew clean benchmark`:

```
Benchmark STANDARD_OUT
    Benchmark results (int[][]):
    19696ms | 30000 random 100 * 100 px, 2 steps, V1 dungeons
    19656ms | 30000 random 100 * 100 px, 2 steps, V2 dungeons
    22095ms | 30000 random 100 * 100 px, 2 steps, V1 traversable dungeons
    23469ms | 30000 random 100 * 100 px, 2 steps, V2 traversable dungeons
    18924ms | 1000 * 1000 px, 1000 steps, 40%, V1,
    19176ms | 1000 * 1000 px, 1000 steps, 40%, V2,
    19186ms | 1000 * 1000 px, 1000 steps, 40%, V2, traversable
    18984ms | 100 * 100 px, 100000 steps, 40%, V1,
```

BitSet[] algorithm benchmarked with `gradlew clean benchmark -Dtest.type=bit`:
```
Benchmark STANDARD_OUT
    Benchmark results (BitSet[][]):
    26675ms | 30000 random 100 * 100 px, 2 steps, V1 dungeons
    24950ms | 30000 random 100 * 100 px, 2 steps, V2 dungeons
    29074ms | 30000 random 100 * 100 px, 2 steps, V1 traversable dungeons
    29879ms | 30000 random 100 * 100 px, 2 steps, V2 traversable dungeons
    25136ms | 1000 * 1000 px, 1000 steps, 40%, V1,
    25330ms | 1000 * 1000 px, 1000 steps, 40%, V2,
    25384ms | 1000 * 1000 px, 1000 steps, 40%, V2, traversable
    25306ms | 100 * 100 px, 100000 steps, 40%, V1,
```
