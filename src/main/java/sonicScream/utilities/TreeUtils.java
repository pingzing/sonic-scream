package sonicScream.utilities;

import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.StringUtils;

public class TreeUtils
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

    /**
     * Gets the root of the tree the given node resides in. If the given node is already a root, simply returns the node.
     * @param node The node whose tree will be searched for a root.
     * @return The node's topmost root, or if the node is a root, the node itself.
     */
    public static TreeItem<String> getRoot(TreeItem<String> node)
    {
        if(node.getParent() == null)
        {
            return node;
        }

        TreeItem<String> parent = node;
        while(parent.getParent() != null)
        {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Walks up a tree from node, and returns the TreeItem just underneath the root in the given node's hierarchy.
     * @param node The node to walk upward from.
     * @return The TreeItem one level below the root in the given node's hierarchy.
     */
    public static TreeItem<String> getRootMinusOne(TreeItem<String> node)
    {
        if(node == null) return new TreeItem<String>("");
        TreeItem<String> child = node;
        TreeItem<String> parent = node.getParent();
        if(parent == null)
        {
            return new TreeItem<String>("");
        }

        if(parent.getParent() == null)
        {
            return child;
        }

        while(parent.getParent() != null)
        {
            parent = parent.getParent();
            child = child.getParent();
        }
        return child;
    }
}
