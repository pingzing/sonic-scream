/*
 * The MIT License
 *
 * Copyright 2015 nmca.
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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FilesEx 
{
    public static List<Path> listFiles(Path path) throws IOException
    {
        List<Path> files = new ArrayList<>();
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(path))
        {
            for(Path p : ds)
            {
                files.add(p);
            }
        }
        return files;
    }
    
    public static List<Path> listFiles(Path path, Filter<? super Path> filter) throws IOException
    {
        List<Path> files = new ArrayList<>();
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(path, filter))
        {
            for(Path p : ds)
            {
                files.add(p);
            }
        }
        return files;
    }
    
    public static List<Path> getDirectories(Path path) throws IOException
    {
        List<Path> dirs = new ArrayList<>();
        DirectoryStream.Filter<Path> filter = (Path file) 
                -> (Files.isDirectory(file));
        
        try(DirectoryStream<Path> ds = Files.newDirectoryStream(path, filter))
        {
            for(Path p: ds)
            {
                dirs.add(p);
            }
        }
        return dirs;
    }

}
