package com.potato.config.interceptor.auth;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.OrganizationServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class OrganizationComponent {

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
        Object path = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final Map<String, String> pathVariables = (Map<String, String>) path;
        String subDomain = pathVariables.get("subDomain");
        return OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
    }

}
