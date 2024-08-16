//package com.aiyangniu.demo.test;
//
///**
// * 计算器（自定义栈，不含小括号）
// * 输入中缀表达式，得出结果
// * 有问题......待优化
// *
// * @author lzq
// * @date 2024/08/12
// */
//public class Calculator4 {
//
//    public static void main(String[] args) {
//        String expression = "10-2*3+3";
//        // 创建两个栈，一个数栈，一个符号栈
//        CalcStack4 numStack = new CalcStack4(10);
//        CalcStack4 operatorStack = new CalcStack4(10);
//        // 定义需要的相关变量，index用于扫描
//        int index = 0;
//        double num1 = 0;
//        double num2 = 0;
//        double operator = 0;
//        double res = 0;
//        // 将每次扫描得到char保存到ch
//        char ch = ' ';
//        // 用于拼接多位数
//        String keepNum = "";
//        do {
//            // 循环扫描expression，依次得到每个字符
//            ch = expression.substring(index, index + 1).charAt(0);
//            // 判断ch，如果是运算符
//            if (operatorStack.isOperator(ch)) {
//                // 判断当前的符号栈是否为空
//                if (!operatorStack.isEmpty()) {
//                    // 如果符号栈有操作符，就进行比较，如果当前的操作符的优先级小于或者等于栈中的操作符，就需要从数栈中pop出两个数，再从符号栈中pop出一个符号，进行运算，将得到结果，入数栈，然后将当前的操作符入符号栈
//                    if (operatorStack.priority(ch) <= operatorStack.priority(operatorStack.peek())) {
//                        num1 = numStack.pop();
//                        num2 = numStack.pop();
//                        operator = operatorStack.pop();
//                        res = numStack.cal(num1, num2, operator);
//                        // 把运算的结果入数栈
//                        numStack.push(res);
//                        // 然后将当前的操作符入符号栈
//                        operatorStack.push(ch);
//                    } else {
//                        // 如果当前的操作符的优先级大于栈中的操作符， 就直接入符号栈
//                        operatorStack.push(ch);
//                    }
//                } else {
//                    // 如果为空直接入符号栈
//                    operatorStack.push(ch);
//                }
//                // 如果是数，则直接入数栈
//            } else {
//                // 分析思路
//                // 1、当处理多位数时，不能发现是一个数就立即入栈，因为他可能是多位数
//                // 2、在处理数，需要向expression的表达式的index后再看一位，如果是数就进行扫描，如果是符号才入栈
//                // 3、因此我们需要定义一个变量字符串，用于拼接
//                keepNum += ch;
//                // 如果ch已经是expression的最后一位，就直接入栈
//                if (index == expression.length() - 1) {
//                    numStack.push(Double.parseDouble(keepNum));
//                } else {
//                    // 判断下一个字符是不是数字，如果是数字，就继续扫描，如果是运算符，则入栈
//                    // 注意是看后一位，不是index++
//                    if (operatorStack.isOperator(expression.substring(index + 1, index + 2).charAt(0))) {
//                        // 如果后一位是运算符，则入栈
//                        numStack.push(Double.parseDouble(keepNum));
//                        // 注意将keepNum清空
//                        keepNum = "";
//                    }
//                }
//            }
//            // 让index+1, 并判断是否扫描到expression最后
//            index++;
//        } while (index < expression.length());
//        // 当表达式扫描完毕，就顺序的从数栈和符号栈中pop出相应的数和符号，并运行
//        while (!operatorStack.isEmpty()) {
//            // 如果符号栈为空，则计算到最后的结果，数栈中只有一个数字【结果】
//            num1 = numStack.pop();
//            num2 = numStack.pop();
//            operator = operatorStack.pop();
//            res = numStack.cal(num1, num2, operator);
//            // 入栈
//            numStack.push(res);
//        }
//        // 将数栈的最后数，pop出，就是结果
//        res = numStack.pop();
//        System.out.printf("表达式：%s\n结果：%.2f\n", expression, res);
//    }
//}
//
///**
// * 创建一个栈
// *
// * @author lzq
// * @date 2024/08/12
// */
//class CalcStack4 {
//
//    /** 栈的大小 **/
//    private int maxSize;
//    /** 数组，数组模拟栈，数据就放在该数组 **/
//    private double[] stack;
//    /** top表示栈顶，初始化为-1 **/
//    private int top = -1;
//
//    public CalcStack4(int maxSize) {
//        this.maxSize = maxSize;
//        stack = new double[this.maxSize];
//    }
//
//    public double peek() {
//        return stack[top];
//    }
//
//    public boolean isFull() {
//        return top == maxSize - 1;
//    }
//
//    public boolean isEmpty() {
//        return top == -1;
//    }
//
//    public void push(double value) {
//        if (isFull()) {
//            System.out.println("栈满！");
//            return;
//        }
//        top++;
//        stack[top] = value;
//    }
//
//    public double pop() {
//        if (isEmpty()) {
//            throw new RuntimeException("栈空，没有数据！");
//        }
//        double value = stack[top];
//        top--;
//        return value;
//    }
//
//    public int priority(double operator) {
//        if (operator == '*' || operator == '/') {
//            return 1;
//        } else if (operator == '+' || operator == '-') {
//            return 0;
//        } else {
//            // 假定目前的表达式只有 +, - , * , /
//            return -1;
//        }
//    }
//
//    public boolean isOperator(char val) {
//        return val == '+' || val == '-' || val == '*' || val == '/';
//    }
//
//    public double cal(double num1, double num2, double operator) {
//        double res = 0;
//        switch ((int) operator) {
//            case '+':
//                res = num1 + num2;
//                break;
//            case '-':
//                res = num2 - num1;
//                break;
//            case '*':
//                res = num1 * num2;
//                break;
//            case '/':
//                res = num2 / num1;
//                break;
//            default:
//                break;
//        }
//        return res;
//    }
//}
