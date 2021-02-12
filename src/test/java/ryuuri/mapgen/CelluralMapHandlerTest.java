package ryuuri.mapgen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CelluralMapHandlerTest {
    private CelluralMapHandler cell;
    private int width, height, aliveChance, seed;

    @BeforeEach
    @Tag("UnitTest")
    public void setUp() {
        width = 20;
        height = 20;
        aliveChance = 40;
        seed = 555;
    }

    @Test
    @Tag("UnitTest")
    void correctStringOutput() {
        cell = new CelluralMapHandler(width, height, aliveChance, 0, false, seed);
        cell.generateDungeon();

        String output = cell.mapToString();
        String expected =
                "#.####...#.##...###.\n" +
                "#..#.......##.#.....\n" +
                "......###...#..#####\n" +
                ".##....##..##..#.##.\n" +
                "..#.##...#.#.#..#..#\n" +
                "#.......#....##.#.#.\n" +
                ".#.##....#..#.#.....\n" +
                ".#.##.......#.#.....\n" +
                "..#...##...#..###...\n" +
                "##..#...#..#..#####.\n" +
                "#.##.##.#..##.#.#...\n" +
                "##.##.......#..#..#.\n" +
                ".##.###....##.....#.\n" +
                "#....#..#.......#...\n" +
                ".#..###..#......#.##\n" +
                "..#..#........####..\n" +
                "##.#.#.#...#..##....\n" +
                ".........#.#........\n" +
                ".###..##...#..#..#..\n" +
                "......#.##.##..####.\n";

        assertEquals(expected, output);
    }

    @Test
    @Tag("UnitTest")
    void correctStringOutputOnThreeSimulationStepsForLogicV2() {
        cell = new CelluralMapHandler(width, height, aliveChance, 3, false, seed);
        cell.setAlgorithmVersions(false, 2);
        cell.generateDungeon();

        String output = cell.mapToString();
        String expected =
                "######...###########\n" +
                "####......##########\n" +
                "#......##..##..#####\n" +
                "#......##..##..#####\n" +
                "#...........##..####\n" +
                "#............##....#\n" +
                "##.##........##.....\n" +
                "#####........###....\n" +
                "####..........###...\n" +
                "####..........####..\n" +
                "######.....##.####.#\n" +
                "######.....##......#\n" +
                "######.............#\n" +
                "##.####...........##\n" +
                "#...###........#####\n" +
                "#...###.......######\n" +
                "##............##....\n" +
                "##..................\n" +
                "##.....#..##.....###\n" +
                "####################\n";

        assertEquals(expected, output);
    }

    @Test
    @Tag("UnitTest")
    void correctStringOutputOnThreeSimulationStepsForInitV2() {
        cell = new CelluralMapHandler(width, height, aliveChance, 3, false, seed);
        cell.setAlgorithmVersions(true, 1);
        cell.generateDungeon();

        String output = cell.mapToString();
        String expected =
                "####################\n" +
                "########...#########\n" +
                "####..#.....########\n" +
                "####.........#######\n" +
                "#######......#######\n" +
                "########....####.###\n" +
                "########...###....##\n" +
                "###...###...#....###\n" +
                "###....###.......###\n" +
                "###..............###\n" +
                "###...##...#..######\n" +
                "####..##...#########\n" +
                "####..##############\n" +
                "####..##############\n" +
                "####..######..##..##\n" +
                "###.............####\n" +
                "###...........######\n" +
                "###........#########\n" +
                "####......##########\n" +
                "####################\n";

        assertEquals(expected, output);
    }

    @Test
    @Tag("UnitTest")
    public void correctAmountOfAliveNeighbours() {
        cell = new CelluralMapHandler(width, height, aliveChance, 0, false, seed);
        cell.generateDungeon();
        assertEquals(cell.countAliveNeighbours(0, 0), 7);
        assertEquals(cell.countAliveNeighbours(10, 10), 2);
    }

    @Test
    @Tag("UnitTest")
    public void correctStringOutputOnThreeSimulationSteps() {
        cell = new CelluralMapHandler(width, height, aliveChance, 3, false, seed);
        cell.generateDungeon();

        String output = cell.mapToString();
        String expected =
                "######...###########\n" +
                "####......##########\n" +
                "##....###..##..#####\n" +
                "###....##..##..#####\n" +
                "###........###..####\n" +
                "###.........###....#\n" +
                "#####.......###.....\n" +
                "#####.......####....\n" +
                "#####..#...######...\n" +
                "#####..##..#######..\n" +
                "######.....##.####.#\n" +
                "######.....##..#...#\n" +
                "#######....##......#\n" +
                "#######...........##\n" +
                "##..###........#####\n" +
                "###.###.......######\n" +
                "###...........##...#\n" +
                "###........#........\n" +
                "####..##..###....###\n" +
                "####################\n";

        assertEquals(expected, output);
    }

    @Test
    @Tag("UnitTest")
    public void correctStringOutputOn10000SimulationSteps() {
        cell = new CelluralMapHandler(width, height, aliveChance, 10000, false, seed);
        cell.generateDungeon();

        String output = cell.mapToString();
        String expected =
                "######...###########\n" +
                "####......##########\n" +
                "###...###..#########\n" +
                "###....##..#########\n" +
                "###........#########\n" +
                "####........#####..#\n" +
                "#####.......####....\n" +
                "#####.......####....\n" +
                "#####..#...######...\n" +
                "#####..##..#######..\n" +
                "######.....#######.#\n" +
                "######.....#####...#\n" +
                "#######....##......#\n" +
                "#######...........##\n" +
                "#######........#####\n" +
                "#######.......######\n" +
                "#######......#######\n" +
                "#######....#########\n" +
                "########..##########\n" +
                "####################\n";

        assertEquals(expected, output);
    }

    @Test
    @Tag("UnitTest")
    public void dungeonIsConnected() {
        cell = new CelluralMapHandler(width, height, aliveChance, 3, false, -406169416787061417L);
        cell.generateDungeon();

        assertTrue(cell.isDungeonConnected());
    }

    @Test
    @Tag("UnitTest")
    public void dungeonIsModifiedToBeConnectedCorrectly() {
        cell = new CelluralMapHandler(200, 200, aliveChance, 3, true, 8888);
        cell.generateDungeon();

        // Automatic check
        assertTrue(cell.isDungeonConnected());

        // Now, let's lie to the class that it's not connected after the generation, so that it has to check it manually.
        cell.connected = false;
        assertTrue(cell.isDungeonConnected());
    }

    @Test
    @Tag("UnitTest")
    public void dungeonIsNotConnected() {
        cell = new CelluralMapHandler(200, 200, aliveChance, 3, false, 8888);
        cell.generateDungeon();

        assertFalse(cell.isDungeonConnected());
    }
}
