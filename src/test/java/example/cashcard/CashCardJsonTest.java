package example.cashcard;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest // This annotation focuses the test context ONLY on JSON serialization/deserialization
public class CashCardJsonTest {

    @Autowired
    private JacksonTester<CashCard> json; // Spring Boot auto-configures this tester for us
    @Autowired
    private JacksonTester<CashCard[]> jsonArray;

    private CashCard cashCard;
    private CashCard[] cashCards;

    @BeforeEach
    void setUp() {
        // Using a setup method to initialize common test data
        cashCard = new CashCard(99L, 123.45, "sarah1");

        cashCards = Arrays.array(
                cashCard,
                new CashCard(100L, 1.00, "sarah1"),
                new CashCard(101L, 150.00, "sarah1")
        );
    }

    @Test
    void cashCardSerializationTest() throws IOException {
        // --- ACT ---
        JsonContent<CashCard> jsonContent = json.write(cashCard);

        // 1. Assert that json structure is the same
        assertThat(jsonContent).isStrictlyEqualToJson("single.json");

        // 2. Assert that specific fields exist.
        assertThat(jsonContent).hasJsonPathNumberValue("@.id");
        assertThat(jsonContent).hasJsonPathNumberValue("@.amount");
        assertThat(jsonContent).hasJsonPathStringValue("@.owner");

        // 3. Assert the values of specific fields.
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id").isEqualTo(99);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.amount").isEqualTo(123.45);
        assertThat(jsonContent).extractingJsonPathStringValue("@.owner").isEqualToIgnoringCase("sarah1");
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        // --- ARRANGE ---
        String expectedJson = """
                {
                  "id": 99,
                  "amount": 123.45,
                  "owner": "sarah1"
                }
                """;

        // --- ACT ---
        CashCard parsedCashCard = json.parseObject(expectedJson);

        // --- ASSERT ---
        assertThat(parsedCashCard).isEqualTo(cashCard); // Using the object from setUp
        assertThat(parsedCashCard.getId()).isEqualTo(99L);
        assertThat(parsedCashCard.getAmount()).isEqualTo(123.45);
        assertThat(parsedCashCard.getOwner()).isEqualToIgnoringCase("sarah1");
    }

    @Test
    void cashCardArraySerializationTest() throws IOException {
        JsonContent<CashCard[]> jsonArrayContent = jsonArray.write(cashCards);
        assertThat(jsonArrayContent).isStrictlyEqualToJson("array.json");
    }

    @Test
    void cashCardArrayDeserializationTest() throws IOException {
        String expectedCashCardList = """
                [
                    { "id": 99, "amount": 123.45, "owner": "sarah1" },
                    { "id": 100, "amount": 1.00, "owner": "sarah1"},
                    { "id": 101, "amount": 150.00, "owner": "sarah1"}
                ]
                """;

        CashCard[] parsedCashCardArray = jsonArray.parseObject(expectedCashCardList);
        assertThat(parsedCashCardArray).isEqualTo(cashCards);
    }
}