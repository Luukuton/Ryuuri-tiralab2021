# Implementation

## Data types

A 2D matrix (int[][]) is used for the dungeon. It seems to be the fastest and most memory efficient one for the time being.

Argument against a boolean 2D matrix (boolean[][]) **[1]**:

> Although the Java Virtual Machine defines a boolean type, it only provides very limited support for it. There are no Java Virtual Machine instructions solely dedicated to operations on boolean values. Instead, expressions in the Java programming language that operate on boolean values are compiled to use values of the Java Virtual Machine int data type.

> In Oracle’s Java Virtual Machine implementation, boolean arrays in the Java programming language are encoded as Java Virtual Machine byte arrays, using 8 bits per boolean element.

Argument against a BitSet array (BitSet[]):

#### int[][]
> Benchmark STANDARD_OUT\
Time (100x100 & 1 000 000 steps): 217883ms\
Time (1000x1000 & 1000 steps): 22190ms

#### BitSet[]
> Benchmark STANDARD_OUT\
Time (100x100 & 1 000 000 steps): 320207ms\
Time (1000x1000 & 1000 steps): 32840ms

## Inputs

Maximum values are the same as denoted in the GUI. Actual maximum corresponds to the maximum of Java's integer and long values. They are used during benchmarking

- Dungeon **Width** (integer, 1 - 10000 px)
- Dungeon **Height** (integer, 1 - 10000 px)
- **Probability** for the cell (pixel) to live a cycle (integer, 0 - 100%)
- **Simulation steps** or **cycles** (integer, 0 - 10000) for the generation
- **Seed** for the Random Number Generator (long, can be locked)
    - When set to 0 or unlocked, the seed will be random
    - Java's implementation of Random
- Traversable dungeon (true or false)
    - Flood fill
- Algorithm logic version (currently 1 or 2)
- If there should be outer walls (true or false)

### Initialization

The algorithm will first fill the 2D matrix with ones (wall) and zeroes (open area). 
It'll create an open straight line in the middle (y-axis). 
Also, if the user wanted outer walls, they will be generated here as well.

The main logic happens with the help of Java's random class and the value of **aliveChance**. 
On every cell, the algorithm will determine if it's 1 (aliveChance %) or 0 (100 - aliveChance %).

### Algorithm Logic Ver. 1

- **Birth** and **death limits**  of 4 and 3 for cells (pixels)

### Algorithm Logic Ver. 2

- Fits better when trying to find traversable dungeons
- **Birth**, **secondary birth** and **death limits** of 4, 5 and 2 for cells (pixels)  **[2]**

## Sources
The algorithm, "living" cells, Ryuuri uses is based on [Cellural Automation](https://en.wikipedia.org/wiki/Cellular_automaton).
A well-known example of that is [Conway's Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life).

##### [1]

_Chapter 2. The Structure of the Java Virtual Machine_ - [docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.3.4](https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.3.4) (2021/02/12)

##### [2]

_Cellular Automata Method for Generating Random Cave-Like Levels_ - [roguelikedevelopment.org](http://roguebasin.roguelikedevelopment.org/index.php?title=Cellular_Automata_Method_for_Generating_Random_Cave-Like_Levels) (2021/02/12)
