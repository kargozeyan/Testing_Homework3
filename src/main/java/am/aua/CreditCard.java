package am.aua;

public class CreditCard {
    private final String name;
    private final String number;
    private double balance;

    public CreditCard(String name, String number, double balance) {
        validateProperties(name, number, balance);
        this.name = name;
        this.number = number;
        this.balance = balance;
    }

    private void validateProperties(String name, String number, double balance) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Credit Card name cannot be blank or null");
        }

        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Credit Card number cannot be blank or null");
        }

        if (number.length() != 16) {
            throw new IllegalArgumentException("Credit Card number length must be 16");
        }

        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("Credit Card must contain only numbers");
            }
        }

        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive value");
        }

        balance += amount;
    }

    public void withdraw(double amount) throws CreditCardException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive value");
        }

        if (amount > balance) {
            throw new CreditCardException("Amount must not be more than balance");
        }

        balance -= amount;
    }

    // Template must include $name, $balance, $number to be filled
    // e.g. "The Credit card '$name' with number $number has $balance amount of money"
    public String generateStatement(String template) {
        if (template == null || !template.contains("$name") || !template.contains("$number") || !template.contains("$balance")) {
            throw new IllegalArgumentException("Invalid template");
        }

        return template
                .replace("$name", name)
                .replace("$number", number)
                .replace("$balance", String.format("%,.2f", balance));
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }
}
