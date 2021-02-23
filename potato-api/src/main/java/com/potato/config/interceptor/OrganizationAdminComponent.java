package com.potato.config.interceptor;

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
public class OrganizationAdminComponent {

    private final OrganizationRepository organizationRepository;

    public void validateOrganizationAdmin(HttpServletRequest request, Long memberId) {
        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String subDomain = pathVariables.get("subDomain");

        // 1차 캐시에 넣어두면 좋을거 같은데 영속성 컨텍스트 범위에서 벗어나서 안되는 상황.. (현재 트랜잭션 범위의 영속성 컨텍스트 전략이라) - 이참에 OSIV 써볼까?
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.validateAdminMember(memberId);
    }

}
