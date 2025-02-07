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
package org.bedework.web;

import org.bedework.util.logging.BwLogger;
import org.bedework.util.servlet.MessageEmit;
import org.bedework.web.exception.WebException;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** This class allows error message generation in the servlet world.
 *
 * @author Mike Douglass douglm@rpi.edu
 * @version 1.0
 */
public class ErrorEmitWeb implements MessageEmit {
  public static final String errorObjAttrName =
          "org.bedework.web.errorobj";

  public static final String exceptionPname =
          "org.bedework.web.error.exc";

  transient protected String id;

  /** We save the message property and the parameters in the following
   * class used for message generation.
   */
  @SuppressWarnings("unused")
  public static class Msg implements Serializable {
    private final ArrayList<Object> params = new ArrayList<>();
    private Object p1;
    private Object p2;
    private Object p3;

    private final String msgId;

    /**
     * @param msgId id
     */
    public Msg(final String msgId) {
      this.msgId = msgId;
    }

    /**
     * @param msgId id
     * @param o object to output
     */
    public Msg(final String msgId, final Object o) {
      this.msgId = msgId;
      addParam(o);
      p1 = o;
    }

    /**
     * @param msgId id
     * @param o1 object to output
     * @param o2 object to output
     */
    public Msg(final String msgId, final Object o1, final Object o2) {
      this.msgId = msgId;
      addParam(o1);
      addParam(o2);
      p1 = o1;
      p2 = o2;
    }

    /**
     * @param msgId id
     * @param o1 object to output
     * @param o2 object to output
     * @param o3 object to output
     */
    public Msg(final String msgId, final Object o1, final Object o2, final Object o3) {
      this.msgId = msgId;
      addParam(o1);
      addParam(o2);
      addParam(o3);
      p1 = o1;
      p2 = o2;
      p3 = o3;
    }

    /**
     * @return String message id
     */
    public String getMsgId() {
      return msgId;
    }

    /**
     * @return params
     */
    public ArrayList<?> getParams() {
      return params;
    }

    private void addParam(final Object o) {
      if (o != null) {
        params.add(o);
      }
    }
  }

  protected ArrayList<Msg> msgList = new ArrayList<>();

  public static class NoSessionException extends RuntimeException {
    public NoSessionException() {
      super("No HTTP session");
    }
  }

  /** Get the error object. If we haven't already got one and
   * getErrorObjAttrName returns non-null create one and implant it in
   * the session.
   *
   * @param request  Needed to locate session
   * @param id       An identifying name
   * @param clear true to clear list
   * @return ErrorEmitSvlt
   */
  public static ErrorEmitWeb getErrorObj(
          final HttpServletRequest request,
          final String id,
          final boolean clear) {
    final HttpSession sess = request.getSession(false);

    if (sess == null) {
      throw new NoSessionException();
    }

    final Object o = sess.getAttribute(errorObjAttrName);
    ErrorEmitWeb err = null;

    // Ensure it's initialised correctly
    if (o instanceof final ErrorEmitWeb errObj) {
      err = errObj;
    }

    if (err == null) {
      err = new ErrorEmitWeb();
    }

    err.reinit(id, clear);

    // Implant in session

    sess.setAttribute(errorObjAttrName, err);

    return err;
  }

  /**
   *
   */
  public ErrorEmitWeb() {
  }

  /** Generation of errors in the servlet world means adding them to the
   *  errors object. We need to call this routine on every entry to the
   *  application
   *
   * @param id       An identifying name
   * @param clear true to clear list
   */
  public void reinit(final String id,
                     final boolean clear) {
    this.id = id;

    if (clear) {
      msgList.clear();
    }
  }

  /**
   * @return msg list
   */
  public ArrayList<Msg> getMsgList() {
    return msgList;
  }

  @Override
  public void emit(final String pname) {
    if (debug()) {
      debugMsg(pname, null, null);
    }

    msgList.add(new Msg(pname));
  }

  @Override
  public void emit(final String pname, final int num) {
    if (debug()) {
      debugMsg(pname, "int", String.valueOf(num));
    }

    emit(pname, Integer.valueOf(num));
  }

  @Override
  public void setExceptionPname(final String pname) {
    throw new WebException("Unsupported: setExceptionPname");
  }

  @Override
  public void emit(final Throwable t) {
    if (debug()) {
      debugMsg(exceptionPname, "Throwable", String.valueOf(t.getMessage()));
    }

    String msg = t.getMessage();
    if (msg == null) {
      msg = "<No-message>";
    }

    error(msg, t);

    emit(exceptionPname, t.getMessage());
  }

  @Override
  public void emit(final String pname, final Object o){
    if (debug()) {
      if (o == null) {
        debugMsg(pname, "null object", "null");
      } else {
        debugMsg(pname, o.getClass().getName(), String.valueOf(o));
      }
    }

    if (o == null) {
      msgList.add(new Msg(pname));
    } else {
      msgList.add(new Msg(pname, o));
    }
  }

  @Override
  public void emit(final String pname, final Object o1, final Object o2){
    if (debug()) {
      debugMsg(pname, "2objects",
               o1 + "; " +
                       o2);
    }

    msgList.add(new Msg(pname, o1, o2));
  }

  @Override
  public void emit(final String pname, final Object o1, final Object o2, final Object o3){
    if (debug()) {
      debugMsg(pname, "2objects",
               o1 + "; " +
                       o2 + "; " +
                       o3);
    }

    msgList.add(new Msg(pname, o1, o2, o3));
  }

  /** Indicate no messages emitted. Null in this implementation.
   */
  @Override
  public void clear() {}

  /** @return true if any messages emitted
   */
  @Override
  public boolean messagesEmitted() {
    return !msgList.isEmpty();
  }

  protected void debugMsg(final String pname, 
                          final String ptype, 
                          final String pval) {
    debug("Emitted: property=" + pname +
                  " ptype=" + ptype +
                  " val(s)=" + pval);
  }

  /* ====================================================================
   *                   Logged methods
   * ==================================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) && (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}

