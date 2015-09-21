package sonicScream.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.ArrayList;
import sonicScream.models.Category;
import sonicScream.models.Script;

/**
 *
 * @author Neil McAlister
 */
public class CategoryConverter implements Converter
{
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
    {
        Category cat = (Category)source;
        
        writer.startNode("CategoryName");
        writer.setValue(cat.getCategoryName());
        writer.endNode();
        
        writer.startNode("CategoryScripts");
        for(Script s : cat.getCategoryScripts())
        {
            context.convertAnother(s);
        }
        writer.endNode();
        
        writer.startNode("vpkPaths");
        for(String s : cat.getVPKPaths())
        {
            writer.startNode("path");
            writer.setValue(s);
            writer.endNode();
        }
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
    {
        reader.moveDown();
        String name = reader.getValue();
        reader.moveUp();
        
        ArrayList<Script> scriptList = new ArrayList<>();
        reader.moveDown();
        while(reader.hasMoreChildren())
        {
            scriptList.add((Script)context.convertAnother(null, Script.class));
        }
        reader.moveUp();
        
        ArrayList<String> vpkPaths = new ArrayList<>();
        reader.moveDown();
        while(reader.hasMoreChildren())
        {
            vpkPaths.add(reader.getValue());
        }
        Category cat = new Category(name, null, vpkPaths);
        return cat;
    }

    @Override
    public boolean canConvert(Class type)
    {
        return(type.equals(Category.class));
    }
    
}
