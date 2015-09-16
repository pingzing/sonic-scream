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

package sonicScream.services;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator 
{
    private static Map<Type, Object> _services;
    
    public static void initialize()
    {
        _services = new HashMap<>();        
    }
    
    public static <T> void registerService(Type serviceType, Object service)
    {
        try
        {
            _services.put(serviceType, service);
        }
        catch(NullPointerException ex)
        {
            System.err.printf("\nFailed to register service. Did you forget to initialize "
                    + "the ServiceLocator? Cause: %s", ex.getMessage());
        }
    }
    
    public static Object getService(Type serviceType)
    {
        return _services.get(serviceType);
    }
}
