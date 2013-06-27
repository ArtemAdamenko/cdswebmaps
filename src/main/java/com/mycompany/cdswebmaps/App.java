package com.mycompany.cdswebmaps;

import com.mycompany.cdswebmaps.JsonReader;
import java.io.IOException;
import org.json.JSONException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, JSONException
    {
        System.out.println( "Hello World!" );
        JsonReader.main(args);
    }
}
