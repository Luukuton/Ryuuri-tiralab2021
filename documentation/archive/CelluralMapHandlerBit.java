package ryuuri.mapgen;

import java.util.BitSet;
import java.util.Random;

public class CelluralMapHandlerBit {
    public BitSet[] map;
    private final int width, height, aliveChance, deathLimit, birthLimit;

    private final Random rand;
    private final long seed;

    private int logicVersion;

    /**
     * Constructor
     *
     * @param width Width in pixels as integer
     * @param height Height in pixels as integer
     * @param aliveChance The chance for the cells (pixels) to die as integer
     * @param steps How many simulation steps to do as integer
     * @param seed Seed for the random number generator as integer
     */
    public CelluralMapHandlerBit(int width, int height, int aliveChance, int steps, long seed, int logicVersion) {
        this.width = width;
        this.height = height;
        this.aliveChance = aliveChance;
        this.birthLimit = 4;
        this.deathLimit = 3;
        this.logicVersion = logicVersion;

        this.seed = (seed == 0) ? new Random().nextLong() : seed;
        rand = new Random(this.seed);

        System.out.println("Seed: " + this.seed);

        map = new BitSet[width];
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
    public CelluralMapHandlerBit(int width, int height, int aliveChance, int steps) {
        this(width, height, aliveChance, steps, 0, 1);
    }

    /** Initializes the dungeon matrix **/
    public void initializeMap() {
        for (int i = 0; i < width; i++) {
            map[i] = new BitSet(height);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((rand.nextInt(100 - 1) + 1) < aliveChance) {
                    map[x].set(y, true);
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
            BitSet[] newMap = new BitSet[width];

            for (int j = 0; j < width; j++) {
                newMap[j] = new BitSet(height);
            }

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (logicVersion == 1) {
                        newMap[x].set(y, dungeonLogic(x, y));
                    } else if (logicVersion == 2) {
                        newMap[x].set(y, dungeonLogicV2(x, y));
                    }
                }
            }

            System.arraycopy(newMap, 0, map, 0, width);
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

                if (neighbourX < 0 || neighbourY < 0 || neighbourX >= width || neighbourY >= height) {
                    count = count + 1;
                } else if (map[neighbourX].get(neighbourY)) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    /**
     * Determines if the cell will be a wall
     *
     * @param x X coordinate as integer
     * @param y Y coordinate as integer
     * @return 1 (wall) or 0 (open path)
     */
    private boolean dungeonLogic(int x, int y) {
        int alive = countAliveNeighbours(x, y);

        if (map[x].get(y)) {
            return alive >= deathLimit;
        } else {
            return alive > birthLimit;
        }
    }

    /**
     * Possibly a better version of the function 'dungeonLogic' when it comes to the connectivity of the dungeon.
     *
     * @param x X coordinate as integer
     * @param y Y coordinate as integer
     * @return 1 (wall) or 0 (open path)
     */
    private boolean dungeonLogicV2(int x, int y) {
        int alive = countAliveNeighbours(x, y);

        if (map[x].get(y)) {
            if (alive >= 4) {
                return true;
            }

            if (alive < 2) {
                return false;
            }

        } else {
            return alive >= 5;
        }
        return false;
    }

    /**
     * Recursive function that returns the amount of connected cells at starting point x,y in a map
     *
     * @param x X coordinate as integer
     * @param y Y coordinate as integer
     * @param map integer 2D matrix to search
     * @return the amount of connected cells as integer
     */
    private int connectedCells(int x, int y, int[][] map) {
        int above = 0;
        int below = 0;
        int right = 0;
        int left = 0;
        map[x][y] = 2;

        if ((x - 1 >= 0) && map[x - 1][y] == 0) {
            above = connectedCells(x - 1, y, map);
        }
        if ((x + 1 < width) && map[x + 1][y] == 0) {
            below = connectedCells(x + 1, y, map);
        }
        if ((y + 1 < height) && map[x][y + 1] == 0) {
            right = connectedCells(x, y + 1, map);
        }
        if ((y - 1 >= 0) && map[x][y - 1] == 0) {
            left = connectedCells(x, y - 1, map);
        }

        return above + below + right + left + 1;
    }

    /**
     * Returns true if the dungeon is connected and false if not.
     *
     * @return true if connected
     */
    public boolean isDungeonConnected() {
        // All cells
        int openTileCount = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!map[x].get(y)) {
                    ++openTileCount;
                }
            }
        }

        // Connected cells
        int[][] intMap = new int[width][height];
        int startX = 0;
        int startY = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                intMap[x][y] = map[x].get(y) ? 1 : 0;

                if (!map[x].get(y) && startX == 0 && startY == 0) {
                    startX = x;
                    startY = y;
                }
            }
        }
        int connectedCells = connectedCells(startX, startY, intMap);

        return connectedCells - openTileCount == 0;
    }

    /**
     * Returns the seed
     *
     * @return The seed as long
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Sets the version of the algorithm logic
     *
     * @param version Version in int (1 or 2)
     */
    public void setLogicVersion(int version) {
        logicVersion = version;
    }

    /**
     * Returns a string version of the dungeon
     *
     * @return A string map of the matrix
     */
    public String mapToString() {
        StringBuilder returnString = new StringBuilder();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x].get(y)) {
                    returnString.append("#");
                } else {
                    returnString.append(".");
                }
            }
            returnString.append("\n");
        }
        return returnString.toString();
    }
}
