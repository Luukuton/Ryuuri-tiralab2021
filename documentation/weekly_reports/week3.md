# Report for week 1

**Hours: 10** 

### Stuff done during this week:

- Improving the algorithm
  - Two versions of the logic for determining if the cell will be a wall or not
- More testing
- Benchmarking
- Documentation
- GUI
  - Loading icon
  - Different threads for generation and the UI
  - Other improvements


I also found about that boolean and int primitives might be the same when it comes to memory usage depending on the Java Virtual Machine.

> Although the Java Virtual Machine defines a boolean type, it only provides very limited support for it. There are no Java Virtual Machine instructions solely dedicated to operations on boolean values. Instead, expressions in the Java programming language that operate on boolean values are compiled to use values of the Java Virtual Machine int data type.

> In Oracle’s Java Virtual Machine implementation, boolean arrays in the Java programming language are encoded as Java Virtual Machine byte arrays, using 8 bits per boolean element.

[Source](https://docs.oracle.com/javase/specs/jvms/se11/html/jvms-2.html#jvms-2.3.4)

I started experimenting with **int[][]**, **boolean[][]**, **BitSet[]**, and just **int[]** where the integer would be in **binary**. 
I'll be making my decision which to use depending on which one is the most efficient in the coming weeks!

### Problems

I'm not sure how to make sure every part of the dungeon is always connected. 
I made a function which can be used to check if every cell is connected after the generation. 
That doesn't really solve the problem though.

### Next week

Possibly making comparisons with other algorithms and aforementioned datatypes (boolean, int, binary).
