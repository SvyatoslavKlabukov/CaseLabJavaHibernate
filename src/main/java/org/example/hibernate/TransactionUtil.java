package org.example.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransactionUtil {

    private Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }


//    public Session openTransactionSession(){
//        session = openSession();
//        transaction = session.beginTransaction();
//        return session;
//    }
//
//    public void closeSession(){
//        session.close();
//    }
//
//    public void closeTransactionSession(){
//        transaction.commit();
//        closeSession();
//    }
}
