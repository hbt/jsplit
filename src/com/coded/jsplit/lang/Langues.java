package com.coded.jsplit.lang;

public interface Langues {
    /** LANGUE FRANÇAISE */
    static String FRANCAIS = "FR";

    /** ENGLISH LANGUAGE */
    static String ENGLISH = "EN";

    /**
     * configure les composants avec la langue choisi
     * @param langue String
     */
    void setLangue(String langue);


}
