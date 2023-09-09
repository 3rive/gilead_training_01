package com.gilead.training.file.read;

import java.nio.file.Path;
import java.nio.file.Paths;


public class FileReaderDemoNio {
    public static void main(String[] args) {
        //read the file
        Path path = Paths.get("README.md");
        TextFileReader textFileReader = new TextFileReader();
        textFileReader.read(path);
    }
}
