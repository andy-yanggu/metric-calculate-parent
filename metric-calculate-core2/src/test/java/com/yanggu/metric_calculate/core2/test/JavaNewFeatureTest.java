package com.yanggu.metric_calculate.core2.test;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("高版本语法特性")
//@Disabled("java语法和api学习, 不需要执行")
class JavaNewFeatureTest {

    @Nested
    @DisplayName("Java9")
    class Java9Test {

        @Test
        @DisplayName("钻石操作符支持匿名内部类")
        void test1() {
            // JDK 8 中是new HashMap<String, Integer>
            Map<String, Integer> map = new HashMap<>() {{
                put("One", 1);
                put("Two", 2);
                put("Three", 3);
            }};

            map.forEach((key, value) -> System.out.println(key + ":" + value));
            // JDK 8 中是new ArrayList<String>
            List<String> names = new ArrayList<>() {{
                add("Alice");
                add("Bob");
                add("Charlie");
            }};

            names.forEach(System.out::println);
        }

        @Test
        @DisplayName("takeWhile语法")
        void test2() {
            // takeWhile是遇到第一个不符合的元素时停止，即使后边仍然有满足的元素，并返回前面的
            Stream<Integer> stream1 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 4);
            Stream<Integer> filteredStream1 = stream1.takeWhile(n -> n < 5);
            filteredStream1.forEach(System.out::println);
        }

        @Test
        @DisplayName(" dropWhile语法")
        void test3() {
            // dropWhile是遇到第一个不符合的元素时停止，丢弃前面所有满足的元素，返回后边的元素
            Stream<Integer> stream2 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 4);
            Stream<Integer> filteredStream2 = stream2.dropWhile(n -> n < 5);
            filteredStream2.forEach(System.out::println);
        }

        @Test
        @DisplayName("ofNullable")
        void test4() {
            Stream<String> stream3 = Stream.ofNullable("Hello");
            stream3.forEach(System.out::println);

            Stream<String> stream4 = Stream.ofNullable(null);
            stream4.forEach(System.out::println);
        }

        @Test
        @DisplayName("iterator方法")
        void test5() {
            System.out.println("=====iterator() 方法的重载=====");
            List<String> list = new ArrayList<>();
            list.add("Java");
            list.add("C++");
            list.add("Python");

            Stream<String> stream = StreamSupport.stream(list.spliterator(), false);
            stream.forEach(System.out::println);

            Optional.of("Hello").stream().map(String::toUpperCase).forEach(System.out::println);

        }

        @Test
        @DisplayName("try with resource")
        void test6() throws Exception {
            String string = "test1\r\n" + "test2";
            //java7的写法
            try (BufferedReader reader = new BufferedReader(new CharArrayReader(string.toCharArray()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            //java9的写法
            BufferedReader reader = new BufferedReader(new CharArrayReader(string.toCharArray()));
            BufferedReader reader2 = new BufferedReader(new CharArrayReader(string.toCharArray()));
            //可以不用在try中声明, 在外面声明好了以后, 放入try中即可。
            //如果try中有多个变量需要自动关闭, 变量名使用分号分割
            try (reader; reader2) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    System.out.println(line2);
                }
            }
        }

        @Test
        @DisplayName("集合新增of工厂方法")
        void test7() {
            // 通过 of 方法创建结合，均不可修改，后边均会报错
            List<String> list = List.of("apple", "banana", "orange");
            Set<Integer> set = Set.of(1, 2, 3, 4);
            Map<String, Integer> map = Map.of("one", 1, "two", 2, "three", 3);
            assertThrows(UnsupportedOperationException.class, () -> list.add("123"));
            assertThrows(UnsupportedOperationException.class, () -> set.add(5));
            assertThrows(UnsupportedOperationException.class, () -> map.put("four", 4));
        }

    }

    @Nested
    @DisplayName("Java10")
    class Java10Test {

        @Test
        @DisplayName("copyOf方法")
        void test8() {
            // copyOf方法，用于创建一个不可变集合的副本
            List<String> originalList = new ArrayList<>();
            originalList.add("apple");
            originalList.add("banana");
            originalList.add("orange");

            List<String> copyList = List.copyOf(originalList);
            assertThrows(UnsupportedOperationException.class, () -> copyList.add("orange"));
        }

        @Test
        @DisplayName("var变量")
        void test9() {
            //新增 var 关键字，只能用于局部变量，不能用于方法的参数、方法返回值、类的字段等。
            var number = 10;
            var pi = 3.14;
            System.out.println("number:" + number + ";pi=" + pi);
            System.out.println("=======");
            var message = "Hello, World!";
            var list = new ArrayList<String>();
            list.add("123");
            list.add("234");
            System.out.println("message:" + message);
            list.forEach(System.out::println);
            System.out.println("=======");
            var numbers = List.of(1, 2, 3, 4, 5);
            var map = new HashMap<Integer, String>();
            map.put(123, "123");
            numbers.forEach(System.out::println);
            map.forEach((key, value) -> System.out.println(map.get(key)));
        }

    }

    @Nested
    @DisplayName("Java11")
    class Java11Test {

        @Test
        @DisplayName("新的HttpClient Api")
        void test10() {
            // 创建一个 HttpClient 实例
            HttpClient httpClient = HttpClient.newHttpClient();
            // 创建一个 GET 请求
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.baidu.com")).build();
            // 发送请求并获取响应
            try {
                System.out.println("==========同步请求==========");
                // 同步请求
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                // 检查响应状态码
                int statusCode = response.statusCode();
                System.out.println("Status Code: " + statusCode);
                // 获取响应的内容
                String responseBody = response.body();
                System.out.println("Response Body: " + responseBody);
                System.out.println("==========异步请求==========");
                // 异步请求
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(System.out::println);
                // 因为是异步，需要等一下主线程再停止
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("在 Lambda 表达式中使用 var 进行参数声明")
        void test11() {
            // 使用 var 类型推断的 Lambda 参数
            Consumer<String> printLength = (var s) -> {
                System.out.println("Length of " + s + ": " + s.length());
            };

            // 调用 Lambda 表达式
            printLength.accept("Hello");
            printLength.accept("Java 11");
        }

        @Test
        @DisplayName("Optional 增强，新增了empty()方法")
        void test12() {
            Optional<String> optional = Optional.empty();
            //判断指定的 Optional 对象是否为空
            System.out.println(optional.isEmpty());
        }

        @Test
        @DisplayName("String增强, 添加了一系类实例方法")
        void test13() {
            // isBlank() 方法：isBlank() 方法用于检查字符串是否为空或仅由空白字符组成。它返回一个布尔值，指示字符串是否为空白。
            String str1 = ""; // 空字符串
            String str2 = "   "; // 仅包含空白字符的字符串

            System.out.println(str1.isBlank()); // 输出 true
            System.out.println(str2.isBlank()); // 输出 true
            // strip() 方法：strip() 方法用于去除字符串的首尾空白字符。它返回一个新的字符串，不包含首尾的空白字符。
            String str = "  Hello, World!  ";

            String stripped = str.strip();
            System.out.println(stripped); // 输出 "Hello, World!"
            //stripLeading() 和 stripTrailing() 方法
            // stripLeading() 方法用于去除字符串开头的空白字符
            // stripTrailing() 方法用于去除字符串末尾的空白字符。
            // 它们分别返回一个新的字符串，不包含开头或末尾的空白字符。
            String str3 = "  Hello, World!  ";

            String strippedLeading = str3.stripLeading();
            System.out.println(strippedLeading); // 输出 "Hello, World!  "

            String strippedTrailing = str3.stripTrailing();
            System.out.println(strippedTrailing); // 输出 "  Hello, World!"
            // lines() 方法：lines() 方法用于将字符串拆分为行的流。它返回一个流，其中每个元素都是字符串的一行。
            String str4 = "Hello\nWorld\nJava";

            str4.lines().forEach(System.out::println);
            System.out.println(str4.lines().count());
            str4.lines().collect(Collectors.toList()).forEach(System.out::println);
            // repeat()，用于将字符串重复指定次数
            String str5 = "Hello";
            String repeated = str5.repeat(3);
            System.out.println(repeated); // 输出 "HelloHelloHello"
        }

    }

    @Nested
    @DisplayName("Java12")
    class Java12Test {

        @Test
        @DisplayName("Files方法增强")
        void test14() {
            // 新增的 mismatch 方法比较文件内容
            Path file1 = Path.of("JavaNewFeatureTest.class");
            try {
                boolean contentEquals = Files.mismatch(file1, file1) == -1;
                if (contentEquals) {
                    System.out.println("文件内容相等");
                } else {
                    System.out.println("文件内容不相等");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Test
        @DisplayName("NumberFormat 类引入了一些新的方法来格式化数字。")
        void test15() {
            double number = 12345123.6789D;
            // 创建一个本地化的 NumberFormat 对象
            NumberFormat numberFormat = NumberFormat.getCompactNumberInstance(Locale.CHINESE, NumberFormat.Style.SHORT);
            // 格式化数字
            String formattedNumber = numberFormat.format(number);
            System.out.println("Formatted number: " + formattedNumber);

            NumberFormat numberFormat1 = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
            // 格式化数字
            String formattedNumber1 = numberFormat1.format(number);
            System.out.println("Formatted number1: " + formattedNumber1);

            NumberFormat numberFormat2 = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
            // 格式化数字
            String formattedNumber2 = numberFormat2.format(number);
            System.out.println("Formatted number2: " + formattedNumber2);

            //输出
            //Formatted number: 1235万
            //Formatted number1: 12M
            //Formatted number2: 12 million
        }

        @Test
        @DisplayName("String 增强方法indent，transform")
        void test16() {
            // String.indent(int n)：该方法用于对字符串进行缩进。
            // 它接受一个整数参数 n，表示缩进的级别。正值表示向右缩进，负值表示向左缩进。
            String text1 = "Hello\nWorld";
            String indentedText = text1.indent(4);
            System.out.println(indentedText);
            String text2 = "    Hello\n    World";
            System.out.println(text2);
            String indentedText2 = text2.indent(-4);
            System.out.println(indentedText2);

            // String.transform(Function<? super CharSequence, ? extends R> f)：该方法用于对字符串进行转换。
            // 它接受一个函数参数 f，该函数将输入的 CharSequence（字符串）转换为类型 R。
            // 实际上就是可以传入一个Function作为参数，对 String 的操作可以各种动态变化
            String text3 = "Hello, Java 12";
            String transformedText = text3.transform(s -> s.replace("Java", "Java SE"));
            System.out.println(transformedText);
        }
    }

}
