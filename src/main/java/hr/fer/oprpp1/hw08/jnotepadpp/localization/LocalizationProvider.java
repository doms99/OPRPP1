package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider{
  private String language;
  private ResourceBundle bundle;
  private static final LocalizationProvider provider = new LocalizationProvider();

  private LocalizationProvider() {
    language = "en";
    updateBundle();
  }

  private void updateBundle() {
    bundle = ResourceBundle.getBundle("translation", Locale.forLanguageTag(language));
  }

  public static LocalizationProvider getInstance() {
    return provider;
  }

  public void setLanguage(String language) {
    if(language == null)
      throw new NullPointerException("Language can't be null.");

    this.language = language;
    updateBundle();
    super.fire();
  }

  @Override
  public String getString(String key) {
    return bundle.getString(key);
  }

  @Override
  public String getCurrentLanguage() {
    return language;
  }
}
