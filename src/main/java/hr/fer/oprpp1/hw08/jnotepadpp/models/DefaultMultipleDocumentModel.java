package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
  private List<SingleDocumentModel> documents;
  private SingleDocumentModel currentDocument;
  private List<MultipleDocumentListener> listeners;

  public DefaultMultipleDocumentModel() {
    this.documents = new ArrayList<>();
    this.currentDocument = null;
    this.listeners = new ArrayList<>();
  }

  private void notifyListener(Consumer<? super MultipleDocumentListener> action) {
    listeners.forEach(action);
  }

  @Override
  public SingleDocumentModel loadDocument(Path path) {
    if(path == null)
      throw new NullPointerException("Path can't be null");

    for(int i = 0; i < documents.size(); i++) {
      if(path.equals(documents.get(i).getFilePath())) {
        this.setSelectedIndex(i);
        return documents.get(i);
      }
    }
    byte[] data;
    try {
      data = Files.readAllBytes(path);
    } catch (IOException e) {
      return null;
    }
    String content = new String(data, StandardCharsets.UTF_8);
    SingleDocumentModel document = new DefaultSingleDocumentModel(path, content);
    this.addDocument(document);

    return document;
  }

  @Override
  public void setSelectedIndex(int index) {
    SingleDocumentModel prevDoc = currentDocument;
    currentDocument = index == -1 ? null : documents.get(index);
    super.setSelectedIndex(index);
    notifyListener(l -> l.currentDocumentChanged(prevDoc, currentDocument));
  }

  @Override
  public SingleDocumentModel getCurrentDocument() {
    return currentDocument;
  }

  private static JScrollPane getStyledJScrollPane(Component comp) {
    JScrollPane scrollPane = new JScrollPane(comp);
    scrollPane.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, comp.getBackground()));

    return scrollPane;
  }

  private void addDocument(SingleDocumentModel document) {
    if(document == null)
      throw new NullPointerException("Document can't be null");

    this.documents.add(document);

    Component textArea = document.getTextComponent();
    JScrollPane pane = new JScrollPane(textArea);
    pane.setBorder(BorderFactory.createMatteBorder(3, 3, 0, 0, textArea.getBackground()));

    Path path = document.getFilePath();
    if(path == null) {
      this.addTab("(unnamed)", loadIcon(document.isModified()), getStyledJScrollPane(document.getTextComponent()));
    } else {
      File file = path.toFile();
      this.addTab(file.getName(), loadIcon(document.isModified()), getStyledJScrollPane(document.getTextComponent()), file.getAbsolutePath());
    }

    document.addSingleDocumentListener(new SingleDocumentListener() {
      @Override
      public void documentModifyStatusUpdated(SingleDocumentModel model) {
        DefaultMultipleDocumentModel.this.setIconAt(documents.indexOf(model), loadIcon(model.isModified()));
      }

      @Override
      public void documentFilePathUpdated(SingleDocumentModel model) {
        DefaultMultipleDocumentModel.this.setTitleAt(documents.indexOf(model), model.getFilePath().toFile().getName());
      }
    });
    this.setSelectedIndex(this.documents.size() - 1);
    SingleDocumentModel old = currentDocument;
    currentDocument = document;
    notifyListener(l -> {
      l.documentAdded(document);
      l.currentDocumentChanged(old, currentDocument);
    });

  }

  private ImageIcon loadIcon(boolean modified) {
    byte[] bytes;
    try(InputStream is = this.getClass().getResourceAsStream("/icons/"+ (modified ? "redDisk.png" : "greenDisk.png"))) {
      if(is==null) {
        JOptionPane.showMessageDialog(this, "Icon doesn't exist", "Icon error", JOptionPane.ERROR_MESSAGE);
        return null;
      }
      bytes = is.readAllBytes();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this, "Unable to load disk icon or the icon doesn't exist", "Icon error", JOptionPane.ERROR_MESSAGE);
      return null;
    }

    return new ImageIcon(bytes);
  }

  @Override
  public void removeTabAt(int index) {
    documents.remove(index);
    super.removeTabAt(index);
  }

  @Override
  public SingleDocumentModel getDocument(int index) throws IndexOutOfBoundsException {
    return documents.get(index);
  }

  @Override
  public void addMultipleDocumentListener(MultipleDocumentListener a) {
    if(a == null)
      throw new NullPointerException("Listener can't be null");

    listeners.add(a);
  }

  @Override
  public void removeMultipleDocumentListener(MultipleDocumentListener a) {
    listeners.remove(a);
  }

  @Override
  public SingleDocumentModel createNewDocument() {
    SingleDocumentModel document = new DefaultSingleDocumentModel(null, "");
    addDocument(document);
    return document;
  }

  @Override
  public void saveDocument(SingleDocumentModel model, Path newPath) {
    if (newPath == null) {
      if (model.getFilePath() == null)
        throw new NullPointerException("No valid path was provided");
    } else {
      String name = newPath.toFile().getName();
      if(!name.contains(".")) {
        model.setFilePath(Path.of(newPath.toFile().getAbsolutePath()+".txt"));
      } else {
        model.setFilePath(newPath);
      }
    }

    JTextArea doc = model.getTextComponent();
    byte[] data = doc.getText().getBytes();
    try {
      Files.write(model.getFilePath(), data);
      model.setModified(false);
    } catch (IOException ignored) {}

    notifyListener(l -> l.currentDocumentChanged(currentDocument, currentDocument));
  }

  @Override
  public void closeDocument(SingleDocumentModel model) {
    if(model == null) return;

    int index = documents.indexOf(model);
    if(index == -1) return;

    removeTabAt(index);

    if(documents.isEmpty()) {
      this.setSelectedIndex(-1);
    } else if(index == 0) {
      this.setSelectedIndex(0);
    } else {
      this.setSelectedIndex(index);
    }
    notifyListener(l -> {
      l.documentRemoved(model);
    });
  }

  @Override
  public int getNumberOfDocuments() {
    return documents.size();
  }

  @Override
  public Iterator<SingleDocumentModel> iterator() {
    return new Iterator<>() {
      private int pos = 0;

      @Override
      public boolean hasNext() {
        return pos < DefaultMultipleDocumentModel.this.documents.size();
      }

      @Override
      public SingleDocumentModel next() {
        return DefaultMultipleDocumentModel.this.documents.get(pos++);
      }
    };
  }

  @Override
  public void forEach(Consumer<? super SingleDocumentModel> action) {
    if(action == null)
      throw new NullPointerException("Action can't be null");

    documents.forEach(action);
  }

  @Override
  public Spliterator<SingleDocumentModel> spliterator() {
    return null;
  }
}
