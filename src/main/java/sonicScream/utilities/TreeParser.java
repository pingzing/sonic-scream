package sonicScream.utilities;

import java.util.List;
import javafx.scene.control.TreeItem;

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
}
