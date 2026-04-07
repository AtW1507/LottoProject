package com.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class NumberReceiverFacadeTest {

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
         String result = numberReceiverFacade.inputNumber(Set.of(1, 2, 3, 4, 5, 6));
        //then
        assertThat(result).isEqualTo("success");
    }
    @Test
    public void should_return_failed_when_user_gave_at_least_one_number_out_of_range_of_1_to_99() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumber(Set.of(1, 2300, 3, 4, 5, 6));
        //then
        assertThat(result).isEqualTo("failed");
    }
    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumber(Set.of(1, 2, 3, 4, 5));
        //then
        assertThat(result).isEqualTo("failed");
    }
    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        //given
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();
        //when
        String result = numberReceiverFacade.inputNumber(Set.of(1, 2, 3, 4, 5, 6, 7));
        //then
        assertThat(result).isEqualTo("failed");
    }

}