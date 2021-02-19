package ryuuri.mapgen;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Benchmark {
    private static ArrayList<String> results;
    private static boolean bitImplementation;

    @BeforeAll
    static void beforeAll() {
        results = new ArrayList<>();

        if (System.getProperty("test.type").equals("bit")) {
            results.add("Benchmark results (BitSet[][]):");
            bitImplementation = true;
        } else {
            results.add("Benchmark results (int[][]):");
            bitImplementation = false;
        }
    }

    @ParameterizedTest
    @MethodSource("singleDungeon")
    @Tag("Benchmark")
    void singleDungeon(int width, int height, int aliveChance, int steps, boolean connected, long seed, int logic) throws IOException {
        CelluralMapAbstract<?> cell = bitImplementation
                ? new CelluralMapHandlerBit(width, height, aliveChance, steps, connected, seed)
                : new CelluralMapHandler(width, height, aliveChance, steps, connected, seed);
        cell.setAlgorithmVersions(false, logic);

        long startTime = System.currentTimeMillis();
        cell.generateDungeon();
        long endTime = System.currentTimeMillis();

        String connectedString = connected ? "_traversable_" : "_";

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(
                        getClass().getClassLoader().getResourceAsStream(
                                width + "xy_" + steps + "step_" + aliveChance + "chance_nowalls_logic" + logic + connectedString + seed + "seed_dungeon.txt")
                ).readAllBytes()
        );

        results.add((endTime  - startTime) + "ms | " + width + " * " + height + " px, " + steps + " steps, " +
                aliveChance + "%, V" + logic + ", " + connectedString.replaceAll("[_]", ""));
        assertEquals(expected, output);
    }

    @ParameterizedTest
    @MethodSource("multipleDungeons")
    @Tag("Benchmark")
    void multipleDungeons(int width, int height, int aliveChance, int steps, boolean connected, int logic, int dungeonAmount) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dungeonAmount; i++) {
            CelluralMapAbstract<?> cell = bitImplementation
                    ? new CelluralMapHandlerBit(width, height, aliveChance, steps, connected)
                    : new CelluralMapHandler(width, height, aliveChance, steps, connected);
            cell.setAlgorithmVersions(false, logic);
            cell.generateDungeon();
        }
        long endTime = System.currentTimeMillis();

        String connectedString = connected ? " traversable " : " ";

        results.add((endTime  - startTime) + "ms | " + dungeonAmount + " random " + width + " * " + height + " px, " +
                steps + " steps, V" + logic + connectedString + "dungeons");
        assertTrue(true);
    }

    @AfterAll
    static void afterAll() {
        for (String s : results) {
            System.out.println(s);
        }
    }

    // Benchmark data
    private static Stream<Arguments> singleDungeon() {
        return Stream.of(
                Arguments.of(1000, 1000, 40, 1000, false, 555, 1),
                Arguments.of(1000, 1000, 40, 1000, false, 555, 2),
                Arguments.of(1000, 1000, 40, 1000, true, -7710797491757861329L, 2),
                Arguments.of(100, 100, 40, 100000, false, 555, 1)
        );
    }

    // Benchmark data
    private static Stream<Arguments> multipleDungeons() {
        return Stream.of(
                Arguments.of(100, 100, 40, 2, false, 1, 30000),
                Arguments.of(100, 100, 40, 2, false, 2, 30000),
                Arguments.of(100, 100, 40, 2, true, 1, 30000),
                Arguments.of(100, 100, 40, 2, true, 2, 30000)
        );
    }
}
