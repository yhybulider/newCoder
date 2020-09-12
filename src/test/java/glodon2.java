import java.util.Scanner;

public class glodon2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int  n = sc.nextInt();
        int num = sc.nextInt();
        double[] dongzuo = new double[n];
        int[] value = new int[n];
        for (int i = 0; i < n; i++) {
            dongzuo[i] = sc.nextDouble();
            value[i] = sc.nextInt();
        }

        double[] dp = new double[num+1];

        for (int i = 0; i < n; i++) {
            for (int j = num; j >= 0 ; j--) {
                if (j -dongzuo[i] > 0){
                    dp[j] = Math.max(dp[j],dp[(int) (j-dongzuo[i])]+value[i]);
                }
            }

        }
        System.out.println((int)dp[num]);

    }
}
