package am.aua;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CreditCardTest {
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard("My Main Card", "1111222233334444", 100);
    }

    @Test
    void testCreateCreditCard_ValidProperties() {
        assertEquals("My Main Card", creditCard.getName());
        assertEquals("1111222233334444", creditCard.getNumber());
        assertEquals(100, creditCard.getBalance());
    }

    @ParameterizedTest
    @MethodSource("invalidCreditCardProperties")
    void testCreatingCredit_InvalidProperties(String name, String number, double balance, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new CreditCard(name, number, balance));
        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    void testDeposit_NegativeAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> creditCard.deposit(-7));
        assertEquals("Amount must be positive value", exception.getMessage());
    }

    @Test
    void testDeposit_ZeroAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> creditCard.deposit(0));
        assertEquals("Amount must be positive value", exception.getMessage());
    }

    @Test
    void testDeposit_PositiveAmount() {
        creditCard.deposit(1000);
        assertEquals(1100, creditCard.getBalance());
    }

    @Test
    void testWithdraw_NegativeAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> creditCard.withdraw(-7));
        assertEquals("Amount must be positive value", exception.getMessage());
    }

    @Test
    void testWithdraw_ZeroAmount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> creditCard.withdraw(0));
        assertEquals("Amount must be positive value", exception.getMessage());
    }

    @Test
    void testWithdraw_PositiveAndMoreThanBalanceAmount() {
        CreditCardException exception = assertThrows(CreditCardException.class, () -> creditCard.withdraw(200));
        assertEquals("Amount must not be more than balance", exception.getMessage());
    }

    @Test
    void testWithdraw_PositiveAndLessThanBalanceAmount() throws CreditCardException {
        creditCard.withdraw(30);
        assertEquals(70, creditCard.getBalance());
    }

    @Test
    void testGenerateStatement_ValidTemplate() {
        String template = "Card named '$name' with number $number has $balance dollars on balance";
        String statement = creditCard.generateStatement(template);
        assertEquals("Card named 'My Main Card' with number 1111222233334444 has 100.00 dollars on balance", statement);
    }

    @ParameterizedTest
    @MethodSource("invalidStatementTemplates")
    void testGenerateStatement_InvalidTemplate(String template) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> creditCard.generateStatement(template));
        assertEquals("Invalid template", exception.getMessage());
    }

    private static Stream<Arguments> invalidCreditCardProperties() {
        return Stream.of(
                //                       name                   number               balance   expected message
                Arguments.of(null,                 "1234567812345678",  10,       "Credit Card name cannot be blank or null"  ),
                Arguments.of("",                   "1234567812345678",  10,       "Credit Card name cannot be blank or null"  ),
                Arguments.of("Salary Credit Card", null              ,  10,       "Credit Card number cannot be blank or null"),
                Arguments.of("Salary Credit Card", ""                ,  10,       "Credit Card number cannot be blank or null"),
                Arguments.of("Salary Credit Card", "123456781234567" ,  10,       "Credit Card number length must be 16"      ),
                Arguments.of("Salary Credit Card", "123456781234567A",  10,       "Credit Card must contain only numbers"     ),
                Arguments.of("Salary Credit Card", "1234567812345678", -10,       "Balance cannot be negative"                )
        );
    }

    private static Stream<Arguments> invalidStatementTemplates() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of("Card with number $number has $balance dollars on balance"),
                Arguments.of("Card named '$name has $balance dollars on balance"),
                Arguments.of("$balance dollars on balance"),
                Arguments.of("Template without variables")
        );
    }
}
