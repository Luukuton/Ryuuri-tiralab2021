package ryuuri.io;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * An utility to scale and generate an image of the dungeon.
 */
public class ImageUtil {
    private int width, height;
    private int[][] data;
    private WritableImage image;

    /**
     * Constructor
     *
     * @param data An integer 2D matrix of the dungeon
     */
    public ImageUtil(int[][] data) {
        this.data = data;
        this.width = data.length;
        this.height = data[0].length;
    }

    /**
     * Constructs a WritableImage from the matrix pixel by pixel.
     *
     * @return The constructed image as WritableImage
     */
    public WritableImage generateImage() {
        image = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color color = (data[x][y] == 0)
                        ? new Color(0.9, 0.9, 0.9, 1)
                        : new Color(0, 0, 0.5, 1);
                image.getPixelWriter().setColor(x, y, color);
            }
        }
        return image;
    }

    /**
     * Scales the image without any quality loss.
     *
     * @param widthFactor How many times to scale in width as integer
     * @param heightFactor How many times to scale in height as integer
     */
    public void scaleData(int widthFactor, int heightFactor) {
        if (widthFactor <= 1 && heightFactor <= 1) {
            return;
        }

        width *= widthFactor;
        height *= heightFactor;

        int rows = data.length * widthFactor;
        int cols = data[0].length * heightFactor;
        int[][] scaledData = new int[rows][cols];

        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                scaledData[x][y] = data[x / widthFactor][y / heightFactor];
            }
        }

        data = scaledData;
    }

    /**
     * Gets the WritableImage
     *
     * @return The image as WritableImage
     */
    public WritableImage getImage() {
        return image;
    }

    /**
     * Writes the image to a file
     *
     * @param file File to save
     */
    public void writeFile(File file) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException | IllegalArgumentException ignored) {
        }
    }

    /**
     * Gets the width in pixels
     *
     * @return width in pixels as integer
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height in pixels
     *
     * @return height in pixels as integer
     */
    public int getHeight() {
        return height;
    }
}
