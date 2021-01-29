# Specifications

## Topic

Random dungeon generator which could be used in games and simulations. Ryuuri's aim is to generate very natural caves or dungeons. The output is currently in text and image formats.

It also includes a graphical interface where testing different parameters is easy. For testing benchmarking I'm using JUnit 5.

## Algorithm

The algorithm Ryuuri uses is based on [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).

## Image

The image is created pixel by pixel from the 2D matrix that the algorithm outputs. The generated image can also be scaled in any direction without loss in image quality.

## UI

Made with JavaFX 15.

Sliders use [this (modified) gist](https://gist.github.com/jewelsea/1962045) for dynamic text fields.

