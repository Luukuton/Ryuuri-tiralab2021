# Specifications

### Bachelor of Computer Science (2nd year)

## Topic

Random dungeon generator which could be used in games and simulations. Ryuuri's aim is to generate natural looking caves or dungeons. The output is currently in text and image formats.

Using Java 11.

It also includes a graphical interface where testing different parameters is easy. For testing and benchmarking JUnit 5 is used.

Everything (code, comments and documentation) is in English.

## Algorithm

The algorithm Ryuuri uses is based on [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life). I found that it's possible to make very natural looking cave or dungeon formations with its implementations.

[Source](http://roguebasin.roguelikedevelopment.org/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels) for inspiration.

### Inputs:
- **Width** in pixels (integer, >= 1)
- **Height** in pixels (integer, >= 1)
- **Probability** for the cell (pixel) to live a cycle (integer, 0 - 100)
- **Simulation steps** or **cycles** (integer, >= 0)
- **Seed** for the Random Number Generator (long)
  - Currently using Java's implementation of Random
- **Birth** and **Death limits** for cells (pixels) if found necessary
  - Currently hard coded as 4 and 3

### Execution time target
- **Aiming for O(n^2) because of 2D matrices. Possibly faster!**
- Less than 1 s for small-sized data sets (parameters in hundreds)
- Less than 10 s for medium-sized data sets (parameters in thousands)
- Currently, no specific target for large-sized data sets, but possibly less than 10 minutes (parameters in tens of thousands)

### Memory target
- Less than 10 MiB for small-sized data sets (parameters in hundreds)
- Less than 100 MiB for medium-sized data sets (parameters in thousands)
- Currently, no specific target for large-sized data sets, but possibly less than 1 GiB (parameters in tens of thousands)

## Image

The image is created pixel by pixel from the 2D matrix that the algorithm outputs. The generated image can also be scaled in any direction without loss in image quality.

### Inputs:
- **Width** in pixels (integer, >= 1)
- **Height** in pixels (integer, >= 1)
- **Factor of X axis** for scaling (integer, >= 1)
- **Factor of Y axis** for scaling (integer, >= 1)

## UI

Made with JavaFX 15.

- [This gist](https://gist.github.com/jewelsea/1962045) (heavily modified) is used with sliders for dynamic text fields.

- [This gist](https://gist.github.com/james-d/ce5ec1fd44ce6c64e81a) (heavily modified) is used for zooming the image in the GUI.

