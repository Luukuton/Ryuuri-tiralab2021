package ryuuri;

import ryuuri.mapgen.CelluralMapHandler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class Benchmark {
    private static ArrayList<String> results;

    @BeforeAll
    static void beforeAll() {
        results = new ArrayList<>();
    }

    @Test
    @Tag("Benchmark")
    void mediumSizeLogicV1() throws IOException {
        CelluralMapHandler cell = new CelluralMapHandler(1000, 1000, 40, 1000, false, 555);

        long startTime = System.currentTimeMillis();
        cell.generateDungeon();
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream("1000xy_1000step_40chance_nowalls_logic1_555seed_dungeon.txt"))
                .readAllBytes()
        );

        results.add("Time (1000x1000px / 1000 steps / V1): " + (endTime  - startTime) + "ms");
        assertEquals(expected, output);
    }

    @Test
    @Tag("Benchmark")
    void mediumSizeLogicV2() throws IOException {
        CelluralMapHandler cell = new CelluralMapHandler(1000, 1000, 40, 1000, false, 555);
        cell.setAlgorithmVersions(false, 2);

        long startTime = System.currentTimeMillis();
        cell.generateDungeon();
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream("1000xy_1000step_40chance_nowalls_logic2_555seed_dungeon.txt"))
                .readAllBytes()
        );

        results.add("Time (1000x1000px / 1000 steps / V2): " + (endTime  - startTime) + "ms");
        assertEquals(expected, output);
    }

    @Test
    @Tag("Benchmark")
    void mediumSizeLogicV2Traversable() throws IOException {
        CelluralMapHandler cell = new CelluralMapHandler(1000, 1000, 40, 1000, true, -7710797491757861329L);
        cell.setAlgorithmVersions(false, 2);

        long startTime = System.currentTimeMillis();
        cell.generateDungeon();
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream("1000xy_1000step_40chance_nowalls_logic2_traversable_-7710797491757861329seed_dungeon.txt"))
                .readAllBytes()
        );

        results.add("Time (1000x1000px / 1000 steps / V2 / traversable): " + (endTime  - startTime) + "ms");
        assertEquals(expected, output);
    }

    @Test
    @Tag("Benchmark")
    void lotsOfSteps() throws IOException {
        CelluralMapHandler cell = new CelluralMapHandler(100, 100, 40, 100000, false, 555);
        long startTime = System.currentTimeMillis();
        cell.generateDungeon();
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream("100xy_100000step_40chance_nowalls_logic1_555seed_dungeon.txt"))
                .readAllBytes()
        );

        results.add("Time (100x100px / 100 000 steps): " + (endTime  - startTime) + "ms");
        assertEquals(expected, output);
    }

    @Test
    @Tag("Benchmark")
    void lotsOfRandomDungeonsV1() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            CelluralMapHandler cell = new CelluralMapHandler(100, 100, 40, 2, false);
            cell.generateDungeon();
        }
        long endTime = System.currentTimeMillis();

        results.add("Time (1 000 000 random 100x100 / 2 step / V1 dungeons): " + (endTime  - startTime) + "ms");
        assertTrue(true);
    }

    @Test
    @Tag("Benchmark")
    void lotsOfRandomDungeonsV2() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            CelluralMapHandler cell = new CelluralMapHandler(100, 100, 40, 2, false);
            cell.setAlgorithmVersions(false, 2);
            cell.generateDungeon();
        }
        long endTime = System.currentTimeMillis();

        results.add("Time (1 000 000 random 100x100 / 2 step / V2 dungeons): " + (endTime  - startTime) + "ms");
        assertTrue(true);
    }

    @Test
    @Tag("Benchmark")
    void lotsOfRandomTraversableDungeonsV1() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            CelluralMapHandler cell = new CelluralMapHandler(100, 100, 40, 2, true);
            cell.generateDungeon();
        }
        long endTime = System.currentTimeMillis();

        results.add("Time (1 000 000 random connected 100x100 / 2 step / V1 dungeons): " + (endTime  - startTime) + "ms");
        assertTrue(true);
    }

    @Test
    @Tag("Benchmark")
    void lotsOfRandomTraversableDungeonsV2() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            CelluralMapHandler cell = new CelluralMapHandler(100, 100, 40, 2, true);
            cell.setAlgorithmVersions(false, 2);
            cell.generateDungeon();
        }
        long endTime = System.currentTimeMillis();

        results.add("Time (1 000 000 random connected 100x100 / 2 step / V2 dungeons): " + (endTime  - startTime) + "ms");
        assertTrue(true);
    }

    @AfterAll
    static void afterAll() {
        for (String s : results) {
            System.out.println(s);
        }
    }
}
