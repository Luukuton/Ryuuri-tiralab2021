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

## Benchmarking

Performed with:
```
gradlew clean benchmark
```

In contrast with unit testing, benchmarking can take up a lot of time. 
Currently, on system with __Ryzen 1700X @ 3.8GHz and 16GB DDR4 @ 3200MHz CL14__ it takes around 4 minutes.

There are currently two large text files in the test resources which include pre-generated data to test against the benchmark data.