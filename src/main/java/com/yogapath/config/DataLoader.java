package com.yogapath.config;

import com.yogapath.model.Goal;
import com.yogapath.model.Limitation;
import com.yogapath.model.YogaStyle;
import com.yogapath.repository.GoalRepository;
import com.yogapath.repository.LimitationRepository;
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
    private final LimitationRepository limitationRepository;
    private final GoalRepository goalRepository;

    public DataLoader(YogaStyleRepository yogaStyleRepository, LimitationRepository limitationRepository, GoalRepository goalRepository) {
        this.yogaStyleRepository = yogaStyleRepository;
        this.limitationRepository = limitationRepository;
        this.goalRepository = goalRepository;
    }

    @Override
    public void run(String... args) {
        if (yogaStyleRepository.count() == 0) {
            seedYogaStyles();
        }
        if (limitationRepository.count() == 0) {
            seedLimitations();
        }
        if (goalRepository.count() == 0) {
            seedGoals();
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

    private void seedLimitations() {
        Limitation asthma = new Limitation();
        asthma.setName("Asthma");
        asthma.setDescription("Intense breath retention or very dynamic practice can trigger symptoms");
        asthma.setNotes("Practice gentle sequences, avoid long breath holds, keep inhalations comfortable, and keep medication nearby");
        limitationRepository.save(asthma);

        Limitation jointConditions = new Limitation();
        jointConditions.setName("Joint Conditions");
        jointConditions.setDescription("May cause pain, stiffness, swelling, and reduced range of motion. Excessive load or deep compression can increase discomfort");
        jointConditions.setNotes("Choose gentle, low-impact practice. Move slowly, avoid forcing range of motion, use props for support, and focus on controlled strengthening and joint mobility rather than deep stretching");
        limitationRepository.save(jointConditions);

        Limitation pregnancy = new Limitation();
        pregnancy.setName("Pregnancy");
        pregnancy.setDescription("Hormonal and physical changes affect balance, flexibility, and abdominal pressure");
        pregnancy.setNotes("Try prenatal yoga, avoid deep twists and strong abdominal work, and inform your yoga teacher");
        limitationRepository.save(pregnancy);

        Limitation varicoseVeins = new Limitation();
        varicoseVeins.setName("Varicose Veins");
        varicoseVeins.setDescription("Long static standing may increase discomfort");
        varicoseVeins.setNotes("Include gentle inversions if appropriate, avoid prolonged static standing, and move regularly");
        limitationRepository.save(varicoseVeins);

        Limitation glaucomaOrEyeSurgery = new Limitation();
        glaucomaOrEyeSurgery.setName("Glaucoma or Recent Eye Surgery");
        glaucomaOrEyeSurgery.setDescription("Increased intraocular pressure can worsen the condition");
        glaucomaOrEyeSurgery.setNotes("Avoid inversions and strong breath retention; consult your ophthalmologist");
        limitationRepository.save(glaucomaOrEyeSurgery);

        Limitation spinalHernias = new Limitation();
        spinalHernias.setName("Spinal Hernias");
        spinalHernias.setDescription("May cause nerve irritation and pain");
        spinalHernias.setNotes("Avoid deep forward folds or strong spinal flexion; practice under professional guidance");
        limitationRepository.save(spinalHernias);

        Limitation cancer = new Limitation();
        cancer.setName("Cancer");
        cancer.setDescription("Energy levels, immunity, and tissue sensitivity may be reduced");
        cancer.setNotes("Choose gentle therapeutic yoga; always consult your healthcare provider");
        limitationRepository.save(cancer);

        Limitation scoliosis = new Limitation();
        scoliosis.setName("Scoliosis");
        scoliosis.setDescription("May create muscular imbalance");
        scoliosis.setNotes("Focus on alignment, strengthening weak areas, and individualized guidance");
        limitationRepository.save(scoliosis);

        Limitation heartDisease = new Limitation();
        heartDisease.setName("Heart Disease");
        heartDisease.setDescription("Excessive strain may be risky");
        heartDisease.setNotes("Avoid intense dynamic sequences and strong breath retention; get medical clearance");
        limitationRepository.save(heartDisease);

        Limitation highBloodPressure = new Limitation();
        highBloodPressure.setName("High Blood Pressure");
        highBloodPressure.setDescription("May increase risk during inversions or breath retention");
        highBloodPressure.setNotes("Avoid long inversions and strong pranayama; emphasize slow, steady breathing");
        limitationRepository.save(highBloodPressure);

        Limitation osteoporosis = new Limitation();
        osteoporosis.setName("Osteoporosis");
        osteoporosis.setDescription("Reduced bone density increases fracture risk");
        osteoporosis.setNotes("Avoid strong spinal flexion and twisting; focus on posture and gentle strengthening");
        limitationRepository.save(osteoporosis);

        Limitation recentSurgery = new Limitation();
        recentSurgery.setName("Recent Surgery");
        recentSurgery.setDescription("Healing tissues may not tolerate strain");
        recentSurgery.setNotes("Resume practice only with medical approval; modify significantly");
        limitationRepository.save(recentSurgery);

        Limitation chronicLowerBackPain = new Limitation();
        chronicLowerBackPain.setName("Chronic Lower Back Pain");
        chronicLowerBackPain.setDescription("May worsen with improper alignment or deep flexion");
        chronicLowerBackPain.setNotes("Strengthen core gently; avoid aggressive forward folds");
        limitationRepository.save(chronicLowerBackPain);

        Limitation vertigoBalanceDisorders = new Limitation();
        vertigoBalanceDisorders.setName("Vertigo / Balance Disorders");
        vertigoBalanceDisorders.setDescription("Sudden position changes may trigger dizziness");
        vertigoBalanceDisorders.setNotes("Move slowly, avoid rapid transitions, and use wall support");
        limitationRepository.save(vertigoBalanceDisorders);

        Limitation diabetes = new Limitation();
        diabetes.setName("Diabetes");
        diabetes.setDescription("Blood sugar fluctuations may affect energy and balance");
        diabetes.setNotes("Monitor glucose levels and avoid prolonged intense sessions without preparation");
        limitationRepository.save(diabetes);

        Limitation neckShoulderInjury = new Limitation();
        neckShoulderInjury.setName("Neck & Shoulder Injury");
        neckShoulderInjury.setDescription("Strain, instability, or past injury in the neck or shoulders may worsen with weight-bearing or poor alignment");
        neckShoulderInjury.setNotes("Avoid headstands, shoulder stands, and heavy arm balances. Keep shoulders stable, reduce load, move mindfully, and prioritize proper alignment. Consider working under professional guidance");
        limitationRepository.save(neckShoulderInjury);
    }

    private void seedGoals() {
        Goal physicalFitness = new Goal();
        physicalFitness.setName("Physical Fitness");
        physicalFitness.setDescription("Improve strength and flexibility");
        physicalFitness.setNotes("Increase your time in asanas, gradually add more challenging variations, and practice a broad range of postures to develop balanced strength and endurance.");
        goalRepository.save(physicalFitness);

        Goal stressRelief = new Goal();
        stressRelief.setName("Stress Relief");
        stressRelief.setDescription("Reduce anxiety and tension");
        stressRelief.setNotes("Include pranayama (breathing techniques), follow physical practice with deep relaxation or yoga nidra, and incorporate guided meditation into your routine.");
        goalRepository.save(stressRelief);

        Goal betterSleep = new Goal();
        betterSleep.setName("Better Sleep");
        betterSleep.setDescription("Improve sleep quality");
        betterSleep.setNotes("If practicing in the evening, keep your sequence slow and grounding — avoid inversions, jumps, and pranayama before bed. Learn basic sleep hygiene, limit device use before sleep, and try reflective journaling.");
        goalRepository.save(betterSleep);

        Goal mentalFocus = new Goal();
        mentalFocus.setName("Mental Focus");
        mentalFocus.setDescription("Improve concentration");
        mentalFocus.setNotes("Add pranayama and yogic meditation, include balance postures in your practice, and — if appropriate — work toward headstand. Support your focus with good sleep, reduced sugar intake, and concentration techniques such as Pomodoro, time-blocking, or intentional focus rituals.");
        goalRepository.save(mentalFocus);

        Goal flexibility = new Goal();
        flexibility.setName("Flexibility");
        flexibility.setDescription("Increase range of motion");
        flexibility.setNotes("Complement your practice with massage or myofascial release (MFR). Focus not only on stretching but also on joint mobility. Be aware that your body may feel stiffer in the morning, and observe how your flexibility changes later in the day. If stress is high, add deep relaxation practices.");
        goalRepository.save(flexibility);

        Goal philosophy = new Goal();
        philosophy.setName("Interested in Philosophy");
        philosophy.setDescription("Explore yoga philosophy");
        philosophy.setNotes("Engage in reflective inquiry into key philosophical concepts, meditate on these ideas, and explore mantra chanting as a contemplative practice.");
        goalRepository.save(philosophy);
    }
}
