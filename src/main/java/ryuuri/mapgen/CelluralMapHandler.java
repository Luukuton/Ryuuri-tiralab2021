package ryuuri.mapgen;

import java.util.Arrays;
import java.util.Random;

public class CelluralMapHandler {
    public int[][] map;
    private final int width, height, aliveChance, deathLimit, birthLimit;
    private final Random rand;
    private final long seed;

    /**
     * Constructor
     *
     * @param width Width in pixels as integer
     * @param height Height in pixels as integer
     * @param aliveChance The chance for the cells (pixels) to die as integer
     * @param steps How many simulation steps to do as integer
     * @param seed Seed for the random number generator as integer
     */
    public CelluralMapHandler(int width, int height, int aliveChance, int steps, long seed) {
        this.width = width;
        this.height = height;
        this.aliveChance = aliveChance;
        this.birthLimit = 4;
        this.deathLimit = 3;

        this.seed = (seed == 0) ? new Random().nextLong() : seed;
        rand = new Random(this.seed);

        map = new int[width][height];
        initializeMap();
        doSimulationStep(steps);
    }

    /**
     * Overloading constructor without seed
     *
     * @param width Width in pixels as integer
     * @param height Height in pixels as integer
     * @param aliveChance The chance for the cells (pixels) to die as integer
     * @param steps How many simulation steps to do as integer
     */
    public CelluralMapHandler(int width, int height, int aliveChance, int steps) {
        this(width, height, aliveChance, steps, 0);
    }

    /** Initializes the dungeon matrix **/
    public void initializeMap() {
        for (int i = 0; i < width; i++) {
            Arrays.fill(map[i], 0);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((rand.nextInt(100 - 1) + 1) < aliveChance) {
                    map[x][y] = 1;
                }
            }
        }
    }

    /**
     * Makes one simulation step
     *
     * @param steps The amount of simulation steps to make as integer
     */
    public void doSimulationStep(int steps) {
        for (int i = 0; i < steps; i++) {
            int[][] newMap = new int[width][height];

            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    int nbs = countAliveNeighbours(x, y);

                    if (map[x][y] > 0) {
                        if (nbs < deathLimit) {
                            newMap[x][y] = 0;
                        } else {
                            newMap[x][y] = 1;
                        }
                    } else {
                        if (nbs > birthLimit) {
                            newMap[x][y] = 1;
                        } else {
                            newMap[x][y] = 0;
                        }
                    }
                }
            }

            map = newMap.clone();
        }
    }

    /**
     * Counts all the neighbours that are alive for the specified cell (coordinate) in the matrix
     *
     * @param x X coordinate as integer
     * @param y Y coordinate as integer
     * @return The count of alive neighbours as integer
     */
    public int countAliveNeighbours(int x, int y) {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int neighbourX = x + i;
                int neighbourY = y + j;

                if (neighbourX < 0 || neighbourY < 0 || neighbourX >= map.length || neighbourY >= map[0].length) {
                    count = count + 1;
                } else if (map[neighbourX][neighbourY] == 1) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    /**
     * Returns the seed.
     *
     * @return The seed as long
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Returns a string version of the dungeon
     *
     * @return A string map of the matrix
     */
    public String mapToString() {
        StringBuilder returnString = new StringBuilder();

        String[] mapSymbols = {".", "#", "+"};

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                returnString.append(mapSymbols[(map[column][row])]);
            }
            returnString.append("\n");
        }
        return returnString.toString();
    }
}
