package project.windows;

import javax.swing.*;

public class WindowError {

    public static void showError(JFrame frame, String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(JFrame frame, String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static int showQuestionMessage(JFrame frame, String message, Object[] options, String title) {
        return JOptionPane.showOptionDialog(frame, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
    }
}
