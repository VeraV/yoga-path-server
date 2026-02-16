package com.yogapath.config;

import com.yogapath.model.YogaStyle;
import com.yogapath.repository.YogaStyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds initial data into the database on application startup.
 * Only inserts data if the tables are empty (won't duplicate on restart).
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final YogaStyleRepository yogaStyleRepository;

    public DataLoader(YogaStyleRepository yogaStyleRepository) {
        this.yogaStyleRepository = yogaStyleRepository;
    }

    @Override
    public void run(String... args) {
        if (yogaStyleRepository.count() == 0) {
            seedYogaStyles();
        }
    }

    private void seedYogaStyles() {
        // Ashtanga: STRUCTURED, DYNAMIC
        YogaStyle ashtanga = new YogaStyle();
        ashtanga.setName("Ashtanga");
        ashtanga.setDescription("Traditional, structured, and more dynamic style of yoga");
        ashtanga.setNotes("Can be physically demanding");
        ashtanga.setStructured(true);
        ashtanga.setCreative(false);
        ashtanga.setDynamic(true);
        ashtanga.setStatik(false);
        ashtanga.setRequiresPhilosophyOpenness(false);
        yogaStyleRepository.save(ashtanga);

        // Sivananda: STRUCTURED, DYNAMIC & STATIC
        YogaStyle sivananda = new YogaStyle();
        sivananda.setName("Sivananda");
        sivananda.setDescription("Classical approach based on a fixed sequence, including vinyasa, 12 basic asanas, pranayama, and relaxation");
        ashtanga.setNotes("Traditionally includes kirtan, meditation, and study of yoga philosophy with regular chanting and meditation");
        sivananda.setStructured(true);
        sivananda.setCreative(false);
        sivananda.setDynamic(true);
        sivananda.setStatik(true);
        sivananda.setRequiresPhilosophyOpenness(false);
        yogaStyleRepository.save(sivananda);

        // Kundalini: STRUCTURED, DYNAMIC, requires philosophy openness
        YogaStyle kundalini = new YogaStyle();
        kundalini.setName("Kundalini");
        kundalini.setDescription("Combines movement, breathing, meditation, and chanting to awaken energy.");
        kundalini.setStructured(true);
        kundalini.setCreative(false);
        kundalini.setDynamic(true);
        kundalini.setStatik(false);
        kundalini.setRequiresPhilosophyOpenness(true);
        yogaStyleRepository.save(kundalini);

        // Iyengar: STRUCTURED, STATIC
        YogaStyle iyengar = new YogaStyle();
        iyengar.setName("Iyengar");
        iyengar.setDescription("Precision-focused practice emphasizing alignment in static asanas");
        iyengar.setNotes("Regular use of props such as blocks, straps, and bolsters is essential");
        iyengar.setStructured(true);
        iyengar.setCreative(false);
        iyengar.setDynamic(false);
        iyengar.setStatik(true);
        iyengar.setRequiresPhilosophyOpenness(false);
        yogaStyleRepository.save(iyengar);

        // Vinyasa: CREATIVE, DYNAMIC
        YogaStyle vinyasa = new YogaStyle();
        vinyasa.setName("Vinyasa");
        vinyasa.setDescription("Dynamic flowing practice that links movement with breath");
        vinyasa.setStructured(false);
        vinyasa.setCreative(true);
        vinyasa.setDynamic(true);
        vinyasa.setStatik(false);
        vinyasa.setRequiresPhilosophyOpenness(false);
        yogaStyleRepository.save(vinyasa);

        // Hatha: CREATIVE, STATIC
        YogaStyle hatha = new YogaStyle();
        hatha.setName("Any Hatha");
        hatha.setDescription("Creative sequences of static postures combined with breathing and relaxation");
        hatha.setStructured(false);
        hatha.setCreative(true);
        hatha.setDynamic(false);
        hatha.setStatik(true);
        hatha.setRequiresPhilosophyOpenness(false);
        yogaStyleRepository.save(hatha);

        // Yin: STRUCTURED & CREATIVE, STATIC
        YogaStyle yin = new YogaStyle();
        yin.setName("Yin");
        yin.setDescription("Slow-paced practice with postures held for longer periods, promoting deep relaxation");
        yin.setNotes("Often requires props such as blocks and bolsters; recommended as a complement to more active forms of exercise");
        yin.setStructured(true);
        yin.setCreative(true);
        yin.setDynamic(false);
        yin.setStatik(true);
        yin.setRequiresPhilosophyOpenness(false);
        yogaStyleRepository.save(yin);
    }
}
