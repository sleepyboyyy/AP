package zad24;

import java.util.*;
import java.util.stream.Collectors;

class Movie implements Comparable<Movie> {
    private String title;
    private int [] ratings;

    public Movie(String title, int [] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }

    public double avgRating() {
        return Arrays.stream(ratings).mapToDouble(x -> x).average().orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", getTitle(), avgRating(), ratings.length);
    }

    @Override
    public int compareTo(Movie o) {
        return Comparator.comparing(Movie::avgRating)
                .reversed()
                .thenComparing(Movie::getTitle)
                .compare(this, o);
    }
}

class MoviesList {
    private List<Movie> movies;

    public MoviesList() {movies = new ArrayList<>();}

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted()
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int totalMoviesRatings = movies.stream().mapToInt(x -> x.getRatings().length).sum();

        Comparator<Movie> compare = Comparator.comparing(movie -> {
            int ratingsLength = movie.getRatings().length;
            return ratingsLength == 0 ? 0 : movie.avgRating() * ratingsLength / totalMoviesRatings;
        });

        return movies.stream()
                .sorted(compare.reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }


}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde
