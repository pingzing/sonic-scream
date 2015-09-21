/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sonicScream.models;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CRCsMapWrapper 
{
    private Map<String, Long> crcs = new HashMap<>();
    public Map<String, Long> getCRCsMap() { return crcs; }
    public void setCRCs(Map<String, Long> value) { crcs = value; }
}
