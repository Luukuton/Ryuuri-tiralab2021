# Implementation

## Data types

A 2D matrix (int[][]) is used for the dungeon. It seems to be the fastest and most memory efficient one for the time being.

Argument against a boolean 2D matrix (boolean[][]) **[1]**:

> Although the Java Virtual Machine defines a boolean type, it only provides very limited support for it. There are no Java Virtual Machine instructions solely dedicated to operations on boolean values. Instead, expressions in the Java programming language that operate on boolean values are compiled to use values of the Java Virtual Machine int data type.

> In Oracle’s Java Virtual Machine implementation, boolean arrays in the Java programming language are encoded as Java Virtual Machine byte arrays, using 8 bits per boolean element.


Argument against a BitSet array (BitSet[]):

#### int[][]
> Benchmark STANDARD_OUT
Time (100x100 & 1 000 000 steps): 217883ms
Time (1000x1000 & 1000 steps): 22190ms

#### BitSet[]
> Benchmark STANDARD_OUT
Time (100x100 & 1 000 000 steps): 320207ms
Time (1000x1000 & 1000 steps): 32840ms

The code made for the BitSet implementation is currently in [archive directory](../archive).

## Inputs

Maximum values are the same as denoted in the GUI. Actual maximum corresponds to the maximum of Java's integer and long values. They are used during benchmarking

- Dungeon **Width** (integer, 1 - 10000 px)
- Dungeon **Height** (integer, 1 - 10000 px)
- **Probability** for the cell (pixel) to live a cycle (integer, 0 - 100%)
- **Simulation steps** or **cycles** (integer, 0 - 10000) for the generation
- **Seed** for the Random Number Generator (long, can be locked)
    - When set to 0 or unlocked, the seed will be random
    - Currently using Java's implementation of Random
- Algorithm version (currently 1 or 2)

### Algorithm V1

- **Birth** and **death limits** for cells (pixels)
    - Currently hard coded as 4 and 3

### Algorithm V2

- **Birth**, **secondary birth** and **death limits** for cells (pixels)  **[2]**
    - Currently hard coded as 4, 5 and 2

## Sources
The algorithm, "living" cells, Ryuuri uses is based on [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).

##### [1]

_Chapter 2. The Structure of the Java Virtual Machine_ - [docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.3.4](https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.3.4) (2021/02/12)

##### [2]

_Cellular Automata Method for Generating Random Cave-Like Levels_ - [roguelikedevelopment.org](http://roguebasin.roguelikedevelopment.org/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels) (2021/02/12)
