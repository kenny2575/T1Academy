package task2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class StringStreams {

    public String getTheLongestWord(String[] text) {
        return Arrays.stream(text)
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }

    public Map<String, Integer> getStringCount(String text) {
        return Arrays.stream(text.split(" "))
                .collect(Collectors.toMap(
                        string -> string,
                        string -> 1,
                        Integer::sum
                ));
    }

    public void printSorted(String[] text) {
        Arrays.stream(text)
                .sorted((s1, s2) -> {
                    if (s1.length() != s2.length()) {
                        return s1.length() - s2.length();
                    } else {
                        return s1.compareToIgnoreCase(s2);
                    }
                })
                .forEach(System.out::println);
    }

    public String getTheLongestWord2(String[] text) {
        return Arrays.stream(text)
                .flatMap(string -> Arrays.stream(string.split(" ")))
                .max(Comparator.comparingInt(String::length))
                .orElseThrow();
    }
}
