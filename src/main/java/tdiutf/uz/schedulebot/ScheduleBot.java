package tdiutf.uz.schedulebot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.json.JSONObject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class ScheduleBot {
    private static TelegramBot bot;
    private static JSONObject schedule;
    private static final String SCHEDULE_FILE = "schedule.json";

    public static void main(String[] args) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        String botToken = "7823199212:AAHr7TCb1PRWdkyndW6Hp6FzBmBpTdF6pOo";
        bot = new TelegramBot(botToken);

        loadSchedule();

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void loadSchedule() {
        File file = new File(SCHEDULE_FILE);
        System.out.println("Attempting to load schedule from: " + file.getAbsolutePath());
        try {
            if (!file.exists()) {
                System.out.println("schedule.json does not exist, creating default schedule.");
                createDefaultSchedule();
                return;
            }
            String content = new String(Files.readAllBytes(Paths.get(SCHEDULE_FILE)));
            if (content.trim().isEmpty()) {
                System.out.println("schedule.json is empty, creating default schedule.");
                createDefaultSchedule();
                return;
            }
            schedule = new JSONObject(content);
            if (!schedule.has("1-kurs") || !schedule.getJSONObject("1-kurs").has("ATT") || 
                !schedule.getJSONObject("1-kurs").getJSONObject("ATT").has("ATT-221")) {
                System.out.println("Loaded schedule is incomplete, using default schedule.");
                createDefaultSchedule();
            } else {
                System.out.println("Loaded schedule: " + schedule.toString(4));
            }
        } catch (IOException | org.json.JSONException e) {
            System.out.println("Failed to load schedule.json or invalid JSON: " + e.getMessage());
            createDefaultSchedule();
        }
    }

    private static void createDefaultSchedule() {
        schedule = new JSONObject();
        JSONObject defaultSchedule = new JSONObject();

        // 1-kurs
        JSONObject kurs1 = new JSONObject();
        JSONObject att1 = new JSONObject();
        JSONObject att221 = new JSONObject();
        att221.put("Dushanba", "Kompyuter tarmoqlari: 09:00-10:20(101-xona)\nAxborot xavfsizligi: 10:30-11:50(101-xona)");
        att221.put("Seshanba", "Buyumlar interneti: 09:00-10:20(102-xona)\nDasturlash: 10:30-11:50(102-xona)");
        att221.put("Chorshanba", "Kompyuter tarmoqlari: 09:00-10:20(101-xona)\nIngliz tili: 10:30-11:50(103-xona)");
        att221.put("Payshanba", "Axborot xavfsizligi: 09:00-10:20(101-xona)\nDasturlash: 10:30-11:50(102-xona)");
        att221.put("Juma", "Ingliz tili: 09:00-10:20(103-xona)\nJava dasturlash: 10:30-11:50(102-xona)");
        JSONObject att222 = new JSONObject();
        att222.put("Dushanba", "Kompyuter tarmoqlari: 09:00-10:20(104-xona)\nAxborot xavfsizligi: 10:30-11:50(104-xona)");
        att222.put("Seshanba", "Dasturlash: 09:00-10:20(105-xona)\nBuyumlar interneti: 10:30-11:50(105-xona)");
        att222.put("Chorshanba", "Ingliz tili: 09:00-10:20(106-xona)\nKompyuter tarmoqlari: 10:30-11:50(104-xona)");
        att222.put("Payshanba", "Dasturlash: 09:00-10:20(105-xona)\nAxborot xavfsizligi: 10:30-11:50(104-xona)");
        att222.put("Juma", "Java dasturlash: 09:00-10:20(105-xona)\nIngliz tili: 10:30-11:50(106-xona)");
        att1.put("ATT-221", att221);
        att1.put("ATT-222", att222);
        JSONObject bank1 = new JSONObject();
        JSONObject bi101 = new JSONObject();
        bi101.put("Dushanba", "Iqtisodiyot: 09:00-10:20(201-xona)\nBank ishi: 10:30-11:50(201-xona)");
        bi101.put("Seshanba", "Buxgalteriya: 09:00-10:20(202-xona)\nStatistika: 10:30-11:50(202-xona)");
        bi101.put("Chorshanba", "Iqtisodiyot: 09:00-10:20(201-xona)\nIngliz tili: 10:30-11:50(203-xona)");
        bi101.put("Payshanba", "Bank ishi: 09:00-10:20(201-xona)\nBuxgalteriya: 10:30-11:50(202-xona)");
        bi101.put("Juma", "Ingliz tili: 09:00-10:20(203-xona)\nStatistika: 10:30-11:50(202-xona)");
        JSONObject bi102 = new JSONObject();
        bi102.put("Dushanba", "Bank ishi: 09:00-10:20(204-xona)\nIqtisodiyot: 10:30-11:50(204-xona)");
        bi102.put("Seshanba", "Statistika: 09:00-10:20(205-xona)\nBuxgalteriya: 10:30-11:50(205-xona)");
        bi102.put("Chorshanba", "Ingliz tili: 09:00-10:20(206-xona)\nIqtisodiyot: 10:30-11:50(204-xona)");
        bi102.put("Payshanba", "Buxgalteriya: 09:00-10:20(205-xona)\nBank ishi: 10:30-11:50(204-xona)");
        bi102.put("Juma", "Statistika: 09:00-10:20(205-xona)\nIngliz tili: 10:30-11:50(206-xona)");
        bank1.put("BI-101", bi101);
        bank1.put("BI-102", bi102);
        JSONObject moliya1 = new JSONObject();
        JSONObject mol101 = new JSONObject();
        mol101.put("Dushanba", "Moliyaviy tahlil: 09:00-10:20(301-xona)\nInvestitsiyalar: 10:30-11:50(301-xona)");
        mol101.put("Seshanba", "Moliyaviy bozorlar: 09:00-10:20(302-xona)\nStatistika: 10:30-11:50(302-xona)");
        mol101.put("Chorshanba", "Moliyaviy tahlil: 09:00-10:20(301-xona)\nIngliz tili: 10:30-11:50(303-xona)");
        mol101.put("Payshanba", "Investitsiyalar: 09:00-10:20(301-xona)\nMoliyaviy bozorlar: 10:30-11:50(302-xona)");
        mol101.put("Juma", "Ingliz tili: 09:00-10:20(303-xona)\nStatistika: 10:30-11:50(302-xona)");
        JSONObject mol102 = new JSONObject();
        mol102.put("Dushanba", "Investitsiyalar: 09:00-10:20(304-xona)\nMoliyaviy tahlil: 10:30-11:50(304-xona)");
        mol102.put("Seshanba", "Statistika: 09:00-10:20(305-xona)\nMoliyaviy bozorlar: 10:30-11:50(305-xona)");
        mol102.put("Chorshanba", "Ingliz tili: 09:00-10:20(306-xona)\nMoliyaviy tahlil: 10:30-11:50(304-xona)");
        mol102.put("Payshanba", "Moliyaviy bozorlar: 09:00-10:20(305-xona)\nInvestitsiyalar: 10:30-11:50(304-xona)");
        mol102.put("Juma", "Statistika: 09:00-10:20(305-xona)\nIngliz tili: 10:30-11:50(306-xona)");
        moliya1.put("MOL-101", mol101);
        moliya1.put("MOL-102", mol102);
        JSONObject iqtisodiyot1 = new JSONObject();
        JSONObject iqt101 = new JSONObject();
        iqt101.put("Dushanba", "Makroiqtisodiyot: 09:00-10:20(401-xona)\nMikroiqtisodiyot: 10:30-11:50(401-xona)");
        iqt101.put("Seshanba", "Ekonometriya: 09:00-10:20(402-xona)\nIqtisodiy tahlil: 10:30-11:50(402-xona)");
        iqt101.put("Chorshanba", "Makroiqtisodiyot: 09:00-10:20(401-xona)\nIngliz tili: 10:30-11:50(403-xona)");
        iqt101.put("Payshanba", "Mikroiqtisodiyot: 09:00-10:20(401-xona)\nEkonometriya: 10:30-11:50(402-xona)");
        iqt101.put("Juma", "Ingliz tili: 09:00-10:20(403-xona)\nIqtisodiy tahlil: 10:30-11:50(402-xona)");
        JSONObject iqt102 = new JSONObject();
        iqt102.put("Dushanba", "Mikroiqtisodiyot: 09:00-10:20(404-xona)\nMakroiqtisodiyot: 10:30-11:50(404-xona)");
        iqt102.put("Seshanba", "Iqtisodiy tahlil: 09:00-10:20(405-xona)\nEkonometriya: 10:30-11:50(405-xona)");
        iqt102.put("Chorshanba", "Ingliz tili: 09:00-10:20(406-xona)\nMakroiqtisodiyot: 10:30-11:50(404-xona)");
        iqt102.put("Payshanba", "Ekonometriya: 09:00-10:20(405-xona)\nMikroiqtisodiyot: 10:30-11:50(404-xona)");
        iqt102.put("Juma", "Iqtisodiy tahlil: 09:00-10:20(405-xona)\nIngliz tili: 10:30-11:50(406-xona)");
        iqtisodiyot1.put("IQT-101", iqt101);
        iqtisodiyot1.put("IQT-102", iqt102);
        JSONObject soliq1 = new JSONObject();
        JSONObject sol101 = new JSONObject();
        sol101.put("Dushanba", "Soliq qonunchiligi: 09:00-10:20(501-xona)\nSoliq tahlili: 10:30-11:50(501-xona)");
        sol101.put("Seshanba", "Soliq auditi: 09:00-10:20(502-xona)\nMoliyaviy hisobot: 10:30-11:50(502-xona)");
        sol101.put("Chorshanba", "Soliq qonunchiligi: 09:00-10:20(501-xona)\nIngliz tili: 10:30-11:50(503-xona)");
        sol101.put("Payshanba", "Soliq tahlili: 09:00-10:20(501-xona)\nSoliq auditi: 10:30-11:50(502-xona)");
        sol101.put("Juma", "Ingliz tili: 09:00-10:20(503-xona)\nMoliyaviy hisobot: 10:30-11:50(502-xona)");
        JSONObject sol102 = new JSONObject();
        sol102.put("Dushanba", "Soliq tahlili: 09:00-10:20(504-xona)\nSoliq qonunchiligi: 10:30-11:50(504-xona)");
        sol102.put("Seshanba", "Moliyaviy hisobot: 09:00-10:20(505-xona)\nSoliq auditi: 10:30-11:50(505-xona)");
        sol102.put("Chorshanba", "Ingliz tili: 09:00-10:20(506-xona)\nSoliq qonunchiligi: 10:30-11:50(504-xona)");
        sol102.put("Payshanba", "Soliq auditi: 09:00-10:20(505-xona)\nSoliq tahlili: 10:30-11:50(504-xona)");
        sol102.put("Juma", "Moliyaviy hisobot: 09:00-10:20(505-xona)\nIngliz tili: 10:30-11:50(506-xona)");
        soliq1.put("SOL-101", sol101);
        soliq1.put("SOL-102", sol102);
        kurs1.put("ATT", att1);
        kurs1.put("Bank ishi", bank1);
        kurs1.put("Moliya", moliya1);
        kurs1.put("Iqtisodiyot", iqtisodiyot1);
        kurs1.put("Soliq", soliq1);
        defaultSchedule.put("1-kurs", kurs1);

        // 2-kurs
        JSONObject kurs2 = new JSONObject();
        JSONObject att2 = new JSONObject();
        JSONObject att221_2 = new JSONObject();
        att221_2.put("Dushanba", "Kompyuter tarmoqlari: 09:00-10:20(307-xona)\nDasturlash texnologiyalari: 10:30-11:50(307-xona)");
        att221_2.put("Seshanba", "Buyumlar interneti: 09:00-10:20(308-xona)\nJava dasturlash: 10:30-11:50(308-xona)");
        att221_2.put("Chorshanba", "Dasturlash texnologiyalari: 09:00-10:20(307-xona)\nBuyumlar interneti: 10:30-11:50(308-xona)");
        att221_2.put("Payshanba", "Kompyuter tarmoqlari: 09:00-10:20(307-xona)\nAxborot xavfsizligi: 10:30-11:50(309-xona)");
        att221_2.put("Juma", "Java dasturlash: 09:00-10:20(308-xona)\nIngliz tili: 10:30-11:50(310-xona)");
        JSONObject att222_2 = new JSONObject();
        att222_2.put("Dushanba", "Dasturlash texnologiyalari: 09:00-10:20(311-xona)\nKompyuter tarmoqlari: 10:30-11:50(311-xona)");
        att222_2.put("Seshanba", "Java dasturlash: 09:00-10:20(312-xona)\nBuyumlar interneti: 10:30-11:50(312-xona)");
        att222_2.put("Chorshanba", "Buyumlar interneti: 09:00-10:20(312-xona)\nDasturlash texnologiyalari: 10:30-11:50(311-xona)");
        att222_2.put("Payshanba", "Axborot xavfsizligi: 09:00-10:20(309-xona)\nKompyuter tarmoqlari: 10:30-11:50(311-xona)");
        att222_2.put("Juma", "Ingliz tili: 09:00-10:20(310-xona)\nJava dasturlash: 10:30-11:50(312-xona)");
        att2.put("ATT-221", att221_2);
        att2.put("ATT-222", att222_2);
        JSONObject bank2 = new JSONObject();
        JSONObject bi201 = new JSONObject();
        bi201.put("Dushanba", "Bank operatsiyalari: 09:00-10:20(201-xona)\nKredit tahlili: 10:30-11:50(201-xona)");
        bi201.put("Seshanba", "Moliyaviy menejment: 09:00-10:20(202-xona)\nBank xizmatlari: 10:30-11:50(202-xona)");
        bi201.put("Chorshanba", "Bank operatsiyalari: 09:00-10:20(201-xona)\nIngliz tili: 10:30-11:50(203-xona)");
        bi201.put("Payshanba", "Kredit tahlili: 09:00-10:20(201-xona)\nMoliyaviy menejment: 10:30-11:50(202-xona)");
        bi201.put("Juma", "Ingliz tili: 09:00-10:20(203-xona)\nBank xizmatlari: 10:30-11:50(202-xona)");
        JSONObject bi202 = new JSONObject();
        bi202.put("Dushanba", "Kredit tahlili: 09:00-10:20(204-xona)\nBank operatsiyalari: 10:30-11:50(204-xona)");
        bi202.put("Seshanba", "Bank xizmatlari: 09:00-10:20(205-xona)\nMoliyaviy menejment: 10:30-11:50(205-xona)");
        bi202.put("Chorshanba", "Ingliz tili: 09:00-10:20(206-xona)\nBank operatsiyalari: 10:30-11:50(204-xona)");
        bi202.put("Payshanba", "Moliyaviy menejment: 09:00-10:20(205-xona)\nKredit tahlili: 10:30-11:50(204-xona)");
        bi202.put("Juma", "Bank xizmatlari: 09:00-10:20(205-xona)\nIngliz tili: 10:30-11:50(206-xona)");
        bank2.put("BI-201", bi201);
        bank2.put("BI-202", bi202);
        JSONObject moliya2 = new JSONObject();
        JSONObject mol201 = new JSONObject();
        mol201.put("Dushanba", "Moliyaviy tahlil: 09:00-10:20(301-xona)\nInvestitsiyalar: 10:30-11:50(301-xona)");
        mol201.put("Seshanba", "Moliyaviy bozorlar: 09:00-10:20(302-xona)\nStatistika: 10:30-11:50(302-xona)");
        mol201.put("Chorshanba", "Moliyaviy tahlil: 09:00-10:20(301-xona)\nIngliz tili: 10:30-11:50(303-xona)");
        mol201.put("Payshanba", "Investitsiyalar: 09:00-10:20(301-xona)\nMoliyaviy bozorlar: 10:30-11:50(302-xona)");
        mol201.put("Juma", "Ingliz tili: 09:00-10:20(303-xona)\nStatistika: 10:30-11:50(302-xona)");
        JSONObject mol202 = new JSONObject();
        mol202.put("Dushanba", "Investitsiyalar: 09:00-10:20(304-xona)\nMoliyaviy tahlil: 10:30-11:50(304-xona)");
        mol202.put("Seshanba", "Statistika: 09:00-10:20(305-xona)\nMoliyaviy bozorlar: 10:30-11:50(305-xona)");
        mol202.put("Chorshanba", "Ingliz tili: 09:00-10:20(306-xona)\nMoliyaviy tahlil: 10:30-11:50(304-xona)");
        mol202.put("Payshanba", "Moliyaviy bozorlar: 09:00-10:20(305-xona)\nInvestitsiyalar: 10:30-11:50(304-xona)");
        mol202.put("Juma", "Statistika: 09:00-10:20(305-xona)\nIngliz tili: 10:30-11:50(306-xona)");
        moliya2.put("MOL-201", mol201);
        moliya2.put("MOL-202", mol202);
        JSONObject iqtisodiyot2 = new JSONObject();
        JSONObject iqt201 = new JSONObject();
        iqt201.put("Dushanba", "Iqtisodiy siyosat: 09:00-10:20(401-xona)\nIqtisodiy strategiyalar: 10:30-11:50(401-xona)");
        iqt201.put("Seshanba", "Makroiqtisodiyot: 09:00-10:20(402-xona)\nMikroiqtisodiyot: 10:30-11:50(402-xona)");
        iqt201.put("Chorshanba", "Iqtisodiy siyosat: 09:00-10:20(401-xona)\nIngliz tili: 10:30-11:50(403-xona)");
        iqt201.put("Payshanba", "Iqtisodiy strategiyalar: 09:00-10:20(401-xona)\nMakroiqtisodiyot: 10:30-11:50(402-xona)");
        iqt201.put("Juma", "Ingliz tili: 09:00-10:20(403-xona)\nMikroiqtisodiyot: 10:30-11:50(402-xona)");
        JSONObject iqt202 = new JSONObject();
        iqt202.put("Dushanba", "Iqtisodiy strategiyalar: 09:00-10:20(404-xona)\nIqtisodiy siyosat: 10:30-11:50(404-xona)");
        iqt202.put("Seshanba", "Mikroiqtisodiyot: 09:00-10:20(405-xona)\nMakroiqtisodiyot: 10:30-11:50(405-xona)");
        iqt202.put("Chorshanba", "Ingliz tili: 09:00-10:20(406-xona)\nIqtisodiy siyosat: 10:30-11:50(404-xona)");
        iqt202.put("Payshanba", "Makroiqtisodiyot: 09:00-10:20(405-xona)\nIqtisodiy strategiyalar: 10:30-11:50(404-xona)");
        iqt202.put("Juma", "Mikroiqtisodiyot: 09:00-10:20(405-xona)\nIngliz tili: 10:30-11:50(406-xona)");
        iqtisodiyot2.put("IQT-201", iqt201);
        iqtisodiyot2.put("IQT-202", iqt202);
        JSONObject soliq2 = new JSONObject();
        JSONObject sol201 = new JSONObject();
        sol201.put("Dushanba", "Soliq tahlili: 09:00-10:20(501-xona)\nSoliq auditi: 10:30-11:50(501-xona)");
        sol201.put("Seshanba", "Soliq qonunchiligi: 09:00-10:20(502-xona)\nMoliyaviy hisobot: 10:30-11:50(502-xona)");
        sol201.put("Chorshanba", "Soliq tahlili: 09:00-10:20(501-xona)\nIngliz tili: 10:30-11:50(503-xona)");
        sol201.put("Payshanba", "Soliq auditi: 09:00-10:20(501-xona)\nSoliq qonunchiligi: 10:30-11:50(502-xona)");
        sol201.put("Juma", "Ingliz tili: 09:00-10:20(503-xona)\nMoliyaviy hisobot: 10:30-11:50(502-xona)");
        JSONObject sol202 = new JSONObject();
        sol202.put("Dushanba", "Soliq auditi: 09:00-10:20(504-xona)\nSoliq tahlili: 10:30-11:50(504-xona)");
        sol202.put("Seshanba", "Moliyaviy hisobot: 09:00-10:20(505-xona)\nSoliq qonunchiligi: 10:30-11:50(505-xona)");
        sol202.put("Chorshanba", "Ingliz tili: 09:00-10:20(506-xona)\nSoliq tahlili: 10:30-11:50(504-xona)");
        sol202.put("Payshanba", "Soliq qonunchiligi: 09:00-10:20(505-xona)\nSoliq auditi: 10:30-11:50(504-xona)");
        sol202.put("Juma", "Moliyaviy hisobot: 09:00-10:20(505-xona)\nIngliz tili: 10:30-11:50(506-xona)");
        soliq2.put("SOL-201", sol201);
        soliq2.put("SOL-202", sol202);
        kurs2.put("ATT", att2);
        kurs2.put("Bank ishi", bank2);
        kurs2.put("Moliya", moliya2);
        kurs2.put("Iqtisodiyot", iqtisodiyot2);
        kurs2.put("Soliq", soliq2);
        defaultSchedule.put("2-kurs", kurs2);

        // 3-kurs
        JSONObject kurs3 = new JSONObject();
        JSONObject att3 = new JSONObject();
        JSONObject att221_3 = new JSONObject();
        att221_3.put("Dushanba", "Ma'lumotlar bazasi tizimlari: 09:00-10:20(413-xona)\nTarmoq administratsiyasi: 10:30-11:50(413-xona)");
        att221_3.put("Seshanba", "Kompyuter tarmoqlari: 09:00-10:20(414-xona)\nJava dasturlash: 10:30-11:50(414-xona)");
        att221_3.put("Chorshanba", "Axborot xavfsizligi: 09:00-10:20(415-xona)\nIngliz tili: 10:30-11:50(416-xona)");
        att221_3.put("Payshanba", "Buyumlar interneti: 09:00-10:20(417-xona)\nMa'lumotlar bazasi tizimlari: 10:30-11:50(413-xona)");
        att221_3.put("Juma", "Ingliz tili: 09:00-10:20(416-xona)\nTarmoq administratsiyasi: 10:30-11:50(413-xona)");
        JSONObject att222_3 = new JSONObject();
        att222_3.put("Dushanba", "Tarmoq administratsiyasi: 09:00-10:20(418-xona)\nMa'lumotlar bazasi tizimlari: 10:30-11:50(418-xona)");
        att222_3.put("Seshanba", "Java dasturlash: 09:00-10:20(417-xona)\nKompyuter tarmoqlari: 10:30-11:50(414-xona)");
        att222_3.put("Chorshanba", "Ingliz tili: 09:00-10:20(416-xona)\nAxborot xavfsizligi: 10:30-11:50(415-xona)");
        att222_3.put("Payshanba", "Ma'lumotlar bazasi tizimlari: 09:00-10:20(418-xona)\nBuyumlar interneti: 10:30-11:50(417-xona)");
        att222_3.put("Juma", "Tarmoq administratsiyasi: 09:00-10:20(413-xona)\nIngliz tili: 10:30-11:50(416-xona)");
        att3.put("ATT-221", att221_3);
        att3.put("ATT-222", att222_3);
        JSONObject bank3 = new JSONObject();
        JSONObject bi301 = new JSONObject();
        bi301.put("Dushanba", "Bank risklari: 09:00-10:20(201-xona)\nMoliyaviy tahlil: 10:30-11:50(201-xona)");
        bi301.put("Seshanba", "Bank auditi: 09:00-10:20(202-xona)\nInvestitsiyalar: 10:30-11:50(202-xona)");
        bi301.put("Chorshanba", "Bank risklari: 09:00-10:20(201-xona)\nIngliz tili: 10:30-11:50(203-xona)");
        bi301.put("Payshanba", "Moliyaviy tahlil: 09:00-10:20(201-xona)\nBank auditi: 10:30-11:50(202-xona)");
        bi301.put("Juma", "Ingliz tili: 09:00-10:20(203-xona)\nInvestitsiyalar: 10:30-11:50(202-xona)");
        JSONObject bi302 = new JSONObject();
        bi302.put("Dushanba", "Moliyaviy tahlil: 09:00-10:20(204-xona)\nBank risklari: 10:30-11:50(204-xona)");
        bi302.put("Seshanba", "Investitsiyalar: 09:00-10:20(205-xona)\nBank auditi: 10:30-11:50(205-xona)");
        bi302.put("Chorshanba", "Ingliz tili: 09:00-10:20(206-xona)\nBank risklari: 10:30-11:50(204-xona)");
        bi302.put("Payshanba", "Bank auditi: 09:00-10:20(205-xona)\nMoliyaviy tahlil: 10:30-11:50(204-xona)");
        bi302.put("Juma", "Investitsiyalar: 09:00-10:20(205-xona)\nIngliz tili: 10:30-11:50(206-xona)");
        bank3.put("BI-301", bi301);
        bank3.put("BI-302", bi302);
        JSONObject moliya3 = new JSONObject();
        JSONObject mol301 = new JSONObject();
        mol301.put("Dushanba", "Moliyaviy menejment: 09:00-10:20(407-xona)\nRisklarni boshqarish: 10:30-11:50(407-xona)");
        mol301.put("Seshanba", "Moliyaviy hisobot: 09:00-10:20(408-xona)\nInvestitsiya strategiyalari: 10:30-11:50(408-xona)");
        mol301.put("Chorshanba", "Moliyaviy menejment: 09:00-10:20(407-xona)\nIngliz tili: 10:30-11:50(409-xona)");
        mol301.put("Payshanba", "Risklarni boshqarish: 09:00-10:20(407-xona)\nMoliyaviy hisobot: 10:30-11:50(408-xona)");
        mol301.put("Juma", "Ingliz tili: 09:00-10:20(409-xona)\nInvestitsiya strategiyalari: 10:30-11:50(408-xona)");
        JSONObject mol302 = new JSONObject();
        mol302.put("Dushanba", "Risklarni boshqarish: 09:00-10:20(410-xona)\nMoliyaviy menejment: 10:30-11:50(410-xona)");
        mol302.put("Seshanba", "Investitsiya strategiyalari: 09:00-10:20(411-xona)\nMoliyaviy hisobot: 10:30-11:50(411-xona)");
        mol302.put("Chorshanba", "Ingliz tili: 09:00-10:20(412-xona)\nMoliyaviy menejment: 10:30-11:50(410-xona)");
        mol302.put("Payshanba", "Moliyaviy hisobot: 09:00-10:20(411-xona)\nRisklarni boshqarish: 10:30-11:50(410-xona)");
        mol302.put("Juma", "Investitsiya strategiyalari: 09:00-10:20(411-xona)\nIngliz tili: 10:30-11:50(412-xona)");
        moliya3.put("MOL-301", mol301);
        moliya3.put("MOL-302", mol302);
        JSONObject iqtisodiyot3 = new JSONObject();
        JSONObject iqt301 = new JSONObject();
        iqt301.put("Dushanba", "Makroiqtisodiyot: 09:00-10:20(401-xona)\nMikroiqtisodiyot: 10:30-11:50(401-xona)");
        iqt301.put("Seshanba", "Ekonometriya: 09:00-10:20(402-xona)\nIqtisodiy tahlil: 10:30-11:50(402-xona)");
        iqt301.put("Chorshanba", "Makroiqtisodiyot: 09:00-10:20(401-xona)\nIngliz tili: 10:30-11:50(403-xona)");
        iqt301.put("Payshanba", "Mikroiqtisodiyot: 09:00-10:20(401-xona)\nEkonometriya: 10:30-11:50(402-xona)");
        iqt301.put("Juma", "Ingliz tili: 09:00-10:20(403-xona)\nIqtisodiy tahlil: 10:30-11:50(402-xona)");
        JSONObject iqt302 = new JSONObject();
        iqt302.put("Dushanba", "Mikroiqtisodiyot: 09:00-10:20(404-xona)\nMakroiqtisodiyot: 10:30-11:50(404-xona)");
        iqt302.put("Seshanba", "Iqtisodiy tahlil: 09:00-10:20(405-xona)\nEkonometriya: 10:30-11:50(405-xona)");
        iqt302.put("Chorshanba", "Ingliz tili: 09:00-10:20(406-xona)\nMakroiqtisodiyot: 10:30-11:50(404-xona)");
        iqt302.put("Payshanba", "Ekonometriya: 09:00-10:20(405-xona)\nMikroiqtisodiyot: 10:30-11:50(404-xona)");
        iqt302.put("Juma", "Iqtisodiy tahlil: 09:00-10:20(405-xona)\nIngliz tili: 10:30-11:50(406-xona)");
        iqtisodiyot3.put("IQT-301", iqt301);
        iqtisodiyot3.put("IQT-302", iqt302);
        JSONObject soliq3 = new JSONObject();
        JSONObject sol301 = new JSONObject();
        sol301.put("Dushanba", "Soliq strategiyalari: 09:00-10:20(501-xona)\nSoliq auditi: 10:30-11:50(501-xona)");
        sol301.put("Seshanba", "Soliq qonunchiligi: 09:00-10:20(502-xona)\nMoliyaviy tahlil: 10:30-11:50(502-xona)");
        sol301.put("Chorshanba", "Soliq strategiyalari: 09:00-10:20(501-xona)\nIngliz tili: 10:30-11:50(503-xona)");
        sol301.put("Payshanba", "Soliq auditi: 09:00-10:20(501-xona)\nSoliq qonunchiligi: 10:30-11:50(502-xona)");
        sol301.put("Juma", "Ingliz tili: 09:00-10:20(503-xona)\nMoliyaviy tahlil: 10:30-11:50(502-xona)");
        JSONObject sol302 = new JSONObject();
        sol302.put("Dushanba", "Soliq auditi: 09:00-10:20(504-xona)\nSoliq strategiyalari: 10:30-11:50(504-xona)");
        sol302.put("Seshanba", "Moliyaviy tahlil: 09:00-10:20(505-xona)\nSoliq qonunchiligi: 10:30-11:50(505-xona)");
        sol302.put("Chorshanba", "Ingliz tili: 09:00-10:20(506-xona)\nSoliq strategiyalari: 10:30-11:50(504-xona)");
        sol302.put("Payshanba", "Soliq qonunchiligi: 09:00-10:20(505-xona)\nSoliq auditi: 10:30-11:50(504-xona)");
        sol302.put("Juma", "Moliyaviy tahlil: 09:00-10:20(505-xona)\nIngliz tili: 10:30-11:50(506-xona)");
        soliq3.put("SOL-301", sol301);
        soliq3.put("SOL-302", sol302);
        kurs3.put("ATT", att3);
        kurs3.put("Bank ishi", bank3);
        kurs3.put("Moliya", moliya3);
        kurs3.put("Iqtisodiyot", iqtisodiyot3);
        kurs3.put("Soliq", soliq3);
        defaultSchedule.put("3-kurs", kurs3);

        // 4-kurs
        JSONObject kurs4 = new JSONObject();
        JSONObject att4 = new JSONObject();
        JSONObject att221_4 = new JSONObject();
        att221_4.put("Dushanba", "Sun'iy intellekt asoslari: 09:00-10:20(513-xona)\nKiberxavfsizlik: 10:30-11:50(513-xona)");
        att221_4.put("Seshanba", "Bulutli texnologiyalar: 09:00-10:20(514-xona)\nMa'lumotlar bazasi tizimlari: 10:30-11:50(514-xona)");
        att221_4.put("Chorshanba", "Axborot xavfsizligi: 09:00-10:20(515-xona)\nIngliz tili: 10:30-11:50(516-xona)");
        att221_4.put("Payshanba", "Kiberxavfsizlik: 09:00-10:20(513-xona)\nSun'iy intellekt asoslari: 10:30-11:50(513-xona)");
        att221_4.put("Juma", "Ingliz tili: 09:00-10:20(516-xona)\nBulutli texnologiyalar: 10:30-11:50(514-xona)");
        JSONObject att222_4 = new JSONObject();
        att222_4.put("Dushanba", "Kiberxavfsizlik: 09:00-10:20(517-xona)\nSun'iy intellekt asoslari: 10:30-11:50(517-xona)");
        att222_4.put("Seshanba", "Ma'lumotlar bazasi tizimlari: 09:00-10:20(518-xona)\nBulutli texnologiyalar: 10:30-11:50(518-xona)");
        att222_4.put("Chorshanba", "Ingliz tili: 09:00-10:20(516-xona)\nAxborot xavfsizligi: 10:30-11:50(515-xona)");
        att222_4.put("Payshanba", "Sun'iy intellekt asoslari: 09:00-10:20(517-xona)\nKiberxavfsizlik: 10:30-11:50(517-xona)");
        att222_4.put("Juma", "Bulutli texnologiyalar: 09:00-10:20(518-xona)\nIngliz tili: 10:30-11:50(516-xona)");
        att4.put("ATT-221", att221_4);
        att4.put("ATT-222", att222_4);
        JSONObject bank4 = new JSONObject();
        JSONObject bi401 = new JSONObject();
        bi401.put("Dushanba", "Xalqaro bank ishi: 09:00-10:20(201-xona)\nBank strategiyalari: 10:30-11:50(201-xona)");
        bi401.put("Seshanba", "Moliyaviy innovatsiyalar: 09:00-10:20(202-xona)\nBank auditi: 10:30-11:50(202-xona)");
        bi401.put("Chorshanba", "Xalqaro bank ishi: 09:00-10:20(201-xona)\nIngliz tili: 10:30-11:50(203-xona)");
        bi401.put("Payshanba", "Bank strategiyalari: 09:00-10:20(201-xona)\nMoliyaviy innovatsiyalar: 10:30-11:50(202-xona)");
        bi401.put("Juma", "Ingliz tili: 09:00-10:20(203-xona)\nBank auditi: 10:30-11:50(202-xona)");
        JSONObject bi402 = new JSONObject();
        bi402.put("Dushanba", "Bank strategiyalari: 09:00-10:20(204-xona)\nXalqaro bank ishi: 10:30-11:50(204-xona)");
        bi402.put("Seshanba", "Bank auditi: 09:00-10:20(205-xona)\nMoliyaviy innovatsiyalar: 10:30-11:50(205-xona)");
        bi402.put("Chorshanba", "Ingliz tili: 09:00-10:20(206-xona)\nXalqaro bank ishi: 10:30-11:50(204-xona)");
        bi402.put("Payshanba", "Moliyaviy innovatsiyalar: 09:00-10:20(205-xona)\nBank strategiyalari: 10:30-11:50(204-xona)");
        bi402.put("Juma", "Bank auditi: 09:00-10:20(205-xona)\nIngliz tili: 10:30-11:50(206-xona)");
        bank4.put("BI-401", bi401);
        bank4.put("BI-402", bi402);
        JSONObject moliya4 = new JSONObject();
        JSONObject mol401 = new JSONObject();
        mol401.put("Dushanba", "Moliyaviy strategiyalar: 09:00-10:20(407-xona)\nXalqaro moliya: 10:30-11:50(407-xona)");
        mol401.put("Seshanba", "Moliyaviy innovatsiyalar: 09:00-10:20(408-xona)\nInvestitsiya tahlili: 10:30-11:50(408-xona)");
        mol401.put("Chorshanba", "Moliyaviy strategiyalar: 09:00-10:20(407-xona)\nIngliz tili: 10:30-11:50(409-xona)");
        mol401.put("Payshanba", "Xalqaro moliya: 09:00-10:20(407-xona)\nMoliyaviy innovatsiyalar: 10:30-11:50(408-xona)");
        mol401.put("Juma", "Ingliz tili: 09:00-10:20(409-xona)\nInvestitsiya tahlili: 10:30-11:50(408-xona)");
        JSONObject mol402 = new JSONObject();
        mol402.put("Dushanba", "Xalqaro moliya: 09:00-10:20(410-xona)\nMoliyaviy strategiyalar: 10:30-11:50(410-xona)");
        mol402.put("Seshanba", "Investitsiya tahlili: 09:00-10:20(411-xona)\nMoliyaviy innovatsiyalar: 10:30-11:50(411-xona)");
        mol402.put("Chorshanba", "Ingliz tili: 09:00-10:20(412-xona)\nMoliyaviy strategiyalar: 10:30-11:50(410-xona)");
        mol402.put("Payshanba", "Moliyaviy innovatsiyalar: 09:00-10:20(411-xona)\nXalqaro moliya: 10:30-11:50(410-xona)");
        mol402.put("Juma", "Investitsiya tahlili: 09:00-10:20(411-xona)\nIngliz tili: 10:30-11:50(412-xona)");
        moliya4.put("MOL-401", mol401);
        moliya4.put("MOL-402", mol402);
        JSONObject iqtisodiyot4 = new JSONObject();
        JSONObject iqt401 = new JSONObject();
        iqt401.put("Dushanba", "Xalqaro iqtisodiyot: 09:00-10:20(507-xona)\nIqtisodiy siyosat: 10:30-11:50(507-xona)");
        iqt401.put("Seshanba", "Iqtisodiy strategiyalar: 09:00-10:20(508-xona)\nIqtisodiy prognozlash: 10:30-11:50(508-xona)");
        iqt401.put("Chorshanba", "Xalqaro iqtisodiyot: 09:00-10:20(507-xona)\nIngliz tili: 10:30-11:50(509-xona)");
        iqt401.put("Payshanba", "Iqtisodiy siyosat: 09:00-10:20(507-xona)\nIqtisodiy strategiyalar: 10:30-11:50(508-xona)");
        iqt401.put("Juma", "Ingliz tili: 09:00-10:20(509-xona)\nIqtisodiy prognozlash: 10:30-11:50(508-xona)");
        JSONObject iqt402 = new JSONObject();
        iqt402.put("Dushanba", "Iqtisodiy siyosat: 09:00-10:20(510-xona)\nXalqaro iqtisodiyot: 10:30-11:50(510-xona)");
        iqt402.put("Seshanba", "Iqtisodiy prognozlash: 09:00-10:20(511-xona)\nIqtisodiy strategiyalar: 10:30-11:50(511-xona)");
        iqt402.put("Chorshanba", "Ingliz tili: 09:00-10:20(512-xona)\nXalqaro iqtisodiyot: 10:30-11:50(510-xona)");
        iqt402.put("Payshanba", "Iqtisodiy strategiyalar: 09:00-10:20(511-xona)\nIqtisodiy siyosat: 10:30-11:50(510-xona)");
        iqt402.put("Juma", "Iqtisodiy prognozlash: 09:00-10:20(511-xona)\nIngliz tili: 10:30-11:50(512-xona)");
        iqtisodiyot4.put("IQT-401", iqt401);
        iqtisodiyot4.put("IQT-402", iqt402);
        JSONObject soliq4 = new JSONObject();
        JSONObject sol401 = new JSONObject();
        sol401.put("Dushanba", "Soliq qonunchiligi: 09:00-10:20(501-xona)\nSoliq tahlili: 10:30-11:50(501-xona)");
        sol401.put("Seshanba", "Soliq auditi: 09:00-10:20(502-xona)\nMoliyaviy hisobot: 10:30-11:50(502-xona)");
        sol401.put("Chorshanba", "Soliq qonunchiligi: 09:00-10:20(501-xona)\nIngliz tili: 10:30-11:50(503-xona)");
        sol401.put("Payshanba", "Soliq tahlili: 09:00-10:20(501-xona)\nSoliq auditi: 10:30-11:50(502-xona)");
        sol401.put("Juma", "Ingliz tili: 09:00-10:20(503-xona)\nMoliyaviy hisobot: 10:30-11:50(502-xona)");
        JSONObject sol402 = new JSONObject();
        sol402.put("Dushanba", "Soliq tahlili: 09:00-10:20(504-xona)\nSoliq qonunchiligi: 10:30-11:50(504-xona)");
        sol402.put("Seshanba", "Moliyaviy hisobot: 09:00-10:20(505-xona)\nSoliq auditi: 10:30-11:50(505-xona)");
        sol402.put("Chorshanba", "Ingliz tili: 09:00-10:20(506-xona)\nSoliq qonunchiligi: 10:30-11:50(504-xona)");
        sol402.put("Payshanba", "Soliq auditi: 09:00-10:20(505-xona)\nSoliq tahlili: 10:30-11:50(504-xona)");
        sol402.put("Juma", "Moliyaviy hisobot: 09:00-10:20(505-xona)\nIngliz tili: 10:30-11:50(506-xona)");
        soliq4.put("SOL-401", sol401);
        soliq4.put("SOL-402", sol402);
        kurs4.put("ATT", att4);
        kurs4.put("Bank ishi", bank4);
        kurs4.put("Moliya", moliya4);
        kurs4.put("Iqtisodiyot", iqtisodiyot4);
        kurs4.put("Soliq", soliq4);
        defaultSchedule.put("4-kurs", kurs4);

        schedule = defaultSchedule;
        System.out.println("Default schedule created: " + schedule.toString(4));
        saveSchedule();
    }

    private static void saveSchedule() {
        try (FileWriter file = new FileWriter(SCHEDULE_FILE)) {
            file.write(schedule.toString(4));
            System.out.println("Schedule saved successfully to " + new File(SCHEDULE_FILE).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to save schedule.json: " + e.getMessage());
        }
    }

    private static void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            long chatId = update.message().chat().id();
            String messageText = update.message().text();

            if (messageText.equals("/start")) {
                sendWelcomeMessage(chatId);
            } else if (messageText.equals("Dars jadvali 📅")) {
                sendCourseMenu(chatId);
            }
        } else if (update.callbackQuery() != null) {
            long chatId = update.callbackQuery().message().chat().id();
            String data = update.callbackQuery().data();

            if (data.equals("back_to_courses")) {
                sendCourseMenu(chatId);
            } else if (data.startsWith("course_")) {
                String course = data.substring(7);
                sendDirectionMenu(chatId, course);
            } else if (data.startsWith("direction_")) {
                String[] parts = data.split("_");
                String course = parts[1];
                String direction = parts[2];
                sendDayMenu(chatId, course, direction, null);
            } else if (data.contains("_")) {
                String[] parts = data.split("_");
                if (parts.length == 4) {
                    String course = parts[0];
                    String direction = parts[1];
                    String group = parts[2];
                    String day = parts[3];
                    sendDaySchedule(chatId, course, direction, group, day);
                } else if (parts.length == 3) {
                    String course = parts[0];
                    String direction = parts[1];
                    String day = parts[2];
                    sendDaySchedule(chatId, course, direction, null, day);
                }
            } else {
                sendErrorMessage(chatId);
            }
        }
    }

    private static void sendWelcomeMessage(long chatId) {
        String welcomeText = "🎓 *Oliygoh dars jadvali botiga xush kelibsiz!* 🎉\n\n" +
                            "📚 Kurs, yo'nalish, guruh va hafta kunini tanlab dars jadvalini ko'ring.\n" +
                            "🔽 Quyidagi tugmani bosing:";

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                new String[]{"Dars jadvali 📅"}
        ).resizeKeyboard(true);

        SendMessage message = new SendMessage(chatId, welcomeText)
                .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
                .replyMarkup(keyboard);
        bot.execute(message);
    }

    private static void sendCourseMenu(long chatId) {
        String menuText = "📚 *Kursni tanlang:*";

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.addRow(
                new InlineKeyboardButton("1-kurs 📘").callbackData("course_1-kurs"),
                new InlineKeyboardButton("2-kurs 📗").callbackData("course_2-kurs")
        );
        keyboard.addRow(
                new InlineKeyboardButton("3-kurs 📙").callbackData("course_3-kurs"),
                new InlineKeyboardButton("4-kurs 📕").callbackData("course_4-kurs")
        );

        SendMessage message = new SendMessage(chatId, menuText)
                .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
                .replyMarkup(keyboard);
        bot.execute(message);
    }

    private static void sendDirectionMenu(long chatId, String course) {
        String menuText = "📚 *Yo'nalishni tanlang* (" + course + "):\n\n🔽 Quyidagilardan birini tanlang:";

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        JSONObject courseData = schedule.getJSONObject(course);
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        
        for (String direction : courseData.keySet()) {
            currentRow.add(new InlineKeyboardButton(direction + " ➡️").callbackData("direction_" + course + "_" + direction));
            
            if (currentRow.size() == 2 || direction.equals(courseData.keySet().toArray()[courseData.length() - 1])) {
                keyboard.addRow(currentRow.toArray(new InlineKeyboardButton[0]));
                currentRow.clear();
            }
        }
        
        keyboard.addRow(new InlineKeyboardButton("🔙 Orqaga").callbackData("back_to_courses"));

        SendMessage message = new SendMessage(chatId, menuText)
                .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
                .replyMarkup(keyboard);
        bot.execute(message);
    }

    private static void sendDayMenu(long chatId, String course, String direction, String group) {
        String menuText = group != null 
            ? "📅 *Hafta kunini tanlang* (" + course + ", " + direction + ", " + group + "):\n\n🔽 Quyidagilardan birini tanlang:"
            : "📅 *Hafta kunini tanlang* (" + course + ", " + direction + "):\n\n🔽 Quyidagilardan birini tanlang:";

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        String callbackPrefix = group != null 
            ? course + "_" + direction + "_" + group 
            : course + "_" + direction;
        keyboard.addRow(
                new InlineKeyboardButton("Dushanba 🗓️").callbackData(callbackPrefix + "_Dushanba"),
                new InlineKeyboardButton("Seshanba 🗓️").callbackData(callbackPrefix + "_Seshanba")
        );
        keyboard.addRow(
                new InlineKeyboardButton("Chorshanba 🗓️").callbackData(callbackPrefix + "_Chorshanba"),
                new InlineKeyboardButton("Payshanba 🗓️").callbackData(callbackPrefix + "_Payshanba")
        );
        keyboard.addRow(
                new InlineKeyboardButton("Juma 🗓️").callbackData(callbackPrefix + "_Juma")
        );
        keyboard.addRow(new InlineKeyboardButton("🔙 Orqaga").callbackData("back_to_courses"));

        SendMessage message = new SendMessage(chatId, menuText)
                .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
                .replyMarkup(keyboard);
        bot.execute(message);
    }

    private static void sendDaySchedule(long chatId, String course, String direction, String group, String day) {
        try {
            JSONObject directionData = schedule.getJSONObject(course).getJSONObject(direction);
            StringBuilder messageText = new StringBuilder(String.format("📚 *Dars jadvali* (%s, %s)\n\n🗓️ *%s:*\n────────────\n", 
                                                                     course, direction, day));

            if (group != null) {
                JSONObject groupData = directionData.getJSONObject(group);
                System.out.println("Group data for " + course + ", " + direction + ", " + group + ": " + groupData.toString(4));
                String scheduleText = groupData.has(day) ? groupData.getString(day) : "Bu kun uchun jadval yo’q.";
                messageText.append(scheduleText);
            } else {
                for (String g : directionData.keySet()) {
                    messageText.append("*Guruh: ").append(g).append("*\n");
                    JSONObject groupData = directionData.getJSONObject(g);
                    String scheduleText = groupData.has(day) ? groupData.getString(day) : "Bu kun uchun jadval yo’q.";
                    messageText.append(scheduleText).append("\n────────────\n");
                }
            }

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            keyboard.addRow(new InlineKeyboardButton("🔙 Orqaga").callbackData("back_to_courses"));

            SendMessage message = new SendMessage(chatId, messageText.toString())
                    .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown)
                    .replyMarkup(keyboard);
            bot.execute(message);
        } catch (org.json.JSONException e) {
            System.out.println("Error accessing schedule data: " + e.getMessage());
            sendErrorMessage(chatId);
        }
    }

    private static void sendErrorMessage(long chatId) {
        String errorText = "❌ Xatolik yuz berdi. Iltimos, qaytadan urinib ko’ring.";
        SendMessage message = new SendMessage(chatId, errorText)
                .parseMode(com.pengrad.telegrambot.model.request.ParseMode.Markdown);
        bot.execute(message);
    }
}