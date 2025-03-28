package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;
import androidx.test.rule.ActivityTestRule;
import it.feio.android.omninotes.BaseAndroidTestCase;
import it.feio.android.omninotes.MainActivity;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.utils.ClickWithoutDisplayConstraint;
import java.util.concurrent.TimeoutException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class BaseEspressoTest extends BaseAndroidTestCase {

  @Rule
  public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class,
      false, false);

  static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }

  @Before
  public void setUp() {
    activityRule.launchActivity(null);
  }

  void selectNoteInList(int number) {
    onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(number, click()));
  }

  void navigateUp() {
    onView(allOf(childAtPosition(allOf(withId(R.id.toolbar),
        childAtPosition(withClassName(is("android.widget.RelativeLayout")), 0)
    ), 0), isDisplayed())).perform(click());
  }

  void navigateUPSearch() {
    onView(allOf(childAtPosition(allOf(withId(R.id.toolbar),
        childAtPosition(withClassName(is("android.widget.RelativeLayout")), 0)
    ), 1), isDisplayed())).perform(click());
  }

  void navigateUpSettings() {
    onView(allOf(withContentDescription(R.string.abc_action_bar_up_description),
        childAtPosition(allOf(withId(R.id.toolbar),
            childAtPosition(withClassName(is("android.widget.RelativeLayout")), 0)),
            1), isDisplayed())).perform(click());
  }

  @Test
  public void testUpdatedFunctionality() {
    // Check if the FrameLayout with a11y-important is displayed
    onView(withId(R.id.menu_search))
        .check(matches(isDisplayed()))
        .check(matches(withContentDescription("Cerca"))); // Check for new content description

    onView(withId(R.id.menu_sort))
        .check(matches(isDisplayed()))
        .check(matches(withContentDescription("Ordinamento"))); // Check for new content description

    // Assuming the Toolbar still exists and no structural changes occurred here
    navigateUp();

    // Additional tests...
  }

  public static ViewAction waitId(final int viewId, final long millis) {
    return new ViewAction() {
      @Override
      public Matcher<View> getConstraints() {
        return isRoot();
      }

      @Override
      public String getDescription() {
        return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
      }

      @Override
      public void perform(final UiController uiController, final View view) {
        uiController.loopMainThreadUntilIdle();
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + millis;
        final Matcher<View> viewMatcher = withId(viewId);

        do {
          for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
            if (viewMatcher.matches(child)) {
              return;
            }
          }

          uiController.loopMainThreadForAtLeast(50);
        }
        while (System.currentTimeMillis() < endTime);

        throw new PerformException.Builder()
            .withActionDescription(this.getDescription())
            .withViewDescription(HumanReadables.describe(view))
            .withCause(new TimeoutException())
            .build();
      }
    };
  }

  ClickWithoutDisplayConstraint getClickAction() {
    return new ClickWithoutDisplayConstraint(
        Tap.SINGLE,
        GeneralLocation.VISIBLE_CENTER,
        Press.FINGER,
        InputDevice.SOURCE_UNKNOWN,
        MotionEvent.BUTTON_PRIMARY);
  }

  ClickWithoutDisplayConstraint getLongClickAction() {
    return new ClickWithoutDisplayConstraint(
        Tap.LONG,
        GeneralLocation.CENTER,
        Press.FINGER,
        InputDevice.SOURCE_UNKNOWN,
        MotionEvent.BUTTON_PRIMARY);
  }

  ViewAction getSwipeAction(final int fromX, final int fromY, final int toX, final int toY) {
    return ViewActions.actionWithAssertions(
        new GeneralSwipeAction(
            Swipe.SLOW,
            view -> new float[]{fromX, fromY},
            view -> new float[]{toX, toY},
            Press.FINGER));
  }

  protected void openDrawer() {
    onView(allOf(withContentDescription("drawer open"),
        childAtPosition(
            allOf(withId(R.id.toolbar),
                childAtPosition(
                    withClassName(is("android.widget.RelativeLayout")),
                    0)),
            1),
        isDisplayed())).perform(click());
  }

  protected void navigateTo(int menuPosition) {
    openDrawer();
    onData(anything())
        .inAdapterView(allOf(withId(R.id.drawer_nav_list),
            childAtPosition(
                withId(R.id.left_drawer),
                1)))
        .atPosition(menuPosition).perform(scrollTo(), click());
  }
}