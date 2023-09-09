package com.gilead.training.file.read;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TextFileReader {
    public void read(Path path){
        try(Stream<String> fileStream = Files.lines(path)){
            fileStream.forEach(System.out::println);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
