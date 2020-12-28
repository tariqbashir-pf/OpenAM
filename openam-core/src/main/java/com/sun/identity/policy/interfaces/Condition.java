/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2006 Sun Microsystems Inc. All Rights Reserved
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://opensso.dev.java.net/public/CDDLv1.0.html or
 * opensso/legal/CDDLv1.0.txt
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at opensso/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * $Id: Condition.java,v 1.7 2009/06/19 22:54:46 mrudul_uchil Exp $
 *
 * Portions Copyrighted 2014 ForgeRock AS.
 */

package com.sun.identity.policy.interfaces;

import com.sun.identity.policy.ConditionDecision;
import com.sun.identity.policy.PolicyException;
import com.sun.identity.policy.Syntax;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The class <code>Condition</code> defines an interface 
 * to allow pluggable condition. These are used to control 
 * policy decisions based on parameters such as time,
 * authentication level of the user session and IP address from which
 * the user is making the request.
 *
 * A condition computes a <code>ConditionDecision</code> based on the state
 * of condition object as  set by <code>setProperties</code> 
 * method call and the environment passed in a map of key/value pairs. 
 *
 * <code>ConditionDecision</code> encapsulates  whether a <code>Policy</code> 
 * applies for the request and <code>Advice</code> messages  generated by 
 * the condition.
 *
 * The following Condition implementation are provided with the 
 * Policy framework:
 * <ul>
 * <li>AuthLevelCondition</li>
 * <li>LEAuthLevelCondition</li>
 * <li>AuthSchemeCondition</li>
 * <li>IPCondition</li>
 * <li>SimpleTimeCondition</li>
 * <li>SessionCondition</li>
 * <li>SessionPropertyCondition</li>
 * <li>AuthenticateToRealmCondition</li>
 * <li>AuthenticateToServiceCondition</li>
 * <li>LDAPFilterCondition</li>
 * </ul>
 *
 * All condition implementations should have a public no argument 
 * constructor.
 *
 * @see com.sun.identity.policy.ConditionDecision
 * 
 * @deprecated since 12.0.0, use {@link com.sun.identity.entitlement.EntitlementCondition}
 */
@Deprecated
public interface Condition extends Cloneable {

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of an AuthLevelCondition or LEAuthLevelCondition.
     */

    /** Key that is used to define the minimum authentication level 
     *  in an <code>AuthLevelCondition</code> or the maximum authentication 
     *  level in a  <code>LEAuthLevelCondition</code> of a  policy being 
     *  evaluated. In case of <code>AuthLevelCondition</code> policy would 
     *  apply if the request authentication level is at least the level 
     *  defined in condition while in case of <code>LEAuthLevelCondition</code> 
     *  policy would apply if the request authentication level is less than
     *  or equal to the level defined in the condition.
     *  The value should be a <code>Set</code> with only one 
     *  element. The element should be a  <code>String</code>, parse-able as 
     *  an integer or a realm qualified integer like "sun:1" where "sun" is a 
     *  realm name.":" needs to used a delimiter between realm name and the 
     *  level.
     *
     *  @see #setProperties(Map)
     */
    public static final String AUTH_LEVEL = "AuthLevel";

    /** Key that is used to define the authentication level of the request.
     *  Its passed down in the <code>env</code> Map to the 
     *  <code>getConditionDecision</code> call of an <code>AuthLevelCondition
     *  </code> or <code>LEAuthLevelCondition</code> for condition evaluation.
     *  <p>
     *  The value should be  an Integer or a <code>Set</code> of 
     *  <code>String</code>s. If it is a <code>Set</code> of 
     *  <code>String</code>s, each element of the set has to be parseable as 
     *  integer or should be a realm qualified integer like "sun:1". If the 
     *  <code>env</code> parameter is null or does not
     *  define value for <code>REQUEST_AUTH_LEVEL</code>,  the value for
     *  <code>REQUEST_AUTH_LEVEL</code> is obtained from the single sign
     *  on token of the user 
     *
     *  @see #getConditionDecision(SSOToken, Map)
     *  @see #AUTH_LEVEL
     */
    public static final String REQUEST_AUTH_LEVEL = "requestAuthLevel";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of an AuthSchemeConditon.
     */

    /** Key that is used to define the authentication scheme 
     *  in an <code>AuthSchemeCondition</code> of a policy.
     *  Policy would apply if the authentication scheme of the request is same
     *  as defined in the condition. The value should be
     *  a <code>Set</code> with only one element. The element should be a 
     *  <code>String</code>, the authentication scheme name.
     *
     *  @see #setProperties(Map)
     */
    public static final String AUTH_SCHEME = "AuthScheme";

    /**
     * Key that is used to specify application name
     * for the resources protected by the policy
     */
    public static final String APPLICATION_NAME = "ApplicationName";

    /**
     * Key that is used to specify the application
     * idle time out
     */
    public static final String APPLICATION_IDLE_TIMEOUT
            = "ApplicationIdleTimeout";

    /** Key that is used to define the name of authentication scheme of the 
     *  request. Its passed down as part of the <code>env</code> Map to
     *  <code>getConditionDecision</code> of an <code>AuthSchemeCondition</code>
     *  for condition evaluation.
     *  Value for the key should be a <code>Set</code> with each element being 
     *  a <code>String</code>.
     *  If the <code>env</code> parameter is null or does not
     *  define value for <code>REQUEST_AUTH_SCHEMES</code>,  the value for
     *  <code>REQUEST_AUTH_SCHEMES</code> is obtained from the single sign
     *  on token of the user 
     *
     *  @see #getConditionDecision(SSOToken, Map)
     *  @see #AUTH_SCHEME
     */
    public static final String REQUEST_AUTH_SCHEMES = "requestAuthSchemes";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of an AuthenticateToRealmCondition.
     */

    /** Key used in <code>AuthenticateToRealmCondition</code> to specify the 
     *  realm for which the user should authenticate for the policy to apply. 
     *  The value should be  a <code>Set</code> with only one element. 
     *  The should be a  <code>String</code>, the realm name.
     *
     *  @see #setProperties(Map)
     */
    public static final String AUTHENTICATE_TO_REALM = "AuthenticateToRealm";


    /** Key that is used to identify the names of authenticated realms 
     *  in the request. Its passed down as part of the <code>env</code> Map to
     *  <code>getConditionDecision</code> of an <code>
     *  AuthenticateToRealmCondition</code> for condition evaluation.
     *  Value for the key should be a <code>Set</code> with each element being 
     *  a <code>String</code>
     *  If the <code>env</code> parameter is null or does not
     *  define value for <code>REQUEST_AUTHENTICATED_TO_REALMS</code>,  the 
     *  value for <code>REQUEST_AUTHENTICATED_TO_REALMS</code> is obtained 
     *  from the single sign on token of the user 
     *
     *  @see #getConditionDecision(SSOToken, Map)
     *  @see #AUTHENTICATE_TO_REALM
     */
    public static final String REQUEST_AUTHENTICATED_TO_REALMS
            = "requestAuthenticatedToRealms";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of an AuthenticateToServiceCondition.
     */

    /** Key that is used in <code>AuthenticateToServiceCondition</code> to 
     *  specify the authentication chain for which the user should authenticate 
     *  for the policy to apply. 
     *  The value should be  a <code>Set</code> with only one element. 
     *  The should be a  <code>String</code>, the realm name.
     *
     *  @see #setProperties(Map)
     */
    public static final String AUTHENTICATE_TO_SERVICE
            = "AuthenticateToService";

    /** Key that is used to identify the names of authentication chains
     *  in the request. Its passed down as part of the <code>env</code> Map to
     *  <code>getConditionDecision</code> of an <code>
     *  AuthenticateToServiceCondition</code> for condition evaluation.
     *  Value for the key should be a <code>Set</code> with each element being 
     *  a <code>String</code>.
     *  If the <code>env</code> parameter is null or does not
     *  define value for <code>REQUEST_AUTHENTICATED_TO_SERVICES</code>,  the 
     *  value for <code>REQUEST_AUTHENTICATED_TO_SERVICES</code> is obtained 
     *  from the single sign on token of the user 
     *
     *  @see #getConditionDecision(SSOToken, Map)
     *  @see #AUTHENTICATE_TO_SERVICE
     */
    public static final String REQUEST_AUTHENTICATED_TO_SERVICES
            = "requestAuthenticatedToServices";

    /** Key that is used identify the advice messages from
     * <code>AuthSchemeCondition</code>
     */
    public static final String AUTH_SCHEME_CONDITION_ADVICE =
            "AuthSchemeConditionAdvice";

    /** Key that is used identify the advice messages from
     * <code>AuthenticateToServiceCondition</code>
     */
    public static final String AUTHENTICATE_TO_SERVICE_CONDITION_ADVICE
            = "AuthenticateToServiceConditionAdvice";

    /** Key that is used identify the advice messages from
     * <code>AuthLevelCondition</code>.
     */
    public static final String AUTH_LEVEL_CONDITION_ADVICE =
            "AuthLevelConditionAdvice";

    /** Key that is used identify the advice messages from
     * <code>AuthenticateToRealmCondition</code>
     */
    public static final String AUTHENTICATE_TO_REALM_CONDITION_ADVICE =
            "AuthenticateToRealmConditionAdvice";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of an IPCondition.
     */

    /** Key used in <code>IPCondition</code> to define the  start of IP 
     * address range for which a policy applies.
     * The value corresponding to the key  has to be a <code>Set</code> that 
     * has just one element which is a <code>String</code>
     * that conforms to the pattern described here. If a value is
     * defined for START_IP,  a value should also be defined for END_IP.
     *
     * The patterns for IP Version 4 is :
     *    n.n.n.n
     * where n would take any integer value between 0 and 255 inclusive.
     *
     * Some sample values are:
     *     122.100.85.45
     *     145.64.55.35
     *     15.64.55.35
     *
     * The patterns for IP Version 6 is:
     *    x:x:x:x:x:x:x:x
     * where x are the hexadecimal values of the eight 16-bit pieces of the address
     *
     * Some sample values are:
     *      FEDC:BA98:7654:3210:FEDC:BA98:7654:3210
     *      1080:0:0:0:8:800:200C:417A
     *
     * @see <a href="http://tools.ietf.org/html/rfc3513#section-2.2">RFC 3513 - Section 2.2</a>
     * @see #setProperties(Map)
     */
    public static final String START_IP = "StartIp";

    /** Key that is used in  <code>IPCondition</code> to define the  end of 
     *  IP address range for which a policy applies. 
     * The value corresponding to the key has to be a <code>Set</code> that 
     * has just one element which is a <code>String</code>
     * that conforms to the pattern described here. If a value is
     * defined for END_IP,  a value should also be defined for START_IP.
     *
     * The patterns is :
     *    n.n.n.n
     * where n would take any integer value between 0 and 255 inclusive.
     *
     * Some sample values are
     *     122.100.85.45
     *     145.64.55.35
     *     15.64.55.35
     *
     * The patterns for IP Version 6 is:
     *    x:x:x:x:x:x:x:x
     * where x are the hexadecimal values of the eight 16-bit pieces of the address
     *
     * Some sample values are:
     *      FEDC:BA98:7654:3210:FEDC:BA98:7654:3210
     *      1080:0:0:0:8:800:200C:417A
     *
     * @see <a href="http://tools.ietf.org/html/rfc3513#section-2.2">RFC 3513 - Section 2.2</a>
     * @see #setProperties(Map)
     */
    public static final String END_IP = "EndIp";

    /** Key that is used in an <code>IPCondition</code> to define the  DNS 
     * name values for which a policy applies. The value corresponding to the 
     * key has to be a <code>Set</code> where each element is a <code>String
     * </code> that conforms to the patterns described here.
     *
     * The patterns is :
     * <pre>
     * ccc.ccc.ccc.ccc
     * *.ccc.ccc.ccc</pre>
     * where c is any valid character for DNS domain/host name.
     * There could be any number of <code>.ccc</code> components.
     * Some sample values are:
     * <pre>
     * www.sun.com
     * finace.yahoo.com
     * *.yahoo.com
     * </pre>
     *
     * @see #setProperties(Map)
     */
    public static final String DNS_NAME = "DnsName";


    /** Key that is used to define request IP address that is passed in
     * the <code>env</code> parameter while invoking
     * <code>getConditionDecision</code> method of an <code>IPCondition</code>.
     * Value for the key should be a <code>String</code> that is a string 
     * representation of IP of the client,
     *
     * For IP version 4:
     * The form is  n.n.n.n where n is a
     * value between 0 and 255 inclusive.
     *
     * For IP version 6:
     * The form is x:x:x:x:x:x:x:x where x is
     * the hexadecimal values of the eight 16-bit pieces of the address
     *
     * @see #getConditionDecision(SSOToken, Map)
     * @see #REQUEST_DNS_NAME
     */
    public static final String REQUEST_IP = "requestIp";

    /** Key that is used to define request DNS name that is passed in
     * the <code>env</code> parameter while invoking
     * <code>getConditionDecision</code> method of an <code>IPCondition</code>.
     * Value for the key should be a set of strings representing the
     * DNS names of the client, in the form <code>ccc.ccc.ccc</code> for IP version 4.
     * For IP version 6, the form would be <code>x:x:x:x:x:x:x:x</code>
     * If the <code>env</code> parameter is null or does not
     * define value for <code>REQUEST_DNS_NAME</code>,  the 
     * value for <code>REQUEST_DNS_NAME</code> is obtained 
     * from the single sign on token of the user 
     *
     * @see #getConditionDecision(SSOToken, Map)
     */
    public static final String REQUEST_DNS_NAME = "requestDnsName";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of a LDAPFilterCondition.
     */
    /**
     *  Key that is used in a <code>LDAPFilterCondition</code> to define the 
     *  ldap filter that should  be satisfied by the ldap entry of the user 
     *  for the condition to be satisifed
     *  The value should be a <code>Set</code> with only one element. 
     *  The element should be a  <code>String</code>.
     *
     *  @see #setProperties(Map)
     */
    public static final String LDAP_FILTER = "ldapFilter";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of a SessionCondition.
     */

    /**
     * Key that is used in <code>SessionCondition</code> to define the maximum 
     * session time in minutes for which a policy applies. 
     * The value corresponding to the key has to be a  <code>Set</code> that 
     * has just one element which is a string and parse-able as an 
     * <code>Integer</code>. 
     */
    public static final String MAX_SESSION_TIME = "MaxSessionTime";

    /**
     * Key in <code>SessionCondition</code> that is used to define the option 
     * to terminate the session if the session exceeds the maximum session
     * time. The value corresponding to the key has to be a <code>Set</code> 
     * that has just one element which is a string. The option is on if
     * the string value is equal to <code>true</code>.
     */
    public static final String TERMINATE_SESSION = "TerminateSession";

    /**
     * Following keys are used to define relevant key names for processing
     * the environment Map of an SimpleTimeCondition.
     */

    /** Key that is used in <code>SimpleTimeCondition</code> to define the  
     * beginning of time range during which a policy applies.
     * The value corresponding to the key has to be a <code>Set</code> that 
     * has just one element which is a <code>String</code> that conforms to 
     * the pattern described here. If a value is defined for 
     * <code>START_TIME</code>,  
     * a value should also be defined for <code>END_TIME</code>.
     *
     * The patterns is:
     * <pre>
     *    HH:mm
     * </pre>
     *
     * Some sample values are
     * <pre>
     *     08:25
     *     18:45
     * </pre>
     *
     * @see #setProperties(Map)
     * @see #END_TIME
     */
    public static final String START_TIME = "StartTime";

    /** Key that is used in a <code>SimpleTimeCondition</code> to define the  
     * end of time range during which a policy applies.The value corresponding 
     * to the key has to be  a <code>Set</code> that has just one element which 
     * is a <code>String</code> that conforms to the pattern described here. 
     * If a value is defined for <code>END_TIME</code>,  a value should also 
     * be defined for <code>START_TIME</code>.
     *
     * The patterns is:
     * <pre>
     *    HH:mm
     * </pre>
     *
     * Some sample values are
     * <pre>
     *     08:25
     *     18:45
     * </pre>
     *
     * @see #setProperties(Map)
     * @see #START_TIME
     */
    public static final String END_TIME = "EndTime";

    /** Key that is used in a <code>SimpleTimeCondition</code> to define the  
     * start of day of week  range for which a policy applies. The value 
     * corresponding to the key has to be a <code>Set</code> that has just one 
     * element which is a <code>String</code> that is one of the values 
     * <code>Sun, Mon, Tue, Wed, Thu, Fri, Sat.</code>
     * If a value is defined for <code>START_DAY</code>,  a value should also be
     * defined for <code>END_DAY</code>.
     *
     * Some sample values are
     * <pre>
     *     Sun
     *     Mon
     * </pre>
     * @see #setProperties(Map)
     * @see #END_DAY
     */
    public static final String START_DAY = "StartDay";

    /** Key that is used in a <code>SimpleTimeCondition</code> to define the  
     * end of day of week  range for which a policy applies. Its defined in a 
     * <code>SimpleTimeCondition </code> associated with the policy. The value 
     * corresponding to the key has to be a <code>Set</code> that has just one 
     * element which is a <code>String</code> that is one of the values 
     * <code>Sun, Mon, Tue, Wed, Thu, Fri, Sat.</code>
     * If a value is defined for <code>END_DAY</code>,  a value should also be
     * defined for <code>START_DAY</code>.
     *
     * Some sample values are
     * <pre>
     *     Sun
     *     Mon
     * </pre>
     * @see #setProperties(Map)
     * @see #START_DAY
     */
    public static final String END_DAY = "EndDay";

    /** Key that is used in a <code>SimpleTimeCondition</code> to define the  
     * start of date range for which a policy applies.
     * The value corresponding to the key has to be a <code>Set</code> that has 
     * just one element which is a <code>String</code> that corresponds to the 
     * pattern described below. If a value is defined for 
     * <code>START_DATE</code>, a value should also be defined for 
     * <code>END_DATE</code>.
     *
     * The pattern is
     * <pre>
     *     yyyy:MM:dd
     * Some sample values are
     *     2001:02:26
     *     2002:12:31
     * </pre>
     *
     * @see #setProperties(Map)
     * @see #END_DATE
     */
    public static final String START_DATE = "StartDate";

    /** Key that is used in a <code>SimpleTimeCondition</code> to define the  
     * end of date range for which a policy applies.The value corresponding to 
     * the key has to be a <code>Set</code> that has just one element which is 
     * a <code>String</code> that corresponds to the pattern described below.
     * If a value is defined for <code>END_DATE</code>,  a value should
     * also be defined for <code>START_DATE</code>.
     *
     * The pattern is
     * <pre>
     *     yyyy:MM:dd
     * Some sample values are
     *     2001:02:26
     *     2002:12:31
     * </pre>
     *
     * @see #setProperties(Map)
     * @see #START_DATE
     */
    public static final String END_DATE = "EndDate";

    /** Key that is used in a <code>SimpleTimeCondition</code> to define the  
     *  time zone basis to evaluate the policy.
     *  The value corresponding to the key
     *  has to be a one element <code>Set</code> where the element is a 
     *  <code>String</code> that is one of the standard timezone IDs supported 
     *  by java or a <code>String</code> of the  pattern 
     *  <code>GMT[+|-]hh[[:]mm]</code>
     *  here. If the value is not a valid time zone id and does
     *  not match the pattern <code>GMT[+|-]hh[[:]mm]</code>, it would default
     *  to GMT
     *
     *  @see java.util.TimeZone
     */
    public static final String ENFORCEMENT_TIME_ZONE
            = "EnforcementTimeZone";

    /** Key that is used to define the time zone that is passed in
     *  the <code>env</code> parameter while invoking
     *  <code>getConditionDecision</code> method of a 
     *  <code>SimpleTimeCondition</code>
     *  Value for the key should be a <code>TimeZone</code> object. This
     *  would be used only if the <code>ENFORCEMENT_TIME_ZONE</code> is not
     *  defined for the <code>SimpleTimeCondition</code>
     *
     *  @see #getConditionDecision(SSOToken, Map)
     *  @see #ENFORCEMENT_TIME_ZONE
     *  @see java.util.TimeZone
     */
    public static final String REQUEST_TIME_ZONE = "requestTimeZone";

    /** Key that is passed in the <code>env</code> parameter while invoking
     * <code>getConditionDecision</code> method of a <code>
     * SessionPropertyCondition</code> to indicate if a case insensitive 
     * match needs to done of the property value against same name property in 
     * the user's single sign on token.
     */
    public static final String VALUE_CASE_INSENSITIVE = "valueCaseInsensitive";

    /** Key that is passed in the <code>env</code> parameter while invoking
     * <code>getConditionDecision</code> method of an
     * <code>AMIdentityMembershipCondition</code>. The value specifies the 
     * uuid(s)  for which the policy would apply.  The value should be
     *  a <code>Set</code>. Each element of the <code>Set</code> should be a 
     *  String, the uuid of the <code>AMIdentity</code> objet.
     */
    public static final String INVOCATOR_PRINCIPAL_UUID
            = "invocatorPrincipalUuid";

    /** Key that is used in a <code>AMIdentityMembershipCondition</code> to 
     *  specify the  uuid(s) of <code>AMIdentiy</code> objects to which the
     *  policy would apply. These uuid(s) are specified in the condition
     *  at policy definition time.
     *
     *  The value should be a <code>Set</code> 
     *  Each element of the <code>Set</code> should be a  String, 
     *  the uuid of the invocator.
     */
    public static final String AM_IDENTITY_NAME = "amIdentityName";

    /**
     * Returns a list of property names for the condition.
     *
     * @return list of property names
     */
    public List<String> getPropertyNames();

    /**
     * Returns the syntax for a property name
     * @see com.sun.identity.policy.Syntax
     *
     * @param property property name
     *
     * @return <code>Syntax<code> for the property name
     */
    public Syntax getPropertySyntax(String property);


    /**
     * Gets the display name for the property name.
     * The <code>locale</code> variable could be used by the plugin to
     * customize the display name for the given locale.
     * The <code>locale</code> variable could be <code>null</code>, in which
     * case the plugin must use the default locale.
     *
     * @param property property name
     * @param locale locale for which the property name must be customized
     * @return display name for the property name.
     * @throws PolicyException
     */
    public String getDisplayName(String property, Locale locale)
        throws PolicyException;

    /**
     * Returns a set of valid values given the property name. This method
     * is called if the property Syntax is either the SINGLE_CHOICE or
     * MULTIPLE_CHOICE.
     *
     * @param property property name
     * @return Set of valid values for the property.
     * @exception PolicyException if unable to get the Syntax.
     */
    public Set<String> getValidValues(String property) throws PolicyException;

    /** Sets the properties of the condition.  
     *  This influences the <code>ConditionDecision</code> that would be
     *  computed by a call to method <code>getConditionDecision(Map)</code> and
     *  the <code>Advice</code> messages generated included in the
     *  <code>ConditionDecision</code>.
     *
     *  <code>ConditionDecision</code> encapsulates whether a policy applies for
     *  the request and advice messages generated by the condition.
     *
     *  For example, for a <code>SimpleTimeCondition</code>, the properties 
     *  would  define <code>StartTime</code> and <code>EndTime</code>, to define
     *  the time range during which the policy applies 
     *
     *  @param properties the properties of the condition 
     *         that would influence the <code>ConditionDecision</code> returned
     *         by a call to method <code>getConditionDecision(Map)</code>.
     *         Keys of the properties have to be String.
     *         Value corresponding to each key have to be a <code>Set</code> of 
     *         <code>String</code> elements. Each implementation of Condition 
     *         could add further  restrictions on the keys and values of this 
     *         <code>Map</code>.
     *  @throws PolicyException for any abnormal condition
     * @see com.sun.identity.policy.ConditionDecision
     */
    public void setProperties(Map<String, Set<String>> properties) throws PolicyException;

    /** Gets the properties of the condition
     *  @return properties of the condition
     *  @see #setProperties
     */
    public Map<String, Set<String>> getProperties();

    /**
     * Gets the decision computed by this condition object, based on the 
     * <code>Map</code> of environment parameters 
     *
     * @param token single-sign-on <code>SSOToken</code> of the user
     *
     * @param env request specific environment <code>Map,/code> of key/value 
     *        pairs For example this would contain IP address of remote
     *        client for an <code>IPCondition</code>.
     *
     * @return the condition decision.
     *         The condition decision encapsulates whether a <code>Policy</code>
     *         applies for the request and  <code>advice</code> messages
     *         generated by the condition.
     *
     * Policy framework continues evaluating a  <code>Policy</code> only if it 
     * applies to the request  as indicated by the 
     * <code>ConditionDecision</code>.
     * Otherwise, further evaluation  of the <code>Policy</code> is skipped. 
     * However, the <code>Advice</code>  messages encapsulated  in the 
     * <code>ConditionDecision</code> are aggregated and passed up, 
     * encapsulated in  the <code>PolicyDecision</code>.
     *
     *
     * @throws PolicyException if the decision could not be computed
     * @throws SSOException if SSO token is not valid
     *
     * @see com.sun.identity.policy.ConditionDecision
     */
    public ConditionDecision getConditionDecision(SSOToken token, Map<String, Set<String>> env)
            throws PolicyException, SSOException;

    /**
     * Returns a copy of this object.
     *
     * @return a copy of this object
     */
    public Object clone();

}
