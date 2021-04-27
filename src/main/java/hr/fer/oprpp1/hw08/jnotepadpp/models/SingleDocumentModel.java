package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.nio.file.Path;

public interface SingleDocumentModel {
  Path getFilePath();
  void setFilePath(Path path);
  boolean isModified();
  void setModified(boolean status);
  JTextArea getTextComponent();
  void addSingleDocumentListener(SingleDocumentListener l);
  void removeSingleDocumentListener(SingleDocumentListener l);

}
