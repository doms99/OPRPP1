package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {
  private boolean connected;
  private String language;
  private ILocalizationProvider provider;
  private ILocalizationListener listener;

  public LocalizationProviderBridge(ILocalizationProvider provider) {
    this.provider = provider;
    this.connected = false;
    listener = this::fire;
  }

  public void connect() {
    if(connected) return;

    if(language != null && !language.equals(getCurrentLanguage())) super.fire();

    provider.addLocalizationListener(listener);
    this.connected = true;
  }

  public void disconnect() {
    if(!connected) return;

    provider.removeLocalizationListener(listener);
    this.connected = false;
    language = provider.getCurrentLanguage();
  }

  @Override
  public String getString(String key) {
    return provider.getString(key);
  }

  @Override
  public String getCurrentLanguage() {
    return provider.getCurrentLanguage();
  }
}
