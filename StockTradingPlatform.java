import java.util.*;

public class StockTradingPlatform {
    static class Stock {
        String symbol;
        String company;
        double price;

        Stock(String symbol, String company, double price) {
            this.symbol = symbol;
            this.company = company;
            this.price = price;
        }
    }

    static class Transaction {
        String type, symbol;
        int quantity;
        double price;

        Transaction(String type, String symbol, int quantity, double price) {
            this.type = type;
            this.symbol = symbol;
            this.quantity = quantity;
            this.price = price;
        }
    }

    static class User {
        String name;
        double cash;
        HashMap<String, Integer> portfolio = new HashMap<>();
        ArrayList<Transaction> transactions = new ArrayList<>();

        User(String name, double cash) {
            this.name = name;
            this.cash = cash;
        }
    }

    static HashMap<String, Stock> market = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        initializeMarket();

        System.out.print("Enter user name: ");
        User user = new User(sc.nextLine(), 100000);

        int choice;
        do {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. Display market data");
            System.out.println("2. Buy stock");
            System.out.println("3. Sell stock");
            System.out.println("4. View portfolio");
            System.out.println("5. View transactions");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> displayMarket();
                case 2 -> buyStock(sc, user);
                case 3 -> sellStock(sc, user);
                case 4 -> viewPortfolio(user);
                case 5 -> viewTransactions(user);
                case 6 -> System.out.println("Thank you for using the platform.");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 6);

        sc.close();
    }

    static void initializeMarket() {
        market.put("TCS", new Stock("TCS", "Tata Consultancy Services", 3650));
        market.put("INFY", new Stock("INFY", "Infosys", 1450));
        market.put("RELIANCE", new Stock("RELIANCE", "Reliance Industries", 2850));
        market.put("HDFCBANK", new Stock("HDFCBANK", "HDFC Bank", 1650));
    }

    static void displayMarket() {
        System.out.println("\nSymbol       Company                         Price");
        for (Stock s : market.values()) {
            System.out.printf("%-12s %-30s ₹%.2f%n", s.symbol, s.company, s.price);
        }
    }

    static void buyStock(Scanner sc, User user) {
        System.out.print("Enter stock symbol: ");
        String symbol = sc.next().toUpperCase();

        if (!market.containsKey(symbol)) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
        Stock stock = market.get(symbol);
        double cost = quantity * stock.price;

        if (quantity <= 0 || cost > user.cash) {
            System.out.println("Invalid quantity or insufficient cash.");
            return;
        }

        user.cash -= cost;
        user.portfolio.put(symbol, user.portfolio.getOrDefault(symbol, 0) + quantity);
        user.transactions.add(new Transaction("BUY", symbol, quantity, stock.price));
        System.out.println("Purchase completed.");
    }

    static void sellStock(Scanner sc, User user) {
        System.out.print("Enter stock symbol: ");
        String symbol = sc.next().toUpperCase();

        if (!market.containsKey(symbol)) {
            System.out.println("Stock not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
        int owned = user.portfolio.getOrDefault(symbol, 0);

        if (quantity <= 0 || quantity > owned) {
            System.out.println("You do not own enough shares.");
            return;
        }

        Stock stock = market.get(symbol);
        user.cash += quantity * stock.price;
        user.portfolio.put(symbol, owned - quantity);
        user.transactions.add(new Transaction("SELL", symbol, quantity, stock.price));
        System.out.println("Sale completed.");
    }

    static void viewPortfolio(User user) {
        double portfolioValue = 0;
        System.out.println("\n===== PORTFOLIO =====");
        System.out.printf("Available Cash: ₹%.2f%n", user.cash);

        for (String symbol : user.portfolio.keySet()) {
            int quantity = user.portfolio.get(symbol);
            if (quantity > 0) {
                double value = quantity * market.get(symbol).price;
                portfolioValue += value;
                System.out.printf("%-12s Quantity: %-5d Value: ₹%.2f%n",
                        symbol, quantity, value);
            }
        }

        System.out.printf("Stock Value: ₹%.2f%n", portfolioValue);
        System.out.printf("Total Portfolio Value: ₹%.2f%n",
                user.cash + portfolioValue);
    }

    static void viewTransactions(User user) {
        System.out.println("\n===== TRANSACTION HISTORY =====");
        if (user.transactions.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (Transaction t : user.transactions) {
            System.out.printf("%s %d shares of %s at ₹%.2f%n",
                    t.type, t.quantity, t.symbol, t.price);
        }
    }
}