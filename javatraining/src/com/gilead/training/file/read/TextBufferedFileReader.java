package com.gilead.training.file.read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class TextBufferedFileReader {

    public void read(String filePath){
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            br.lines().forEach(System.out::println);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }

    }

}
