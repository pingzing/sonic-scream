/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sonicScream.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import sonicScream.models.Script;

public class ScriptConverter implements Converter
{
    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
    {
        Script scr = (Script)source;
        writer.startNode("ParentCategoryName");
        writer.setValue(scr.getParentCategoryName());
        writer.endNode();
        
        writer.startNode("InternalScriptName");
        writer.setValue(scr.get)
        
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
    {
        
    }

    @Override
    public boolean canConvert(Class type)
    {
        return(type.equals(Script.class));
    }

}
