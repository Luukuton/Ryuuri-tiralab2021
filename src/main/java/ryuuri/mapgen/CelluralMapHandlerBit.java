package ryuuri.mapgen;

import java.util.BitSet;
import java.util.Random;

public class CelluralMapHandlerBit extends CelluralMapAbstract<BitSet[]> {
    public BitSet[] map;

    /**
     * Constructor
     *
     * @param width width in pixels as integer
     * @param height height in pixels as integer
     * @param aliveChance the chance for the cells (pixels) to die as integer
     * @param steps how many simulation steps to do as integer
     * @param connected true if the dungeon has to be completely connected (traversable)
     * @param seed seed for the random number generator as long
     */
    public CelluralMapHandlerBit(int width, int height, int aliveChance, int steps, boolean connected, long seed) {
        this.outerWalls = false;
        this.logicVersion = 1;

        this.width = width;
        this.height = height;
        this.aliveChance = aliveChance;
        this.steps = steps;
        this.connected = connected;

        this.seed = (seed == 0) ? new Random().nextLong() : seed;
        this.rand = new Random(this.seed);

        map = new BitSet[width];
        for (int i = 0; i < width; i++) {
            map[i] = new BitSet(height);
        }
    }

    /**
     * Overloads the constructor without seed
     *
     * @param width width in pixels as integer
     * @param height height in pixels as integer
     * @param aliveChance the chance for the cells (pixels) to die as integer
     * @param connected true if the dungeon has to be completely connected (traversable)
     * @param steps how many simulation steps to do as integer
     */
    public CelluralMapHandlerBit(int width, int height, int aliveChance, int steps, boolean connected) {
        this(width, height, aliveChance, steps, connected, 0);
    }

    /**
     * Initializes the dungeon BitSet array
     * **/
    public void generateDungeon() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == (height / 2)) {
                    map[x].set(y, false);
                } else if (outerWalls && (x == 0 || y == 0 || x == width - 1 || y == height - 1)) {
                    map[x].set(y, true);
                } else {

                    map[x].set(y, ((rand.nextInt(100 - 1) + 1) < aliveChance));
                }
            }
        }

        doSimulationStep(steps);
        if (connected) {
            connectDungeon(map);
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
            for (int x = 0; x < width; x++) {
                newMap[x] = new BitSet(height);
                for (int y = 0; y < height; y++) {
                    newMap[x].set(y, dungeonLogic(x, y));
                }
            }
            System.arraycopy(newMap, 0, map, 0, width);
        }
    }

    /**
     * Determines if the cell will be a wall. Two versions embedded.
     *
     * @param x x coordinate as integer
     * @param y y coordinate as integer
     * @return true (wall) or false (open path)
     */
    private boolean dungeonLogic(int x, int y) {
        int alive = countAliveNeighbours(x, y);
        if (logicVersion == 2) {
            if (map[x].get(y)) {
                if (alive >= 4) { // birthLimit
                    return true;
                }
                if (alive < 2) { // deathLimit
                    return false;
                }
            } else {
                return alive >= 5; // secondary birthLimit
            }
            return false;
        } else {
            if (map[x].get(y)) {
                return !(alive < 3); // deathLimit
            } else {
                return (alive > 4); // birthLimit
            }
        }
    }

    /**
     * Counts all the neighbours that are alive for the specified cell (coordinate) in the BitSet array.
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
     * Connects the dungeon by removing all residual separate parts of the dungeon.
     *
     * @param map BitSet array map to edit
     * @return amount of open cells as integer
     */
    public int connectDungeon(BitSet[] map) {
        int[][] intMap = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                intMap[i][j] = (map[i].get(j) ? 1 : 0);
            }
        }

        int x = width / 2;
        int y = height / 2;
        intMap[x][y] = 0;

        byte newCell = 2;
        int filled = connectDungeonUtil(intMap, x, y, intMap[x][y], newCell);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i].set(j, intMap[i][j] != newCell);
            }
        }
        return filled;
    }

    /**
     * Returns true if the dungeon is connected and false if not.
     *
     * @return true if connected
     */
    public boolean isDungeonConnected() {
        if (connected) {
            return true;
        }

        BitSet[] map = new BitSet[width];
        for (int x = 0; x < width; x++) {
            map[x] = new BitSet(height);
            for (int y = 0; y < height; y++) {
                map[x].set(y, this.map[x].get(y));
            }
        }

        return connectDungeon(map) - countCells(false, true) == 0;
    }

    /**
     * Counts cells
     *
     * @param wall if walls should be counted
     * @param openCell if open cells (dungeon areas) should be counted
     *
     * @return cell count
     */
    public int countCells(boolean wall, boolean openCell) {
        int count = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (openCell && !map[x].get(y)) {
                    ++count;
                }
                if (wall && map[x].get(y)) {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * Returns a string version of the dungeon
     *
     * @return A string map of the BitSet array
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
