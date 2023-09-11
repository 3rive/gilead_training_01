package com.gilead.training.file.read;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileBufferWrite {
    public static void main(String[] args) throws IOException {
        String text = "Rajapandian Hello!!!";
        String path = "C:\\Users\\HP\\IdeaProjects\\write.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        bufferedWriter.write(text);
        bufferedWriter.close();
    }
}
