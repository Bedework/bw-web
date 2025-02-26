/* ********************************************************************
    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.
*/
package org.bedework.web.exception;

import org.bedework.synch.shared.SynchDefs;

import jakarta.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

/** Base exception thrown by exchange synch classes
 * <p>You may be asking why an unchecked exception?
 *
 * <p>I became convinced after years of working with large
 * applications that checked exceptions are a nuisance to evil.
 *
 * <p>Eventually, every method has a "throws MyException" on it
 * because we wrap lower checked or unchecked exceptions in our
 * custom exception. Given that we allow that exception for every
 * method, why list it?
 *
 * <p>Here are some links - mnostly to old posts but still valid:
 * <ul>
 *   <li><a href="http://userstories.blogspot.com/2008/12/checked-exception-why-debat-is-not-over.html">http://userstories.blogspot.com/2008/12/checked-exception-why-debat-is-not-over.html</a></li>
 *   <li><a href="http://wiki.c2.com/?CheckedExceptionsAreOfDubiousValue">http://wiki.c2.com/?CheckedExceptionsAreOfDubiousValue</a></li>
 *   <li><a href="https://radio-weblogs.com/0122027/stories/2003/04/01/JavasCheckedExceptionsWereAMistake.html">https://radio-weblogs.com/0122027/stories/2003/04/01/JavasCheckedExceptionsWereAMistake.html</a></li>
 * </ul>
 *
 *
 *   @author Mike Douglass   douglm@rpi.edu
 */
public class WebException extends RuntimeException {
  /** > 0 if set
   */
  int statusCode = -1;
  QName errorTag;

  /** */
  public static QName unknownCalendarItemType = new QName(SynchDefs.synchNamespace,
                                                          "unknown-calendar-item-type");

  /* Internal errors */

  /**
   */
  public static String connectorNotStarted = "org.bedework.synch.error.connectorNotStarted";

  /** Constructor
   *
   * @param s
   */
  public WebException(final String s) {
    super(s);
    if (statusCode < 0) {
      statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
  }

  /** Constructor
   *
   * @param t
   */
  public WebException(final Throwable t) {
    super(t);
    if (statusCode < 0) {
      statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
  }

  /** Constructor
   *
   * @param st
   */
  public WebException(final int st) {
    statusCode = st;
  }

  /** Constructor
   *
   * @param st
   * @param msg
   */
  public WebException(final int st, final String msg) {
    super(msg);
    statusCode = st;
  }

  /** Constructor
   *
   * @param errorTag
   */
  public WebException(final QName errorTag) {
    statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    this.errorTag = errorTag;
  }

  /** Constructor
   *
   * @param st
   * @param errorTag
   */
  public WebException(final int st, final QName errorTag) {
    statusCode = st;
    this.errorTag = errorTag;
  }

  /** Constructor
   *
   * @param st
   * @param errorTag
   * @param msg
   */
  public WebException(final int st, final QName errorTag, final String msg) {
    super(msg);
    statusCode = st;
    this.errorTag = errorTag;
  }

  /** Set the status
   * @param val int status
   */
  public void setStatusCode(final int val) {
    statusCode = val;
  }

  /** Get the status
   *
   * @return int status
   */
  public int getStatusCode() {
    return statusCode;
  }

  /** Get the errorTag
   *
   * @return QName
   */
  public QName getErrorTag() {
    return errorTag;
  }
}
