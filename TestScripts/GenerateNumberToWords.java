import java.util.*;
import java.io.*;

class GenerateNumberToWords {
    public static final String lessThanTen[] = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    public static final String lessThanTwenty[] = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    public static final String lessThanHundred[] = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    
    public static String numberToWords(int num) {
        if(num == 0)
            return "Zero";
        return recursionFunction(num);
    }
    
    public static String recursionFunction(int num) {
        if(num < 10) return lessThanTen[num];
        if(num < 20) return lessThanTwenty[num-10];
        
        String output = new String();
        if(num < 100) output = lessThanHundred[num/10] + "" + recursionFunction(num % 10);
        else if(num < 1000) output = recursionFunction(num/100) + "Hundred" + recursionFunction(num % 100);
        else if(num < 1000000) output = recursionFunction(num/1000) + "Thousand" + recursionFunction(num % 1000);
        else if(num < 1000000000) output = recursionFunction(num/1000000) + "Million" + recursionFunction(num % 1000000);
        else output = recursionFunction(num/1000000000) + "Billion" + recursionFunction(num % 1000000000);
        
        //System.out.println(output);
        return output.trim();
    }

    public static void main(String args[])  throws IOException{
        //generateNGetsAndPuts(Integer.parseInt(args[0]), args[1]);
        //generateNGetsAndPutsSmaller(Integer.parseInt(args[0]), args[1]);
        generateHotKeyDistribution(Integer.parseInt(args[0]), args[1]);
    }
    public static void generateNGetsAndPutsSmaller(int n, String fileName) throws IOException {
        HashMap<String, String> dict = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            dict.put(numberToWords(i), String.valueOf(i));
        }
        List<String> getKeys = new ArrayList<>();
        for(int i = 0; i < n; i++)
            getKeys.add("");

        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("I" + "\n");
        for(Map.Entry<String, String> entry: dict.entrySet()) {
            printWriter.print("P," + entry.getKey() + "," + entry.getValue() + "\n");
        }
        printWriter.print("S\n");
        Object[] values = dict.keySet().toArray();
        printWriter.print("I" + "\n");
        for(int i = 0; i < n; i++) {
            printWriter.print("G," + values[generateRandomInt(10)] + "\n");
        }
        printWriter.print("S\n");
        printWriter.close();
    }

    public static void generateNGetsAndPuts(int n, String fileName) throws IOException {
        HashMap<String, String> dict = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            dict.put(numberToWords(i), String.valueOf(i));
        }
        List<String> getKeys = new ArrayList<>();
        for(int i = 0; i < n; i++)
            getKeys.add("");

        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        int serverIndex = 0;
        for(Map.Entry<String, String> entry: dict.entrySet()) {
            printWriter.print("K," + serverIndex + "\n");
            printWriter.print("P," + entry.getKey() + "," + entry.getValue() + "\n");
            printWriter.print("S\n");
            serverIndex++;
            if(serverIndex >= 10)
                serverIndex = 0;
        }
        Object[] values = dict.keySet().toArray();
        for(int i = 0; i < n; i++) {
            printWriter.print("K," + serverIndex + "\n");
            printWriter.print("G," + values[generateRandomInt(100)] + "\n");
            printWriter.print("S\n");
            serverIndex++;
            if(serverIndex >= 10)
                serverIndex = 0;
        }
        printWriter.close();
    }

    public static int generateRandomInt(int upperRange){
        Random random = new Random();
        return random.nextInt(upperRange);
    }

    public static void generateHotKeyDistribution(int bound, String fileName) throws IOException {
        int limit = 100;
        System.out.println(bound);
        HashMap<String, String> dict = new HashMap<>();
        for(int i = 0; i < limit; i++) {
            dict.put(numberToWords(i), String.valueOf(i));
        }
        for(Map.Entry<String, String> entry: dict.entrySet()) {
            System.out.println("P," + entry.getKey() + "," + entry.getValue());
        } 

        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        List<String> result = new ArrayList<>();
        for(int i = 0; i < bound; i++)
            result.add("");
        List<String> hotKeys = new ArrayList<>();
        List<String> nonHotKeys = new ArrayList<>();
        boolean visited[] = new boolean[bound];

        int numberOfHotKeys = (int)(0.1 * limit);
        int traffic = (int)(0.9 * bound);
        for(Map.Entry<String, String> entry: dict.entrySet()) {
            if(numberOfHotKeys != 0) {
                hotKeys.add(entry.getKey());
                numberOfHotKeys--;
            } else {
                nonHotKeys.add(entry.getKey());
            }
        }

        System.out.println(hotKeys);

        printWriter.print("I\n");
        for(Map.Entry<String, String> entry: dict.entrySet()) {
            printWriter.print("P," + entry.getKey() + "," + entry.getValue() + "\n");
        }
        printWriter.print("S\n");

        while(traffic != 0) {
            int index = generateRandomInt(bound);
            while(visited[index] == true) {
                index++;
                if(index >= bound)
                    index = 0;
            }
            //System.out.println(index);
            int hotKeyIndex = generateRandomInt(hotKeys.size()-1);
            System.out.println(index + " : " + hotKeys.get(hotKeyIndex));
            result.set(index, hotKeys.get(hotKeyIndex));
            visited[index] = true;
            traffic--;
            System.out.println(result);
        }
        System.out.println(result);
        for(int i = 0; i < visited.length; i++)
            System.out.println(visited[i]);
        int index = 0;
        printWriter.print("I\n");
        System.out.println("Size of resutl : " + result.size());
        System.out.println(result);
        for(int i = 0; i < bound; i++) {
            System.out.println(result.get(i));
            if(result.get(i).equals("")) {
                System.out.println("Empty " + i);
                System.out.println(nonHotKeys.get(index));
                System.out.println(result);
                result.set(i, nonHotKeys.get(index));
                System.out.println(result);
                index++;
                if(index >= nonHotKeys.size())
                    index = 0;
            }
        }

        for(String key: result) {
            if(!key.equals(""))
                printWriter.print("G," + key + "\n");
        }
        printWriter.print("S\n");

        /*
        String []opns = {"G","P"};
        boolean isStart = true;
        printWriter.print("I\n");
        int index = 0;
        for(Map.Entry<String, String> entry: dict.entrySet()) {
            String opn = opns[generateRandomInt(2)];
            if(opn.equals("S") && isStart != true) {
                opn = "I";
            }
            if(opn.equals("I"))
                isStart = true;

            switch(opn) {
                case "I":
                    printWriter.print("I" + "\n");
                    break;
                case "S":
                    printWriter.print("S" + "\n");
                    break;
                case "G":
                    printWriter.print("G," + entry.getKey() + "\n");
                    break;
                case "P":
                    printWriter.print("P," + entry.getKey() + "," + entry.getValue() + "\n");
                    break;
                default:
                    break;
            }
            index++;
            if(index%15 == 0) {
                printWriter.print("S" + "\n");
                printWriter.print("I" + "\n");
            }
                
        }
        */
//        printWriter.print("S" + "\n");
       printWriter.close();
//    }
    }
}

