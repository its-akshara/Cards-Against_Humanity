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
        String deckName = local_cp.getDeckName();

        System.out.println(deckName);
        System.out.println("Black Cards: " + local_cp.getNumberOfBlackCards() + " total");
        //System.out.println(local_cp.getBlackCardsString());

        for(int i = -1; i <= local_cp.getNumberOfBlackCards(); i++)
        {
            System.out.println(i + ": " + local_cp.getBlackCardByIndex(i));
        }

        System.out.println("White Cards: " + local_cp.getNumberOfWhiteCards() + " total");
        //System.out.println(local_cp.getWhiteCardsString());

        for(int i = -1; i <= local_cp.getNumberOfWhiteCards(); i++)
        {
            System.out.println(i + ": " + local_cp.getWhiteCardByIndex(i));
        }
    }
}