package com;

public class LottoGameApplication {
    public static void main(String[] args) {
        System.out.println("Cześć Junior Java Ready!");
        LottoNumberGenerator lottoNumberGenerator = new LottoNumberGenerator();
        double random = lottoNumberGenerator.generateRandomNumber();
        System.out.println(random);
    }
}