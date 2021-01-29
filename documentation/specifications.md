# Specifications

### Bachelor of Computer Science (2nd year)

## Topic

Random dungeon generator which could be used in games and simulations. Ryuuri's aim is to generate natural looking caves or dungeons. The output is currently in text and image formats.

Using Java 11.

It also includes a graphical interface where testing different parameters is easy. For testing benchmarking I'm using JUnit 5.

## Algorithm

The algorithm Ryuuri uses is based on [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life). I found that it's possibly to make very natural looking cave or dungeon formations with it.

[Source](http://roguebasin.roguelikedevelopment.org/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels) for inspiration.

### Inputs:
- **Width** in pixels (integer, >= 1)
- **Height** in pixels (integer, >= 1)
- **Probability** for the cell (pixel) to live a cycle (integer, 0 - 100)
- **Simulation steps** or **cycles** (integer, >= 0)
- **Seed** for the Random Number Generator (integer)
  - Currently using Java's implementation of Random
- **Birth** and **Death limits** for cells (pixels) if found necessary 
   - Currently hard coded as 4 and 3


## Image

The image is created pixel by pixel from the 2D matrix that the algorithm outputs. The generated image can also be scaled in any direction without loss in image quality.

### Inputs:
- **Width** in pixels (integer, >= 1)
- **Height** in pixels (integer, >= 1)
- **Factor of X axis** for scaling (integer, >= 1)
- **Factor of Y axis** for scaling (integer, >= 1)

## UI

Made with JavaFX 15.

Sliders use [this (modified) gist](https://gist.github.com/jewelsea/1962045) for dynamic text fields.
