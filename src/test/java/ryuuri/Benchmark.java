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
    void mediumSized() throws IOException {
        long startTime = System.currentTimeMillis();
        CelluralMapHandler cell = new CelluralMapHandler(1000, 1000, 40, 1000, 555, 1);
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream("1000xy_1000step_40chance_555seed_dungeon.txt"))
                .readAllBytes()
        );

        results.add("Time (1000x1000 & 1000 steps): " + (endTime  - startTime) + "ms");
        assertEquals(output, expected);
    }

    @Test
    @Tag("Benchmark")
    void lotsOfSteps() throws IOException {
        long startTime = System.currentTimeMillis();
        CelluralMapHandler cell = new CelluralMapHandler(100, 100, 40, 1000000, 555, 1);
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();
        String expected = new String(Objects
                .requireNonNull(getClass().getClassLoader().getResourceAsStream("100xy_1000000step_40chance_555seed_dungeon.txt"))
                .readAllBytes()
        );

        results.add("Time (100x100 & 1 000 000 steps): " + (endTime  - startTime) + "ms");
        assertEquals(output, expected);
    }

    @AfterAll
    static void afterAll() {
        for (String s : results) {
            System.out.println(s);
        }
    }
}
