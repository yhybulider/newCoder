import java.util.Scanner;
import java.util.TreeSet;

public class glodon3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
         long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextLong();
        }
        // 自定义排序
        TreeSet<Long> ts = new TreeSet<>((a,b) -> (int) (b-a));
        for (long num : arr){
            boolean flag = ts.add(num);
            // 循环处理多个相同数字
            while (!flag){
                //说明存在
                ts.remove(num);
                num = num *2;
                flag = ts.add(num);//重新放置
            }
        }
        StringBuilder sb = new StringBuilder();
        for (long num : ts){
            sb.append(num+" ");
        }
        System.out.println(sb);

    }
}
