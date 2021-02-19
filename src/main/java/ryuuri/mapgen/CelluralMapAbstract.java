package ryuuri.mapgen;

import java.util.Random;

abstract class CelluralMapAbstract<T>  {
    protected Random rand;
    protected long seed;
    protected int width, height, aliveChance, steps, logicVersion;
    protected boolean outerWalls, connected;

    abstract void generateDungeon();

    abstract void doSimulationStep(int steps);

    abstract int countAliveNeighbours(int x, int y);

    abstract int connectDungeon(T map);

    abstract boolean isDungeonConnected();

    abstract int countCells(boolean wall, boolean openCell);

    abstract String mapToString();

    /**
     * A recursive helper function for connectDungeon
     *
     * @param map BitSet array map to edit
     * @param x starting X coordinate as integer
     * @param y starting Y coordinate as integer
     * @param prevCell previous cell data
     * @param newCell new cell data
     * @return the amount of connected cells as integer
     */
    protected int connectDungeonUtil(int[][] map, int x, int y, int prevCell, int newCell) {
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
     * Sets the the "is dungeon connected" variable to true or false. Only used for testing!
     *
     * @param connected Dungeon connected (true) or not (false)
     */
    protected void setConnected(boolean connected) {
        this.connected = connected;
    }
}
