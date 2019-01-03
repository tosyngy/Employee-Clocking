package emplyeclockinsystem;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

public class VerificationForm extends CaptureForm {

  private DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();

  VerificationForm(Frame owner) {
    super(owner);
  }

  @Override
  protected void init() {
    System.out.println("type " + Main.type);
    super.init();
    this.setTitle("Fingerprint Enrollment");
    updateStatus(0);
  }
  int one = 0, hr;

  static String bringcard, comp, sur, gende, add, username, geno, dob, type, timeIn, timeOut;
  DPFPVerificationResult result;
  DPFPVerificationResult result2;

  @Override
  protected void process(DPFPSample sample) {
    super.process(sample);
    try {
      byte[] dby;
      DPFPFeatureSet features = null;
      DPFPFeatureSet features2 = null;
      LoadDriver();
      String SQLCommand2 = "select bio,fullname,position,sex,dob,address,mobileno,username,bio_index from registration";
      rs = st.executeQuery(SQLCommand2);
      // rs.next();
      System.out.println(">>????? Vicsoft [hia");

      while (rs.next()) {
        System.out.println("na im b dix " + rs.getBinaryStream(1));
        bringcard = rs.getString(2);
        comp = rs.getString(3);
        sur = rs.getString(4);
        add = rs.getString(6);
        username = rs.getString(8);

        //bringImage = rs.getBinaryStream(1);
        try (InputStream stream = rs.getBinaryStream(1);) {
          dby = new byte[stream.available()];
          stream.read(dby);
        }

        t = DPFPGlobal.getTemplateFactory().createTemplate();
        t.deserialize(dby);
        setTemplate(t);
        features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        // Check quality of the sample and start verification if it's good
        if (features != null) {
          // Compare the feature set with our template
          DPFPTemplate ms = getTemplate();
          // JOptionPane.showMessageDialog(rootPane, Main.cardno);
          result = verificator.verify(features, ms);
          System.out.println("na d result b dix " + result.getFalseAcceptRate());
          if (result.isVerified()) {
//                        gettypeHere();
//                        if (type.equals("signin")) {
//                            makeReport("\n--------------------------------------------------------------------------------"
//                                    + "  \n|                     WARNING!!!                                                        |\n"
//                                    + "--------------------------------------------------------------------------------\n"
//                                    + "|YOU HAVE ALREADY SIGN IN THIS EMPLOYEE AT " + timeIn + ""
//                                    + "\n--------------------------------------------------------------------------------\n");
//                        } else {
            //JOptionPane.showMessageDialog(this, "Card No " + Main.cardno + " thumbprint verification was Succesful.", "Fingerprint Status", JOptionPane.INFORMATION_MESSAGE);
//System.out.println("wat is it oga "+pss.next());
            myImage(username);
            setPrompt("VERIFICATION SUCCESSFUL");
            one = 0;
            log.setText("");
            makeReport("\n--------------------------------------------------------------------------------"
                    + "   \n|                             employee's PROFILE                                      |\n"
                    + "--------------------------------------------------------------------------------\n"
                    + "|    Fullname: " + bringcard + "\n|   Username:  " + username + "\n|   Position: " + comp + "\n|    Gender: " + sur + "\n| Address: " + add + ""
                    + "\n--------------------------------------------------------------------------------\n");
            updateStatus(0);
            updateemployeeSession();

            break;
            //}
          } else {
            try (InputStream stream2 = rs.getBinaryStream(9);) {
              dby = new byte[stream2.available()];
              stream2.read(dby);
            }
            t2 = DPFPGlobal.getTemplateFactory().createTemplate();
            t2.deserialize(dby);
            setTemplate(t2);
            features2 = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);
            if (features2 != null) {
              // Compare the feature set with our template
              ms = getTemplate();
              // JOptionPane.showMessageDialog(rootPane, Main.cardno);
              result = verificator.verify(features, ms);
              System.out.println("na d result b dix " + result.getFalseAcceptRate());
              if (result.isVerified()) {
//                        gettypeHere();
//                        if (type.equals("signin")) {
//                            makeReport("\n--------------------------------------------------------------------------------"
//                                    + "  \n|                     WARNING!!!                                                        |\n"
//                                    + "--------------------------------------------------------------------------------\n"
//                                    + "|YOU HAVE ALREADY SIGN IN THIS EMPLOYEE AT " + timeIn + ""
//                                    + "\n--------------------------------------------------------------------------------\n");
//                        } else {
                //JOptionPane.showMessageDialog(this, "Card No " + Main.cardno + " thumbprint verification was Succesful.", "Fingerprint Status", JOptionPane.INFORMATION_MESSAGE);
//System.out.println("wat is it oga "+pss.next());
                myImage(username);
                setPrompt("VERIFICATION SUCCESSFUL");
                one = 0;
                log.setText("");
                makeReport("\n--------------------------------------------------------------------------------"
                        + "   \n|                             employee's PROFILE                                      |\n"
                        + "--------------------------------------------------------------------------------\n"
                        + "|    Fullname: " + bringcard + "\n|   Username:  " + username + "\n|   Position: " + comp + "\n|    Gender: " + sur + "\n| Address: " + add + ""
                        + "\n--------------------------------------------------------------------------------\n");
                updateStatus(0);
                updateemployeeSession();

                break;
              } else {
                if (rs.isLast()) {
                  updateStatus(result.getFalseAcceptRate());
                  break;
                }
              }

            }

          }
        }
      }
      con.close();
    } catch (HeadlessException | IOException | IllegalArgumentException | SQLException v) {
      System.err.println("ok na verification error " + v);
    }
    // Process the sample and create a feature set for the enrollment purpose.

  }

  private void updateStatus(int FAR) {
    // Show "False accept rate" value
    //  setStatus(String.format("False Accept Rate (FAR) = %1$s", FAR));
    setStatus(String.format("SCAN EMPLOYEE RIGHT THUMB!!!!!"));

    if ((one > 3 || FAR > 0)) {
      // JOptionPane.showMessageDialog(this, "Thumbprint verification was not Succesful.\n Please contact Administrator or Developer.", "Fingerprint Status", JOptionPane.ERROR_MESSAGE);
      log.setText("employee thumbprint verification was not Succesful.");
      picture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/imageser.jpg")));
      setPrompt("VERIFICATION NOT SUCCESSFUL. employee record not available.");
    }
    one++;
  }

  public void LoadDriver() {
    try {
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            con = DriverManager.getConnection("jdbc:odbc:speech");
//            st = con.createStatement();
      String url = "jdbc:mysql://127.0.0.1:3306/employeeatt";
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      con = DriverManager.getConnection(url, "root", "");
      //  con = DriverManager.getConnection("jdbc:odbc:chat");
      System.err.println("DATABASE CONNECTION ESTABLISHED...\n Remember if u are on the networkor internet change the ip of the system.");
      st = con.createStatement();
    } catch (Exception e) {
      System.out.println("Driver Error " + e);
    }
  }
  Connection con;
  PreparedStatement ps;
  ResultSet rs, pss;
  Statement st;
  DefaultComboBoxModel combomodel;
  DefaultTableModel dataSet;

  public static String TEMPLATE_PROPERTY = "template";
  public static DPFPTemplate template;

  public DPFPTemplate getTemplate() {
    return template;
  }

  public void setTemplate(DPFPTemplate template) {
    DPFPTemplate old = this.template;
    this.template = template;
    firePropertyChange(TEMPLATE_PROPERTY, old, template);
  }
  public static DPFPTemplate t;
  public static DPFPTemplate t2;

  void myImage(String card) {
    try {
      System.out.println("na here we dey " + username);
      String SQLCommand02 = "select dp from registration where username='" + card + "'";
      rs = st.executeQuery(SQLCommand02);
      rs.next();
      System.out.println("na here we dey " + rs.getBinaryStream(1));
      InputStream stream = rs.getBinaryStream(1);
      ByteArrayOutputStream out1 = new ByteArrayOutputStream();
      int a1 = stream.read();
      while (a1 >= 0) {
        out1.write((char) a1);
        a1 = stream.read();
      }
      Image myImage = Toolkit.getDefaultToolkit().createImage(out1.toByteArray());
      Image scaledDaffy = myImage.getScaledInstance(240, 280, Image.SCALE_AREA_AVERAGING);//To scale an image
      // jLabel22.setBorder(new javax.swing.border.MatteBorder(new javax.swing.ImageIcon(myImage)));
      picture.setIcon(null);
      picture.setIcon(new javax.swing.ImageIcon(scaledDaffy));
    } catch (Exception v) {
      System.out.println("image error " + v);
    }
  }

  void updateemployeeSession() {

    try {
      String type = Main.type;
      if (type.equals("signin")) {
        String sql = "INSERT INTO report VALUES(?,?,?,?,?,?,?,?)";
        ps = con.prepareStatement(sql);
        System.out.println("type is " + Main.type);
        ps.setString(1, "0");
        ps.setString(2, bringcard);
        ps.setString(3, username);
        ps.setString(4, "" + (new Date().getMonth() + 1));
        ps.setString(5, "" + new Date());
        ps.setString(6, "");
        ps.setString(7, "0");
        ps.setString(8, type);
        ps.executeUpdate();

      } else if (type.equals("signout")) {
        System.out.println("usernam is " + username);
        String sql = "update report set timeOut=?,type=?,duration=? where username='" + username + "'";
        ps = con.prepareStatement(sql);
        ps.setString(1, "" + new Date());
        ps.setString(2, type);
        ps.setInt(3, gettypeHere());
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      System.out.println("Na Vicsoft do dix stuff " + e);
    }
  }

  int gettypeHere() {
    try {
      LoadDriver();
      String SQLComm = "select timeIn,typefrom report where username='" + username + "' ORDER BY id DESC";
      rs = st.executeQuery(SQLComm);
      while (rs.next()) {
        type = rs.getString(2);
        timeIn = rs.getString(1);
        if (!type.equalsIgnoreCase("signout")) {
          int d = Integer.parseInt(timeIn.trim().substring(8, 10)) * 24;
          int d2 = Integer.parseInt(("" + new Date()).substring(8, 10)) * 24;
          hr = (Integer.parseInt(("" + new Date()).substring(11, 13)) + d2) - (Integer.parseInt(timeIn.trim().substring(11, 13)) + d);
          break;
        }
      }

    } catch (Exception e) {
    }
    return Integer.parseInt(("" + new Date()).substring(11, 13)) - Integer.parseInt(timeIn.trim().substring(11, 13));
  }
}
