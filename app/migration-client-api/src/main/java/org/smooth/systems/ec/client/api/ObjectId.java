package org.smooth.systems.ec.client.api;

import lombok.Builder;
import lombok.Data;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on
 * 31.05.18.
 */
@Data
@Builder
public class ObjectId {

  private Long objectId;

  private String langIso;
}
