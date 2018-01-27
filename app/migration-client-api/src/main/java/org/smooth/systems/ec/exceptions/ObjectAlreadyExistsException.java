package org.smooth.systems.ec.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ObjectAlreadyExistsException extends Exception {

  private static final long serialVersionUID = 1L;

  public ObjectAlreadyExistsException(String msg) {
    super(msg);
  }
}
