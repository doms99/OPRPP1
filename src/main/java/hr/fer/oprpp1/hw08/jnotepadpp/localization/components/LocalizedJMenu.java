package hr.fer.oprpp1.hw08.jnotepadpp.localization.components;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;

import javax.swing.*;

public class LocalizedJMenu extends JMenu {
  private final String key;
  private final ILocalizationProvider provider;
  private ILocalizationListener listener;

  public LocalizedJMenu(String key, ILocalizationProvider provider) {
    this.key = key;
    this.provider = provider;
    this.listener = new ILocalizationListener() {
      @Override
      public void localizationChanged() {
        update();
      }
    };
    provider.addLocalizationListener(listener);
    update();
  }

  private void update() {
    LocalizedJMenu.this.setText(provider.getString(key));
  }
}
