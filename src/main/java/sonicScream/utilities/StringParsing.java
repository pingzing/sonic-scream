/*
 * The MIT License
 *
 * Copyright 2015 Neil McAlister.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sonicScream.utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.text.WordUtils;

public class StringParsing {
    
    private static Map<String, String> _specialCaseNames = new HashMap<String, String>();    
    
    public static String getScriptNameFromFileName(String fileName)
    {
        String name = fileName;
        name = name.replace("game_sounds_", "");
        name = name.replace("vo_", "");
        name = name.replace("music_", "");
        int dotIndex = name.indexOf('.');
        name = name.substring(0, dotIndex);
                
        return name;
    }

    /**
     * Formats a hero name by doing the following: Replacing underscores with spaces,
     * and capitalizing individual words.
     * @param name The name to format.
     * @return The formatted name.
     */
    public static String prettyFormatScriptName(String name)
    {
        name = handleSpecialCaseName(name);
        name = name.replace("_", " ");
        name = WordUtils.capitalize(name);
        return name;
    }
        
    /**
     * Converts internal, or inconsistently-named heroes or objects into their game-facing, 
     * consistently-formatted versions. No spaces, only underscores, all lowercase.     
     * @param name The name to normalize.
     * @return The normalized name.
     */
    public static String handleSpecialCaseName(String name)
    {
        initSpecialCaseNames();        
        return _specialCaseNames.getOrDefault(name, name);        
    }
    
    private static void initSpecialCaseNames()
    {
        if(_specialCaseNames.isEmpty())
        {                  
            //----HEROES-----
            String[] odFirstNames = new String[] {"outhouse", "offworld", "overgrown", "obsidian"};
            String[] odLastNames = new String[] {"demolisher", "destroyer", "desolator", "demonstrator"};
            
            _specialCaseNames.put("antimage", "anti_mage");
            _specialCaseNames.put("batrider", "bat_rider");
            _specialCaseNames.put("crystalmaiden", "crystal_maiden");
            _specialCaseNames.put("doombringer", "doom");
            _specialCaseNames.put("drowranger", "drow_ranger"); 
            _specialCaseNames.put("furion", "nature's_prophet");
            _specialCaseNames.put("lifestealer", "life_stealer");
            _specialCaseNames.put("nevermore", "shadow_fiend");
            _specialCaseNames.put("nightstalker", "night_stalker");
            
            //Heh, couldn't resist.
            if(ThreadLocalRandom.current().nextInt() % 100 == 0)
            {
                String firstName = odFirstNames[ThreadLocalRandom.current().nextInt(0, odFirstNames.length)];
                String lastName = odLastNames[ThreadLocalRandom.current().nextInt(0, odLastNames.length)];
                _specialCaseNames.put("obsidian_destroyer", firstName + "_ " + lastName);
            }
            else
            {
                _specialCaseNames.put("obsidian_destroyer", "outworld_devourer");   
            }            
            _specialCaseNames.put("queenofpain", "queen_of_pain");
            _specialCaseNames.put("rattletrap", "clockwerk");
            _specialCaseNames.put("shadowshaman", "shadown_shaman");
            _specialCaseNames.put("shredder", "timbersaw");
            _specialCaseNames.put("skeletonking", "wraith_king");
            _specialCaseNames.put("stormspirit", "storm_spirit");
            _specialCaseNames.put("treant", "treant_protector");
            _specialCaseNames.put("vengefulspirit", "vengeful_spirit");
            _specialCaseNames.put("windrunner", "windranger");
            _specialCaseNames.put("wisp", "io");
            _specialCaseNames.put("witchdoctor", "witch_doctor");
            _specialCaseNames.put("zuus", "zeus"); 
            
            //----ITEMS----
            
            //----ANNOUNCERS----
            
            //----MUSIC
        }
    }
}
