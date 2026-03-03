package org.example;

import org.example.entities.*;
import org.example.services.AssuranceService;
import org.example.services.ClientService;
import org.example.services.ContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTestContratsByCin {

    private static final String CIN_TEST = "C111111";

    public static void main(String[] args) {
        seedWithServices();

        ContratService contratService = new ContratService();

        List<Contrat> l1 = contratService.findByClientCinHql(CIN_TEST);
        System.out.println("HQL  -> contrats = " + l1.size());
        print(l1);

        List<Contrat> l2 = contratService.findByClientCinCriteria(CIN_TEST);
        System.out.println("CRIT -> contrats = " + l2.size());
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

        Assurance aSante = assuranceService.findByTypeHql(TypeAssurance.SANTE);
        if (aSante == null) {
            aSante = new Assurance(TypeAssurance.SANTE, new BigDecimal("900.00"), "SANTE");
            assuranceService.create(aSante);
        }

        // Nettoyage contrats (sans delete join)
        List<Contrat> old = contratService.findByClientCinHql(CIN_TEST);
        for (Contrat c : old) {
            contratService.delete(c);
        }

        // Insertion contrats
        LocalDate today = LocalDate.now();
        contratService.create(new Contrat(today.minusMonths(6), today.minusDays(3), client, aAuto));     // expiré
        contratService.create(new Contrat(today.minusMonths(2), today.plusMonths(4), client, aAuto));   // actif
        contratService.create(new Contrat(today.minusDays(20), today.plusMonths(2), client, aSante));   // actif
    }

    private static void print(List<Contrat> list) {
        for (Contrat ct : list) {
            // Si tes relations sont LAZY, éviter d'afficher client/assurance ici.
            // Sinon, tu peux afficher:
            System.out.println(ct.getId() + " | " + ct.getDateDebut() + " -> " + ct.getDateFin());
        }
    }
}