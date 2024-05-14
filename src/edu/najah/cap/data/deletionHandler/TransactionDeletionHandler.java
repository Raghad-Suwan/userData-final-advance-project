package edu.najah.cap.data.deletionHandler;

import edu.najah.cap.data.proxy.PaymentProxy;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;

public class TransactionDeletionHandler implements ItemDeletionHandler<Transaction> {
    private final IPayment paymentProxy ;

    public TransactionDeletionHandler(IPayment paymentService) {
        this.paymentProxy = new PaymentProxy(paymentService);
    }
    @Override
    public void handleDeletion(Transaction transaction) throws SystemBusyException, BadRequestException, NotFoundException {
        paymentProxy.removeTransaction(transaction.getUserName(), transaction.getId());
    }
}