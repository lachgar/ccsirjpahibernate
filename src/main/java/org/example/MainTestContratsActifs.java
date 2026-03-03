package org.example;

import org.example.entities.*;
import org.example.services.AssuranceService;
import org.example.services.ClientService;
import org.example.services.ContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTestContratsActifs {

    private static final String CIN_TEST = "C111111";

    public static void main(String[] args) {
        seedWithServices();

        ContratService contratService = new ContratService();
        LocalDate ref = LocalDate.now();

        List<Contrat> l1 = contratService.findActiveAfterHql(ref);
        System.out.println("HQL  -> actifs après " + ref + " = " + l1.size());
        print(l1);

        List<Contrat> l2 = contratService.findActiveAfterCriteria(ref);
        System.out.println("CRIT -> actifs après " + ref + " = " + l2.size());
        print(l2);
    }

    private static void seedWithServices() {
        ClientService clientService = new ClientService();
        AssuranceService assuranceService = new AssuranceService();
        ContratService contratService = new ContratService();

        // Client
        Client client = clientService.findByCinHql(CIN_TEST);
        if (client == null) {
            client = new Client(CIN_TEST, "Test", "Client");
            clientService.create(client);
        }

        // Assurance
        Assurance aSante = assuranceService.findByTypeHql(TypeAssurance.SANTE);
        if (aSante == null) {
            aSante = new Assurance(TypeAssurance.SANTE, new BigDecimal("900.00"), "SANTE");
            assuranceService.create(aSante);
        }

        // Nettoyage contrats
        List<Contrat> old = contratService.findByClientCinHql(CIN_TEST);
        for (Contrat c : old) {
            contratService.delete(c);
        }

        // Insertion: 1 expiré + 1 actif
        LocalDate today = LocalDate.now();
        contratService.create(new Contrat(today.minusMonths(5), today.minusDays(2), client, aSante)); // expiré
        contratService.create(new Contrat(today.minusDays(10), today.plusMonths(6), client, aSante)); // actif
    }

    private static void print(List<Contrat> list) {
        for (Contrat ct : list) {
            System.out.println(ct.getId() + " | " + ct.getDateDebut() + " -> " + ct.getDateFin());
        }
    }
}