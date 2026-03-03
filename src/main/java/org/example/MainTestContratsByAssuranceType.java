package org.example;

import org.example.entities.*;
import org.example.services.AssuranceService;
import org.example.services.ClientService;
import org.example.services.ContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTestContratsByAssuranceType {

    private static final String CIN_TEST = "C111111";

    public static void main(String[] args) {
        seedWithServices();

        ContratService contratService = new ContratService();

        List<Contrat> l1 = contratService.findByAssuranceTypeHql(TypeAssurance.AUTO);
        System.out.println("HQL  -> AUTO contrats = " + l1.size());
        print(l1);

        List<Contrat> l2 = contratService.findByAssuranceTypeCriteria(TypeAssurance.AUTO);
        System.out.println("CRIT -> AUTO contrats = " + l2.size());
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

        // Assurances
        Assurance aAuto = assuranceService.findByTypeHql(TypeAssurance.AUTO);
        if (aAuto == null) {
            aAuto = new Assurance(TypeAssurance.AUTO, new BigDecimal("1200.00"), "AUTO");
            assuranceService.create(aAuto);
        }

        Assurance aHab = assuranceService.findByTypeHql(TypeAssurance.HABITATION);
        if (aHab == null) {
            aHab = new Assurance(TypeAssurance.HABITATION, new BigDecimal("700.00"), "HABITATION");
            assuranceService.create(aHab);
        }

        // Nettoyage contrats
        List<Contrat> old = contratService.findByClientCinHql(CIN_TEST);
        for (Contrat c : old) {
            contratService.delete(c);
        }

        // Insertion
        LocalDate today = LocalDate.now();
        contratService.create(new Contrat(today.minusMonths(1), today.plusMonths(5), client, aAuto));
        contratService.create(new Contrat(today.minusDays(12), today.plusMonths(2), client, aAuto));
        contratService.create(new Contrat(today.minusDays(7), today.plusMonths(1), client, aHab));
    }

    private static void print(List<Contrat> list) {
        for (Contrat ct : list) {
            System.out.println(ct.getId() + " | " + ct.getDateDebut() + " -> " + ct.getDateFin());
        }
    }
}