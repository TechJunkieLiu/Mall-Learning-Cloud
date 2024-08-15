package com.aiyangniu.demo.test;

import java.util.Arrays;
import java.util.Stack;

/**
 * 计算器（Stack栈，含小括号，含小数点）
 * 输入中缀表达式，转为后缀表达式，计算得出结果
 *
 * @author lzq
 * @date 2024/08/13
 */
public class Calculator1 {

    public static void main(String[] args) {
        String infix = "3-25.36/3+2.77*3.88-(2+4.99)";
        Calculator1 calculator1 = new Calculator1();
        String[] suffix = calculator1.infix2Suffix(infix);
        System.out.println("后缀List：" + Arrays.toString(suffix));
        double result = calculator1.cal(suffix);
        System.out.println("结果：" + result);
    }

    /**
     * 中缀表达式转后缀表达式
     */
    public String[] infix2Suffix(String infix) {
        // 用于拼接多位数
        String keepNum = "";
        // 符号栈，用于存储运算符
        Stack<String> operatorStack = new Stack<>();
        // 后缀表达式，为了将多位数存储为独立的字符串
        Stack<String> suffixQueue = new Stack<>();

        // 遍历中缀表达式
        for (int i = 0; i < infix.length(); i++) {
            // 数字直接加入后缀表达式，符号需要操作处理
            if ("1234567890.".indexOf(infix.charAt(i)) >= 0) {
                // 判断并记录多位操作数（注意每次开始时都要清空）
                keepNum = "";
                for (; i < infix.length() && "0123456789.".indexOf(infix.charAt(i)) >= 0; i++) {
                    keepNum += infix.charAt(i);
                }
                // 避免跳过对非数字字符的处理
                i--;
                suffixQueue.push(keepNum);
                // 左括号入栈
            } else if ("(".indexOf(infix.charAt(i)) >= 0) {
                operatorStack.push(String.valueOf(infix.charAt(i)));
                // 右括号，栈顶元素循环出栈，直到遇到左括号为止
            } else if (")".indexOf(infix.charAt(i)) >= 0) {
                while (!"(".equals(operatorStack.peek())) {
                    suffixQueue.push(operatorStack.pop());
                }
                // 删除左括号
                operatorStack.pop();
                // 运算符
            } else if ("*%/+-".indexOf(infix.charAt(i)) >= 0) {
                // 若栈不为空或栈顶元素不为左括号
                if (!operatorStack.empty() && !"(".contains(operatorStack.peek())) {
                    // 当栈顶元素为高优先级或同级运算符时，让栈顶元素出栈进入后缀表达式后，直到符合规则后，当前运算符再入栈
                    boolean rule = ("*%/+-".contains(operatorStack.peek()) && "+-".indexOf(infix.charAt(i)) >= 0) || ("*%/".contains(operatorStack.peek()) && "*%/".indexOf(infix.charAt(i)) >= 0);
                    while (!operatorStack.empty() && rule) {
                        // 返回栈顶的元素但不移除
                        suffixQueue.push(operatorStack.peek());
                        operatorStack.pop();
                    }
                }
                operatorStack.push(String.valueOf(infix.charAt(i)));
            }
        }
        // 遍历结束后将栈中剩余元素依次出栈进入后缀表达式
        while (!operatorStack.empty()) {
            suffixQueue.push(operatorStack.pop());
        }
        // 将后缀表达式栈转换为字符串数组格式
        String[] suffix = new String[suffixQueue.size()];
        for (int i = suffixQueue.size() - 1; i >= 0; i--) {
            suffix[i] = suffixQueue.pop();
        }
        return suffix;
    }

    public double cal(String[] suffix) {
        // 顺序存储的栈
        Stack<String> result = new Stack<>();
        int i;
        for (i = 0; i < suffix.length; i++) {
            // 数字直接入栈，运算符字符将栈顶两个元素出栈计算并将结果返回栈顶
            if ("1234567890.".indexOf(suffix[i].charAt(0)) >= 0) {
                result.push(suffix[i]);
            } else {
                double x, y, n = 0;
                // 顺序出栈两个数字字符串，并转换为double类型
                x = Double.parseDouble(result.pop());
                y = Double.parseDouble(result.pop());
                switch (suffix[i]) {
                    case "*":
                        n = y * x;
                        break;
                    case "/":
                        if (x == 0) {
                            return 000000;
                        } else {
                            n = y / x;
                        }
                        break;
                    case "%":
                        if (x == 0) {
                            return 000000;
                        } else {
                            n = y % x;
                        }
                        break;
                    case "-":
                        n = y - x;
                        break;
                    case "+":
                        n = y + x;
                        break;
                    default:
                        throw new RuntimeException("运算符有误！");
                }
                // 将运算结果重新入栈
                result.push(String.valueOf(n));
            }
        }
        return Double.parseDouble(result.peek());
    }
}
