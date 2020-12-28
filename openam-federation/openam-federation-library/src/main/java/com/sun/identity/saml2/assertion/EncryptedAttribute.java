/*
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
 * $Id: EncryptedAttribute.java,v 1.2 2008/06/25 05:47:41 qcheng Exp $
 *
 * Portions Copyrighted 2015 ForgeRock AS.
 */
package com.sun.identity.saml2.assertion;

import java.security.PrivateKey;
import java.util.Set;

import com.sun.identity.saml2.common.SAML2Exception;

/**
 * The <code>EncryptedAttribute</code> element represents a SAML attribute
 * in encrypted fashion. It's of type <code>EncryptedElementType</code>.
 * <p>
 * <pre>
 * &lt;element name="EncryptedAttribute" 
 * type="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedElementType"/>
 * </pre>
 * 
 */
public interface EncryptedAttribute extends EncryptedElement {

    /**
     * Returns an instance of <code>Attribute</code> object.
     *
     * @param privateKeys Private keys of the recipient used to decrypt the secret key.
     * @return <code>Attribute</code> object.
     * @throws SAML2Exception if error occurs.
     */
    public Attribute decrypt(Set<PrivateKey> privateKeys) throws SAML2Exception;
}
