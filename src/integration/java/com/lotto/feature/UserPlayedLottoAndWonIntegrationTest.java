package com.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.lotto.domain.numbergenerator.WinningNumberGeneratorFacade;
import com.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import com.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import org.junit.jupiter.api.Test;
import com.lotto.BaseIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    WinningNumberGeneratorFacade winningNumberGeneratorFacade;

    @Test
    public void should_user_win_and_system_should_generate_winners() throws Exception {
//    step 1: external services return 6 random numbers (1, 2, 3, 4, 5, 6)
        //given
        wireMockServer.stubFor(WireMock.get("/v1/integer?min=1&max=99&count=15")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "items": [1, 2, 3, 4, 5, 6, 7, 8, 84, 56, 86, 83, 31, 53, 93]
                                }""".trim()
                        )));

//    step 2: system fetched winning numbers for draw date: 19.11.2022 12:00
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);

        //when && then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {

                            try {
                                return !winningNumberGeneratorFacade.retrieveWinningNumberByDate(drawDate).getWinningNumbers().isEmpty();
                            } catch (WinningNumbersNotFoundException e) {
                                return false;
                            }
                        }

                );
//    step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
        //given
        //when
        ResultActions performPostInputNumbers = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1,2,20,35,50,68]
                        }
                        """.trim()
                ).contentType(MediaType.APPLICATION_JSON)
        );
        //then
        MvcResult mvcResult = performPostInputNumbers.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);
        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("SUCCESS"),
                () -> assertThat(numberReceiverResponseDto.ticketDto().hash()).isNotNull()
                );


//    step 4 : user made GET /results/notExisting and system returned 404(NOT_FOUND) and body with (message: Not found for id: notExistingId and status NOT_FOUND)
        //given
        //when
        ResultActions performResultWithNotExistingId = mockMvc.perform(get("/results/notExistingId"));
        //then
        performResultWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                "message" : "Not found for id: notExistingId",
                                "status" : "NOT_FOUND"
                                }
                                """.trim()
                ));


//    step 5: 3 days and 1 minute passed, and it is 1 minute after the draw date (19.11.2022 12:01)
//    step 6: system generated result for TicketId: sampleTicketId with draw date 19.11.2022 12:00, and saved it with 6 hits
//    step 7: 3 hours passed, and it is 1 minute after announcement time (19.11.2022 15:01)
//    step 8: user made GET /results/sampleTicketId and system returned 200 (OK)
    }
}
