package ryuuri.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import ryuuri.io.ImageUtil;
import ryuuri.mapgen.CelluralMapHandler;

import javax.imageio.ImageIO;

/**
 * A class for the application GUI.
 */
public class GUI extends Application {
    private BorderPane mainView;
    private GridPane controls;
    private Slider widthSlider, heightSlider, chanceSlider, stepsSlider, xScaleSlider, yScaleSlider;
    private Button saveImgFile, saveDataBtn;
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

        // Input //
        widthSlider = new Slider(1, 10000, 50);
        heightSlider = new Slider(1, 10000, 50);
        chanceSlider = new Slider(1, 100, 40);
        stepsSlider = new Slider(0, 10000, 3);
        xScaleSlider = new Slider(1, 16, 4);
        yScaleSlider = new Slider(1, 16, 4);
        seedField = new LongField(Long.MIN_VALUE, Long.MAX_VALUE, 0);
        seedField.setPromptText("Seed");

        HBox widthFrame = createSlider(
                widthSlider,
                "x (px)",
                "Width of the dungeon in pixels.",
                50,
                10000
        );
        HBox heightFrame = createSlider(
                heightSlider,
                "y (px)",
                "Height of the dungeon in pixels.",
                50,
                10000
        );
        HBox chanceFrame = createSlider(
                chanceSlider,
                "%",
                "Chance for the cells (pixels) to die on each step.",
                40,
                100
        );
        HBox stepsFrame = createSlider(
                stepsSlider,
                "Steps",
                "The amount of simulation steps to run.",
                3,
                10000
        );
        HBox xScaleFrame = createSlider(
                xScaleSlider,
                "x (factor)",
                "Times to scale the x axis of the image.",
                4,
                16
        );
        HBox yScaleFrame = createSlider(
                yScaleSlider,
                "y (factor)",
                "Times to scale the y axis of the image.",
                4,
                16
        );

        String controlStyling = "-fx-spacing: 5px; -fx-background-color: white; -fx-padding: 10; -fx-font-size: 12; -fx-alignment: baseline-left;";

        Label seedLabel = new Label("Seed");
        seedLabel.setTooltip(new Tooltip("The seed for the dungeon. Inputting 0 or nothing means no seed. Max: 99999999."));
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
        Button generateBtn = new Button("Generate");
        saveImgFile = new Button("Save image as");
        saveDataBtn = new Button("Copy data to clipboard");
        // importBtn = new Button("Import raw data");

        HBox buttons = new HBox(generateBtn, saveImgFile, saveDataBtn);
        buttons.setStyle("-fx-spacing: 5px; -fx-alignment: baseline-left;");

        generateBtn.setStyle("-fx-padding: 10px; -fx-alignment: baseline-right;");

        saveImgFile.setDisable(true);
        saveDataBtn.setDisable(true);

        // Actions
        imageView = new ImageView();
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

        // Close all child windows when exiting the main app
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        mainView.setCenter(imageView);
        mainView.setLeft(controls);
        var scene = new Scene(mainView);

        stage.setMinWidth(960);
        stage.setMinHeight(570);
        stage.setTitle("Ryuuri");
        stage.getIcons().add(new Image("ryuuri-logo.png"));
        stage.setScene(scene);
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
                    Label stackOverflow = new Label("Stack overflow error! Try generating without the dungeon having to be connected or with lower values. Changing the logic version might help too.");
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

            rawData = cells.mapToString();
            imageUtil = new ImageUtil(cells.map, width, height);
            imageUtil.scaleData(xFactor, yFactor);
            imageUtil.generateImage();

            // Set current values, remove loading icon and enable controls
            Platform.runLater(() -> {
                mainView.setCenter(imageView);
                seedField.setValue(currentSeed);
                imageView.setImage(imageUtil.getImage());
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

    /** Copies the raw data to clipboard */
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
    private HBox createSlider(Slider slider, String unit, String tooltip, int initial, int max) {
        Label label = new Label(unit);
        label.setTooltip(new Tooltip(tooltip));
        label.setPrefWidth(50);

        final LongField longField = new LongField(0, max, initial);
        longField.setTooltip(new Tooltip("Click on me to edit the slider's value!"));
        longField.valueProperty().bindBidirectional(slider.valueProperty());
        longField.setPrefWidth(50);

        HBox sliderFrame = new HBox(10);
        sliderFrame.getChildren().addAll(label, longField, slider);
        sliderFrame.setStyle("-fx-background-color: white; -fx-padding:10; -fx-font-size: 12; -fx-alignment: baseline-left;");

        return sliderFrame;
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
