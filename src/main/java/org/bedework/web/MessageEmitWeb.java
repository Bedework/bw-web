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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** This class allows informational message generation.
 *
 * @author Mike Douglass douglm@rpi.edu
 * @version 1.0
 */
public class MessageEmitWeb extends ErrorEmitWeb {
  public static final String messageObjAttrName =
          "org.bedework.web.messageobj";

  /** Get the message object. If we haven't already got one and
   * getMessageObjAttrName returns non-null create one and implant it in
   * the session.
   *
   * @param request  Needed to locate session
   * @param id       An identifying name
   * @param clear clear list if true
   * @return MessageEmitWeb
   */
  public static MessageEmitWeb getMessageObj(
          final HttpServletRequest request,
          final String id,
          final boolean clear) {
    final HttpSession sess = request.getSession(false);

    if (sess == null) {
      throw new NoSessionException();
    }

    final Object o = sess.getAttribute(messageObjAttrName);
    MessageEmitWeb msg = null;

    // Ensure it's initialised correctly
    if (o instanceof MessageEmitWeb) {
      msg = (MessageEmitWeb)o;
    }

    if (msg == null) {
      msg = new MessageEmitWeb();
    }

    msg.reinit(id, clear);

    // Implant in session

    sess.setAttribute(messageObjAttrName, msg);

    return msg;
  }

  /**
   *
   */
  public MessageEmitWeb() {
  }
}

