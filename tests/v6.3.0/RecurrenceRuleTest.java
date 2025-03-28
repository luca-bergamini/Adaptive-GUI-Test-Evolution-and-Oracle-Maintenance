package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.helpers.date.RecurrenceHelper;
import it.feio.android.omninotes.models.Note;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecurrenceRuleTest extends BaseEspressoTest {

  private Note testNote;

  @Before
  @Override
  public void setUp() {
    super.setUp();
    testNote = new Note();
    testNote.setTitle("Title 1");
    Calendar c = Calendar.getInstance();
    c.add(Calendar.MINUTE, 2);
    testNote.setAlarm(c.getTimeInMillis());
    testNote.setRecurrenceRule("FREQ=WEEKLY;WKST=MO;BYDAY=MO,TU,TH");
    dbHelper.updateNote(testNote, false);
  }

  @Test
  public void recurrenceRuleTextHasValue() throws InterruptedException {

    Thread.sleep(1500);

    onView(allOf(withId(R.id.menu_sort), withContentDescription("Ordinamento"),
        childAtPosition(
            childAtPosition(
                withId(R.id.toolbar),
                2),
            1),
        isDisplayed())).perform(click());

    Thread.sleep(1500);

    onView(allOf(withId(R.id.title), withText("Creation date"),
        childAtPosition(
            childAtPosition(
                withId(R.id.content),
                0),
            0),
        isDisplayed())).perform(click());

    selectNoteInList(0);

    String expectedText = RecurrenceHelper
        .getNoteRecurrentReminderText(Long.parseLong(testNote.getAlarm()),
            testNote.getRecurrenceRule());
    onView(withId(R.id.datetime)).check(matches(withText(expectedText)));
  }

}