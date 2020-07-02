import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static List<int[]> searchForThreeTerms(int[] array, int sum) {
        List<int[]> list = new ArrayList<>();
        int[] numbers;
        int index;

        Arrays.sort(array);

        for(int i = 0; i < array.length - 2; i++){
            for(int j = i + 1; j < array.length - 1; j++){
                index = binarySearchSumWithTwoObject(array, sum, i, j);
                if(index != -1){
                    numbers = new int[]{array[i], array[j], array[index]};

                    list.add(numbers);
                }
            }
        }
        return list;
    }


    private static int binarySearchSumWithTwoObject(int[] array, int sum, int firstIndex, int secondIndex) {
        int start = 0;
        int end = array.length - 1;
        int position = (start + end) / 2;

        while (array[position] + array[firstIndex] + array[secondIndex] != sum && start <= end) {
            if (array[position] + array[firstIndex] + array[secondIndex] > sum) {
                end = position - 1;
            } else {
                start = position + 1;
            }
            position = (start + end) / 2;
        }

        if(position <= firstIndex || position <= secondIndex ){
            return -1;
        }

        if (start <= end) {
            return position;
        } else {
            return -1;
        }
    }
}
