package m117.andrewpiro.cardsagainsthumanityfrontend;

import org.junit.Test;

import cahCardParser.cardParser;

import static org.junit.Assert.*;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    cardParser local_cp;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void cardParserTest(){
        local_cp = new cardParser();
        local_cp.parse("C:\\Users\\Jess\\Documents\\GitHub\\Cards-Against_Humanity\\Frontend\\cardsagainsthumanityfrontend\\app\\src\\main\\assets\\cards.json");
    }
}