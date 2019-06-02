/*******************************************************************************
 * Copyright 2018, 2019 Silpion IT-Solutions GmbH
 * Copyright 2018, 2019 iVentureGroup GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/Apache-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.silpion.zimbra.extension.crowd;

import java.util.stream.Collectors;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.account.Domain;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.extension.ExtensionException;
import com.zimbra.cs.extension.ZimbraExtension;

import de.silpion.zimbra.extension.crowd.auth.CrowdAuthHandler;
import de.silpion.zimbra.extension.crowd.auth.CrowdAuthMech;
import de.silpion.zimbra.extension.crowd.pass.CrowdChangePasswordListener;

public class CrowdExtension implements ZimbraExtension {
    // This string is used to refer to this extension
    public static final String ID = "crowd";
    
    public String getName() {
        return ID;
    }
    
    public void init() throws ExtensionException, ServiceException {
        new CrowdAuthHandler().register(ID);
        new CrowdChangePasswordListener().register(ID);
        
        final String s = Provisioning.getInstance().getAllDomains().stream()
            .filter(d -> new CrowdAuthMech(d).isEnabled())
            .map(Domain::getName)
            .collect(Collectors.joining(", "));
        ZimbraLog.extensions.info("Crowd authentication enabled for domains: %s", s.isEmpty() ? "(none)" : s);
    }

    public void destroy() {
        ZimbraLog.extensions.debug("Crowd extension destroyed");
    }
}
