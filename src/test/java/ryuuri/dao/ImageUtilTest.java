package ryuuri.dao;

import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilTest {
    private ImageUtil imageUtil;

    @BeforeEach
    void setUp() {
        int[][] data = {{1,1,1},{1,0,0},{0,0,1}};
        imageUtil = new ImageUtil(data);
    }

    @Test
    void generateImage() {
        WritableImage output = imageUtil.generateImage();
        // TODO: expected from file
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
