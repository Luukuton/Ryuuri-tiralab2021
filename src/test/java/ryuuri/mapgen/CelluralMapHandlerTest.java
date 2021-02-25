package ryuuri.mapgen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CelluralMapHandlerTest {
    private int width, height, aliveChance, seed;

    @BeforeEach
    @Tag("UnitTest")
    public void setUp() {
        width = 20;
        height = 20;
        aliveChance = 40;
        seed = 555;
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    void correctStringOutputBasic(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 0, false, seed)
                : new CelluralMapHandler(width, height, aliveChance, 0, false, seed);
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

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    void correctStringOutputOnThreeSimulationStepsForLogicV2(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 3, false, seed)
                : new CelluralMapHandler(width, height, aliveChance, 3, false, seed);
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

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    void correctStringOutputOnThreeSimulationStepsWalls(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 3, false, seed)
                : new CelluralMapHandler(width, height, aliveChance, 3, false, seed);
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

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    public void correctAmountOfAliveNeighbours(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 0, false, seed)
                : new CelluralMapHandler(width, height, aliveChance, 0, false, seed);
        cell.generateDungeon();
        assertEquals(cell.countAliveNeighbours(0, 0), 7);
        assertEquals(cell.countAliveNeighbours(10, 10), 2);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    public void correctStringOutputOnThreeSimulationSteps(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 3, false, seed)
                : new CelluralMapHandler(width, height, aliveChance, 3, false, seed);
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

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    public void correctStringOutputOn10000SimulationSteps(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 10000, false, seed)
                : new CelluralMapHandler(width, height, aliveChance, 10000, false, seed);
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

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    public void dungeonIsConnected(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, 3, false, -406169416787061417L)
                : new CelluralMapHandler(width, height, aliveChance, 3, false, -406169416787061417L);
        cell.generateDungeon();

        System.out.println(cell.mapToString());

        assertTrue(cell.isDungeonConnected());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    public void dungeonIsModifiedToBeConnectedCorrectly(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(200, 200, aliveChance, 3, true, 8888)
                : new CelluralMapHandler(200, 200, aliveChance, 3, true, 8888);
        cell.generateDungeon();

        // Automatic check
        assertTrue(cell.isDungeonConnected());

        // Now after the generation, let's lie to the class that it's not connected,
        // so that it has to check the connectivity manually.
        cell.setConnected(false);
        assertTrue(cell.isDungeonConnected());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @Tag("UnitTest")
    public void dungeonIsNotConnected(boolean bitImplementation) {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(200, 200, aliveChance, 3, false, 8888)
                : new CelluralMapHandler(200, 200, aliveChance, 3, false, 8888);
        cell.generateDungeon();

        assertFalse(cell.isDungeonConnected());
    }
}
