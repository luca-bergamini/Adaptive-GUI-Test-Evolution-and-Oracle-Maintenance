package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import it.feio.android.omninotes.R;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchTagBackArrowTest extends BaseEspressoTest {

    @Test
    public void tagSearchFlowLinkedText() {
        createTestNote("A Title", "Some text to test #hashtag", 0);

        selectNoteInList(0);
        onView(withId(R.id.detail_content)).perform(click());
        onView(withText("Open")).perform(click());
        onView(withId(R.id.menu_search)).perform(click()); // Updated to handle menu search button
        onView(withId(R.id.menu_sort)).perform(click()); // Assuming an action to navigate back or sort, adjust if necessary
    }

}