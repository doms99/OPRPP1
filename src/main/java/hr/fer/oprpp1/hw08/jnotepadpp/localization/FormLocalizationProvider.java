package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FormLocalizationProvider extends LocalizationProviderBridge {
  public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
    super(provider);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        FormLocalizationProvider.this.disconnect();
      }

      @Override
      public void windowOpened(WindowEvent e) {
        FormLocalizationProvider.this.connect();
      }
    });
  }
}
