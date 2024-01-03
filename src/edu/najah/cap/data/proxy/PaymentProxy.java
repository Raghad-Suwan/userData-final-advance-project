package edu.najah.cap.data.proxy;

import edu.najah.cap.data.export.ValidateUser;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.PaymentService;
import edu.najah.cap.payment.Transaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PaymentProxy implements IPayment {
    private static final Logger logger = Logger.getLogger(PaymentProxy.class.getName());
    private IPayment paymentService;
    private static final Map<String, List<Transaction>> transactionMap = PaymentService.getTransactionsMap();

    public PaymentProxy(IPayment paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void pay(Transaction transaction) {
        try {
            paymentService.pay(transaction);
            logger.info("Payment processed for transaction: " + transaction.getId());
        } catch (Exception e) {
            logger.severe("Error processing payment for transaction: " + transaction.getId() + "; " + e.getMessage());
            throw e;
        }
    }

    @Override
    public double getBalance(String userName) {
        try {
            double balance = paymentService.getBalance(userName);
            logger.info("Balance retrieved for user: " + userName);
            return balance;
        } catch (Exception e) {
            logger.severe("Error retrieving balance for user: " + userName + "; " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeTransaction(String userName, String id) throws BadRequestException, NotFoundException {
        try {
            Thread.sleep(100); // Simulating delay
            ValidateUser.validateUser(userName);
            if (!transactionMap.containsKey(userName)) {
                logger.warning("User not found: " + userName);
                throw new NotFoundException("User does not exist");
            }

            Iterator<Transaction> iterator = transactionMap.get(userName).iterator();
            while (iterator.hasNext()) {
                Transaction transaction = iterator.next();
                if (transaction.getId().equals(id)) {
                    iterator.remove();
                    logger.info("Transaction removed: " + id + " for user: " + userName);
                }
            }
            transactionMap.put(userName, transactionMap.get(userName));
        } catch (InterruptedException e) {
            logger.severe("Interrupted during transaction removal: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getTransactions(String userName) throws BadRequestException, NotFoundException {
        ValidateUser.validateUser(userName);
        if (!transactionMap.containsKey(userName)) {
            logger.warning("User not found: " + userName);
            throw new NotFoundException("User does not exist");
        }
        logger.info("Transactions retrieved for user: " + userName);
        return transactionMap.get(userName);
    }
}
