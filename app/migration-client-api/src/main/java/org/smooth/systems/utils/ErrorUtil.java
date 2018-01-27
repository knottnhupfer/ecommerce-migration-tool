package org.smooth.systems.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ErrorUtil {

  private ErrorUtil() {
  }

  public static void throwAndLog(boolean condition, String errorMsg) {
    if (condition) {
      throwAndLog(errorMsg);
    }
  }

  /**
   * 
   * @param errorMsg
   * @return return statement will never be called
   */
  public static <T> T throwAndLog(String errorMsg) {
    log.error(errorMsg);
    throw new IllegalStateException(errorMsg);
  }

  /**
   * 
   * @param errorMsg
   * @return return statement will never be called
   */
  public static <T> T throwAndLog(String errorMsg, Throwable t) {
    log.error(errorMsg);
    throw new IllegalStateException(errorMsg, t);
  }
}
