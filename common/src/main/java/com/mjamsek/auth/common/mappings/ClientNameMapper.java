package com.mjamsek.auth.common.mappings;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.mjamsek.auth.common.config.ConfigKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Miha Jamsek
 * @since 2.0.0
 */
public class ClientNameMapper {
    
    public static Map<String, String> getClientRoleMappings() {
        ConfigurationUtil configUtil = ConfigurationUtil.getInstance();
        List<String> clientNames = configUtil.getMapKeys(ConfigKeys.CLIENTS).orElse(new ArrayList<>());
        return clientNames.stream()
            .collect(
                Collectors.toMap(
                    clientName -> clientName,
                    clientName -> configUtil.get(ConfigKeys.CLIENTS + "." + clientName).orElse(clientName)
                )
            );
    }
    
}
