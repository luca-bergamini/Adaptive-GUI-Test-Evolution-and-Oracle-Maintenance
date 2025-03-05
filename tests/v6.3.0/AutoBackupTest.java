package it.feio.android.omninotes.ui;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.helpers.BackupHelper;
import it.feio.android.omninotes.models.Note;
import it.feio.android.omninotes.utils.Constants;
import it.feio.android.omninotes.utils.ConstantsBase;
import it.feio.android.omninotes.utils.StorageHelper;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore("Ignored until merging autobackup feature branch")
@LargeTest
@RunWith(AndroidJUnit4.class)
public class AutoBackupTest extends BaseEspressoTest {

  @Before
  @Override
  public void setUp() {
    super.setUp();
    prefs.edit().putBoolean(Constants.PREF_ENABLE_AUTOBACKUP, false).apply();
  }

  @After
  public void tearDown() throws Exception {
    File backupFolder = StorageHelper.getOrCreateBackupDir(Constants.AUTO_BACKUP_DIR);
    FileUtils.deleteDirectory(backupFolder);
  }

  @Test
  public void autoBackupPreferenceActivation() {
    assertFalse(prefs.getBoolean(Constants.PREF_ENABLE_AUTOBACKUP, false));
    autoBackupActivationFromPreferences();
    assertTrue(prefs.getBoolean(Constants.PREF_ENABLE_AUTOBACKUP, false));
  }

  @Test
  public void autoBackupWithNotesCheck() throws InterruptedException {
    createTestNote("A Title", "A content", 0);
    enableAutobackup();
    createTestNote("B Title", "B content", 0);

    // Waiting a little to ensure background service completes auto backup
    Thread.sleep(1200);

    List<Note> currentNotes = dbHelper.getAllNotes(false);
    assertEquals(2, currentNotes.size());
    for (Note currentNote : currentNotes) {
      File backupNoteFile = BackupHelper
          .getBackupNoteFile(StorageHelper.getOrCreateBackupDir(Constants.AUTO_BACKUP_DIR)
              , currentNote);
      assertTrue(backupNoteFile.exists());
      Note backupNote = BackupHelper.getImportNote(backupNoteFile);
      assertEquals(backupNote, currentNote);
    }
  }

  @Test
  public void everyUpdateToNotesShouldTriggerAutobackup() throws InterruptedException {
    enableAutobackup();

    createTestNote("C Title", "C content", 0);
    assertAutobackupIsCorrect();

    // Category addition
    onData(anything()).inAdapterView(ViewMatchers.withId(R.id.list)).atPosition(0).perform(click());

    onView(allOf(withId(R.id.menu_category), withContentDescription(R.string.category),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.buttonNegativeDP), withText(R.string.add_category),
        isDisplayed())).perform(scrollTo(), click());

    onView(allOf(withId(R.id.category_title),
        isDisplayed())).perform(replaceText("cat1"), closeSoftKeyboard());

    onView(allOf(withId(R.id.save), withText("Ok"), isDisplayed())).perform(click());

    navigateUp();
    assertAutobackupIsCorrect();

    // Reminder addition
    onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

    onView(allOf(withId(R.id.reminder_layout),
        isDisplayed())).perform(scrollTo(), click());

    onView(allOf(withId(R.id.buttonPositive), withText("Ok"),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.done),
        isDisplayed())).perform(click());

    navigateUp();
    assertAutobackupIsCorrect();

    onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

    onView(allOf(withId(R.id.menu_attachment),
        isDisplayed())).perform(click());

    onView(allOf(withId(R.id.recording), withText(R.string.record),
        isDisplayed())).perform(click());

    Thread.sleep(1000);

    onView(allOf(withId(R.id.recording),
        isDisplayed())).perform(click());

    navigateUp();
    navigateUp();
    assertAutobackupIsCorrect();
  }

  private void enableAutobackup() {
    prefs.edit().putBoolean(Constants.PREF_ENABLE_AUTOBACKUP, true).apply();
    BackupHelper.startBackupService(Constants.AUTO_BACKUP_DIR);
  }

  private void assertAutobackupIsCorrect() {
    List<LinkedList<DiffMatchPatch.Diff>> autobackupDifferences = BackupHelper
        .integrityCheck(StorageHelper.getOrCreateBackupDir(ConstantsBase.AUTO_BACKUP_DIR));
    assertEquals(0, autobackupDifferences.size());
  }

  private void autoBackupActivationFromPreferences() {
    onView(allOf(withId(R.id.toolbar),
        isDisplayed())).perform(click());

    getSettingsMenuItemView()
        .perform(scrollTo(), click());

    onView(allOf(withId(android.R.id.list),
        isDisplayed()))
        .perform(click());

    onView(allOf(withId(android.R.id.list),
        isDisplayed()))
        .perform(click());

    onView(allOf(withId(android.R.id.list),
        isDisplayed())).perform(scrollTo(), click());

    onView(allOf(withId(R.id.buttonNegativeDP), isDisplayed())).perform(click());

    navigateUpSettings();
    navigateUpSettings();
    navigateUpSettings();
  }

  private ViewInteraction getSettingsMenuItemView() {
    boolean existsAtLeastOneCategory = dbHelper.getCategories().size() > 0;
    return existsAtLeastOneCategory ? onView(withId(R.id.drawer_tag_list))
        : onView(withId(R.id.settings_view));
  }
}