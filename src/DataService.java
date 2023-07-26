import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public interface DataService extends Remote {
    Map<String, String> getData() throws RemoteException;

    String generateText(int length) throws RemoteException;

    void setData(String data) throws RemoteException;
}


class DataServiceImpl extends UnicastRemoteObject implements DataService {
    private final Map<String, String> dictionary = new HashMap<>();
    private final Utilities utils = new Utilities();

    public DataServiceImpl() throws RemoteException {
        super();
    }

    public Map<String, String> getData() throws RemoteException {
        return dictionary;
    }

    public String generateText(int length) throws RemoteException {
        return utils.generateText(length);
    }

    public void setData(String data) throws RemoteException {
        long startTime = System.nanoTime();
        sequentialTime(data);
        long endTime = System.nanoTime();
        long seqDuration = (endTime - startTime);
        System.out.println("sequentialTime Execution Time: " + seqDuration + " ns");
        dictionary.put("sequentialTime", String.valueOf(seqDuration));

        startTime = System.nanoTime();
        threaded(data);
        endTime = System.nanoTime();
        long tDuration = (endTime - startTime);  // in nanoseconds
//        print the execution time in seconds
        System.out.println("Threaded Execution Time: " + seqDuration + " ns");
        dictionary.put("threadedTime", String.valueOf(tDuration));
//        calculate the speedup
        double speedup = (double) seqDuration / tDuration;
        System.out.println("Speedup: " + speedup);
        dictionary.put("speedup", String.valueOf(speedup));
    }

    public void sequentialTime(String data) {
        Utilities utils = new Utilities();

        dictionary.put("letterCount", String.valueOf(utils.count(data)));
        dictionary.put("longestWord", utils.longest(data));
        dictionary.put("shortestWord", utils.shortest(data));
        dictionary.put("repeatedWord", utils.repeatedWords(data));
        dictionary.put("frequency", utils.repeat(data).toString());

    }

    public void threaded(String data) {
        Utilities utils = new Utilities();
//        create 5 threads make each one call one of the methods in the utilities class and put the result in the dictionary
//        then wait for all the threads to finish and print the execution time in seconds
        Thread t1 = new Thread(() -> {
            dictionary.put("letterCount", String.valueOf(utils.count(data)));
        });
        Thread t2 = new Thread(() -> {
            dictionary.put("longestWord", utils.longest(data));
        });
        Thread t3 = new Thread(() -> {
            dictionary.put("shortestWord", utils.shortest(data));
        });
        Thread t4 = new Thread(() -> {
            dictionary.put("repeatedWord", utils.repeatedWords(data));
        });
        Thread t5 = new Thread(() -> {
            dictionary.put("frequency", utils.repeat(data).toString());
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}


class Server {
    public static void main(String[] args) {
        try {
            DataServiceImpl obj = new DataServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Data", obj);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}


class Utilities {
    // 1- Count : for the number of letters.
    public int count(String data) {
        return data.replaceAll("\\s", "").length();
    }

    // 2- repeatedWords : for the words repeated more than once.
    // you can use the map result from the repeat method to get the repeated words.
    // where the key is the word and the value is the number of times it appeared.
    public String repeatedWords(String data) {
        Map<String, Integer> repeated = repeat(data);
        StringBuilder repeatedWords = new StringBuilder();
        for (Map.Entry<String, Integer> entry : repeated.entrySet()) {
            if (entry.getValue() > 1) {
                repeatedWords.append(entry.getKey()).append(" ");
            }
        }
        return repeatedWords.toString();
    }

    // 3- longest: for the longest word.
    public String longest(String data) {
        String[] words = data.split("\\s+");
        String longest = "";
        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }
        return longest;
    }

    // 4- shortest: for the shortest word.
    public String shortest(String data) {
        String[] words = data.split("\\s+");
        String shortest = words[0];
        for (String word : words) {
            if (word.length() < shortest.length()) {
                shortest = word;
            }
        }
        return shortest;

    }

    // 5- Repeat: for the number of times each word appeared.*/
    public Map<String, Integer> repeat(String data) {
        Map<String, Integer> repeated = new HashMap<>();
        String[] words = data.split("\\s+");
        for (String word : words) {
            if (repeated.containsKey(word)) {
                repeated.put(word, repeated.get(word) + 1);
            } else {
                repeated.put(word, 1);
            }
        }
        return repeated;
    }

    public String generateText(int length) {
        String[] texts = {
                "RMI, or Remote Method Invocation, is a Java-based programming technology that enables communication between distributed systems. It allows objects in one Java Virtual Machine (JVM) to invoke methods on objects in another JVM, even if they are running on different machines. This enables developers to build distributed applications that can be easily scaled, and that can take advantage of the processing power and resources available across multiple machines.\n" +
                        "\n" +
                        "RMI works by serializing objects and their data, transmitting them across the network, and then deserializing them on the receiving end. This allows objects to be passed as parameters in method calls, and for return values to be returned from the remote method invocation. RMI also provides transparent access to remote objects, so that calling a remote method is as easy as calling a local method.\n" +
                        "\n" +
                        "One of the main advantages of RMI is that it allows developers to build distributed applications without having to worry about the complexities of network programming. RMI takes care of the low-level details of network communication, such as socket handling, data marshaling and unmarshaling, and error handling. This makes it easier for developers to focus on building the core functionality of their applications, rather than spending time on network programming.\n" +
                        "RMI also provides a number of features that make it well-suited for building distributed systems. For example, it supports distributed garbage collection, which allows remote objects to be automatically removed from memory when they are no longer needed. It also supports the Java Naming and Directory Interface (JNDI), which allows remote objects to be located and accessed by name.\n" +
                        "However, RMI does have some limitations. For example, it is specific to Java, which means that it cannot be used to build distributed applications in other programming languages.\n" +
                        "It also requires a network connection between the client "
                ,
                "GUI, or Graphical User Interface, refers to the visual components of an application that allow users to interact with it. GUIs provide a graphical representation of an application's functionality, allowing users to perform tasks through visual elements such as windows, menus, buttons, and icons.\n" +
                        "\n" +
                        "The primary advantage of a GUI is its ease of use. By presenting information and functionality in a visual way, GUIs make it easier for users to navigate an application and complete tasks. Instead of having to memorize commands or type in text-based commands, users can simply click on buttons and select options from menus.\n" +
                        "\n" +
                        "Another advantage of GUIs is that they can be customized to suit the needs of different users. GUIs can be designed to be simple and straightforward for novice users, while providing more advanced options for experienced users. GUIs can also be customized to match the look and feel of an organization's branding, helping to reinforce brand recognition.\n" +
                        "\n" +
                        "GUIs are used in a wide range of applications, from desktop software to web applications and mobile apps. GUI frameworks, such as JavaFX and Qt, provide developers with tools for building GUI-based applications. These frameworks allow developers to create custom UI components, layout interfaces, and handle user input.\n" +
                        "\n" +
                        "However, GUIs also have some disadvantages. One of the main challenges of designing a GUI is ensuring that it is intuitive and easy to use. This requires careful consideration of factors such as user workflows, usability testing, and accessibility.\n" +
                        "\n" +
                        "Another challenge is ensuring that the GUI is responsive and performs well, particularly in applications with complex UIs. This can require optimization techniques such as caching, lazy loading, and asynchronous processing.In conclusion, GUIs are an important component of modern software applications, providing users with an intuitive and easy-to-use interface for interacting with complex functionality. While designing and "
        };
        return texts[(int) (Math.random() * texts.length)];
    }

}