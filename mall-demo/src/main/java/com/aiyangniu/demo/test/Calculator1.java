package com.aiyangniu.demo.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 计算器（Stack栈，含小括号，含小数点）
 * 输入中缀表达式，转为对应中缀List，转为对应后缀List，计算得出结果
 *
 * @author lzq
 * @date 2024/08/13
 */
public class Calculator1 {

    public static void main(String[] args) {
        Calculator1 calculator1 = new Calculator1();
        List<String> infixList = calculator1.toInfixList("100-25.36/3+2.77*3.88-(2+4.99)");
        System.out.println("中缀List：" + infixList);
        List<String> suffixList = calculator1.infix2SuffixList(infixList);
        System.out.println("后缀List：" + suffixList);
        double result = calculator1.calculate(suffixList);
        System.out.println("结果：" + result);
    }

    /**
     * 中缀表达式 => 对应List
     */
    public List<String> toInfixList(String s) {
        List<String> infixList = new ArrayList<>();
        // 指针用于遍历中缀表达式字符串
        int i = 0;
        // 对多位数的拼接
        String str;
        // 每遍历到一个字符，就放入到c
        char c;
        do {
            // 如果c是一个非数字（除小数点），则加入到infixList
            if (((c = s.charAt(i)) < 48 || (c = s.charAt(i)) > 57) && (c = s.charAt(i)) != 46) {
                infixList.add("" + c);
                // i需要后移
                i++;
                // 如果是一个数（包括小数点），需要考虑多位数（'0'[48]->'9'[57]）或（'.'[46]）
            } else {
                str = "";
                while (i < s.length() && ((c = s.charAt(i)) >= 48 || (c = s.charAt(i)) == 46) && (c = s.charAt(i)) <= 57) {
                    // 拼接
                    str += c;
                    i++;
                }
                infixList.add(str);
            }
        } while (i < s.length());
        return infixList;
    }

    /**
     * 中缀表达式对应List => 后缀表达式对应List
     */
    public List<String> infix2SuffixList(List<String> infixList) {
        Stack<String> operatorStack = new Stack<>();
        // 因为tempList这个栈在整个转换过程中，没有pop操作，而且后面还需要逆序输出，所以用List
        List<String> tempList = new ArrayList<>();
        for (String infix : infixList) {
            // 如果是一个数（整数+小数），加入tempList
            if (infix.matches("^[0-9]+(\\.[0-9]+)?$")) {
                tempList.add(infix);
                // 如果是 (，则直接入栈
            } else if (infix.equals("(")) {
                operatorStack.push(infix);
            } else if (infix.equals(")")) {
                // 如果是 )，则依次弹出栈顶的运算符，并将括号内的值算出，压入tempList直到遇到左括号为止，此时将这一对括号丢弃
                while (!operatorStack.empty() && !operatorStack.peek().equals("(")) {
                    tempList.add(operatorStack.pop());
                }
                // 将 ( 弹出栈，消除小括号
                operatorStack.pop();
            } else {
                // 否则比较当前运算符和栈顶运算符优先级
                // 当infix的优先级小于等于栈顶运算符
                // 将栈顶的运算符弹出并加入到tempList中，再次与新的栈顶运算符相比较
                while (operatorStack.size() != 0 && priority(operatorStack.peek()) >= priority(infix)) {
                    tempList.add(operatorStack.pop());
                }
                // 还需要将infix压入栈
                operatorStack.push(infix);
            }
        }
        // 将剩余的运算符依次弹出并加入tempList
        while (operatorStack.size() != 0) {
            tempList.add(operatorStack.pop());
        }
        // 注意因为是存放到List，因此按顺序输出就是对应的后缀表达式对应的List
        return tempList;
    }

    public double calculate(List<String> suffixList) {
        Stack<String> result = new Stack<>();
        for (String suffix : suffixList) {
            // 使用正则表达式来取出数，匹配的是多位数（整数+小数）
            if (suffix.matches("^[0-9]+(\\.[0-9]+)?$")) {
                // 入栈
                result.push(suffix);
            } else {
                // pop出两个数，并运算，再入栈
                double num2 = Double.parseDouble(result.pop());
                double num1 = Double.parseDouble(result.pop());
                double res = 0;
                switch (suffix) {
                    case "+":
                        res = num1 + num2;
                        break;
                    case "-":
                        res = num1 - num2;
                        break;
                    case "*":
                        res = num1 * num2;
                        break;
                    case "/":
                        res = num1 / num2;
                        break;
                    default:
                        throw new RuntimeException("运算符有误！");
                }
                // 入栈
                result.push("" + res);
            }
        }
        // 最后留在栈中的数据是运算结果
        return Double.parseDouble(result.pop());
    }

    public int priority(String operator) {
        if ("*".equals(operator) || "/".equals(operator)) {
            return 1;
        } else if ("+".equals(operator) || "-".equals(operator)) {
            return 0;
        } else {
            // 假定目前的表达式只有 + - * /
            return -1;
        }
    }
}
