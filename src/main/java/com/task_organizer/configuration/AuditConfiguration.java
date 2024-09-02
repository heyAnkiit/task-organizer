package com.task_organizer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_organizer.utility.ApplicationConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.Optional;

@Component
public class AuditConfiguration implements AuditorAware<String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<String> getCurrentAuditor() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String authorization = request.getHeader("Authorization");
            if (authorization== null){
                return Optional.of(ApplicationConstants.LoggedInUser);
            }
            String token = authorization.split(StringUtils.SPACE)[1];
            String jsonPayload = decodeJWTToken(token);
            JSONObject jsonObject = objectMapper.convertValue(jsonPayload, JSONObject.class);

            if (jsonObject.has("sub")) {
                final String name = jsonObject.getString("sub");
                ApplicationConstants.LoggedInUser = name;
                return Optional.of(name);
            }
        }
        return Optional.of(ApplicationConstants.LoggedInUser);
    }

    public static String decodeJWTToken(String token) {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] chunks = token.split("\\.");
        return new String(decoder.decode(chunks[1]));
    }

    public String getLoggedInUser(){
        Optional<String> auditor = getCurrentAuditor();
        String loggedInUser = null;
        if (auditor.isPresent()) {
            loggedInUser = auditor.get();
        }
        return loggedInUser;
    }
}