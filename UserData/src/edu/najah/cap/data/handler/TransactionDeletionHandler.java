package edu.najah.cap.data.handler;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;

public class TransactionDeletionHandler implements ItemDeletionHandler<Transaction> {
    private final IPayment paymentService;
    public TransactionDeletionHandler(IPayment paymentService) {
        this.paymentService = paymentService;
    }
    @Override
    public void handleDeletion(Transaction transaction) throws SystemBusyException, BadRequestException, NotFoundException {
        paymentService.removeTransaction(transaction.getUserName(), transaction.getId());
    }
}
