package hr.fer.oprpp1.hw08.jnotepadpp.localization.components;

import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.localization.ILocalizationProvider;

import javax.swing.*;

public abstract class LocalizedAction extends AbstractAction {
  private static long serialVersionUID = 1L;
  private String key;
  private ILocalizationProvider provider;
  private ILocalizationListener listener;

  public LocalizedAction(String key, ILocalizationProvider provider) {
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
    LocalizedAction.this.putValue(Action.NAME, provider.getString(key));
    LocalizedAction.this.putValue(Action.SHORT_DESCRIPTION, provider.getString(key+"_desc"));
  }
}
