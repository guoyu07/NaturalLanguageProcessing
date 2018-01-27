import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ViterbiForward {

	public static void main(String[] args) {

		
		String s="331123312";
		if (args.length > 0) {
			s = args[0];
		}
		
		int T = s.length();
		int[] obs = new int[s.length() + 1];
         //observations
		for (int i = 1; i < obs.length; i++) {
			obs[i] = Character.getNumericValue(s.charAt(i - 1));

		}
		
		// b is emission probabilities
		float[][] b = new float[][] { { 0, 0, 0, 0 }, { 0, 0.2f, 0.4f, 0.4f }, { 0, 0.5f, 0.4f, 0.1f } };

		// a is transition probabilities 
		float[][] a = new float[][] { { 0, 0.8f, 0.2f, 0 }, { 0, 0.7f, 0.3f, 0 }, { 0, 0.4f, 0.6f, 0 },
				{ 0, 0, 0, 0 } };
				viterbi(obs, a, b, T, 2);
	

	
	}
	private static float max(float[][] v, int N, float[][] a, int t, float[][] b, int s, int[] o) {

		float max = Float.MIN_VALUE;

		
		
		for (int s1 = 1; s1 <= N; s1++) {
			float check = v[s1][t - 1] * a[s1][s] * b[s][o[t]];

			if (max < check) {
				max = check;

			}

		}

		return max;
	}

	//Return the max state  
	private static float argmax(float[][] v, int N, float[][] a, int t, int s) {

		float max = Float.MIN_VALUE;
		int maxstate = 1;
		for (int s1 = 1; s1 <= N; s1++) {
			float check = v[s1][t - 1] * a[s1][s];
			if (max < check) {
				max = check;
				maxstate = s1;
			}

		}
		return maxstate;
	}

	//Return the max final state
	private static float argmaxFinal(float[][] v, int N, float[][] a, int T) {
		int maxstate = 1;
		float max = Float.MIN_VALUE;
		for (int s = 1; s <= N; s++) {
			float check = v[s][T] * a[s][a.length - 1];
			if (max < check) {
				max = check;
				maxstate = s;

			}

		}
		return maxstate;
	}

	private static float maxFinal(float[][] v, int N, float[][] a, int T) {

		float max = Float.MIN_VALUE;

		float sum = 0f;
		for (int s = 1; s <= N; s++) {
			float check = v[s][T] * a[s][a.length - 1];
			sum = sum + v[s][T] * a[s][a.length - 1];
			if (max < check) {
				max = check;

			}

		}
		return max;
	}
	
	
	//sum(v, N, a, t, b, s, o);
	static float sum(float f[][],int N,float [][] a,int t, float[][] b,int s, int [] o){
		
		
        float sum = 0;

		
		
		for (int s1 = 1; s1 <= N; s1++) {
		  sum =sum+ f[s1][t - 1] * a[s1][s] * b[s][o[t]];


		}

		return sum;
	}

	// T is observations
	// a- state transitions
	// N - no of states
	static void viterbi(int[] o, float[][] a, float[][] b, int T, int N) {

		
		
		
		//forward[s,t]← ∑ forward[s′,t −1] ∗ as′,s ∗ bs(ot) s′ =1
		
		
		
		
		// np-backtrace matrix
		float[][] bp = new float[N + 2][T + 1];
		float v[][] = new float[N + 2][T + 1];
		float f[][] = new float[N + 2][T + 1];
		for (int s = 1; s <= N; s++) {
			v[s][1] = a[0][s] * b[s][o[1]];
			f[s][1]= a[0][s] * b[s][o[1]];
			bp[s][1] = 0;
		}
		for (int t = 2; t <= T; t++) {
			for (int s = 1; s <= N; s++) {
				v[s][t] = max(v, N, a, t, b, s, o);
				f[s][t]=sum(f, N, a, t, b, s, o);
				bp[s][t] = argmax(v, N, a, t, s);

			}

		}

		v[v.length - 1][T] = maxFinal(v, N, a, T);

		bp[bp.length - 1][T] = argmaxFinal(v, N, a, T);

		
		
		System.out.println("ViterbiDecoding matrix :");
		
		for (int i = 1; i < v.length-1; i++) {
			System.out.println(Arrays.toString(v[i]));

			
		}

		System.out.println();
		
		
		for (int i = 1; i < f.length-1; i++) {
			System.out.println(Arrays.toString(f[i]));

			
		}
		System.out.println(f[1][f[0].length-1]+f[f.length-2][f[0].length-1]);
		
		System.out.println("Backtrace Matrix :");
		for (int i = 1; i < bp.length-1; i++) {
			System.out.println(Arrays.toString(bp[i]));

		}

		
		
		// backtrack

		ArrayList<String> sb = new ArrayList<String>();
		float laststate = 0;
		float maxProb=-1;
		for (int j = v[0].length - 1; j < v[0].length; j++) {

			laststate = findmax(v[1][j], v[2][j]);
			if(maxProb==-1){
			maxProb=Math.max(v[1][j], v[2][j]);}

		}
		
		//last state means find the maximum state from viterbi matrix
		if (laststate == 1) {
			sb.add(0, "hot");

		} else {
			sb.add(0, "cold");

		}

		int t1 = T;
		while (t1 > 1) {

			laststate = bp[(int) laststate][t1];
			//maxProb=maxProb*v[(int)laststate][t1];
			if (laststate == 1) {
				sb.add(0, "hot");

			} else if (laststate == 2) {
				sb.add(0, "cold");

			}
			t1--;
		}

		System.out.println();
		System.out.println("Sequence is :");
		System.out.println(sb.toString());
		System.out.println("Probability at final time step is :");
		System.out.println(maxProb);

	}

	static int findmax(float one, float two) {

		if (one > two) {
			return 1;
		} else {
			return 2;
		}
	}

}
