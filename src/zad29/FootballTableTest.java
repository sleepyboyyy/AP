package zad29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Team implements Comparable<Team>{
    private String name;
    private int scoredGoals;
    private int concededGoals;
    private int wins;
    private int loses;
    private int draws;

    public Team(String name) {
        this.name = name;

        this.scoredGoals = 0;
        this.concededGoals = 0;
        this.wins = 0;
        this.loses = 0;
        this.draws = 0;
    }

    public void playOff(int scoredGoals, int concededGoals) {
        this.scoredGoals += scoredGoals;
        this.concededGoals += concededGoals;

        if (scoredGoals - concededGoals > 0) wins++;
        else if (scoredGoals - concededGoals < 0) loses++;
        else draws++;
    }

    public String getName() {
        return name;
    }

    public int getTotalPlayOffs() {
        return wins + loses + draws;
    }

    public int getPointsEarned() {
        return (wins * 3) + draws;
    }

    public int getScoreToConcedeRatio() {
        return scoredGoals - concededGoals;
    }

    @Override
    public int compareTo(Team o) {
        return Comparator.comparing(Team::getPointsEarned).reversed()
                .thenComparing(Team::getScoreToConcedeRatio)
                .thenComparing(Team::getName)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%5d%5d%5d%5d%5d", getTotalPlayOffs(), wins, draws, loses, getPointsEarned());
    }
}


class FootballTable {
    Map<String, Team> teams;

    public FootballTable() {
        teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teams.computeIfAbsent(homeTeam, x -> new Team(homeTeam));
        teams.computeIfAbsent(awayTeam, x -> new Team(awayTeam));
        Team hTeam = teams.get(homeTeam);
        Team aTeam = teams.get(awayTeam);

        hTeam.playOff(homeGoals, awayGoals);
        aTeam.playOff(awayGoals, homeGoals);
    }

    public void printTable() {
        List<Team> sortedTeams = teams.values().stream()
                .sorted()
                .collect(Collectors.toList());

        int indx = 1;
        for (Team team : sortedTeams) {
            System.out.println(String.format("%2d. %-15s%s", indx, team.getName(), team));
            indx++;
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here


