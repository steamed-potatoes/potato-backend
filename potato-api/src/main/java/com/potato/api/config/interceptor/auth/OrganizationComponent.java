package com.potato.api.config.interceptor.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.api.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OrganizationComponent {

    private static final String ORGANIZATION_SUBDOMAIN = "subDomain";

    private final ObjectMapper objectMapper;
    private final OrganizationRepository organizationRepository;

    public void validateOrganizationAdmin(HttpServletRequest request, Long memberId) {
        Organization organization = getOrganization(request);
        organization.validateAdminMember(memberId);
    }

    public void validateOrganizationMember(HttpServletRequest request, Long memberId) {
        Organization organization = getOrganization(request);
        organization.validateIsMemberInOrganization(memberId);
    }

    private Organization getOrganization(HttpServletRequest request) {
        final Map<String, String> pathVariables = objectMapper.convertValue(
            request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE), new TypeReference<>() {
            });
        String subDomain = pathVariables.get(ORGANIZATION_SUBDOMAIN);
        return OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
    }

}
