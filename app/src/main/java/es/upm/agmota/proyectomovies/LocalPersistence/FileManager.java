package es.upm.agmota.proyectomovies.LocalPersistence;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private String fileName;

    private Context context;

    public FileManager(String fileName, Context context) {
        this.fileName = fileName;
        this.context = context;
    }

    public void saveFile(List<Integer> filmsId) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE); // removes previously existing content
            fos.write(TextUtils.join("\n", filmsId).getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Integer> loadFile() {
        FileInputStream fis = null;
        List<Integer> aux = new ArrayList<>();
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                aux.add(Integer.decode(text));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return aux;
        }
    }
}
