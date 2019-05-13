package net.bashayer.baking;

import android.util.Log;

import com.google.gson.Gson;

import net.bashayer.baking.model.Recipes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

public class JSONUtils {
    private static final String LOGTAG = "JSONUtils";

    public static Recipes getResponse(InputStream resourceReader) {
        String response = readResponse(resourceReader);
        Gson gson = new Gson();
        return gson.fromJson(response, Recipes.class);
    }

    private static String readResponse(InputStream resourceReader) {

        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e(LOGTAG, "Unhandled exception while using JSONResourceReader", e);
            }
        }

        return writer.toString();
    }
}
