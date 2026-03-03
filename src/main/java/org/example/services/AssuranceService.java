package org.example.services;

import org.example.entities.Assurance;
import org.example.entities.TypeAssurance;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AssuranceService extends AbstractFacade<Assurance> {

    public AssuranceService() {
        super(Assurance.class);
    }

    public Assurance findByTypeHql(TypeAssurance type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Assurance a where a.type = :type",
                            Assurance.class
                    ).setParameter("type", type)
                    .uniqueResult();
        }
    }

    public Assurance findByTypeCriteria(TypeAssurance type) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Assurance> cq = cb.createQuery(Assurance.class);
            Root<Assurance> a = cq.from(Assurance.class);

            cq.select(a).where(cb.equal(a.get("type"), type));

            return session.createQuery(cq).uniqueResult();
        }
    }
}