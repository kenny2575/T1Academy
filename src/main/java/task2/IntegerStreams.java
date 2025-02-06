package task2;

import java.util.Comparator;
import java.util.List;
public class IntegerStreams {


    public Integer getThirdMax(List<Integer> list) {
        return list.stream()
                .sorted(getReverseOrderComparator())
                .skip(2)
                .findFirst()
                .orElseThrow();
    }

    public Integer getThirdUniqMax(List<Integer> list) {
        return list.stream()
                .distinct()
                .sorted(getReverseOrderComparator())
                .skip(2)
                .findFirst()
                .orElseThrow();

    }

    private Comparator<Integer> getReverseOrderComparator() {
        return (o1, o2) -> o2 - o1;
    }

    public List<Integer> getDistinctList(List<Integer> list) {
        return list.stream()
                .distinct()
                .toList();
    }
}