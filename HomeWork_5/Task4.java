package HomeWork_5;

public class Task4 {
    public static void main(String[] args) {

        String[] heroes = {
                "Pudge",
                "Invoker",
                "Juggernaut",
                "Crystal Maiden",
                "Earthshaker"
        };

        int[] kills = {8, 12, 10, 2, 4};
        int[] deaths = {6, 3, 2, 9, 5};
        int[] assists = {14, 9, 8, 18, 16};
        int[] netWorth = {14500, 21200, 24800, 9800, 12300};

        double[] kdaRatios = new double[5];

        int totalKills = 0;
        int totalNetWorth = 0;

        for (int i = 0; i < heroes.length; i++) {
            totalKills = totalKills + kills[i];
            totalNetWorth = totalNetWorth + netWorth[i];
        }

        for (int i = 0; i < heroes.length; i++) {

            if (deaths[i] == 0) {
                kdaRatios[i] = (double) (kills[i] + assists[i]) / 1;
            } else {
                kdaRatios[i] = (double) (kills[i] + assists[i]) / deaths[i];
            }
        }

        // MVP
        int mvp = 0;

        for (int i = 1; i < kdaRatios.length; i++) {
            if (kdaRatios[i] > kdaRatios[mvp]) {
                mvp = i;
            }
        }

        System.out.println("DOTA 2 - POST MATCH REPORT");
        System.out.println();

        for (int i = 0; i < heroes.length; i++) {

            double killParticipation = (double) (kills[i] + assists[i]) / totalKills * 100;

            System.out.println("Герой: " + heroes[i]);

            System.out.println("K/D/A: " + kills[i] + "/" + deaths[i] + "/" + assists[i]
            );

            System.out.println("Net Worth: " + netWorth[i]);
            System.out.println("KDA: " + kdaRatios[i]);
            System.out.println("Kill Participation: " + killParticipation + "%");

            System.out.println();
        }

        System.out.println("Загальні вбивства команди: " + totalKills);
        System.out.println("Загальний Net Worth: " + totalNetWorth);

        System.out.println("MVP: " + heroes[mvp] + " з KDA " + kdaRatios[mvp]
        );
    }
}
