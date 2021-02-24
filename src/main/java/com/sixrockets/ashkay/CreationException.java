/*
 *  Copyright 2004 David C. Brown
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sixrockets.ashkay;

/**
 * CreationExceptions represent an error in the creation of an object in an
 * ObjectFactory.
 *
 * @author <a href="mailto:dave@sixrockets.com">Dave Brown</a>
 */
public class CreationException extends Exception {

  private static final long serialVersionUID = 4147695037317302378L;

  /**
   * Constructs a default CreationException without a message or source
   */
  public CreationException() {
    super();
  }

  /**
   * Constructs a CreationException with a message but no source
   * @param msg - the message
   */
  public CreationException(String msg) {
    super(msg);
  }

  /**
   * Constructs a CreationException with a source but no message
   * @param p_source - the source exception
   */
  public CreationException(Throwable p_source) {
    super(p_source);
  }

  /**
   * Contructs a CreationException with a message and source
   * @param msg - the message
   * @param p_source - the source exception
   */
  public CreationException(String msg, Throwable p_source) {
    super(msg, p_source);
  }
}
