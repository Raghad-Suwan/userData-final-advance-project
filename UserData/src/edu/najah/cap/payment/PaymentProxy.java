package edu.najah.cap.payment;

import edu.najah.cap.data.ValidateUser;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.exceptions.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PaymentProxy implements IPayment {
    private  IPayment paymentService;

    private static final Map<String, List<Transaction>> transactionMap = PaymentService.getTransactionsMap();

   public PaymentProxy(IPayment paymentService) {
        this.paymentService = paymentService;
    }
    @Override
    public void pay(Transaction transaction) {

    paymentService.pay(transaction);
    }

    @Override
    public double getBalance(String userName) {
        paymentService.getBalance(userName);
        return 0;
    }

    @Override
    public void removeTransaction(String userName, String id) throws BadRequestException, NotFoundException {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ValidateUser.validateUser(userName);
        if (!transactionMap.containsKey(userName)) {
            throw new NotFoundException("User does not exist");
        }
        Iterator<Transaction> iterator = transactionMap.get(userName).iterator();
        while (iterator.hasNext()) {
            Transaction transaction = iterator.next();
            if (transaction.getId().equals(id)) {
                iterator.remove();
            }
        }
        transactionMap.put(userName, transactionMap.get(userName));
    }

    @Override
    public List<Transaction> getTransactions(String userName) throws BadRequestException, NotFoundException {
      ValidateUser.validateUser(userName);
        if (!transactionMap.containsKey(userName)) {
            throw new NotFoundException("User does not exist");
        }
        return transactionMap.get(userName);
    }

}
