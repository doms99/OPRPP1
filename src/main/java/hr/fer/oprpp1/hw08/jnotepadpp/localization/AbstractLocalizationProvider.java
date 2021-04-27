package hr.fer.oprpp1.hw08.jnotepadpp.localization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
  List<ILocalizationListener> listeners;

  public AbstractLocalizationProvider() {
    this.listeners = new ArrayList<>();
  }

  @Override
  public void addLocalizationListener(ILocalizationListener listener) {
    if(listener == null)
      throw new NullPointerException("Listener can't be null.");

    listeners.add(listener);
  }

  @Override
  public void removeLocalizationListener(ILocalizationListener listener) {
    listeners.remove(listener);
  }

  public void fire() {
    listeners.forEach(ILocalizationListener::localizationChanged);
  }
}
