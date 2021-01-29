package ryuuri.dao;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
        width = data.length;
        height = data[0].length;
    }

    /**
     * Constructs a WritableImage from the matrix pixel by pixel.
     *
     * @return The constructed image as WritableImage
     */
    public WritableImage generateImage() {
        image = new WritableImage(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color = (data[i][j] == 0)
                        ? new Color(0.9, 0.9, 0.9, 1)
                        : new Color(0, 0, 0.5, 1);
                image.getPixelWriter().setColor(i, j, color);
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

        int rows = data.length * xFactor;
        int cols = data[0].length * yFactor;
        int[][] scaledData = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                scaledData[r][c] = data[r / xFactor][c / yFactor];
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
     * TODO!
     */
    public void writeFile() {
//        File output = new File("dungeon.png");
//        ImageIO.write(image, "png", output);
    }
}