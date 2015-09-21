/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sonicScream.models;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wrapper class for the Settings map, so JAXB can marhsall/unmarshall it.
 * @author Neil McAlister
 */
@XmlRootElement
public class SettingsMapWrapper 
{
    private Map<String, String> settingsMap = new HashMap<String, String>();
    public Map<String, String> getSettingsMap() { return settingsMap; }
    public void setSettingsMap(Map<String, String> value) { this.settingsMap = value; }
}
