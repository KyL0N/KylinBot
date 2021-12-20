package top.kylinbot.demo.service;

import java.util.Stack;

public class AlgorithmService {

    public AlgorithmService() {

    }

    public int bracketCalculate(String str) {
        Stack<Character> stk = new Stack<>();
        int i = 0, ans = 0;
        while (i < str.length()) {
            Character chr = str.charAt(i);
            if (chr.equals('('))
                stk.push(chr);
            else if (chr.equals(')')) {
                Character c = stk.pop();
                if (c.equals('('))
                    ans++;
                else if (c.equals(')'))
                    ans = ans * 2;
                stk.push(')');
            }
            i++;
        }
        return ans;
    }
}
