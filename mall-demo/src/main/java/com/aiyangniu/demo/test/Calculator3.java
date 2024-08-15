package com.aiyangniu.demo.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Stack;

/**
 * 计算器（Stack栈，含小括号，含UI）
 * 继承JFrame框架，实现事件监听器接口
 * 输入中缀表达式，转为后缀表达式，计算得出结果
 *
 * @author lzq
 * @date 2024/08/13
 */
public class Calculator3 extends JFrame implements ActionListener {

    private final String[] KEYS = {"7", "8", "9", "AC", "4", "5", "6", "-", "1", "2", "3", "+", "0", "e", "pi", "/", "sqrt", "%", "x*x", "*", "(", ")", ".", "="};
    private JButton[] keys = new JButton[KEYS.length];
    /** 文本域组件TextArea可容纳多行文本，文本框内容初始值设为0.0 **/
    private JTextArea resultText = new JTextArea("0.0");
    /** 历史记录文本框初始值设为空 **/
    private JTextArea history = new JTextArea();
    private JPanel jp2 = new JPanel();
    /** 给输入显示屏文本域新建一个垂直滚动滑条 **/
    private JScrollPane gdt1 = new JScrollPane(resultText);
    /** 给历史记录文本域新建一个垂直滚动滑条 **/
    private JScrollPane gdt2 = new JScrollPane(history);
    private JLabel label = new JLabel("历史记录");
    /** 计算文本框输入的中缀表达式 **/
    private String input = "";

    /**
     * 构造方法
     */
    public Calculator3() {
        // “超”关键字，表示调用父类的构造函数
        super("Caculator");
        // 设置文本框大小
        resultText.setBounds(20, 18, 255, 115);
        // 文本框内容右对齐
        resultText.setAlignmentX(RIGHT_ALIGNMENT);
        // 文本框不允许修改结果
        resultText.setEditable(false);
        // 设置结果文本框输入文字的字体、类型、大小
        resultText.setFont(new Font("monospaced", Font.PLAIN, 18));
        // 设置历史记录文本框输入文字的字体、类型、大小
        history.setFont(new Font("monospaced", Font.PLAIN, 18));
        // 设置文本框大小
        history.setBounds(290, 40, 250, 370);
        // 文本框内容右对齐
        history.setAlignmentX(LEFT_ALIGNMENT);
        // 文本框不允许修改结果
        history.setEditable(false);
        // 设置标签位置及大小
        label.setBounds(300, 15, 100, 20);
        // 设置面板窗口位置及大小
        jp2.setBounds(290, 40, 250, 370);
        jp2.setLayout(new GridLayout());
        JPanel jp1 = new JPanel();
        // 设置面板窗口位置及大小
        jp1.setBounds(20, 18, 255, 115);
        jp1.setLayout(new GridLayout());
        // 激活自动换行功能
        resultText.setLineWrap(true);
        // 激活断行不断字功能
        resultText.setWrapStyleWord(true);
        resultText.setSelectedTextColor(Color.RED);
        // 自动换行
        history.setLineWrap(true);
        history.setWrapStyleWord(true);
        history.setSelectedTextColor(Color.blue);
        // 使滚动条显示出来
        gdt1.setViewportView(resultText);
        gdt2.setViewportView(history);
        // 设置让垂直滚动条一直显示
        gdt1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // 设置让垂直滚动条一直显示
        gdt2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // 设置让水平滚动条一直显示
        gdt2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        // 将滚动条添加入面板窗口中
        jp1.add(gdt1);
        jp2.add(gdt2);
        // 将面板添加到总窗体中
        this.add(jp1);
        // 将面板添加到总窗体中
        this.add(jp2);
        this.setLayout(null);
        // 新建“历史记录”标签
        this.add(label);

        // 放置按钮 x,y 为按钮的横纵坐标
        int x = 20, y = 150;
        for (int i = 0; i < KEYS.length; i++) {
            keys[i] = new JButton();
            keys[i].setText(KEYS[i]);
            keys[i].setBounds(x, y, 60, 40);
            if (x < 215) {
                x += 65;
            } else {
                x = 20;
                y += 45;
            }
            this.add(keys[i]);
        }

        // 每个按钮都注册事件监听器
        for (int i = 0; i < KEYS.length; i++) {
            keys[i].addActionListener(this);
        }
        this.setResizable(false);
        this.setBounds(500, 200, 567, 480);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Calculator3 calculator3 = new Calculator3();
    }

    /**
     * 事件处理
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // 获得事件源的标签
        String label = e.getActionCommand();
        // 清空按钮，消除显示屏文本框前面所有的输入和结果
        if (Objects.equals(label, "AC"))
        {
            input = "";
            // 更新文本域的显示，显示初始值;
            resultText.setText("0.0");
        } else if (Objects.equals(label, "sqrt")) {
            String n;
            // 加判断，先输入数字再输入开方符号才是合法的
            if(input.isEmpty()) {
                n="error!";
            } else {
                n = String.valueOf(kfys(input));
            }
            // 使运算表达式显示在输入界面
            resultText.setText("sqrt" + "(" + input + ")" + "=" + n);
            // 获取输入界面的运算表达式并使其显示在历史记录文本框
            history.setText(history.getText() + resultText.getText() + "\n");
            input = n;
        } else if (Objects.equals(label, "x*x")) {
            String m;
            if(input.isEmpty()) {
                m="error!";
            } else {
                m = String.valueOf(pfys(input));
            }
            resultText.setText(input + "^2" + "=" + m);
            history.setText(history.getText() + resultText.getText() + "\n");
            input = m;
        } else if (Objects.equals(label, "=")) {
            if (input.isEmpty()) {
                return;
            }
            // 将中缀表达式转换为后缀表达式
            String[] s = infix2Suffix(input);
            // 计算后缀表达式得出最终算式结果
            double result = cal(s);
            resultText.setText(input + "=" + result);
            history.setText(history.getText() + resultText.getText() + "\n");
        } else {
            if (Objects.equals(label, "e")) {
                // 将e的值以字符串的形式传给m
                String m = String.valueOf(2.71828);
                label = m;
            } else if (Objects.equals(label, "pi")) {
                String m = String.valueOf(3.14159);
                label = m;
            }
            input = input + label;
            resultText.setText(input);
        }
    }

    /**
     * 中缀表达式转后缀表达式
     */
    private String[] infix2Suffix(String infix) {
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
                    keepNum = keepNum + infix.charAt(i);
                }
                //避免跳过对非数字字符的处理
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

    /**
     * 开方运算方法
     */
    public double kfys(String str) {
        double a = Double.parseDouble(str);
        return Math.sqrt(a);
    }

    /**
     * 平方运算方法
     */
    public double pfys(String str) {
        double a = Double.parseDouble(str);
        return Math.pow(a, 2);
    }

    public double cal(String[] suffix) {
        // 顺序存储的栈，数据类型为字符串
        Stack<String> result = new Stack<>();
        int i;
        for (i = 0; i < suffix.length; i++) {
            //遇到数字，直接入栈
            if ("1234567890.".indexOf(suffix[i].charAt(0)) >= 0) {
                result.push(suffix[i]);
                // 遇到运算符字符，将栈顶两个元素出栈计算并将结果返回栈顶
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
