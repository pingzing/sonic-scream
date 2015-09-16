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

import org.apache.commons.lang3.text.WordUtils;

public class StringParsing {
    
    public static String getScriptNameFromFileName(String fileName)
    {
        String name = fileName;
        name = name.replace("game_sounds_", "");
        name = name.replace("vo_", "");
        int dotIndex = name.indexOf('.');
        name = name.substring(0, dotIndex);
                
        return name;
    }

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
        switch(name)
        {
            case "nevermore":
                return "shadow_fiend";
        }
        return name;
    }
}
