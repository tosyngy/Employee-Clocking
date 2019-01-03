/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emplyeclockinsystem;

import java.util.Date;

/**
 *
 * @author tosyngy
 */
public class testdate {

  public static void main(String[] args) {
    int d = Integer.parseInt("Sat Nov 09 00:43:03 PST 2014".trim().substring(8, 10)) * 24;
    int d2 = Integer.parseInt(("" + new Date()).substring(8, 10)) * 24;
    int hr = (Integer.parseInt(("" + new Date()).substring(11, 13)) + d2) - (Integer.parseInt("Sat Nov 09 00:43:03 PST 2014".trim().substring(11, 13)) + d);
    System.out.println(hr);
  }
}
