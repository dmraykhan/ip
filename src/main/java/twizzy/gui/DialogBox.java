package twizzy.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/** Represents one user or chatbot message in the conversation. */
public class DialogBox extends HBox {
    private static final String USER_STYLE = "-fx-background-color: #2f6fed; -fx-background-radius: 12;"
            + " -fx-text-fill: white;";

    private static final String BOT_STYLE = "-fx-background-color: #e8edf5; -fx-background-radius: 12;"
            + " -fx-text-fill: #1f2937;";

    private DialogBox(String text, boolean isUser) {
        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(340);
        message.setPadding(new Insets(10));
        message.setStyle(isUser ? USER_STYLE : BOT_STYLE);

        setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        setPadding(new Insets(6, 12, 6, 12));
        getChildren().add(message);
    }

    /** Creates a right-aligned dialog for a user message. */
    public static DialogBox user(String text) {
        return new DialogBox(text, true);
    }

    /** Creates a left-aligned dialog for a Twizzy response. */
    public static DialogBox bot(String text) {
        return new DialogBox(text, false);
    }
}
