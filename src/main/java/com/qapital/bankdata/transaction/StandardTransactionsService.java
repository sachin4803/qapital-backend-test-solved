package com.qapital.bankdata.transaction;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StandardTransactionsService implements TransactionsService {

    @Override
    public List<Transaction> latestTransactionsForUser(Long userId) {
        return createDummyTransactions(userId);
    }

    private static List<Transaction> createDummyTransactions(Long userId) {

        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(1L,userId, BigDecimal.valueOf(-5.34d), "Starbucks", LocalDate.of(2015,7,1)));
        transactions.add(new Transaction(2L,userId, BigDecimal.valueOf(-2.16d), "Starbucks", LocalDate.of(2015,7,2)));
        transactions.add(new Transaction(3L,userId, BigDecimal.valueOf(-3.09d), "McDonald's", LocalDate.of(2015,7,2)));
        transactions.add(new Transaction(4L,userId, BigDecimal.valueOf(-1.03d), "Starbucks", LocalDate.of(2015,7,3)));
        transactions.add(new Transaction(5L,userId, BigDecimal.valueOf(-2.99d), "Apple Itunes", LocalDate.of(2015,7,7)));
        transactions.add(new Transaction(6L,userId, BigDecimal.valueOf(1945.00d), "Salary", LocalDate.of(2015,7,25)));
        transactions.add(new Transaction(7L,userId, BigDecimal.valueOf(-9.76d), "Amazon", LocalDate.of(2015,7,8)));
        transactions.add(new Transaction(8L,userId, BigDecimal.valueOf(-59.45d), "Walmart", LocalDate.of(2015,7,8)));
        transactions.add(new Transaction(9L,userId, BigDecimal.valueOf(-13.14d), "Papa Joe's", LocalDate.of(2015,7,13)));
        transactions.add(new Transaction(10L,userId, BigDecimal.valueOf(-2.16d), "Starbucks", LocalDate.of(2015,7,29)));
        transactions.add(new Transaction(11L, userId, BigDecimal.valueOf(-1.99d), "Apple Itunes", LocalDate.of(2015, 8, 3)));

        return transactions;
    }


}
