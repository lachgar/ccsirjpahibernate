package org.example.services;


import org.example.entities.Client;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ClientService extends AbstractFacade<Client> {

    public ClientService() {
        super(Client.class);
    }

    public Client findByCinHql(String cin) {
        Session session = null;
        Transaction tx = null;
        Client client = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            client = session.createQuery(
                            "from Client c where c.cin = :cin",
                            Client.class
                    ).setParameter("cin", cin)
                    .uniqueResult();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return client;
    }

}