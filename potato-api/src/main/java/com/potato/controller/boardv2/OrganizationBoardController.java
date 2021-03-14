package com.potato.controller.boardv2;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.boardv2.OrganizationBoardService;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class OrganizationBoardController {

    private final OrganizationBoardService organizationBoardService;

    @Auth
    @PostMapping("/api/v1/organization/board/{subDomain}")
    public ApiResponse<String> createOrganizationBoard(@PathVariable String subDomain,
                                                       @Valid @RequestBody CreateOrganizationBoardRequest request,
                                                       @MemberId Long memberId) {
        organizationBoardService.createBoard(subDomain, request, memberId);
        return ApiResponse.OK;
    }

}
