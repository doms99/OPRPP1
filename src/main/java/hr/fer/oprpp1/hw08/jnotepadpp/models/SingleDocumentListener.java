package hr.fer.oprpp1.hw08.jnotepadpp.models;

import javax.swing.*;

public interface SingleDocumentListener {
  void documentModifyStatusUpdated(SingleDocumentModel model);
  void documentFilePathUpdated(SingleDocumentModel model);
}
