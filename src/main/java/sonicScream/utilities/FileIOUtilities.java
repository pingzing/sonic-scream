package sonicScream.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;

public class FileIOUtilities 
{
    public static Path chooseSoundFile(Stage parentStage) throws IOException
    {
        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);
        String previousSoundDir = settings.getSetting(Constants.SETTING_PREVIOUS_SOUND_DIRECTORY);
        Path initialDir = previousSoundDir != null ? Paths.get(previousSoundDir) : null;
        
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Sound files(*.mp3, *.wav)", "*.mp3", "*.wav"));
        chooser.setTitle("Select new sound file");        
        if(initialDir != null)
        {
            chooser.setInitialDirectory(initialDir.toFile());
        }
        File result = chooser.showOpenDialog(parentStage);
        settings.putSetting(Constants.SETTING_PREVIOUS_SOUND_DIRECTORY, result.getParent());
        return Paths.get(result.getCanonicalPath());
    }
}
