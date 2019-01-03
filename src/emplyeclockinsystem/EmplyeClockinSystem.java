/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emplyeclockinsystem;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel;
/**
 *
 * @author Vicsoft
 */
public class EmplyeClockinSystem {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    UIManager.setLookAndFeel(new SubstanceCremeCoffeeLookAndFeel());
                    //UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
                    //UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());
                    //UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
                } catch (Exception e) {
                    System.out.println("Substance Raven Graphite failed to initialize");
                }
                new Login().setVisible(true);
            }
        });
    }
}