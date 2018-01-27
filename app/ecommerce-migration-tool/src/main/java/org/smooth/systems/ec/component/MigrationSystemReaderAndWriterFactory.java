package org.smooth.systems.ec.component;

import java.util.List;

import org.smooth.systems.ec.client.api.MigrationSystemReader;
import org.smooth.systems.ec.client.api.MigrationSystemWriter;
import org.smooth.systems.ec.exceptions.NotFoundException;
import org.smooth.systems.ec.utils.ComponentCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MigrationSystemReaderAndWriterFactory {

  private ComponentCache<MigrationSystemReader> readers = new ComponentCache<>();

  private ComponentCache<MigrationSystemWriter> writers = new ComponentCache<>();

  @Autowired
  public MigrationSystemReaderAndWriterFactory(List<MigrationSystemReader> readers, List<MigrationSystemWriter> writers) {
    readers.forEach(reader -> this.readers.addFactory(reader));
    writers.forEach(reader -> this.writers.addFactory(reader));
  }

  public MigrationSystemReader getSystemReaderForType(String typeName) throws NotFoundException {
    return readers.getFactory(typeName);
  }

  public MigrationSystemWriter getSystemWriterForType(String typeName) throws NotFoundException {
    return writers.getFactory(typeName);
  }
}
