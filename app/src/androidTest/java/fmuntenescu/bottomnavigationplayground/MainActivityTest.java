package fmuntenescu.bottomnavigationplayground;


import android.support.design.internal.BottomNavigationItemView;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest2() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button1), withText("Go to Top News"),
                        withParent(allOf(withId(R.id.activity_main),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        assertNavigationButtomCheckStatus(R.id.bottomTopNews, true);


        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.bottomHome), isDisplayed()));
        bottomNavigationItemView.perform(click());

        assertNavigationButtomCheckStatus(R.id.bottomHome, true);

    }

    private void assertNavigationButtomCheckStatus(int buttonId, boolean isChecked) {
        ViewInteraction buttonNews = onView(
                allOf(withId(buttonId),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                1),
                        isDisplayed()));
        buttonNews.check(matches(withBottomNavItemCheckedStatus(isChecked)));
    }

    private static Matcher<View> childAtPosition(
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

    public static Matcher<View> withBottomNavItemCheckedStatus(final boolean isChecked) {
        return new BoundedMatcher<View, BottomNavigationItemView>(BottomNavigationItemView.class) {
            boolean triedMatching;

            @Override
            public void describeTo(Description description) {
                if (triedMatching) {
                    description.appendText("with BottomNavigationItem check status: " + String.valueOf(isChecked));
                    description.appendText("But was: " + String.valueOf(!isChecked));
                }
            }

            @Override
            protected boolean matchesSafely(BottomNavigationItemView item) {
                triedMatching = true;
                return item.getItemData().isChecked() == isChecked;
            }
        };
    }
}
