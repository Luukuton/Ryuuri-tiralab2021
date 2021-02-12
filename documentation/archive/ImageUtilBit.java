package ryuuri.io;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

public class ImageUtilBit {
    private int width, height;
    private BitSet[] data;
    private WritableImage image;

    /**
     * Constructor
     *
     * @param data An integer 2D matrix of the dungeon
     */
    public ImageUtilBit(BitSet[] data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
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
                Color color = (!data[x].get(y))
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
     * @param xFactor How many times to scale in width as integer
     * @param yFactor How many times to scale in height as integer
     */
    public void scaleData(int xFactor, int yFactor) {
        width *= xFactor;
        height *= yFactor;
        BitSet[] scaledData = new BitSet[width];
        for (int j = 0; j < width; j++) {
            scaledData[j] = new BitSet(height);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                scaledData[x].set(y, data[x / xFactor].get(y / yFactor));
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
        } catch (IOException ignored) {
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
