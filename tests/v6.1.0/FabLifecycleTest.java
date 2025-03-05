package it.feio.android.omninotes.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertNotNull;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import it.feio.android.omninotes.R;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;
import org.junit.runner.RunWith;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class FabLifecycleTest extends BaseEspressoTest {

  @Test
  public void fabOpenCloseTest() {

    onView(Matchers.allOf(ViewMatchers.withId(R.id.fab_expand_menu_button),
        withParent(withId(R.id.fab)))).perform(click());

    onView(allOf(withId(R.id.fab_expand_menu_button),
        withParent(withId(R.id.fab)))).perform(click());
  }

  @Test
  public void fabActionsTest() {

    onView(allOf(withId(R.id.fab_expand_menu_button),
        withParent(withId(R.id.fab)),
        isDisplayed())).perform(click());

    ViewInteraction checklistFabAction = onView(
        allOf(withId(R.id.fab_checklist),
            childAtPosition(
                allOf(withId(R.id.fab),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                        4)),
                3),
            isDisplayed()));
    assertNotNull(checklistFabAction);

    ViewInteraction cameraFabAction = onView(
        allOf(withId(R.id.fab_camera),
            childAtPosition(
                allOf(withId(R.id.fab),
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.FrameLayout.class),
                        4)),
                1),
            isDisplayed()));
    assertNotNull(cameraFabAction);
  }
}
