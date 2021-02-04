package ryuuri.dao;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilTest {
    private ImageUtil imageUtil;
    int[][] data;

    @BeforeEach
    void setUp() {
        // 3x3, 25%, 0 steps, seed: -8653063932943180940
        data = new int[][] {{1, 1, 1}, {0, 0, 0}, {1, 0, 1}};
        imageUtil = new ImageUtil(data);
    }

    @Test
    @Tag("UnitTest")
    void generateImage() throws IOException {
        WritableImage output = imageUtil.generateImage();
        InputStream resourceBuff= Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("dungeon@1.png"));
        Image image = SwingFXUtils.toFXImage(ImageIO.read(resourceBuff), null);
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                assertTrue((image.getPixelReader().getArgb(x, y) == output.getPixelReader().getArgb(x, y)));
            }
        }
    }

    @Test
    void scaleData() {
        WritableImage output = imageUtil.generateImage();
        imageUtil.scaleData(8, 8);
        // TODO: expected from file
    }

    @Test
    @Tag("UnitTest")
    void getImage() {
        imageUtil.generateImage();
        WritableImage output = imageUtil.getImage();
        assertEquals(output, imageUtil.getImage());
    }

    @Test
    void writeFile() {
    }
}
