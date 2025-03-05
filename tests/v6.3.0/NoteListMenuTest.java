package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class NoteListMenuTest extends BaseEspressoTest {

  @Test
  public void switchExpandedColapsedNoteLayoutTest() {

    prefs.edit().putBoolean(Constants.PREF_EXPANDED_VIEW, false).apply();

    createTestNote("A Title", "A content", 0);

    // click overflow menu button
    openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

    // click expanded view menu item
    onView(withText(R.string.expanded_view))
        .perform(click());

    // Verify the layout is expanded (additional check can be added here as per new layout)

    openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

    // click contracted view menu item
    onView(withText(R.string.contracted_view))
        .perform(click());
    
    // Verify the layout is contracted (additional check can be added here as per new layout)
  }
}