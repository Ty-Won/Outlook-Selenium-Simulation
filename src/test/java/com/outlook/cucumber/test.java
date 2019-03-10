package com.outlook.cucumber;

import static org.junit.Assert.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

public class test {

  @Test
  public void test() {
    LocalTime sendTime = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String time = sendTime.format(formatter);  
    String hold = "h";
  }

}
