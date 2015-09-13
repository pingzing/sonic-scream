/*
 ** 2013 April 21
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.vpk.examples;

import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.apache.commons.io.FileUtils;

/**
 * Example: extract all entries in a VPK archive.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class Extract {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (String arg : args) {
            File file = new File(arg);
            VPKArchive vpk = new VPKArchive();
            
            System.out.println("In extract.");
            System.out.println(file);
            
            try {
                vpk.load(file);
            } catch (Exception ex) {
                System.err.println("Can't open archive: " + ex.getMessage());
                return;
            }
            
            File destDir = file.getParentFile();

            for (VPKEntry entry : vpk.getEntries()) {                
                
                //Put criteria to search for here
                if(entry.getName().contains("game_sounds_tusk")) {
                
                    File entryFile = new File(destDir, entry.getPath());
                    File entryDir = entryFile.getParentFile();
                    if (entryDir != null && !entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    try (FileChannel fc = FileUtils.openOutputStream(entryFile).getChannel()) {                   
                            fc.write(entry.getData());
                            System.out.println("Writing mirana's game sounds file out.");
                            break;                    
                    } 
                    catch (IOException ex) {
                        System.err.println("Can't write " + entry.getPath() + ": " + ex.getMessage());
                    }
                }
            }
        }
    }
}
