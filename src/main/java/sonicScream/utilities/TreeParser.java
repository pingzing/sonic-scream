package sonicScream.utilities;

import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.StringUtils;

public class TreeParser 
{
    /**
     * Recursively searches the given tree for the given key. Returns upon finding the first instance.
     * @param tree
     * @param key
     * @return 
     */
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
    }    
}
