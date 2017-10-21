/*
 * Copyright 2007 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing;

/**
 * Thrown when a barcode was successfully detected and decoded, but
 * was not returned because its checksum feature failed.
 *
 * @author Sean Owen
 */
public final class ChecksumException extends ReaderException {

  private static final com.google.zxing.ChecksumException INSTANCE = new com.google.zxing.ChecksumException();
  static {
    INSTANCE.setStackTrace(NO_TRACE); // since it's meaningless
  }

  private ChecksumException() {
    // do nothing
  }

  private ChecksumException(Throwable cause) {
    super(cause);
  }

  public static com.google.zxing.ChecksumException getChecksumInstance() {
    return isStackTrace ? new com.google.zxing.ChecksumException() : INSTANCE;
  }

  public static com.google.zxing.ChecksumException getChecksumInstance(Throwable cause) {
    return isStackTrace ? new com.google.zxing.ChecksumException(cause) : INSTANCE;
  }
}