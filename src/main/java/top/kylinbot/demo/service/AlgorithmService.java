package top.kylinbot.demo.service;

import java.util.Stack;

public class AlgorithmService {

    public AlgorithmService() {

    }

    public int bracketCalculate(String str) {
        Stack<Character> stk = new Stack<>();
        Stack<Integer> ansStk = new Stack<>();

        int i = 0, ans = 0;
        while (i < str.length()) {
            Character chr = str.charAt(i);
            if (chr.equals('(')) {
                stk.push(chr);
            } else if (chr.equals(')')) {
                Character c;
                try {
                    c = stk.peek();
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }

                if (c != null && c.equals('(')) {
                    ans++;
                    if (i + 2 < str.length() && str.charAt(i + 2) == '(') {
                        ansStk.push(ans);
                        ans = 0;
                    }
                } else if (c != null && c.equals(')')) {
                    ans = ans * 2;
                }
                stk.push(')');
            }
            i++;
        }
        while (!ansStk.isEmpty()) {
            ans += ansStk.pop();
        }
        return ans;
    }
}
