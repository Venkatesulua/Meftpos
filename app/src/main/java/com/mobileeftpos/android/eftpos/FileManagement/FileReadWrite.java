package com.mobileeftpos.android.eftpos.FileManagement;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by venkat on 7/17/2017.
 */

public class FileReadWrite {

    public static void WriteIntoFile(String fileName, byte[] bytes) throws Exception {
        File f = new File(fileName);
        long fileLength = f.length();
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        raf.seek(fileLength);
        raf.write(bytes);
        raf.close();
    }

    public byte[] ReadFromFile(String fileName,int Offset,int noOfBytes){

        byte[] buffer = new byte[noOfBytes];
        try {
            RandomAccessFile randomAccessFile = null;
            File f = new File(fileName);
            long fileLength = f.length();

            if(Offset>fileLength)
                return null;

            // read / write permissions
            randomAccessFile = new RandomAccessFile(f, "r");

            // Place the file pointer at the end of the first line
            randomAccessFile.seek(Offset);

            randomAccessFile.read(buffer,Offset,noOfBytes);
            System.out.println(new String(buffer));

            randomAccessFile.close();
        }catch(IOException ex){
            return null;
        }
        return buffer;
    }
    public static void DeleteFile(String fileName){
        File f = new File(fileName);
        if(f != null)
            f.delete();
        return;
    }


}
