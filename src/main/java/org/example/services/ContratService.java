package org.example.services;

import org.example.entities.Assurance;
import org.example.entities.Client;
import org.example.entities.Contrat;
import org.example.entities.TypeAssurance;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class ContratService extends AbstractFacade<Contrat> {

    public ContratService() {
        super(Contrat.class);
    }

    public List<Contrat> findByClientCinHql(String cin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select ct from Contrat ct where ct.client.cin = :cin order by ct.dateDebut desc",
                            Contrat.class
                    ).setParameter("cin", cin)
                    .getResultList();
        }
    }

    public List<Contrat> findByClientCinCriteria(String cin) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Contrat> cq = cb.createQuery(Contrat.class);
            Root<Contrat> ct = cq.from(Contrat.class);

            Join<Contrat, Client> cl = ct.join("client"); // ct.client
            cq.select(ct)
                    .where(cb.equal(cl.get("cin"), cin))
                    .orderBy(cb.desc(ct.get("dateDebut")));

            return session.createQuery(cq).getResultList();
        }
    }

    public List<Contrat> findByAssuranceTypeHql(TypeAssurance type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select ct from Contrat ct where ct.assurance.type = :type order by ct.dateDebut desc",
                            Contrat.class
                    ).setParameter("type", type)
                    .getResultList();
        }
    }

    public List<Contrat> findByAssuranceTypeCriteria(TypeAssurance type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Contrat> cq = cb.createQuery(Contrat.class);
            Root<Contrat> ct = cq.from(Contrat.class);

            Join<Contrat, Assurance> as = ct.join("assurance"); // ct.assurance
            cq.select(ct)
                    .where(cb.equal(as.get("type"), type))
                    .orderBy(cb.desc(ct.get("dateDebut")));

            return session.createQuery(cq).getResultList();
        }
    }

    public List<Contrat> findActiveAfterHql(LocalDate dateRef) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select ct from Contrat ct " +
                                    "where ct.dateFin is not null and ct.dateFin > :d " +
                                    "order by ct.dateFin asc",
                            Contrat.class
                    ).setParameter("d", dateRef)
                    .getResultList();
        }
    }

    public List<Contrat> findActiveAfterCriteria(LocalDate dateRef) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Contrat> cq = cb.createQuery(Contrat.class);
            Root<Contrat> ct = cq.from(Contrat.class);

            Predicate notNull = cb.isNotNull(ct.get("dateFin"));
            Predicate gtDate = cb.greaterThan(ct.get("dateFin"), dateRef);

            cq.select(ct)
                    .where(cb.and(notNull, gtDate))
                    .orderBy(cb.asc(ct.get("dateFin")));

            return session.createQuery(cq).getResultList();
        }
    }
}