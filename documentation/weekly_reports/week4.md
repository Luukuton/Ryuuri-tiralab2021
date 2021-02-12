# Report for week 4

**Hours: 15**

### Stuff done during this week:

- Flood fill (and a some helpers for it) for creating connected dungeons
- Testing differences between **int[][]**, **BitSet[]**, and **boolean[][]**
- More benchmarking
- GUI
  - New parameters
  - Other improvements
- Documentation
  - User guide
  - Testing document
  - Implementation document

Hardware: Ryzen 1700X @ 3.8GHz, 16GB DDR4 @ 3200MHz CL14

#### int[][]
> Benchmark STANDARD_OUT\
Time (100x100 & 1 000 000 steps): 217883ms\
Time (1000x1000 & 1000 steps): 22190ms

#### BitSet[]
> Benchmark STANDARD_OUT\
Time (100x100 & 1 000 000 steps): 320207ms\
Time (1000x1000 & 1000 steps): 32840ms

#### boolean[][]
> Benchmark STANDARD_OUT\
Time (100x100 & 1 000 000 steps): 216304ms\
Time (1000x1000 & 1000 steps): 22243ms

The memory usage seemed to be about the same across all versions.

I will be using 2D integer matrix for the algorithm as it seems to be the fastest at least with Java 11 (OpenJDK).

Might to additional testing on this in the coming weeks if I have the time.

The code made for the BitSet implementation is currently in a [archive directory](../archive).

### Problems

Nothing this time!

### Next week

More benchmarking and O() notations.
Currently, too large dungeons fail when trying to find a traversable version of them due to the stack size limit of Java. Will try to optimize and improve the flood fill.
