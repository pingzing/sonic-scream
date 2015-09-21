package sonicScream.models;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ScriptListWrapper 
{
    private ArrayList<Script> scriptList = new ArrayList<Script>();
    public ArrayList<Script> getScriptList() { return scriptList; }
    public void setScriptList(ArrayList<Script> value) { this.scriptList = value; }
}
