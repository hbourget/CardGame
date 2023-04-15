import com.groupe1.atelier3.cards.controllers.CardService;
import com.groupe1.atelier3.cards.models.Card;
import com.groupe1.atelier3.cards.models.CardRepository;
import com.groupe1.atelier3.cards.models.CardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    private Card testCard;
    private CardDTO testCardDTO;

    @BeforeEach
    public void setUp() {
        testCard = new Card(1, "Test Card", "This is a test card.", 10, 20, 100, "test-image.png", "Type", 5);
        testCardDTO = new CardDTO(1, "Test Card", "This is a test card.", 10, 20, 100, "test-image.png", "Type", 5);
    }

    @Test
    public void testAddCard() {
        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        Card result = cardService.addCard(testCardDTO);
        assertNotNull(result);
        assertEquals(testCard.getName(), result.getName());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    public void testAddCards() {
        List<CardDTO> cards = new ArrayList<>();
        cards.add(testCardDTO);

        when(cardRepository.save(any(Card.class))).thenReturn(testCard);

        List<Card> result = cardService.addCards(cards);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCard.getName(), result.get(0).getName());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    public void testGetCard() {
        when(cardRepository.findById(1)).thenReturn(Optional.of(testCard));

        Card result = cardService.getCard(1);
        assertNotNull(result);
        assertEquals(testCard.getName(), result.getName());
        verify(cardRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteCard() {
        when(cardRepository.findById(1)).thenReturn(Optional.of(testCard));

        boolean result = cardService.deleteCard(1);
        assertTrue(result);
        verify(cardRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAllCards() {
        cardService.deleteAllCards();
        verify(cardRepository, times(1)).deleteAll();
    }

    @Test
    public void testGetAllCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(testCard);

        when(cardRepository.findAll()).thenReturn(cards);

        List<Card> result = cardService.getAllCards();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCard.getName(), result.get(0).getName());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCard() {
        Card updatedCard = new Card(1, "Updated Card", "Updated test card.", 15, 25, 110, "updated-image.png", "Updated Type", 6);

        when(cardRepository.findById(1)).thenReturn(Optional.of(testCard));
        when(cardRepository.save(any(Card.class))).thenReturn(updatedCard);
        Card result = cardService.updateCard(1, updatedCard);
        assertNotNull(result);
        assertEquals(updatedCard.getName(), result.getName());
        assertEquals(updatedCard.getDescription(), result.getDescription());
        assertEquals(updatedCard.getPower(), result.getPower());
        assertEquals(updatedCard.getHealth(), result.getHealth());
        assertEquals(updatedCard.getPrice(), result.getPrice());
        assertEquals(updatedCard.getImage(), result.getImage());
        assertEquals(updatedCard.getType(), result.getType());
        assertEquals(updatedCard.getEnergy(), result.getEnergy());

        verify(cardRepository, times(1)).findById(1);
        verify(cardRepository, times(1)).save(any(Card.class));
    }
}

