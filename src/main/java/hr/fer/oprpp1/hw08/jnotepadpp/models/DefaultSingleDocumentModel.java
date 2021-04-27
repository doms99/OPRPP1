package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DefaultSingleDocumentModel implements SingleDocumentModel{
  private Path path;
  private boolean modificationStatus;
  private JTextArea textArea;
  private List<SingleDocumentListener> listeners;

  public DefaultSingleDocumentModel(Path path, String content) {
    this.path = path;
    this.textArea = new JTextArea(content);
    this.modificationStatus = path == null;
    this.listeners = new ArrayList<>();

    this.textArea.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        changeStatue();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        changeStatue();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        changeStatue();
      }

      private void changeStatue() {
        DefaultSingleDocumentModel.this.setModified(true);
      }
    });
  }

  private void notifyListener(Consumer<? super SingleDocumentListener> action) {
    listeners.forEach(action);
  }

  @Override
  public Path getFilePath() {
    return path;
  }

  @Override
  public boolean isModified() {
    return modificationStatus;
  }

  @Override
  public void setModified(boolean status) {
    modificationStatus = status;
    notifyListener(l -> l.documentModifyStatusUpdated(this));
  }

  @Override
  public JTextArea getTextComponent() {
    return textArea;
  }

  @Override
  public void setFilePath(Path path) {
    if(path == null)
      throw new NullPointerException("Document path can't be null");

    this.path = path;
    notifyListener(l -> l.documentFilePathUpdated(this));
  }

  @Override
  public void addSingleDocumentListener(SingleDocumentListener l) {
    if(l == null)
      throw new NullPointerException("Listener can't be null");

    listeners.add(l);
  }


  @Override
  public void removeSingleDocumentListener(SingleDocumentListener l) {
    listeners.remove(l);
  }
}
