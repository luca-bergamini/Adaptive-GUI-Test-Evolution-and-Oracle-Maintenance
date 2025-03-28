package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import de.greenrobot.event.EventBus;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.async.bus.NotesUpdatedEvent;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Observable;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FabCameraNoteTest extends BaseEspressoTest {

    @Test
    public void fabCameraNoteTest() {
        EventBus.getDefault().register(this);
        Intents.init();
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getInstrumentation().getTargetContext().getResources(),
                R.mipmap.ic_launcher);

        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK,
                resultData);

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

        onView(allOf(withId(R.id.fab_expand_menu_button),
                childAtPosition(
                        allOf(withId(R.id.fab),
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0)), // Updated position as per new layout
                        0), // Updated position as per new layout
                isDisplayed())).perform(click());

        onView(allOf(withId(R.id.menu_camera), // Changed id from R.id.fab_camera to new resource id
                childAtPosition(
                        allOf(withId(R.id.fab),
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0)), // Updated position as per new layout
                        0), // Updated position as per new layout
                isDisplayed())).perform(click());

        pressBack();
    }

    public void onEvent(NotesUpdatedEvent notesUpdatedEvent) {
        Note updatedNote = Observable.from(notesUpdatedEvent.getNotes()).toBlocking().first();

        assertEquals(0, updatedNote.getAttachmentsList().size());
        assertEquals(Constants.MIME_TYPE_IMAGE, updatedNote.getAttachmentsList().get(0).getMime_type());
    }
}