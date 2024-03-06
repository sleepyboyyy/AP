package zad38_re;
//Да се имплементира класа QuizProcessor со единствен метод Map<String, Double> processAnswers(InputStream is).
//
//Методот потребно е од влезниот поток is да ги прочита одговорите на студентите на еден квиз. Информациите за квизовите се дадени во посебни редови и се во следниот формат:
//
//ID; C1, C2, C3, C4, C5, … ,Cn; A1, A2, A3, A4, A5, …,An.
//каде што ID е индексот на студентот, Ci е точниот одговор на i-то прашање, а Ai е одговорот на студентот на i-то прашање. Студентот добива по 1 поен за точен одговор, а по -0.25 за секој неточен одговор. Бројот на прашања n може да биде различен во секој квиз.
//
//Со помош на исклучоци да се игнорира квиз во кој бројот на точни одговори е различен од бројот на одговорите на студентот.
//
//Во резултантната мапа, клучеви се индексите на студентите, а вредности се поените кои студентот ги освоил. Пример ако студентот на квиз со 6 прашања, има точни 3 прашања, а неточни 3 прашања, студентот ќе освои 3*1 - 3*0.25 = 2.25.

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidQuizEntryException extends Exception {
    public InvalidQuizEntryException(String msg) {
        super(msg);
    }
}

class Quiz {
    private final String student_id;
    private final List<String> correct_answers;
    private final List<String> student_answers;
    private double quizPoints;

    public Quiz(String student_id, List<String> correct_answers, List<String> student_answers) throws InvalidQuizEntryException {
        if(correct_answers.size() != student_answers.size()) throw new InvalidQuizEntryException("A quiz must have same number of correct and selected answers");

        this.student_id = student_id;
        this.correct_answers = correct_answers;
        this.student_answers = student_answers;

        calculatePoints(correct_answers, student_answers);
    }

    public void calculatePoints(List<String> correct_answers, List<String> student_answers) {
        IntStream.range(0, correct_answers.size())
                .forEach(i -> {
                    if(correct_answers.get(i).equals(student_answers.get(i))) quizPoints++;
                    else quizPoints -= 0.25;
                });
    }

    public static Quiz createQuiz(String line) throws InvalidQuizEntryException {
        String [] data = line.split(";");
        List<String> correct_answers = new ArrayList<>(Arrays.asList(data[1].split(",")));
        List<String> student_answers = new ArrayList<>(Arrays.asList(data[2].split(",")));

        return new Quiz(data[0], correct_answers, student_answers);
    }

    public String getStudent_id() {
        return student_id;
    }

    public List<String> getCorrect_answers() {
        return correct_answers;
    }

    public List<String> getStudent_answers() {
        return student_answers;
    }

    public double getQuizPoints() {
        return quizPoints;
    }
}

class QuizProcessor {
    public static Map<String, Double> processAnswers(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines()
                .map(input -> {
                    try {
                        return Quiz.createQuiz(input);
                    } catch(InvalidQuizEntryException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getStudent_id, Quiz::getQuizPoints, Double::sum, TreeMap::new));
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}