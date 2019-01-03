package emplyeclockinsystem;

import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import java.awt.Frame;
import javax.swing.JOptionPane;

public class EnrollmentForm extends CaptureFormEnrolling {

  private DPFPEnrollment enroller = DPFPGlobal.getEnrollmentFactory().createEnrollment();
  public static byte print[];
  public static byte print2[];
  static int hand;

  EnrollmentForm(Frame owner) {
    super(owner);

  }

  @Override
  protected void init() {
    super.init();
    this.setTitle("ThInc Fingerprint Enrollment");
    updateStatus();
  }

  Main m = new Main();

  @Override
  protected void process(DPFPSample sample) {
    super.process(sample);
    // Process the sample and create a feature set for the enrollment purpose.
    DPFPFeatureSet features = extractFeatures(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);
    // Check quality of the sample and add to enroller if it's good
    if (features != null) {
      try {
        makeReport("employee fingerprint feature set was created.");
        //makeReport("The fingerprint feature set was created.");
        enroller.addFeatures(features);		// Add feature set to template.
      } catch (DPFPImageQualityException ex) {
        //System.out.println("i hia u " + ex);
      } finally {
        updateStatus();
        // Check if template has been created.
        switch (enroller.getTemplateStatus()) {
          case TEMPLATE_STATUS_READY:	// report success and stop capturing
            stop();
            m.setTemplate(enroller.getTemplate());
            if (hand == 1) {
              print = new byte[m.getTemplate().serialize().length];
              print = m.getTemplate().serialize();
              //   System.err.println("This is ur thumb print bytes <><<<???? " + print);
            } else {
              print2 = new byte[m.getTemplate().serialize().length];
              print2 = m.getTemplate().serialize();
              //  System.err.println("This is ur index print bytes <><<<???? " + print2);
            }
            System.err.println("This is ur print bytes <><<<???? " + print);
            setPrompt("Click Close, and then click Fingerprint Verification.");
            break;

          case TEMPLATE_STATUS_FAILED:	// report failure and restart capturing
            enroller.clear();
            stop();
            updateStatus();
            m.setTemplate(null);
            // JOptionPane.showMessageDialog(EnrollmentForm.this, "The fingerprint template is not valid. Repeat fingerprint enrollment.", "Fingerprint Enrollment", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(EnrollmentForm.this, "The employee fingerprint is not valid. Repeat fingerprint enrollment.", "Fingerprint Enrollment", JOptionPane.ERROR_MESSAGE);
            start();
            break;
        }
      }
    }
  }

  private void updateStatus() {
    // Show number of samples needed.
    setStatus(String.format("Fingerprint needed: %1$s", enroller.getFeaturesNeeded()));
    //    setStatus(String.format("Fingerprint samples needed: %1$s", enroller.getFeaturesNeeded()));
  }

}
