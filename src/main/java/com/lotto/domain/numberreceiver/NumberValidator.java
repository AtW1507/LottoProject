package com.lotto.domain.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {


    private static final int QUANTITY_OF_NUMBERS_FROM_USER = 6;
    private static final int MINIMAL_NUMBER_FROM_USER = 1;
    private static final int MAXIMAL_NUMBER_FROM_USER = 99;

    List<ValidationResult> errors;

    List<ValidationResult> validate(Set<Integer> numbersFromUser){
        errors = new LinkedList<>();
        if(!isNumbersSizeEqualsSix(numbersFromUser)){
            errors.add(ValidationResult.NOT_SIX_NUMBERS_GIVEN);
        }
        if(!areAllNumbersInRange(numbersFromUser)){
            errors.add(ValidationResult.NOT_IN_RANGE);
        }
        return errors;
    }
    String createResultMessage(){
        return this.errors
                .stream()
                .map(validationResult -> validationResult.info)
                .collect(Collectors.joining(", "));
    }

    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream().
                allMatch(number -> number >= MINIMAL_NUMBER_FROM_USER && number <= MAXIMAL_NUMBER_FROM_USER);

    }
    boolean isNumbersSizeEqualsSix(Set<Integer> numbersFromUser){
        return numbersFromUser.size() == QUANTITY_OF_NUMBERS_FROM_USER;
    }
}
