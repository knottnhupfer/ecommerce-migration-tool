package org.smooth.systems.ec.component;

import java.util.List;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.configuration.MigrationConfiguration;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.utils.ComponentCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MigrationSystemReaderAndWriterFactory {

  private final MigrationConfiguration config;

  private ComponentCache<MigrationSystemReader> readers = new ComponentCache<>();

  private ComponentCache<MigrationSystemWriter> writers = new ComponentCache<>();

  @Autowired
  public MigrationSystemReaderAndWriterFactory(MigrationConfiguration config, List<MigrationSystemReader> readers,
      List<MigrationSystemWriter> writers) {
    this.config = config;
    readers.forEach(reader -> this.readers.addFactory(reader));
    writers.forEach(reader -> this.writers.addFactory(reader));
  }

  public MigrationSystemReader getSystemReaderForType(String typeName) throws NotFoundException {
    return readers.getFactory(typeName);
  }

  public MigrationSystemWriter getSystemWriterForType(String typeName) throws NotFoundException {
    return writers.getFactory(typeName);
  }

  public MigrationSystemReader getMigrationReader() {
    return getMigrationReader(config.getSourceSystemName());
  }

  public MigrationSystemReader getMigrationReader(String systemName) {
    try {
      return getSystemReaderForType(systemName);
    } catch (NotFoundException e) {
      log.error("Error while retrieve migration reader. Reason: {}", e.getMessage());
      throw new IllegalStateException(e.getMessage());
    }
  }  

  public MigrationSystemWriter getMigrationWriter() {
    try {
      return getSystemWriterForType(config.getDestinationSystemName());
    } catch (NotFoundException e) {
      log.error("Error while retrieve migration writer. Reason: {}", e.getMessage());
      throw new IllegalStateException(e.getMessage());
    }
  }
}
