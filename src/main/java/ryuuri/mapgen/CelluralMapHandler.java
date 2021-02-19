package ryuuri.mapgen;

import java.util.Arrays;
import java.util.Random;

/**
 * Main algorithm for the dungeon generation.
 */
public class CelluralMapHandler {
    private final Random rand;
    private final long seed;

    public int[][] map;
    private final int width, height, aliveChance, steps;
    protected boolean connected;

    private boolean outerWalls;
    private int logicVersion;

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
    public CelluralMapHandler(int width, int height, int aliveChance, int steps, boolean connected, long seed) {
        this.outerWalls = false;
        this.logicVersion = 1;

        this.width = width;
        this.height = height;
        this.aliveChance = aliveChance;
        this.steps = steps;
        this.connected = connected;

        this.seed = (seed == 0) ? new Random().nextLong() : seed;
        this.rand = new Random(this.seed);

        map = new int[width][height];
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
    public CelluralMapHandler(int width, int height, int aliveChance, int steps, boolean connected) {
        this(width, height, aliveChance, steps, connected, 0);
    }

    /**
     * Initializes the dungeon matrix
     * **/
    public void generateDungeon() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == (height / 2)) {
                    map[x][y] = 0;
                } else if (outerWalls && (x == 0 || y == 0 || x == width - 1 || y == height - 1)) {
                    map[x][y] = 1;
                } else {
                    map[x][y] = ((rand.nextInt(100 - 1) + 1) < aliveChance) ? 1 : 0;
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
     * @param steps the amount of simulation steps to make as integer
     */
    public void doSimulationStep(int steps) {
        for (int i = 0; i < steps; i++) {
            int[][] newMap = new int[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    newMap[x][y] = dungeonLogic(x, y);
                }
            }
            map = Arrays.stream(newMap).map(int[]::clone).toArray(int[][]::new);
        }
    }

    /**
     * Determines if the cell will be a wall. Two versions embedded.
     *
     * @param x x coordinate as integer
     * @param y y coordinate as integer
     * @return 1 (wall) or 0 (open path)
     */
    private int dungeonLogic(int x, int y) {
        int alive = countAliveNeighbours(x, y);
        if (logicVersion == 2) {
            if (map[x][y] == 1) {
                if (alive >= 4) { // birthLimit
                    return 1;
                }
                if (alive < 2) { // deathLimit
                    return 0;
                }
            } else {
                if (alive >= 5) { // secondary birthLimit
                    return 1;
                }
            }
            return 0;
        } else {
            if (map[x][y] == 1) {
                return (alive < 3) ? 0 : 1; // deathLimit
            } else {
                return (alive > 4) ? 1 : 0; // birthLimit
            }
        }
    }

    /**
     * Counts all the neighbours that are alive for the specified cell (coordinate) in the matrix.
     *
     * @param x x coordinate as integer
     * @param y y coordinate as integer
     * @return the count of alive neighbours as integer
     */
    public int countAliveNeighbours(int x, int y) {
        int count = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int neighbourX = x + i;
                int neighbourY = y + j;

                if (neighbourX < 0 || neighbourY < 0 || neighbourX >= width || neighbourY >= height) {
                    count = count + 1;
                } else if (map[neighbourX][neighbourY] == 1) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    /**
     * A recursive helper function for connectDungeon
     *
     * @param map integer 2D matrix map to edit
     * @param x starting X coordinate as integer
     * @param y starting Y coordinate as integer
     * @param prevCell previous cell data
     * @param newCell new cell data
     * @return the amount of connected cells as integer
     */
    private int connectDungeonUtil(int[][] map, int x, int y, int prevCell, int newCell) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        if (map[x][y] != prevCell) {
            return 0;
        }

        map[x][y] = newCell;

        int right = connectDungeonUtil(map, x + 1, y, prevCell, newCell);
        int left = connectDungeonUtil(map, x - 1, y, prevCell, newCell);
        int above = connectDungeonUtil(map, x, y + 1, prevCell, newCell);
        int below = connectDungeonUtil(map, x, y - 1, prevCell, newCell);

        // + 1 for the current cell
        return right + left + above + below + 1;
    }

    /**
     * Connects the dungeon by removing all residual separate parts of the dungeon.
     *
     * @param map integer 2D matrix map to edit
     * @return amount of open cells as integer
     */
    public int connectDungeon(int[][] map) {
        int x = width / 2;
        int y = height / 2;
        map[x][y] = 0;

        int newCell = 2;
        int filled = connectDungeonUtil(map, x, y, map[x][y], newCell);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j] == newCell) {
                    map[i][j] = 0;
                } else {
                    map[i][j] = 1;
                }
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

        int[][] map = Arrays.stream(this.map).map(int[]::clone).toArray(int[][]::new);
        return connectDungeon(map) - countCells(false, true) == 0;
    }

    /**
     * Sets the version of the algorithm logic.
     *
     * @param outerWalls if the dungeon has outer walls
     * @param logicVersion dungeon logic version (1 or 2)
     */
    public void setAlgorithmVersions(boolean outerWalls, int logicVersion) {
        this.outerWalls = outerWalls;
        this.logicVersion = logicVersion;
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
                if (openCell && map[x][y] == 0) {
                    ++count;
                }

                if (wall && map[x][y] == 1) {
                    ++count;
                }
            }
        }
        return count;
    }

    /**
     * Returns a string version of the dungeon
     *
     * @return a string map of the matrix
     */
    public String mapToString() {
        StringBuilder returnString = new StringBuilder();

        String[] mapSymbols = {".", "#"};

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                returnString.append(mapSymbols[map[x][y]]);
            }
            returnString.append("\n");
        }
        return returnString.toString();
    }
}
