package com.lotto.domain.resultannouncer;

class ResultResponseNotFoundException extends RuntimeException{

    ResultResponseNotFoundException(String message){
        super(message);
    }
}
