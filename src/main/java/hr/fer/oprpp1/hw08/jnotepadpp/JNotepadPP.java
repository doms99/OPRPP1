package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.LocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.components.LocalizedAction;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.components.LocalizedJMenu;
import hr.fer.oprpp1.hw08.jnotepadpp.models.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

public class JNotepadPP extends JFrame {

  /**
   * JTabbedPane and MultipleDocumentModel.
   */
  private MultipleDocumentModel tabbedPane;

  /**
   * Localization component.
   */
  private final FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

  /**
   * Application name.
   */
  private static final String NAME = "JNotepad++";

  /**
   * Name of new documents created inside the application.
   */
  private static final String NEW_DOCUMENT_NAME = "unnamed";

  /**
   * Action that creates new document and adds it to objects MultipleDocumentsModel.
   */
  private final Action newDocumentAction = new LocalizedAction("new", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      tabbedPane.createNewDocument();
    }
  };

  /**
   * Opens an existing document from the computer.
   */
  private final Action openDocumentAction = new LocalizedAction("open", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser = new JFileChooser();
      int result = fileChooser.showOpenDialog(JNotepadPP.this);

      if(result != JFileChooser.APPROVE_OPTION) return;

      File file = fileChooser.getSelectedFile();
      Path filePath = file.toPath();
      if(!Files.isReadable(filePath)) {
        JOptionPane.showMessageDialog(JNotepadPP.this, "The file " + file.getAbsolutePath() + " doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
      tabbedPane.loadDocument(filePath);
    }
  };

  /**
   * Saves current document.
   * If document doesn't have a path associated with it it will open a file chooser dialog.
   */
  private final Action saveDocumentAction = new LocalizedAction("save", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      SingleDocumentModel currDoc = tabbedPane.getCurrentDocument();
      Path path = currDoc.getFilePath();
      if(path == null) {
        path = selectPath(null);
      }

      tabbedPane.saveDocument(currDoc, path);
    }
  };;

  /**
   * Saves the document to selected location and opens a file chooser dialog by default.
   */
  private final Action saveAsDocumentAction = new LocalizedAction("saveAs", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      SingleDocumentModel currDoc = tabbedPane.getCurrentDocument();

      Path path = selectPath(currDoc.getFilePath());
      if(path == null) return;

      if(currDoc.getFilePath() == null) {
        tabbedPane.saveDocument(tabbedPane.getCurrentDocument(), path);
        return;
      }

      SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, currDoc.getTextComponent().getText());
      tabbedPane.saveDocument(newDoc, path);
      tabbedPane.loadDocument(path);
    }
  };

  /**
   * Closes current document without checking if it was modified.
   */
  private final Action closeDocumentAction = new LocalizedAction("close", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      tabbedPane.closeDocument(tabbedPane.getCurrentDocument());
    }
  };

  /**
   * Action that displays current file length, line count...
   */
  private final Action infoDocumentAction = new LocalizedAction("info", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      SingleDocumentModel model = tabbedPane.getCurrentDocument();

      JTextArea textArea = model.getTextComponent();
      String content = textArea.getText();
      int numOfCharacters = content.length();
      int numOfNonBlankCharacters = content.replaceAll("\\s+", "").length();
      int numOfLines = textArea.getLineCount();
      JOptionPane.showMessageDialog(JNotepadPP.this, String.format("Your document has %s characters, %s non-blank characters and %s lines.", numOfCharacters, numOfNonBlankCharacters, numOfLines));
    }
  };

  /**
   * Copies selected part of the text from current document to the clipboard.
   */
  private final Action copySelectedPartAction = new LocalizedAction("copy", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      transferToClipboard(false);
    }
  };

  /**
   * Cuts selected part of the text from current document to the clipboard.
   */
  private final Action cutSelectedPartAction = new LocalizedAction("cut", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      transferToClipboard(true);
    }
  };

  /**
   * Inserts text from clipboard to selected location.
   */
  private final Action pasteToDocumentAction = new LocalizedAction("paste", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      String text;
      try {
        text = (String) clipboard.getData(DataFlavor.stringFlavor);
      } catch(UnsupportedFlavorException | IOException ex) {
        ex.printStackTrace();
        return;
      }

      JTextArea editor = tabbedPane.getCurrentDocument().getTextComponent();
      Document doc = editor.getDocument();
      Caret caret = editor.getCaret();
      if(caret.getDot() != caret.getMark())
        getSelected(true, false, false);

      try {
        doc.insertString(caret.getMark(), text, null);
      } catch(BadLocationException badLocationException) {
        badLocationException.printStackTrace();
      }
    }
  };

  /**
   * Exits the application and closes the window, but checks for all modified files.
   */
  private final Action exitWindowAction = new LocalizedAction("exit", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      JNotepadPP.this.closeWindowPrompt();
    }
  };

  /**
   * Sets the language to croatian.
   */
  private final Action hr = new LocalizedAction("hr", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      LocalizationProvider.getInstance().setLanguage("hr");
    }
  };

  /**
   * Sets the language to english.
   */
  private final Action en = new LocalizedAction("en", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      LocalizationProvider.getInstance().setLanguage("en");
    }
  };

  /**
   * Sets the language to german.
   */
  private final Action de = new LocalizedAction("de", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      LocalizationProvider.getInstance().setLanguage("de");
    }
  };

  /**
   * Transforms all selected letter to uppercase.
   */
  private final Action toUppercaseAction = new LocalizedAction("to_uppercase", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      String selected = getSelected(true, false, false);
      if(selected.equals("")) return;

      appendFromCursor(changeCase(selected, true));
    }
  };

  /**
   * Transforms all selected letter to lowercase.
   */
  private final Action toLowercaseAction = new LocalizedAction("to_lowercase", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      String selected = getSelected(true, false, false);
      if(selected.equals("")) return;

      appendFromCursor(changeCase(selected, false));
    }
  };

  /**
   * Transforms all selected letter to different case.
   * If letters were lowercase they will become uppercase and vice versa.
   * If nothing was selected, action will be applied to entire document.
   */
  private final Action invertCaseAction = new LocalizedAction("invert_case", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      String selected = getSelected(true, true, false);

      appendFromCursor(changeCase(selected, null));
    }
  };

  /**
   * Sorts selected lines in ascending order.
   */
  private final Action ascSortAction = new LocalizedAction("asc", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      String selected = getSelected(true, false, true);
      if(selected.equals("")) appendFromCursor("");

      appendFromCursor(sortText(selected, true));
    }
  };

  /**
   * Sorts selected lines in descending order.
   */
  private final Action descSortAction = new LocalizedAction("desc", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      String selected = getSelected(true, false, true);
      if(selected.equals("")) appendFromCursor("");

      appendFromCursor(sortText(selected, false));
    }
  };

  /**
   * Removes all duplicate lines from selected part.
   * Only first occurrences are left.
   */
  private final Action uniqueAction = new LocalizedAction("unique", this.flp) {
    @Override
    public void actionPerformed(ActionEvent e) {
      String selected = getSelected(true, false, true);
      if(selected.equals("")) appendFromCursor("");

      Set<String> set = new LinkedHashSet<>();
      Collections.addAll(set, selected.split("\n"));

      StringBuilder builder = new StringBuilder();
      String[] array = new String[1];
      array = set.toArray(array);
      for(int i = 0; i < array.length-1; i++) {
        builder.append(array[i]).append('\n');
      }
      builder.append(array[array.length-1]);
      appendFromCursor(builder.toString());
    }
  };

  /**
   * Creates instance of the class and initializes values and GUI.
   */
  public JNotepadPP() {
    super(NAME);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setPreferredSize(new Dimension(1000, 600));
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        JNotepadPP.this.closeWindowPrompt();
      }
    });
    createActions();
    initGUI();
    pack();
    setLocationRelativeTo(null);
  }

  /**
   * Initializes GUI.
   */
  private void initGUI() {
    DefaultMultipleDocumentModel model = new DefaultMultipleDocumentModel();
    tabbedPane = model;
    model.addMultipleDocumentListener(new AbstractMultipleDocumentListener() {
      @Override
      public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if(currentModel == null) {
          JNotepadPP.this.setTitle(NAME);
          return;
        }

        Path path = currentModel.getFilePath();
        JNotepadPP.this.setTitle((path == null ? "(" + NEW_DOCUMENT_NAME + ")" : path.toFile().getName()) + " - " + NAME);
      }
    });

    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    cp.add(panel, BorderLayout.CENTER);

    panel.add(model, BorderLayout.CENTER);

    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);

    JMenu fileMenu = new LocalizedJMenu("file", this.flp);
    fileMenu.add(new JMenuItem(newDocumentAction));
    fileMenu.add(new JMenuItem(openDocumentAction));
    fileMenu.add(new JMenuItem(saveDocumentAction));
    fileMenu.add(new JMenuItem(saveAsDocumentAction));
    fileMenu.addSeparator();
    fileMenu.add(new JMenuItem(closeDocumentAction));
    fileMenu.add(new JMenuItem(infoDocumentAction));
    fileMenu.addSeparator();
    fileMenu.add(new JMenuItem(exitWindowAction));
    menuBar.add(fileMenu);

    JMenu editMenu = new LocalizedJMenu("edit", this.flp);
    editMenu.add(new JMenuItem(copySelectedPartAction));
    editMenu.add(new JMenuItem(cutSelectedPartAction));
    editMenu.add(new JMenuItem(pasteToDocumentAction));
    editMenu.addSeparator();

    JMenu sortMenu = new LocalizedJMenu("sort", this.flp);
    sortMenu.add(new JMenuItem(ascSortAction));
    sortMenu.add(new JMenuItem(descSortAction));
    editMenu.add(sortMenu);

    editMenu.add(new JMenuItem(uniqueAction));

    menuBar.add(editMenu);

    JMenu langMenu = new LocalizedJMenu("lang", this.flp);
    langMenu.add(new JMenuItem(hr));
    langMenu.add(new JMenuItem(en));
    langMenu.add(new JMenuItem(de));
    menuBar.add(langMenu);

    JMenu caseMenu = new LocalizedJMenu("change_case", this.flp);
    caseMenu.add(new JMenuItem(toUppercaseAction));
    caseMenu.add(new JMenuItem(toLowercaseAction));
    caseMenu.add(new JMenuItem(invertCaseAction));

    menuBar.add(caseMenu);

    Dimension separator = new Dimension(20, 20);

    JToolBar toolBar = new JToolBar(flp.getString("toolbar"));
    JButton newButton = new JButton(newDocumentAction);
    JButton openButton = new JButton(openDocumentAction);
    JButton saveButton = new JButton(saveAsDocumentAction);
    toolBar.add(newButton);
    toolBar.add(openButton);
    toolBar.add(saveButton);
    toolBar.addSeparator(separator);

    JButton copyButton = new JButton(copySelectedPartAction);
    JButton cutButton = new JButton(cutSelectedPartAction);
    JButton pasteButton = new JButton(pasteToDocumentAction);
    toolBar.add(copyButton);
    toolBar.add(cutButton);
    toolBar.add(pasteButton);
    toolBar.addSeparator(separator);

    JButton toUppercaseButton = new JButton(toUppercaseAction);
    JButton toLowercaseButton = new JButton(toLowercaseAction);
    JButton invertCaseButton = new JButton(invertCaseAction);
    toolBar.add(toUppercaseButton);
    toolBar.add(toLowercaseButton);
    toolBar.add(invertCaseButton);

    cp.add(toolBar, BorderLayout.PAGE_START);
    JPanel statusBar = new JPanel();
    statusBar.setLayout(new GridLayout(1, 3));

    JLabel left = new JLabel();
    JLabel middle = new JLabel();
    JLabel right = new JLabel();
    right.setHorizontalAlignment(SwingConstants.RIGHT);

    initStatusBar(left, middle);
    flp.addLocalizationListener(new ILocalizationListener() {
      @Override
      public void localizationChanged() {
        initStatusBar(left, middle);
      }
    });
    initClock(right);

    statusBar.add(left);
    statusBar.add(middle);
    statusBar.add(right);

    panel.add(statusBar, BorderLayout.PAGE_END);

    tabbedPane.addMultipleDocumentListener(new AbstractMultipleDocumentListener() {
      @Override
      public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        setActionEnable(currentModel != null);
      }
    });
  }

  /**
   * Changes the case of passed text
   * @param text text that will be changed
   * @param toUppercase if <code>true</code> all letters will transformed to uppercase,
   *                    if <code>false</code> all letters will transformed to lowercase,
   *                    if <code>null</code> cases will be switched
   * @return transformed <code>String</code>
   */
  private String changeCase(String text, Boolean toUppercase) {
    char[] array = text.toCharArray();
    for(int i = 0; i < array.length; i++) {
      char c = array[i];
      if(toUppercase == null) {
        if(Character.isUpperCase(array[i])) {
          array[i] = Character.toLowerCase(c);
        } else if(Character.isLowerCase(array[i])) {
          array[i] = Character.toUpperCase(c);
        }
      } else if(toUppercase) {
        array[i] = Character.toUpperCase(c);
      } else {
        array[i] = Character.toLowerCase(c);
      }
    }
    return new String(array);
  }

  /**
   * Sorts passed lines selected order.
   * @param lines Lines that will be sorted.
   * @param ascending if <code>true</code> order will be ascending, else it will be descending
   * @return array containing sorted lines
   */
  private String[] sortLine(String[] lines, boolean ascending) {
    Locale locale = new Locale(flp.getCurrentLanguage());
    Collator collator = Collator.getInstance(locale);

    for(int i = lines.length-1; i >= 0; i--) {
      for(int j = 0; j < i; j++) {
        int r = ascending ? collator.compare(lines[i], lines[j]) : collator.compare(lines[j], lines[i]);
        if(r < 0) {
          String temp = lines[i];
          lines[i] = lines[j];
          lines[j] = temp;
        }
      }
    }
    return lines;
  }

  /**
   * Helper method that will split text by lines and call method for sorting.
   * @param text text that will be sorted
   * @param ascending if <code>true</code> order will be ascending, else it will be descending
   * @return text in which the lines are sorted
   */
  private String sortText(String text, boolean ascending) {
    String[] sorted = sortLine(text.split("\n"), ascending);
    StringBuilder builder = new StringBuilder();
    for(int i = 0; i < sorted.length-1; i++) {
      builder.append(sorted[i]).append('\n');
    }
    builder.append(sorted[sorted.length-1]);
    return builder.toString();
  }

  /**
   * Checks all currently opened documents if they were modified.
   * If document was modified then it will ask the user if he wants to save the document before saving.
   */
  private void closeWindowPrompt() {
    int num = tabbedPane.getNumberOfDocuments();
    while(num > 0) {
      SingleDocumentModel document = tabbedPane.getDocument(0);
      if(document == null)
        throw new NullPointerException("Document can't be null");

      if(document.isModified()) {
        Path path = document.getFilePath();
        String[] options = new String[] {flp.getString("yes"), flp.getString("no"), flp.getString("cancel")};
        int result = JOptionPane.showOptionDialog(
                this,
                (path == null ? "(" + NEW_DOCUMENT_NAME + ")" : path.toFile().getName()) + " " + flp.getString("unsaved_msg"),
                flp.getString("option_title"),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]);
        if(result == JOptionPane.OK_OPTION) {
          if(path == null) {
            path = selectPath(null);
            if(path != null) tabbedPane.saveDocument(document, path);

          } else tabbedPane.saveDocument(document, null);
        } else if(result == JOptionPane.NO_OPTION) {
          tabbedPane.closeDocument(document);
        } else return;
      } else {
        tabbedPane.closeDocument(document);
      }
      num = tabbedPane.getNumberOfDocuments();
    }
    this.dispose();
  }

  /**
   * Enables and disables certain actions depending if current document is null or not.
   * @param enable
   */
  private void setActionEnable(boolean enable) {
    saveDocumentAction.setEnabled(enable);
    saveAsDocumentAction.setEnabled(enable);
    closeDocumentAction.setEnabled(enable);
    infoDocumentAction.setEnabled(enable);
    copySelectedPartAction.setEnabled(enable);
    cutSelectedPartAction.setEnabled(enable);
    pasteToDocumentAction.setEnabled(enable);
    toLowercaseAction.setEnabled(enable);
    toUppercaseAction.setEnabled(enable);
    invertCaseAction.setEnabled(enable);
    ascSortAction.setEnabled(enable);
    descSortAction.setEnabled(enable);
    uniqueAction.setEnabled(enable);
  }

  /**
   * Initializes status bar components. First argument will show length of document and the second current cursor position
   * @param left left part of the status bar
   * @param middle middle part of the status bar
   */
  private void initStatusBar(JLabel left, JLabel middle) {
    String length = flp.getString("length");
    String ln = flp.getString("ln");
    String col = flp.getString("col");
    String sel = flp.getString("sel");
    left.setText(length+ ":----");
    middle.setText(ln +":-- "+ col +":-- "+ sel +":--");

    CaretListener listener = new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        if(tabbedPane.getCurrentDocument() == null) return;
        JTextArea editor = tabbedPane.getCurrentDocument().getTextComponent();
        Caret caret = editor.getCaret();
        String text = editor.getText();
        char[] array = text.toCharArray();
        left.setText(length + text.length());
        int line = 1;
        int lastNewLinePos = 0;
        int caretPos = Math.max(caret.getDot(), caret.getMark());
        for(int i = 0; i < caretPos; i++) {
          if(array[i] == '\n') {
            line++;
            lastNewLinePos = i;
          }
        }

        int column = caretPos - lastNewLinePos + 1;
        middle.setText(String.format(ln +":%d "+ col +":%d "+ sel +": %d", line, column, Math.abs(caret.getDot() - caret.getMark())));
      }
    };

    tabbedPane.addMultipleDocumentListener(new AbstractMultipleDocumentListener() {
      @Override
      public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if(previousModel != null) {
          previousModel.getTextComponent().removeCaretListener(listener);
        }
        if(currentModel == null) {

          left.setText(length+ ":----");
          middle.setText(ln +":-- "+ col +":-- "+ sel +":--");
        } else {
          currentModel.getTextComponent().addCaretListener(listener);
          listener.caretUpdate(null);
        }
      }
    });
  }

  /**
   * Initializes a thread for updating the clock of the status bar.
   * @param field filed that will be showing current time
   */
  private void initClock(JLabel field) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    Thread clockThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          Date date = Calendar.getInstance().getTime();
          field.setText(dateFormat.format(date));

          try {
            Thread.sleep(1000);
          } catch(InterruptedException ignored) {
          }
        }
      }
    });
    clockThread.setDaemon(true);
    clockThread.start();
  }

  /**
   * Opens a prompt for selection a location on which the file will be saved.
   * @param currPath initial selected path
   * @return selected path or <code>null</code> if nothing was selected
   */
  private Path selectPath(Path currPath) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setSelectedFile(currPath == null ? null : currPath.toFile());
    int result = fileChooser.showSaveDialog(JNotepadPP.this);

    if(result != JFileChooser.APPROVE_OPTION) return null;

    File file = fileChooser.getSelectedFile();

    if(file.exists()) {
      String[] options = new String[] {flp.getString("yes"), flp.getString("no")};
      result = JOptionPane.showOptionDialog(
              this,
              flp.getString("file_exists"),
              flp.getString("overwrite_title"),
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE,
              null,
              options,
              options[0]);
      if(result != JOptionPane.OK_OPTION)
        return null;
    }
    return file.toPath();
  }

  /**
   * Gets selected part of the text from current document.
   * @param remove if <code>true</code> selected part will be removed from the document
   * @param selectAll if <code>true</code> and if nothing was selected method will return everything from the document
   * @param fullLines if <code>true</code> method will return whole lines even if they weren't selected in full
   * @return selected part of the document dependent on options selected
   */
  private String getSelected(boolean remove, boolean selectAll, boolean fullLines) {
    if(tabbedPane.getCurrentDocument() == null)
      throw new NullPointerException("Can't select from null");

    JTextArea editor = tabbedPane.getCurrentDocument().getTextComponent();
    Document doc = editor.getDocument();
    int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
    int offset = 0;
    if(len == 0) {
      if(selectAll) {
        len = doc.getLength();
      } else {
        return "";
      }
    } else {
      offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
    }

    if(fullLines) {
      char[] array;
      try {
        array = doc.getText(0, doc.getLength()).toCharArray();
      } catch(BadLocationException e) {
        e.printStackTrace();
        return "";
      }

      while(offset > 0) {
        if(array[offset] == '\n') break;
        else offset--;
      }

      while(offset+len < doc.getLength()) {
        if(array[offset+len] == '\n') break;
        else len++;
      }
    }


    try {
      String text = doc.getText(offset, len);

      if(remove) {
        doc.remove(offset, len);
      }
      return text;
    } catch(BadLocationException ex) {
      return "";
    }
  }

  /**
   * Appends passed text to the document starting at the position that the cursor is on.
   * @param value <code>String</code> that will be appended
   */
  private void appendFromCursor(String value) {
    if(tabbedPane.getCurrentDocument() == null)
      throw new NullPointerException("Can't set to null");

    JTextArea editor = tabbedPane.getCurrentDocument().getTextComponent();
    Document doc = editor.getDocument();

    try {
      doc.insertString(editor.getCaretPosition(), value, null);
    } catch(BadLocationException ignored) {}
  }

  /**
   * Transfers the selected part of the text from the current document to the clipboard.
   * @param remove if <code>true</code> selected part will be removed from the current document
   */
  private void transferToClipboard(boolean remove) {
    String selected = JNotepadPP.this.getSelected(remove, false, false);

    if(selected.length() == 0) return;

    StringSelection stringSelection = new StringSelection(selected);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
  }

  /**
   * Initializes the actions.
   */
  private void createActions() {

    newDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control N"));
    newDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_N);

    openDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control O"));
    openDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_O);

    saveDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control S"));
    saveDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_S);
    saveDocumentAction.setEnabled(false);


    saveAsDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control alt S"));
    saveAsDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_A);
    saveAsDocumentAction.setEnabled(false);

    closeDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control alt X"));
    closeDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_X);
    closeDocumentAction.setEnabled(false);

    infoDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control I"));
    infoDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_I);
    infoDocumentAction.setEnabled(false);

    copySelectedPartAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control C"));
    copySelectedPartAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_C);
    copySelectedPartAction.setEnabled(false);

    cutSelectedPartAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control X"));
    cutSelectedPartAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_X);
    cutSelectedPartAction.setEnabled(false);

    pasteToDocumentAction.putValue(
            Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke("control V"));
    pasteToDocumentAction.putValue(
            Action.MNEMONIC_KEY,
            KeyEvent.VK_V);
    pasteToDocumentAction.setEnabled(false);

    toLowercaseAction.setEnabled(false);

    toUppercaseAction.setEnabled(false);

    invertCaseAction.setEnabled(false);

    ascSortAction.setEnabled(false);

    descSortAction.setEnabled(false);

    uniqueAction.setEnabled(false);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new JNotepadPP().setVisible(true);
    });
  }
}
