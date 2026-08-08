package org.enigma.livecodeloan.constant;

public class AppPath {
    public static final String API = "/api";
    public static final String AUTH = API + "/auth";
    public static final String CUSTOMERS = API + "/customers";
    public static final String INSTALMENT_TYPES = API + "/instalment-types";
    public static final String LOAN_TRANSACTIONS = API + "/transactions";
    public static final String LOAN_TYPES = API + "/loan-types";
    public static final String USERS = API + "/users";
    public static final String GET_BY_ID = "/{id}";
    public static final String DELETE_BY_ID = "/{id}";
    public static final String APPROVE_LOAN = "/{adminId}/approve";
    public static final String REJECT_LOAN = "/reject";
    public static final String PAY_LOAN = "/{trxId}/pay";
}
