package sonicScream.utilities;

import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.StringUtils;

public class TreeParser 
{
    public static TreeItem<String> searchForKey(TreeItem<String> tree, String key)
    {
        if(tree.getValue().trim().equals(key))
        {
            return tree;
        }
        
        List<TreeItem<String>> children = tree.getChildren();
        TreeItem<String> result = null;
        for(TreeItem<String> node : children)
        {
            if(result != null)
            {
                break;
            }
            result = searchForKey(node, key);
        }
        return result;
    }
    
    /**
     * Attempts to retrieve the list of waves used for a given tree. It will look
     * through the given tree for the first occurrence of "vsnd_files" (quotes
     * included), and return the wave0, wave1, etc entries underneath it.
     * @param root The tree to search for a "vsnd_files" entry in.
     * @return Either an Optional wrapped around a List of TreeItem<String>s, 
     * or Optional.empty().
     */
    public static Optional<List<TreeItem<String>>> getWaveStrings(TreeItem<String> root)
    {
        //The first child of vsnd_files is always a single "values" key, with all the vsnds as children.
        TreeItem<String> vsnds = searchForKey(root, "\"vsnd_files\"");
        if(vsnds != null)
        {
            List<TreeItem<String>> sounds = vsnds.getChildren();
            if(sounds != null)
            {
                return  Optional.ofNullable(sounds.get(0).getChildren());
            }
        }
      
        return Optional.empty();
        getWav
    }
    
    /**
     * Takes in a full Script's tree and removes everything but each entry's title
     * and its waves, and flattens the hierarchy. Does not modify the input tree.
     * @param root The tree to simplify.
     * @return A new copy of the now-simplified tree.
     */
    public static TreeItem<String> getSimpleTree(TreeItem<String> root)
    {
        TreeItem<String> local = new TreeItem<String>("root");
        for(TreeItem<String> child : root.getChildren())
        {
            List<TreeItem<String>> localWaveStrings = FXCollections.observableArrayList();
            List<TreeItem<String>> waveStrings = getWaveStrings(child).orElse(null);
            if(waveStrings == null) continue;
            for(TreeItem<String> wave : waveStrings)
            {                
                TreeItem<String> sound = new TreeItem<String>();
                //Remove whitespace, quotes, and wave# text.
                String waveValue = wave.getValue().trim();
                int thirdQuoteIndex = StringUtils.ordinalIndexOf(waveValue, "\"", 3);
                waveValue = (waveValue.substring(thirdQuoteIndex + 1, waveValue.length() - 1));  
                sound.setValue(waveValue);
                localWaveStrings.add(sound);
            }
            TreeItem<String> localChild = new TreeItem<>(child.getValue());
            localChild.getChildren().setAll(localWaveStrings);    
            local.getChildren().add(localChild);
        }
        return local;
    }
    
    //TODO: Transform a Simple Tree back into a regular tree. fuuuuun.
}
