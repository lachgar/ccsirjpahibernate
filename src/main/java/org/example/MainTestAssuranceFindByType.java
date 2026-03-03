package org.example;

import org.example.entities.*;
import org.example.services.AssuranceService;
import org.example.services.ClientService;
import org.example.services.ContratService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MainTestAssuranceFindByType {

    private static final String CIN_TEST = "C111111";

    public static void main(String[] args) {
        seedWithServices(); // insertion au début

        AssuranceService service = new AssuranceService();

        Assurance a1 = service.findByTypeHql(TypeAssurance.AUTO);
        System.out.println("HQL  => " + (a1 == null ? "null" : (a1.getType() + " | " + a1.getMontant())));

        Assurance a2 = service.findByTypeCriteria(TypeAssurance.AUTO);
        System.out.println("CRIT => " + (a2 == null ? "null" : (a2.getType() + " | " + a2.getMontant())));
    }

    private static void seedWithServices() {

        ClientService clientService = new ClientService();
        AssuranceService assuranceService = new AssuranceService();
        ContratService contratService = new ContratService();

        // 1) Client getOrCreate (utilise ta méthode existante)
        Client client = clientService.findByCinHql(CIN_TEST); // adapte le nom si besoin
        if (client == null) {
            client = new Client(CIN_TEST, "Test", "Client");
            clientService.create(client);
        }

        // 2) Assurance AUTO getOrCreate
        Assurance aAuto = assuranceService.findByTypeHql(TypeAssurance.AUTO);
        if (aAuto == null) {
            aAuto = new Assurance(TypeAssurance.AUTO, new BigDecimal("1200.00"), "Couverture AUTO standard");
            assuranceService.create(aAuto);
        }

        // 3) Nettoyage contrats du client (sans requête DELETE avec JOIN)
        List<Contrat> old = contratService.findByClientCinHql(CIN_TEST); // adapte le nom si besoin
        for (Contrat c : old) {
            contratService.delete(c);
        }

        // 4) Insertion contrat
        LocalDate today = LocalDate.now();
        Contrat ct = new Contrat(today.minusDays(10), today.plusMonths(2), client, aAuto);
        contratService.create(ct);
    }
}