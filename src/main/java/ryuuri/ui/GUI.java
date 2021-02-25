package ryuuri.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;

import java.io.File;
import java.io.IOException;

import ryuuri.io.ImageUtil;
import ryuuri.mapgen.CelluralMapHandler;

/**
 * A class for the application GUI.
 */
public class GUI extends Application {
    private BorderPane mainView;
    private GridPane controls;
    private Slider widthSlider, heightSlider, chanceSlider, stepsSlider, xScaleSlider, yScaleSlider;
    private Button generateBtn, saveImgFile, saveDataBtn;
    private CheckBox seedLocked, connected;
    private ChoiceBox<String> algorithmInitVersion, algorithmLogicVersion;
    private ImageView imageView;
    private ImageUtil imageUtil;
    private LongField seedField;
    private String rawData;
    private long currentSeed;

    /**
     * The main window of the application.
     *
     * @param stage Main stage
     */
    private void initUI(Stage stage) {
        // Better antialiasing for the text.
        System.setProperty("prism.lcdtext", "false");

        // Basic UI settings //
        controls = new GridPane();
        mainView = new BorderPane();
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setPadding(new Insets(10));
        controls.setVgap(5);
        controls.setHgap(5);

        // Inputs (buttons, sliders, fields) //
        initInputs();

        // Actions //
        generateBtn.setOnAction(e -> generate(
                (int) widthSlider.getValue(),
                (int) heightSlider.getValue(),
                (int) chanceSlider.getValue(),
                (int) stepsSlider.getValue(),
                (int) xScaleSlider.getValue(),
                (int) yScaleSlider.getValue(),
                seedField.getValue()
        ));
        saveImgFile.setOnAction(e -> {
            try {
                saveToFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        saveDataBtn.setOnAction(e -> copyRawData());

        // Image and Zooming //
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        Tooltip.install(imageView, new Tooltip("Scroll to zoom, drag to pan and double-click to reset."));
        initZoom();

        // Closes all child windows when exiting the main app
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        // Initializes the main window //
        mainView.setLeft(controls);
        stage.setMinWidth(960);
        stage.setMinHeight(570);
        stage.setTitle("Ryuuri");
        stage.getIcons().add(new Image("ryuuri-logo.png"));
        stage.setScene(new Scene(mainView));
        stage.show();
    }

    /**
     * Helper method for generating the dungeon and image for it. Also updates JavaFX scenes accordingly.
     *
     * @param width Width in pixels as integer
     * @param height Height in pixels as integer
     * @param chance The chance for the cells (pixels) to die as integer
     * @param steps Simulation steps to do as integer
     * @param xFactor Times to scale the image in the x axis
     * @param yFactor Times to scale the image in the y axis
     * @param seed The seed for Random class
     */
    public void generate(int width, int height, int chance, int steps, int xFactor, int yFactor, long seed) {
        if (!seedLocked.isSelected()) {
            seed = 0;
        }

        controls.setDisable(true);

        // Loading icon
        final ProgressIndicator progress = new ProgressIndicator();
        progress.setMinSize(60, 60);
        VBox overlay = new VBox(progress);
        overlay.setAlignment(Pos.CENTER);
        mainView.setCenter(overlay);

        long finalSeed = seed; // Variable used in lambda expression should be final or effectively final
        new Thread(() -> {
            boolean outerWalls = algorithmInitVersion.getSelectionModel().getSelectedItem().equals("Walls");
            int logicVersion = algorithmLogicVersion.getSelectionModel().getSelectedItem().equals("Logic Ver. 2") ? 2 : 1;

            CelluralMapHandler cells = new CelluralMapHandler(width, height, chance, steps, connected.isSelected(), finalSeed);
            cells.setAlgorithmVersions(outerWalls, logicVersion);

            try {
                cells.generateDungeon();
            } catch (StackOverflowError err) {
                Platform.runLater(() -> {
                    Label stackOverflow = new Label("Stack overflow error! Try generating without the dungeon having to be connected or with lower values. \nChanging the logic version might help too.");
                    mainView.setCenter(stackOverflow);
                    controls.getChildren().remove(overlay);
                    controls.setDisable(false);
                });
                return;
            } catch (OutOfMemoryError err) {
                Platform.runLater(() -> {
                    Label outOfMemory = new Label("Out of memory! Try generating with less steps or smaller dimensions.");
                    mainView.setCenter(outOfMemory);
                    controls.getChildren().remove(overlay);
                    controls.setDisable(false);
                });
                return;
            }

            if (finalSeed == 0) {
                currentSeed = cells.getSeed();
            } else {
                currentSeed = finalSeed;
            }

            try {
                imageUtil = new ImageUtil(cells.map);
                imageUtil.scaleData(xFactor, yFactor);
                imageUtil.generateImage();
                rawData = cells.mapToString();
            } catch (OutOfMemoryError err) {
                Platform.runLater(() -> {
                    Label outOfMemory = new Label("Out of memory on trying to scale the dungeon. Try to lower the scale factors.");
                    mainView.setCenter(outOfMemory);
                    controls.getChildren().remove(overlay);
                    controls.setDisable(false);
                });
                return;
            }

            // Set current values, remove loading icon and enable controls
            Platform.runLater(() -> {
                seedField.setValue(currentSeed);

                imageView.setImage(imageUtil.getImage());
                resetImageViewSize();

                Pane container = new Pane(imageView);
                imageView.fitWidthProperty().bind(container.widthProperty());
                imageView.fitHeightProperty().bind(container.heightProperty());

                mainView.setCenter(new VBox(container));
                controls.getChildren().remove(overlay);
                saveImgFile.setDisable(false);
                saveDataBtn.setDisable(false);
                controls.setDisable(false);
            });
        }).start();
    }

    /**
     * Helper method for creating the dungeon.
     *
     * @throws IOException on IO error
     */
    public void saveToFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("dungeon.png");
        File file = fileChooser.showSaveDialog(null);
        imageUtil.writeFile(file);
    }

    /**
     * Copies the raw data to clipboard
     */
    public void copyRawData() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(rawData);
        clipboard.setContent(content);
    }

    /**
     * Creates a slider with editable box and tooltips.
     *
     * @param slider Slider class
     * @param unit String which shows the unit
     * @param tooltip Hoverable tooltip as String
     * @param initial Initial value for the slider as integer
     * @param max Max value for the slider as integer
     * @return The slider with HBox as its frame
     */
    private HBox createSlider(Slider slider, String unit, String tooltip, int initial, int min, int max) {
        Label label = new Label(unit);
        label.setTooltip(new Tooltip(tooltip));
        label.setPrefWidth(50);

        final LongField longField = new LongField(min, max, initial);
        longField.setTooltip(new Tooltip("Click on me to edit the slider's value!"));
        longField.valueProperty().bindBidirectional(slider.valueProperty());
        longField.setPrefWidth(50);

        HBox sliderFrame = new HBox(10);
        sliderFrame.getChildren().addAll(label, longField, slider);
        sliderFrame.setStyle("-fx-background-color: white; -fx-padding:10; -fx-font-size: 12; -fx-alignment: baseline-left;");

        return sliderFrame;
    }

    /**
     * Initializes all input fields.
     */
    private void initInputs() {
        widthSlider = new Slider(1, 10000, 50);
        heightSlider = new Slider(1, 10000, 50);
        chanceSlider = new Slider(1, 100, 40);
        stepsSlider = new Slider(0, 10000, 3);
        xScaleSlider = new Slider(1, 16, 4);
        yScaleSlider = new Slider(1, 16, 4);
        seedField = new LongField(Long.MIN_VALUE, Long.MAX_VALUE, 0, 0);
        seedField.setPromptText("Seed");

        HBox widthFrame = createSlider(
                widthSlider,
                "x (px)",
                "Width of the dungeon in pixels.",
                50,
                1,
                10000
        );
        HBox heightFrame = createSlider(
                heightSlider,
                "y (px)",
                "Height of the dungeon in pixels.",
                50,
                1,
                10000
        );
        HBox chanceFrame = createSlider(
                chanceSlider,
                "%",
                "Chance for the cells (pixels) to die on each step.",
                40,
                0,
                100
        );
        HBox stepsFrame = createSlider(
                stepsSlider,
                "Steps",
                "The amount of simulation steps to run.",
                3,
                0,
                10000
        );
        HBox xScaleFrame = createSlider(
                xScaleSlider,
                "x (factor)",
                "Times to scale the x axis of the image.",
                4,
                1,
                16
        );
        HBox yScaleFrame = createSlider(
                yScaleSlider,
                "y (factor)",
                "Times to scale the y axis of the image.",
                4,
                1,
                16
        );

        String controlStyling = "-fx-spacing: 5px; -fx-background-color: white; -fx-padding: 10; -fx-font-size: 12; -fx-alignment: baseline-left;";

        Label seedLabel = new Label("Seed");
        seedLabel.setTooltip(new Tooltip("The seed for the dungeon. Inputting 0 or anything else than a number means no seed. Max: 2^63-1 & min: -2^63."));
        seedLabel.setPrefWidth(60);

        // If not checked, the seed will change on clicking Generate.
        seedLocked = new CheckBox("Lock");

        HBox seedFrame = new HBox(seedLabel, seedField, seedLocked);
        seedFrame.setStyle(controlStyling);

        algorithmInitVersion = new ChoiceBox<>();
        algorithmInitVersion.setItems(FXCollections.observableArrayList("No Walls", "Walls"));
        algorithmInitVersion.getSelectionModel().select(0);
        algorithmInitVersion.setPrefWidth(100);
        algorithmInitVersion.setPrefHeight(25);

        algorithmLogicVersion = new ChoiceBox<>();
        algorithmLogicVersion.setItems(FXCollections.observableArrayList("Logic Ver. 1", "Logic Ver. 2"));
        algorithmLogicVersion.getSelectionModel().select(0);
        algorithmLogicVersion.setPrefWidth(100);
        algorithmLogicVersion.setPrefHeight(25);

        Label versionLabel = new Label("Versions");
        versionLabel.setTooltip(new Tooltip("Two different algorithm version for dungeon initialization and generation logic."));
        versionLabel.setPrefWidth(60);

        HBox versionFrame = new HBox(versionLabel, algorithmInitVersion, algorithmLogicVersion);
        versionFrame.setStyle(controlStyling);

        connected = new CheckBox("Should the dungeon be connected?");

        // Buttons //
        generateBtn = new Button("Generate");
        saveImgFile = new Button("Save image as");
        saveDataBtn = new Button("Copy data to clipboard");
        // importBtn = new Button("Import raw data");

        HBox buttons = new HBox(generateBtn, saveImgFile, saveDataBtn);
        buttons.setStyle("-fx-spacing: 5px; -fx-alignment: baseline-left;");

        generateBtn.setStyle("-fx-padding: 10px; -fx-alignment: baseline-right;");

        saveImgFile.setDisable(true);
        saveDataBtn.setDisable(true);

        int row = 0;
        controls.add(connected,   0, row);
        controls.add(widthFrame,  0, ++row);
        controls.add(heightFrame, 0, ++row);
        controls.add(chanceFrame, 0, ++row);
        controls.add(stepsFrame,  0, ++row);
        controls.add(xScaleFrame, 0, ++row);
        controls.add(yScaleFrame, 0, ++row);
        controls.add(versionFrame, 0, ++row);
        controls.add(seedFrame,   0, ++row);
        controls.add(buttons, 0, ++row);
    }

    /**
     * Enables zooming of the image
     */
    private void initZoom() {
        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        imageView.setOnMousePressed(e -> {
            Point2D mousePress = calcScaleCoordinate(new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        imageView.setOnMouseDragged(e -> {
            Point2D dragPoint = calcScaleCoordinate(new Point2D(e.getX(), e.getY()));
            shiftImage(dragPoint.subtract(mouseDown.get()));
            mouseDown.set(calcScaleCoordinate(new Point2D(e.getX(), e.getY())));
        });

        imageView.setOnScroll(e -> {
            Rectangle2D viewport = imageView.getViewport();

            double scale = clamp(
                    Math.pow(1.01, e.getDeltaY()),
                    Math.min(0 / viewport.getWidth(), 0 / viewport.getHeight()),
                    Math.max(imageView.getImage().getWidth() / viewport.getWidth(), imageView.getImage().getHeight() / viewport.getHeight())
            );

            Point2D mouse = calcScaleCoordinate(new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;
            double newMinX = clamp(
                    mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
                    0,
                    imageView.getImage().getWidth() - newWidth
            );
            double newMinY = clamp(
                    mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
                    0,
                    imageView.getImage().getHeight() - newHeight
            );

            imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        imageView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                resetImageViewSize();
            }
        });
    }

    /**
     * Resets the size (viewport) of the image to its original values.
     */
    private void resetImageViewSize() {
        imageView.setViewport(new Rectangle2D(0, 0, imageView.getImage().getWidth(), imageView.getImage().getHeight()));
    }

    /**
     * Shifts the position of the image.
     *
     * @param delta The amount to shift
     */
    private void shiftImage(Point2D delta) {
        Rectangle2D viewport = imageView.getViewport();
        double maxX = imageView.getImage().getWidth() - viewport.getWidth();
        double maxY = imageView.getImage().getHeight() - viewport.getHeight();
        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY, viewport.getWidth(), viewport.getHeight()));
    }

    /**
     * Compares given value and returns it back but never anything less than given min or more than given max.
     *
     * @param value The value (double) for comparison
     * @param min Min value to return (double)
     * @param max Max value to return (double)
     *
     * @return double
     */
    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }

        return Math.min(value, max);
    }

    /**
     * Gets a new Point2D value calculated from the current position of the cursor.
     *
     * @param imageViewCoordinates Cursor coordinates
     *
     * @return Point2D
     */
    private Point2D calcScaleCoordinate(Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(viewport.getMinX() + xProportion * viewport.getWidth(), viewport.getMinY() + yProportion * viewport.getHeight());
    }


    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    /** Starts the GUI.
     *
     * @param args Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
