package emplyeclockinsystem;

import com.digitalpersona.onetouch.DPFPCaptureFeedback;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPImageQualityEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CaptureForm
        //extends JFrame {
        extends JDialog {

  public static DPFPCapture capturer = DPFPGlobal.getCaptureFactory().createCapture();
  public static JLabel picture = new JLabel();
  private JTextField prompt = new JTextField();
  public static JTextArea log = new JTextArea();
  private JTextField status = new JTextField("[status line]");
  Image im;
  static int hand;

  public CaptureForm(Frame owner) {
    //super();
    super(owner, true);
    setTitle("ThInc Fingerprint Enrollment");
    URL url = this.getClass().getClassLoader().getResource("ThInc.png");
    Image im = Toolkit.getDefaultToolkit().getImage(url);
    setIconImage(im);
    log.setLineWrap(true);
    setLayout(new BorderLayout());
    rootPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    picture.setPreferredSize(new Dimension(240, 280));
    picture.setBorder(BorderFactory.createLoweredBevelBorder());
    prompt.setFont(UIManager.getFont("Panel.font"));
    prompt.setEditable(false);
    prompt.setColumns(40);
    prompt.setMaximumSize(prompt.getPreferredSize());
    prompt.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Prompt:"),
                    BorderFactory.createLoweredBevelBorder()
            ));
    log.setColumns(40);
    log.setEditable(false);
    log.setFont(UIManager.getFont("Panel.font"));
    JScrollPane logpane = new JScrollPane(log);
    logpane.setBorder(
            BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Status:"),
                    BorderFactory.createLoweredBevelBorder()
            ));

    status.setEditable(false);
    status.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    status.setFont(UIManager.getFont("Panel.font"));

    JButton Refresh = new JButton("Refresh");
//        Refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Actions-view-refresh-icon.png")));
    Refresh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        log.setText("Fingerprint scanner was reset...");
        status.setText("SCAN voter RIGHT or THUMB finger for verification!!!!!");
        if (hand == 1) {
          prompt.setText("SCAN employee RIGHT THUMB!!!!!");
        } else {
          prompt.setText("SCAN employee RIGHT INDEX!!!!!");
        }
        prompt.setText("Using the fingerprint reader, scan the employee RIGHT THUMB or INDEX .");
        picture.setIcon(null);
      }

    });

    JButton quit = new JButton("Exit");
    //quit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Apps-session-logout-icon.png")));
    quit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setVisible(false);
      }
    });
    JPanel right = new JPanel(new BorderLayout());
    right.setBackground(Color.getColor("control"));
    right.add(prompt, BorderLayout.PAGE_START);
    right.add(logpane, BorderLayout.CENTER);

    JPanel center = new JPanel(new BorderLayout());
    center.setBackground(Color.getColor("control"));
    center.add(right, BorderLayout.CENTER);
    center.add(picture, BorderLayout.LINE_START);
    center.add(status, BorderLayout.PAGE_END);

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.TRAILING));
    bottom.setBackground(Color.getColor("control"));
//        if (Main.tabg != 1) {
    bottom.add(Refresh);
         //   Main.tabg = 0;
    // }
    bottom.add(quit);

    setLayout(new BorderLayout());
    add(center, BorderLayout.CENTER);
    add(bottom, BorderLayout.PAGE_END);

    this.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentShown(ComponentEvent e) {
        init();
        start();
      }

      @Override
      public void componentHidden(ComponentEvent e) {
        stop();
      }

    });

    pack();
    setLocationRelativeTo(null);
  }

  protected void init() {
    capturer.addDataListener(new DPFPDataAdapter() {
      @Override
      public void dataAcquired(final DPFPDataEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            makeReport("The fingerprint sample was captured.");

            setPrompt("Scan the same fingerprint again.");
            process(e.getSample());
          }
        });
      }
    });
    capturer.addReaderStatusListener(new DPFPReaderStatusAdapter() {
      @Override
      public void readerConnected(final DPFPReaderStatusEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            makeReport("------------------------------------------------------------------\nThe fingerprint reader was connected.");
          }
        });
      }

      @Override
      public void readerDisconnected(final DPFPReaderStatusEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            makeReport("The fingerprint reader was disconnected.");
          }
        });
      }
    });
    capturer.addSensorListener(new DPFPSensorAdapter() {
      @Override
      public void fingerTouched(final DPFPSensorEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            picture.setIcon(null);
            makeReport("The fingerprint reader was touched.");
          }
        });
      }

      @Override
      public void fingerGone(final DPFPSensorEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            makeReport("The finger was removed from the fingerprint reader.\n");
          }
        });
      }
    });
    capturer.addImageQualityListener(new DPFPImageQualityAdapter() {
      @Override
      public void onImageQuality(final DPFPImageQualityEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            if (e.getFeedback().equals(DPFPCaptureFeedback.CAPTURE_FEEDBACK_GOOD)) {
              makeReport("The quality of the fingerprint sample is good.");
            } else {
              makeReport("The quality of the fingerprint sample is poor.");
            }
          }
        });
      }
    });
  }

  protected void process(DPFPSample sample) {
    // Draw fingerprint sample image.
    drawPicture(convertSampleToBitmap(sample));
  }

  protected void start() {
    capturer.startCapture();
    setPrompt("Using the fingerprint reader, scan the employee RIGHT THUMB only.");
  }

  protected void stop() {
    capturer.stopCapture();
  }

  public void setStatus(String string) {
    status.setText(string);
  }

  public void setPrompt(String string) {
    prompt.setText(string);
  }

  public void makeReport(String string) {
    if (string.equals("The finger was removed from the fingerprint reader.\n")) {
      // log.append("\n" + string + "\n");
    } else {
      log.setText(string + "\n");
    }
  }

  public void drawPicture(Image image) {
    picture.setIcon(new ImageIcon(
            image.getScaledInstance(picture.getWidth(), picture.getHeight(), Image.SCALE_DEFAULT)));
  }

  protected Image convertSampleToBitmap(DPFPSample sample) {
    return DPFPGlobal.getSampleConversionFactory().createImage(sample);
  }

  protected DPFPFeatureSet extractFeatures(DPFPSample sample, DPFPDataPurpose purpose) {
    DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
    try {
      return extractor.createFeatureSet(sample, purpose);
    } catch (DPFPImageQualityException e) {
      return null;
    }
  }

}
