package ryuuri.ui;

import ryuuri.dao.ImageUtil;
import ryuuri.mapgen.CelluralMapHandler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A class for the application GUI.
 */

public class GUI extends Application {
    private GridPane header, content;
    private Slider widthField, heightField, chanceField, stepsField;
    private Button generateBtn, saveFileBtn;
    private ImageView dungeonImage;

    /**
     * The main window of the application.
     *
     * @param stage Main stage
     */
    private void initUI(Stage stage) {

        // Better antialiasing for the text.
        System.setProperty("prism.lcdtext", "false");

        // Basic UI settings //
        StackPane base = new StackPane();
        var layout = new VBox();
        content = new GridPane();
        header = new GridPane();

        content.setMinHeight(100);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(10));
        content.setVgap(5);
        content.setHgap(5);

        header.setAlignment(Pos.TOP_CENTER);
        header.setPadding(new Insets(10));
        header.setVgap(5);
        header.setHgap(5);

        // Sliders //
        widthField = new Slider(1, 10000, 30);
        heightField = new Slider(1, 10000, 30);
        chanceField = new Slider(1, 100, 45);
        stepsField = new Slider(1, 10000, 3);

        HBox widthFrame = createSlider(
                widthField,
                "x (px)",
                "Width of the dungeon in pixels.",
                30,
                10000
        );
        HBox heightFrame = createSlider(
                heightField,
                "y (px)",
                "Height of the dungeon in pixels.",
                30,
                10000
        );
        HBox chanceFrame = createSlider(
                chanceField,
                "%",
                "Chance for the cells (pixels) to die on each step.",
                45,
                100
        );
        HBox stepsFrame = createSlider(
                stepsField,
                "Steps",
                "The amount of simulation steps to run.",
                3,
                10000
        );

        // Buttons //
        generateBtn = new Button("Generate");

        // Do not focus on anything at app launch.
        generateBtn.setFocusTraversable(false);

        // Actions
        generateBtn.setOnAction(e -> {
            generate(
                    (int) widthField.getValue(),
                    (int) heightField.getValue(),
                    (int) chanceField.getValue(),
                    (int) stepsField.getValue()
            );

            content.getChildren().removeAll();
            content.add(dungeonImage, 0, 0);
        });

//        //saveFileBtn.setOnAction(e -> {
//            // Possibly TODO: Save to file
//        //});

        int row = 0;
        header.add(generateBtn,         0, row);
        header.add(widthFrame,         1, row);
        header.add(heightFrame,         1, ++row);
        header.add(chanceFrame,         1, ++row);
        header.add(stepsFrame,         1, ++row);

        // Close all child windows when exiting the main app
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        layout.getChildren().addAll(header, content);
        base.getChildren().add(layout);
        var scene = new Scene(base, 360, 360);

        stage.setMinHeight(360);
        stage.setMinWidth(360);
        stage.setTitle("Ryuuri");
        // TODO: Add an icon
        // stage.getIcons().add(new Image(""));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Helper method for creating the dungeon.
     *
     * @param width Width in pixels as integer
     * @param height Height in pixels as integer
     * @param chance The chance for the cells (pixels) to die as integer
     * @param steps How many simulation steps to do as integer
     */
    public void generate(int width, int height, int chance, int steps) {
        CelluralMapHandler cell2 = new CelluralMapHandler(width, height, chance, steps);

        // String output = cell2.mapToString();

        ImageUtil imageUtil = new ImageUtil(cell2.map);
        imageUtil.scaleData(1, 1);

        dungeonImage = new ImageView(imageUtil.generateImage());
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

        final IntField intField = new IntField(0, max, initial);
        intField.setTooltip(new Tooltip("Click on me to edit the slider's value!"));
        intField.valueProperty().bindBidirectional(slider.valueProperty());
        intField.setPrefWidth(50);

        HBox sliderFrame = new HBox(10);
        sliderFrame.getChildren().addAll(label, intField, slider);
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
