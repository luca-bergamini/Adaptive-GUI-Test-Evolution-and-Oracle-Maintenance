package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.db.DbHelper;
import it.feio.android.omninotes.models.Category;
import java.util.ArrayList;
import java.util.Calendar;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CategoryLifecycleTest extends BaseEspressoTest {

  @Test
  public void addNewCategory() {
    String categoryName = "Cat_" + Calendar.getInstance().getTimeInMillis();

    onView(Matchers.allOf(ViewMatchers.withId(R.id.fab_expand_menu_button),
        withParent(withId(R.id.fab)),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.fab_note),
        withParent(withId(R.id.fab)),
        isDisplayed())).perform(click());

    onView(
        allOf(withId(R.id.menu_category), withContentDescription(R.string.category), isDisplayed()))
        .perform(click());

    // Material dialog "Add Category"
    onView(isRoot()).perform(waitId(R.id.md_buttonDefaultPositive, 5000));

    onView((withText(R.string.add_category))).perform(click());

    onView(withId(R.id.category_title)).perform(replaceText(categoryName), closeSoftKeyboard());

    onView(allOf(withId(R.id.save), withText("Ok"), isDisplayed())).perform(click());

    onView(allOf(withId(R.id.detail_title),
        withParent(allOf(withId(R.id.title_wrapper),
            withParent(withId(R.id.detail_tile_card)))),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.detail_title),
        withParent(allOf(withId(R.id.title_wrapper),
            withParent(withId(R.id.detail_tile_card)))),
        isDisplayed())).perform(replaceText("Note with new category"), closeSoftKeyboard());

    onView(allOf(withContentDescription(R.string.drawer_open),
        withParent(withId(R.id.toolbar)),
        isDisplayed())).perform(click());

    ArrayList<Category> categories = DbHelper.getInstance().getCategories();

    assertEquals(1, categories.size());
    assertEquals(categoryName, categories.get(0).getName());
  }

  @Test
  public void checkCategoryCreation() {
    String categoryName = "Cat_" + Calendar.getInstance().getTimeInMillis();
    createCategory(categoryName);

    onView(allOf(withContentDescription(R.string.drawer_open),
        withParent(withId(R.id.toolbar)),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.title), withText(categoryName)))
        .check(matches(withText(categoryName)));
  }

  @Test
  public void categoryColorChange() {
    String categoryName = "Cat_" + Calendar.getInstance().getTimeInMillis();
    createCategory(categoryName);

    onView(allOf(withContentDescription(R.string.drawer_open),
        withParent(withId(R.id.toolbar)))).perform(click());

    onView(allOf(withId(R.id.title), withText(categoryName))).perform(longClick());

    onView(allOf(withId(R.id.color_chooser), isDisplayed())).check(matches(isDisplayed()));

    onView(allOf(withId(R.id.color_chooser), isDisplayed())).perform(click());

    onView(allOf(withId(R.id.md_buttonDefaultNeutral), withText("Custom"),
        childAtPosition(
            allOf(withId(R.id.md_root),
                childAtPosition(
                    withId(android.R.id.content),
                    0)),
            2))).perform(click());

    onView(allOf(withId(R.id.md_buttonDefaultNeutral), withText(R.string.md_presets_label),
        withParent(allOf(withId(R.id.md_root),
            withParent(withId(android.R.id.content)))))).perform(click());

    onView(childAtPosition(
        withId(R.id.md_grid),
        18)).perform(scrollTo(), click());

    onView(childAtPosition(
        withId(R.id.md_grid),
        9)).perform(scrollTo(), click());

    onView(allOf(withId(R.id.md_buttonDefaultPositive), withText(R.string.md_done_label),
        childAtPosition(
            allOf(withId(R.id.md_root),
                childAtPosition(
                    withId(android.R.id.content),
                    0)),
            4))).perform(click());

    onView(allOf(withId(R.id.color_chooser), isDisplayed())).check(
        matches(withBackgroundColor(Color.parseColor("#FF263238"))));
  }

  @Test
  public void categoryDeletion() {
    String categoryName = "Cat_" + Calendar.getInstance().getTimeInMillis();
    createCategory(categoryName);

    onView(allOf(withContentDescription(R.string.drawer_open),
        withParent(withId(R.id.toolbar)),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.title), withText(categoryName))).perform(longClick());

    onView(allOf(withId(R.id.delete), withText(R.string.delete), isDisplayed())).perform(click());

    onView(withText(R.string.confirm)).perform(click());

    // Waiting a little to ensure Eventbus post propagation
    try {
      sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    onView(allOf(withId(R.id.title), withText(categoryName))).check(doesNotExist());
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private static Matcher<View> withBackgroundColor(final int backgroundColor) {
    return new TypeSafeMatcher<View>() {

      @Override
      public boolean matchesSafely(View view) {
        ColorFilter cf = new PorterDuffColorFilter(Color.parseColor("#FF263238"),
            PorterDuff.Mode.SRC_ATOP);
        ColorFilter cf1 = ((AppCompatImageView) view).getDrawable().getColorFilter();
        return cf.equals(cf1);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("with background color from ID: " + backgroundColor);
      }
    };
  }
}