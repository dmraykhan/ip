package twizzy.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import twizzy.Twizzy;

/** Controls the main Twizzy chat window. */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    private Twizzy twizzy;

    /** Configures scrolling after FXML fields have been injected. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the task application used to process GUI commands.
     *
     * @param twizzy application instance to use
     */
    public void setTwizzy(Twizzy twizzy) {
        this.twizzy = twizzy;
        twizzy.initializeForGui();
        dialogContainer.getChildren().add(DialogBox.bot("Yo, I'm Twizzy — your task-list twin.\n"
                + "Drop a command. I'll keep the chaos organized."));
    }

    /** Processes text submitted with Enter or the Send button. */
    @FXML
    private void handleUserInput() {
        String command = userInput.getText();
        if (command.isBlank()) {
            return;
        }

        dialogContainer.getChildren().add(DialogBox.user(command));
        dialogContainer.getChildren().add(DialogBox.bot(twizzy.getResponse(command)));
        userInput.clear();

        if (command.equals("bye")) {
            Platform.runLater(Platform::exit);
        }
    }
}
