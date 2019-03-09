package com.outlook.cucumber;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class test {

  @Test
  public void test() {
    System.out.print(System.getProperty("user.dir")+"\n");  
    String path = "C:\\Users\\Toshiba\\hub\\SEP-Assignment-B\\src\\test\\ressources\\cat.jpg";
    File tmpDir = new File(path);
    System.out.println(tmpDir.exists());
    
    URL url = this.getClass().getResource("/cat.jpg");
    System.out.println(url);
    
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString();
    System.out.println("Current relative path is: " + s);
    }

}
