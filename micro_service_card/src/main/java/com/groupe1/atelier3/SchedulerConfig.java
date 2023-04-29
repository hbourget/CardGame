package com.groupe1.atelier3;

import com.groupe1.atelier3.cards.controllers.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    private CardService cService;
    @Transactional
    @Scheduled(cron = "*/30 * * * * *")
    public void scheduleTask() {
        cService.getAllCards().forEach(card -> {
            //check if card energy is less than 150 add 5
            if (card.getEnergy() < 150) {
                card.setEnergy(card.getEnergy() + 5);
                cService.updateCard(card.getId(), card);
            }
        });
        System.out.println("JOB: Regen cards + 5");
    }
}