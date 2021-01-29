package ryuuri;

import ryuuri.mapgen.CelluralMapHandler;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class Benchmark {

    @Test
    @Tag("UnitTest")
    void mediumSized() throws IOException {
        long startTime = System.currentTimeMillis();
        CelluralMapHandler cell = new CelluralMapHandler(1000, 1000, 40, 1000, 555);
        long endTime = System.currentTimeMillis();

        String output = cell.mapToString();

        String expected = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("1000xy_1000step_40chance_555seed_dungeon.txt")).readAllBytes());

        System.out.println("Total execution time: " + (endTime-startTime) + "ms");
        assertEquals(output, expected);
    }
}
