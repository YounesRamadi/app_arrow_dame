package controller;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.nio.file.Path;
import java.nio.file.Paths;

public class History {

    private String fileName;
    private LocalDateTime date;
    private File file;

    public History(Context context){
        date = LocalDateTime.now();
        file = new File(context.getFilesDir(), date.toString());
        Path path = Paths.get(file.getAbsolutePath());
    }

    public History(Context context, String dirPath) throws IOException {
        Files.list(new File(dirPath).toPath()).limit(1).forEach(path -> {
                    System.out.println(path);
                    file = new File(String.valueOf(path));
        });
    }

    public void write(String data) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        }
        catch(Exception e){
            System.err.println("writing error");
        }
    }

    public String read(){
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            System.err.println("readind error");
        }
        String result = text.toString();
        return result;
    }

}
