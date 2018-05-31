package org.smooth.systems.ec.utils;

import java.util.HashMap;

import org.smooth.systems.ec.client.api.RegisterableComponent;
import org.smooth.systems.ec.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComponentCache<T extends RegisterableComponent> {

  private HashMap<String, T> elements = new HashMap<>();

  public synchronized void addFactory(T factory) {
    addFactory(factory, false);
  }

  public synchronized void addFactory(T factory, boolean replace) {
    boolean alreadyRegistered = elements.containsKey(factory.getName());
    if (!alreadyRegistered || alreadyRegistered && replace) {
      log.info("Register new component: {}=[{}]", factory.getName(), factory.getClass().getName());
      elements.put(factory.getName(), factory);
    }
  }

  public synchronized T getFactory(String name) throws NotFoundException {
    boolean alreadyRegistered = elements.containsKey(name);
    if (!alreadyRegistered) {
      log.warn("Unable to find element with name: {}", name);
      throw new NotFoundException("Unable to find element with name: " + name);
    }
    return elements.get(name);
  }
}
