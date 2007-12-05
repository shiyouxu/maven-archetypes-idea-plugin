package org.google.code.archetypes;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WarningDialog extends DialogWrapper {
  private FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();  
  private TextFieldWithBrowseButton workingDirectoryField = new TextFieldWithBrowseButton();

  protected String myMessage;
  protected Icon myIcon;

  public WarningDialog(Project project, String path, String message, String title, Icon icon) {
    super(project, false);

    workingDirectoryField.addBrowseFolderListener("Navigate to Working Directory", "Select the Location of Working Directory", null, descriptor);

    workingDirectoryField.setText(path);

    _init(title, message, icon);
  }

  private void _init(String title, String message, Icon icon) {
    setTitle(title);
    myMessage = message;
    myIcon = icon;
    setButtonsAlignment(SwingUtilities.CENTER);
    init();
  }

  protected Action[] createActions() {
    Action[] actions = new Action[1];

    actions[0] = new AbstractAction("Close") {
      public void actionPerformed(ActionEvent e) {
        close(0);
      }
    };

    actions[0].putValue(DEFAULT_ACTION, Boolean.TRUE);

    return actions;
  }

  public void doCancelAction() {
    close(-1);
  }

  protected JComponent createNorthPanel() {
    return null;
  }

  protected JComponent createCenterPanel() {
    JPanel panel = new JPanel(new BorderLayout(15, 0));

    if (myIcon != null) {
      JLabel iconLabel = new JLabel(myIcon);
      Container container = new Container();
      container.setLayout(new BorderLayout());
      container.add(iconLabel, BorderLayout.NORTH);
      panel.add(container, BorderLayout.WEST);
    }

    if (myMessage != null) {
      JLabel label = new JLabel();
      final JTextPane messageComponent = new JTextPane();
      messageComponent.setFont(label.getFont());
      if (BasicHTML.isHTMLString(myMessage)) {
        final HTMLEditorKit editorKit = new HTMLEditorKit();
        editorKit.getStyleSheet().addRule(UIUtil.displayPropertiesToCSS(label.getFont(), label.getForeground()));
        messageComponent.setEditorKit(editorKit);
        messageComponent.setContentType("text/html");
      }
      messageComponent.setText(myMessage);
      messageComponent.setEditable(false);
      messageComponent.setCaretPosition(0);
      messageComponent.setBackground(UIUtil.getOptionPaneBackground());
      messageComponent.setForeground(label.getForeground());

      final Dimension screenSize = messageComponent.getToolkit().getScreenSize();
      final Dimension textSize = messageComponent.getPreferredSize();
      if (textSize.width > screenSize.width * 4 / 5 || textSize.height > screenSize.height / 2) {
        final JScrollPane pane = ScrollPaneFactory.createScrollPane(messageComponent);
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        final int scrollSize = (int) new JScrollBar(JScrollBar.VERTICAL).getPreferredSize().getWidth();
        final Dimension preferredSize =
            new Dimension(Math.min(textSize.width, screenSize.width * 4 / 5) + scrollSize, Math.min(textSize.height, screenSize.height / 2) + scrollSize);
        pane.setPreferredSize(preferredSize);
        panel.add(pane, BorderLayout.CENTER);
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            final Dimension textSize = messageComponent.getPreferredSize();
            final Dimension preferredSize = new Dimension(Math.min(textSize.width, screenSize.width * 4 / 5) + scrollSize,
                Math.min(textSize.height, screenSize.height / 2) + scrollSize);
            pane.setPreferredSize(preferredSize);
            SwingUtilities.getWindowAncestor(pane).pack();
          }
        });
      } else {
        JPanel panel1 = new JPanel();

        panel1.add(messageComponent);
        panel1.add(workingDirectoryField);
        panel.add(panel1, BorderLayout.CENTER);
      }
    }
    return panel;
  }
  
}
