//package com.aiyangniu.demo.test;
//
//import java.util.Iterator;
//import java.util.Scanner;
//
///**
// * 计算器（自定义栈，含小括号）
// * 输入中缀表达式，得出结果
// * 有问题......待优化
// *
// * @author lzq
// * @date 2024/08/11
// */
//public class Calculator5 {
//
//    public static void main(String[] args) throws Exception {
//        Scanner input = new Scanner(System.in);
//
//        CalcStack5<Integer> value = new CalcStack5<>();
//        CalcStack5<Character> operation = new CalcStack5<>();
//
//        System.out.print("请输入一个中缀表达式：");
//        // 从键盘输入一个中缀表达式，然后格式化后存储为一个字符串数组
//        String[] expression = formatInput(input.nextLine());
//
//        // 首先，遍历expression数组，同时以下列原则进行操作，然后，对栈内的剩余数字进行运算，直到操作符栈为空
//        for (String s : expression) {
//            // 因为格式化的时候是在非数字符号前后加空格，所以会存在分割出来是空的情况
//            if (s.length() == 0) {
//                continue;
//                // 遇到运算符，将栈内优先级大于等于自己的符号拿出来参与计算
//            } else if (s.charAt(0) == '+' || s.charAt(0) == '-' || s.charAt(0) == '*' || s.charAt(0) == '/') {
//                // 循环直到栈空或者遇到优先级较小的符号
//                while (!operation.isEmpty() && priorityIsBiggerOrTheSame(operation.peek() + "", s)) {
//                    compute(value, operation);
//                }
//                // 当前符号入栈
//                operation.push(s.charAt(0));
//                // 右括号入栈
//            } else if (s.charAt(0) == '(') {
//                operation.push('(');
//                // 遇到左括号，将栈内符号全部出栈参与运算
//            } else if (s.charAt(0) == ')') {
//                // 循环直到遇到左括号
//                while (operation.peek() != '(') {
//                    compute(value, operation);
//                }
//                // 左括号出栈
//                operation.pop();
//                // 数字入栈
//            } else {
//                value.push(Integer.parseInt(s));
//            }
//        }
//        // 最后将所有的符号出栈参与运算
//        while (!operation.isEmpty()) {
//            compute(value, operation);
//        }
//        System.out.println("计算结果是：" + value.pop());
//    }
//
//    /**
//     * 提取出操作数栈的前2个数，操作符栈的栈顶操作符，运算
//     */
//    public static void compute(CalcStack5<Integer> value, CalcStack5<Character> operation) {
//        int v1 = value.pop();
//        int v2 = value.pop();
//        char op = operation.pop();
//        switch (op) {
//            case '+':
//                value.push(v2 + v1);
//                break;
//            case '-':
//                value.push(v2 - v1);
//                break;
//            case '*':
//                value.push(v2 * v1);
//                break;
//            case '/':
//                value.push(v2 / v1);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 格式化中缀表达式输入，即在符号前后添加空格，便于后面的分隔操作
//     */
//    public static String[] formatInput(String s) {
//        String temp = "";
//        /*
//         * 提取出表达式中有效的字符（非空格），然后在字符后面统一添上一个空格，方便后面使用静态方法String.split()来
//         * 例如：1 *(2+ 3) /4     ----->     1 * ( 2 + 3 ) / 4
//         * 你也可以直接使用List类来存储提取的有效字符 总之最后的目的就是返回一个数组，其中存储的是有效字符
//         */
//        for (int i = 0; i < s.length(); i++) {
//            char c = s.charAt(i);
//            if (c == '+' || c == '-' || c == '*' || c == '/' ) {
//                temp += " " + c + " ";
//            } else {
//                // 数字不用加空格
//                temp += c;
//            }
//        }
//        // 分割
//        return temp.split(" ");
//    }
//
//    /**
//     * 优先级判断
//     */
//    public static boolean priorityIsBiggerOrTheSame(String a, String b) {
//        String s = "+- */";
//        return (s.indexOf(a) - s.indexOf(b)) >= 2;
//    }
//
//}
//
//class CalcStack5<E> implements Iterable<E> {
//
//    /** 初始大小定为16 **/
//    private final int MAX_SIZE = 16;
//    /** 数据域 **/
//    private E[] data;
//    /** 栈中元素个数 **/
//    private int size = 0;
//
//    /**
//     * 默认无参构造方法
//     */
//    public CalcStack5() {
//        data = (E[]) new Object[MAX_SIZE];
//    }
//
//    /**
//     * 含参构造方法，实现栈的初始大小自定义
//     */
//    public CalcStack5(int cap) {
//        data = (E[]) new Object[cap];
//    }
//
//    /**
//     * 入栈
//     */
//    public void push(E e) {
//        ensureCapcity();
//        data[size++] = e;
//    }
//
//    /**
//     * 出栈
//     */
//    public E pop() {
//        E top = data[size - 1];
//        data[size - 1] = null;
//        size--;
//        return top;
//    }
//
//    /**
//     * 获得栈顶元素
//     */
//    public E peek() throws Exception {
//        if (isEmpty()) {
//            throw new Exception("The stack is Empty!");
//        } else {
//            return data[size - 1];
//        }
//    }
//
//    /**
//     * 实现栈的大小不够时自动增加
//     */
//    public void ensureCapcity() {
//        if (size == data.length) {
//            E[] temp = (E[]) new Object[data.length * 2];
//            for (int i = 0; i < data.length; i++) {
//                temp[i] = data[i];
//            }
//            data = temp;
//        }
//    }
//
//    /**
//     * 判空
//     */
//    public boolean isEmpty() {
//        return size == 0;
//    }
//
//    @Override
//    public String toString() {
//        String result = "data: ";
//        for (int i = 0; i < size; i++) {
//            result += data[i] + " ";
//        }
//        return result;
//    }
//
//    /**
//     * 继承接口Iterable后需要重写的方法，返回一个迭代器
//     */
//    @Override
//    public Iterator<E> iterator() {
//        return new ReverseIterator();
//    }
//
//    /**
//     * 内部类迭代器，从栈顶到栈底访问数据
//     */
//    private class ReverseIterator implements Iterator<E> {
//
//        /** current表示当前下标，倒序遍历，所以current初始化为size-1 **/
//        private int current = size - 1;
//
//        @Override
//        public boolean hasNext() {
//            // 倒序遍历，为了实现foreach，这里应该是>=0
//            return current >= 0;
//        }
//
//        @Override
//        public E next() {
//            return data[current--];
//        }
//    }
//}
//
