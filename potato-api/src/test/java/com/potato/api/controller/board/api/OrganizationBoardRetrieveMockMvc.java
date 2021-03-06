package com.potato.api.controller.board.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.potato.api.controller.AbstractMockMvc;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.board.organization.dto.request.RetrieveImminentBoardsRequest;
import com.potato.api.service.board.organization.dto.request.RetrieveLatestBoardsRequest;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponseWithImage;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import com.potato.domain.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrganizationBoardRetrieveMockMvc extends AbstractMockMvc {

    public OrganizationBoardRetrieveMockMvc(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    public ApiResponse<OrganizationBoardWithCreatorInfoResponse> retrieveOrganizationBoard(Long organizationBoardId, String token, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/organization/board")
            .param("organizationBoardId", String.valueOf(organizationBoardId))
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(token));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<BoardWithOrganizationDto>> retrieveLatestOrganizationBoardList(long lastOrganizationBoardId, long size, int expectedStatus) throws Exception {
        MultiValueMap<String, String> retrieveLatestBoardsRequest = new LinkedMultiValueMap<>();
        retrieveLatestBoardsRequest.add("lastOrganizationBoardId", String.valueOf(lastOrganizationBoardId));
        retrieveLatestBoardsRequest.add("size", String.valueOf(size));
        MockHttpServletRequestBuilder builder = get("/api/v2/organization/board/list")
            .params(retrieveLatestBoardsRequest)
            .contentType(MediaType.APPLICATION_JSON);

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<OrganizationBoardInfoResponse>> retrievePopularBoard(int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/organization/board/popular/list")
            .param("size", String.valueOf(5))
            .contentType(MediaType.APPLICATION_JSON);

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<BoardWithOrganizationDto>> getBoardsInOrganization(String subDomain, RetrieveLatestBoardsRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/organization/board/list/in/".concat(subDomain))
            .param("lastOrganizationBoardId", String.valueOf(request.getLastOrganizationBoardId()))
            .param("size", String.valueOf(request.getSize()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

    public ApiResponse<List<OrganizationBoardInfoResponseWithImage>> retrieveImminentBoards(RetrieveImminentBoardsRequest request, int expectedStatus) throws Exception {
        MockHttpServletRequestBuilder builder = get("/api/v2/organization/board/list/imminentBoards")
            .param("dateTime", request.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
            .param("size", String.valueOf(request.getSize()));

        return objectMapper.readValue(
            mockMvc.perform(builder)
                .andExpect(status().is(expectedStatus))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
            }
        );
    }

}
