package twizzy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests Twizzy's GUI-facing behavior. */
class TwizzyTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_unreadableDataBlocksChangesAndPreservesFile() throws IOException {
        Path dataFile = temporaryDirectory.resolve("twizzy.txt");
        String invalidData = "T | invalid-status | task";
        Files.writeString(dataFile, invalidData);
        Twizzy twizzy = new Twizzy(new Storage(dataFile));

        twizzy.initializeForGui();
        String response = twizzy.getResponse("todo protect saved data");

        assertNotNull(twizzy.getGuiStartupError());
        assertTrue(response.contains("changes are locked to protect it"));
        assertEquals(invalidData, Files.readString(dataFile));
    }
}
