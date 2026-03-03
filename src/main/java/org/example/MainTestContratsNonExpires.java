package org.example;

import org.example.entities.*;
import org.example.services.AssuranceService;
import org.example.services.ClientService;
import org.example.services.ContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTestContratsNonExpires {

    private static final String CIN_TEST = "C111111";

    public static void main(String[] args) {
        seedWithServices(); // insertion au début

        ContratService contratService = new ContratService();

        // Date donnée (exemple)
        LocalDate dateRef = LocalDate.now();

        // ===== HQL =====
        List<Contrat> hql = contratService.findActiveAfterHql(dateRef);
        System.out.println("HQL -> Contrats non expirés après " + dateRef + " : " + hql.size());
        print(hql);

        // ===== Criteria =====
        List<Contrat> crit = contratService.findActiveAfterCriteria(dateRef);
        System.out.println("Criteria -> Contrats non expirés après " + dateRef + " : " + crit.size());
        print(crit);
    }

    private static void seedWithServices() {
        ClientService clientService = new ClientService();
        AssuranceService assuranceService = new AssuranceService();
        ContratService contratService = new ContratService();

        // 1) Client
        Client client = clientService.findByCinHql(CIN_TEST);
        if (client == null) {
            client = new Client(CIN_TEST, "Test", "Client");
            clientService.create(client);
        }

        // 2) Assurance
        Assurance aAuto = assuranceService.findByTypeHql(TypeAssurance.AUTO);
        if (aAuto == null) {
            aAuto = new Assurance(TypeAssurance.AUTO, new BigDecimal("1200.00"), "AUTO");
            assuranceService.create(aAuto);
        }

        // 3) Nettoyage contrats (sans delete join)
        List<Contrat> old = contratService.findByClientCinHql(CIN_TEST);
        for (Contrat c : old) {
            contratService.delete(c);
        }

        // 4) Insertion contrats : 1 expiré + 2 actifs
        LocalDate today = LocalDate.now();

        // Expiré (dateFin < dateRef)
        contratService.create(new Contrat(today.minusMonths(4), today.minusDays(1), client, aAuto));

        // Actifs (dateFin > dateRef)
        contratService.create(new Contrat(today.minusMonths(1), today.plusMonths(2), client, aAuto));
        contratService.create(new Contrat(today.minusDays(15), today.plusDays(10), client, aAuto));
    }

    private static void print(List<Contrat> list) {
        for (Contrat ct : list) {
            System.out.println(ct.getId() + " | " + ct.getDateDebut() + " -> " + ct.getDateFin());
        }
    }
}