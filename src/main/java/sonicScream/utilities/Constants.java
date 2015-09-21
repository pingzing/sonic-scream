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

public class Constants 
{    
    //Navigation enum
    public enum navigationSource {UNSET, STARTUP, MAIN}
    
    //-------Category names-------
    public static final String CATEGORY_HEROES = "Heroes";
    public static final String CATEGORY_ITEMS = "Items";
    public static final String CATEGORY_MUSIC = "Music";
    public static final String CATEGORY_VOICES = "Voices";
    
    //-------Settings file names-------
    public static final String SETTINGS_FILE_NAME = "settings.xml";
    public static final String CRC_CACHE_FILE_NAME = "crcs.xml";
    public static final String PROFILE_FILE_SUFFIX = "profile.xml";    
    public static final String PROFILE_FILES_DIRECTORY = "profiles";
    
    //-------Settings keys-------
    public static final String SETTING_MAIN_VPK_PATH = "mainVPKPathSetting";
    public static final String SETTING_MAIN_VPK_DIR = "mainVPKDirSetting";
    public static final String SETTING_ADDON_PATH = "addonPath";
    public static final String SETTING_ACTIVE_PROFILE ="activeProfile";
    
    //-------Standard VPK paths------
    public static final String HERO_SPELLS_SCRIPTS_VPK_PATH = "soundevents/game_sounds_heroes/";
    public static final String ITEMS_SCRIPTS_VPK_PATH = "soundevents/game_sounds_items.vsndevts_c";
    public static final String VOICE_SCRIPTS_VPK_PATH = "soundevents/voscripts/";
    //Music
    public static final String MUSIC_DEFAULT_SCRIPTS_VPK_PATH = "soundevents/music/valve_dota_001/";
    public static final String MUSIC_TI4_SCRIPTS_VPK_PATH = "soundevents/music/valve_ti4/";
    public static final String MUSIC_TI5_SCRIPTS_VPK_PATH = "soundevents/music/valve_ti5/";
    //TODO add custom music packs?
    
}
