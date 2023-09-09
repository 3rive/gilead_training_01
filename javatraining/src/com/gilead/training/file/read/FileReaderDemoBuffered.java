package com.gilead.training.file.read;

public class FileReaderDemoBuffered {
    //read the file
    public static void main(String[] args) {

        String path = "C:\\Users\\HP\\IdeaProjects\\sample.txt";
        TextBufferedFileReader textFileReader = new TextBufferedFileReader();
        textFileReader.read(path);
    }
}
