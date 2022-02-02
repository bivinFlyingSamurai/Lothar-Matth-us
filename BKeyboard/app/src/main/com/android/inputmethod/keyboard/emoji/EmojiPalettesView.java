package com.android.inputmethod.keyboard.emoji;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.viewpager.widget.ViewPager;

import com.Matthaus.Lothar.keyboard.MainActivity;
import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.keyboard.Key;
import com.android.inputmethod.keyboard.KeyboardActionListener;
import com.android.inputmethod.keyboard.KeyboardLayoutSet;
import com.android.inputmethod.keyboard.KeyboardView;
import com.android.inputmethod.keyboard.internal.KeyDrawParams;
import com.android.inputmethod.keyboard.internal.KeyVisualAttributes;
import com.android.inputmethod.keyboard.internal.KeyboardIconsSet;
import com.android.inputmethod.latin.AudioAndHapticFeedbackManager;
import com.android.inputmethod.latin.RichInputMethodSubtype;
import com.android.inputmethod.latin.common.Constants;
import com.android.inputmethod.latin.utils.ResourceUtils;

import java.util.Objects;

import static com.android.inputmethod.latin.common.Constants.NOT_A_COORDINATE;

/**
 * View class to implement Emoji palettes.
 * The Emoji keyboard consists of group of views layout/emoji_palettes_view.
 * <ol>
 * <li> Emoji category tabs.
 * <li> Delete button.
 * <li> Emoji keyboard pages that can be scrolled by swiping horizontally or by selecting a tab.
 * <li> Back to main keyboard button and enter button.
 * </ol>
 * Because of the above reasons, this class doesn't extend {@link KeyboardView}.
 */
public final class EmojiPalettesView extends LinearLayoutCompat implements OnTabChangeListener,
        ViewPager.OnPageChangeListener, View.OnClickListener, View.OnTouchListener,
        EmojiPageKeyboardView.OnKeyEventListener {
    private final int mFunctionalKeyBackgroundId;
    private final int mSpacebarBackgroundId;
    private final boolean mCategoryIndicatorEnabled;
    private final int mCategoryIndicatorDrawableResId;
    private final int mCategoryIndicatorBackgroundResId;
    private final int mCategoryPageIndicatorColor;
    private final int mCategoryPageIndicatorBackground;
    private EmojiPalettesAdapter mEmojiPalettesAdapter;
    private final EmojiLayoutParams mEmojiLayoutParams;
    private final DeleteKeyOnTouchListener mDeleteKeyOnTouchListener;

    private ImageButton mDeleteKey;
    private TextView mAlphabetKeyLeft;
    private TextView mAlphabetKeyRight;
    private TextView mDotKey;
    private TextView mCommaKey;
    private View mSpacebar;
    // TODO: Remove this workaround.
    private View mSpacebarIcon;
    private TabHost mTabHost;
    private ViewPager mEmojiPager;
    private int mCurrentPagerPosition = 0;
    private EmojiCategoryPageIndicatorView mEmojiCategoryPageIndicatorView;

    private KeyboardActionListener mKeyboardActionListener = KeyboardActionListener.EMPTY_LISTENER;

    private final EmojiCategory mEmojiCategory;
    private ImageView ivEmoji;

    public EmojiPalettesView(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.emojiPalettesViewStyle);
    }

    public EmojiPalettesView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray keyboardViewAttr = context.obtainStyledAttributes(attrs,
                R.styleable.KeyboardView, defStyle, R.style.KeyboardView);
        final int keyBackgroundId = keyboardViewAttr.getResourceId(
                R.styleable.KeyboardView_keyBackground, 0);
        mFunctionalKeyBackgroundId = keyboardViewAttr.getResourceId(
                R.styleable.KeyboardView_functionalKeyBackground, keyBackgroundId);
        mSpacebarBackgroundId = keyboardViewAttr.getResourceId(
                R.styleable.KeyboardView_spacebarBackground, keyBackgroundId);
        keyboardViewAttr.recycle();
        final KeyboardLayoutSet.Builder builder = new KeyboardLayoutSet.Builder(
                context, null /* editorInfo */);
        final Resources res = context.getResources();
        mEmojiLayoutParams = new EmojiLayoutParams(res);
        builder.setSubtype(RichInputMethodSubtype.getEmojiSubtype());
        builder.setKeyboardGeometry(ResourceUtils.getDefaultKeyboardWidth(res),
                mEmojiLayoutParams.mEmojiKeyboardHeight);
        final KeyboardLayoutSet layoutSet = builder.build();
        final TypedArray emojiPalettesViewAttr = context.obtainStyledAttributes(attrs,
                R.styleable.EmojiPalettesView, defStyle, R.style.EmojiPalettesView);
        mEmojiCategory = new EmojiCategory(PreferenceManager.getDefaultSharedPreferences(context),
                res, layoutSet, emojiPalettesViewAttr);
        mCategoryIndicatorEnabled = emojiPalettesViewAttr.getBoolean(
                R.styleable.EmojiPalettesView_categoryIndicatorEnabled, false);
        mCategoryIndicatorDrawableResId = emojiPalettesViewAttr.getResourceId(
                R.styleable.EmojiPalettesView_categoryIndicatorDrawable, 0);
        mCategoryIndicatorBackgroundResId = emojiPalettesViewAttr.getResourceId(
                R.styleable.EmojiPalettesView_categoryIndicatorBackground, 0);
        mCategoryPageIndicatorColor = emojiPalettesViewAttr.getColor(
                R.styleable.EmojiPalettesView_categoryPageIndicatorColor, 0);
        mCategoryPageIndicatorBackground = emojiPalettesViewAttr.getColor(
                R.styleable.EmojiPalettesView_categoryPageIndicatorBackground, 0);
        emojiPalettesViewAttr.recycle();
        mDeleteKeyOnTouchListener = new DeleteKeyOnTouchListener();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final Resources res = getContext().getResources();
        // The main keyboard expands to the entire this {@link KeyboardView}.
        final int width = ResourceUtils.getDefaultKeyboardWidth(res)
                + getPaddingLeft() + getPaddingRight();
        final int height = ResourceUtils.getDefaultKeyboardHeight(res)
                + res.getDimensionPixelSize(R.dimen.config_suggestions_strip_height)
                + res.getDimensionPixelSize(R.dimen.config_suggestions_strip_height)
                + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width, height);
    }

    public void setEmojiBackground(int resId) {
        Resources res = getContext().getResources();
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(
                        ResourceUtils.getDefaultKeyboardWidth(res),
                        ResourceUtils.getDefaultKeyboardHeight(res));
        ivEmoji.setLayoutParams(layoutParams);
        ivEmoji.setBackgroundResource(resId);
    }

    private void addTab(final TabHost host, final int categoryId) {
        final String tabId = EmojiCategory.getCategoryName(categoryId, 0 /* categoryPageId */);
        final TabHost.TabSpec tspec = host.newTabSpec(tabId);
        tspec.setContent(R.id.emoji_keyboard_dummy);
        final ImageView iconView = (ImageView)LayoutInflater.from(getContext()).inflate(
                R.layout.emoji_keyboard_tab_icon, null);
        // TODO: Replace background color with its own setting rather than using the
        //       category page indicator background as a workaround.
        iconView.setBackgroundColor(mCategoryPageIndicatorBackground);
        iconView.setImageResource(mEmojiCategory.getCategoryTabIcon(categoryId));
        iconView.setContentDescription(mEmojiCategory.getAccessibilityDescription(categoryId));
        tspec.setIndicator(iconView);
        host.addTab(tspec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTabHost = findViewById(R.id.emoji_category_tabhost);
        mTabHost.setup();
        for (final EmojiCategory.CategoryProperties properties
                : mEmojiCategory.getShownCategories()) {
            addTab(mTabHost, properties.mCategoryId);
        }
        mTabHost.setOnTabChangedListener(this);
        final TabWidget tabWidget = mTabHost.getTabWidget();
        tabWidget.setStripEnabled(mCategoryIndicatorEnabled);
        if (mCategoryIndicatorEnabled) {
            // On TabWidget's strip, what looks like an indicator is actually a background.
            // And what looks like a background are actually left and right drawables.
            tabWidget.setBackgroundResource(mCategoryIndicatorDrawableResId);
            tabWidget.setLeftStripDrawable(mCategoryIndicatorBackgroundResId);
            tabWidget.setRightStripDrawable(mCategoryIndicatorBackgroundResId);
        }

        mEmojiPalettesAdapter = new EmojiPalettesAdapter(mEmojiCategory, this, getContext());

        ivEmoji = findViewById(R.id.emoji_keyboard);
        mEmojiPager = findViewById(R.id.emoji_keyboard_pager);
        mEmojiPager.setAdapter(mEmojiPalettesAdapter);
        mEmojiPager.setOnPageChangeListener(this);
        mEmojiPager.setOffscreenPageLimit(0);
        mEmojiPager.setPersistentDrawingCache(PERSISTENT_NO_CACHE);
        mEmojiLayoutParams.setPagerProperties(mEmojiPager);

        mEmojiCategoryPageIndicatorView =
                findViewById(R.id.emoji_category_page_id_view);
        mEmojiCategoryPageIndicatorView.setColors(
                mCategoryPageIndicatorColor, mCategoryPageIndicatorBackground);
        mEmojiLayoutParams.setCategoryPageIdViewProperties(mEmojiCategoryPageIndicatorView);

        setCurrentCategoryId(mEmojiCategory.getCurrentCategoryId(), true /* force */);

        final LinearLayout actionBar = findViewById(R.id.emoji_action_bar);
        mEmojiLayoutParams.setActionBarProperties(actionBar);

        mDeleteKey = findViewById(R.id.emoji_keyboard_delete);
        mDeleteKey.setBackgroundResource(mFunctionalKeyBackgroundId);
        mDeleteKey.setTag(Constants.CODE_DELETE);
        mDeleteKey.setOnTouchListener(mDeleteKeyOnTouchListener);

        mAlphabetKeyLeft = findViewById(R.id.emoji_keyboard_alphabet_left);
        mAlphabetKeyLeft.setBackgroundResource(mFunctionalKeyBackgroundId);
        mAlphabetKeyLeft.setTag(Constants.CODE_ALPHA_FROM_EMOJI);
        mAlphabetKeyLeft.setOnTouchListener(this);
        mAlphabetKeyLeft.setOnClickListener(this);

        mAlphabetKeyRight = findViewById(R.id.emoji_keyboard_alphabet_right);
        mAlphabetKeyRight.setBackgroundResource(mFunctionalKeyBackgroundId);
        mAlphabetKeyRight.setTag(Constants.CODE_ALPHA_FROM_EMOJI);
        mAlphabetKeyRight.setOnTouchListener(this);
        mAlphabetKeyRight.setOnClickListener(this);

        mCommaKey = findViewById(R.id.emoji_keyboard_comma);
        mCommaKey.setBackgroundResource(mFunctionalKeyBackgroundId);
        mCommaKey.setTag(Constants.CODE_COMMA);
        mCommaKey.setOnTouchListener(this);
        mCommaKey.setOnClickListener(this);

        mDotKey = findViewById(R.id.emoji_keyboard_dot);
        mDotKey.setBackgroundResource(mFunctionalKeyBackgroundId);
        mDotKey.setTag(Constants.CODE_PERIOD);
        mDotKey.setOnTouchListener(this);
        mDotKey.setOnClickListener(this);

        mSpacebar = findViewById(R.id.emoji_keyboard_space);
        mSpacebar.setBackgroundResource(mSpacebarBackgroundId);
        mSpacebar.setTag(Constants.CODE_SPACE);
        mSpacebar.setOnTouchListener(this);
        mSpacebar.setOnClickListener(this);

        mEmojiLayoutParams.setKeyProperties(mSpacebar);
        mSpacebarIcon = findViewById(R.id.emoji_keyboard_space_icon);

        LinearLayoutCompat mLayout = findViewById(R.id.menu_strip_view);

        ImageView ivSetting = mLayout.findViewById(R.id.menu_setting);
        ivSetting.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(i);
        });

        ImageView ivInstagram = mLayout.findViewById(R.id.menu_instagram);
        ivInstagram.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://www.instagram.com/lotharmatthaus10/"));
            getContext().startActivity(i);
        });

        ImageView ivTwitter = mLayout.findViewById(R.id.menu_twitter);
        ivTwitter.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://wewave.com/products/lothar-matthaeus"));
            getContext().startActivity(i);
        });

        ImageView ivHome = mLayout.findViewById(R.id.menu_home);
        ivHome.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://lotharmatthaeus10.com/"));
            getContext().startActivity(i);
        });

        ImageView ivShirt = mLayout.findViewById(R.id.menu_play);
        ivShirt.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://www.youtube.com/channel/UCk_mSMrm7-9RrtIKfe-YONw"));
            getContext().startActivity(i);
        });

        ImageView ivCoke = mLayout.findViewById(R.id.menu_puma);
        ivCoke.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://eu.puma.com/de/de/home"));
            getContext().startActivity(i);
        });

        ImageView ivAdidas = mLayout.findViewById(R.id.menu_news);
        ivAdidas.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://sport.sky.de/thema/kolumne-lothar-matthaeus/7279"));
            getContext().startActivity(i);
        });
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        // Add here to the stack trace to nail down the {@link IllegalArgumentException} exception
        // in MotionEvent that sporadically happens.
        // TODO: Remove this override method once the issue has been addressed.
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onTabChanged(final String tabId) {
        AudioAndHapticFeedbackManager.getInstance().performHapticAndAudioFeedback(
                Constants.CODE_UNSPECIFIED, this);
        final int categoryId = mEmojiCategory.getCategoryId(tabId);
        setCurrentCategoryId(categoryId, false /* force */);
        updateEmojiCategoryPageIdView();
    }

    @Override
    public void onPageSelected(final int position) {
        final Pair<Integer, Integer> newPos =
                mEmojiCategory.getCategoryIdAndPageIdFromPagePosition(position);
        setCurrentCategoryId(newPos.first /* categoryId */, false /* force */);
        mEmojiCategory.setCurrentCategoryPageId(newPos.second /* categoryPageId */);
        updateEmojiCategoryPageIdView();
        mCurrentPagerPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        // Ignore this message. Only want the actual page selected.
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset,
                               final int positionOffsetPixels) {
        mEmojiPalettesAdapter.onPageScrolled();
        final Pair<Integer, Integer> newPos =
                mEmojiCategory.getCategoryIdAndPageIdFromPagePosition(position);
        final int newCategoryId = newPos.first;
        final int newCategorySize = mEmojiCategory.getCategoryPageSize(newCategoryId);
        final int currentCategoryId = mEmojiCategory.getCurrentCategoryId();
        final int currentCategoryPageId = mEmojiCategory.getCurrentCategoryPageId();
        final int currentCategorySize = mEmojiCategory.getCurrentCategoryPageSize();
        if (newCategoryId == currentCategoryId) {
            mEmojiCategoryPageIndicatorView.setCategoryPageId(
                    newCategorySize, newPos.second, positionOffset);
        } else if (newCategoryId > currentCategoryId) {
            mEmojiCategoryPageIndicatorView.setCategoryPageId(
                    currentCategorySize, currentCategoryPageId, positionOffset);
        } else if (newCategoryId < currentCategoryId) {
            mEmojiCategoryPageIndicatorView.setCategoryPageId(
                    currentCategorySize, currentCategoryPageId, positionOffset - 1);
        }
    }

    /**
     * Called from {@link EmojiPageKeyboardView} through {@link OnTouchListener}
     * interface to handle touch events from View-based elements such as the space bar.
     * Note that this method is used only for observing {@link MotionEvent#ACTION_DOWN} to trigger
     * {@link KeyboardActionListener#onPressKey}. {@link KeyboardActionListener#onReleaseKey} will
     * be covered by {@link #onClick} as long as the event is not canceled.
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        final Object tag = v.getTag();
        if (!(tag instanceof Integer)) {
            return false;
        }
        final int code = (Integer) tag;
        mKeyboardActionListener.onPressKey(
                code, 0 /* repeatCount */, true /* isSinglePointer */);
        // It's important to return false here. Otherwise, {@link #onClick} and touch-down visual
        // feedback stop working.
        return false;
    }

    /**
     * Called from {@link EmojiPageKeyboardView} through {@link OnClickListener}
     * interface to handle non-canceled touch-up events from View-based elements such as the space
     * bar.
     */
    @Override
    public void onClick(View v) {
        final Object tag = v.getTag();
        if (!(tag instanceof Integer)) {
            return;
        }
        final int code = (Integer) tag;
        mKeyboardActionListener.onCodeInput(code, NOT_A_COORDINATE, NOT_A_COORDINATE,
                false /* isKeyRepeat */);
        mKeyboardActionListener.onReleaseKey(code, false /* withSliding */);
    }

    /**
     * Called from {@link EmojiPageKeyboardView} through
     * {@link com.android.inputmethod.keyboard.emoji.EmojiPageKeyboardView.OnKeyEventListener}
     * interface to handle touch events from non-View-based elements such as Emoji buttons.
     */
    @Override
    public void onPressKey(final Key key) {
        final int code = key.getCode();
        mKeyboardActionListener.onPressKey(code, 0 /* repeatCount */, true /* isSinglePointer */);
    }

    /**
     * Called from {@link EmojiPageKeyboardView} through
     * {@link com.android.inputmethod.keyboard.emoji.EmojiPageKeyboardView.OnKeyEventListener}
     * interface to handle touch events from non-View-based elements such as Emoji buttons.
     */
    @Override
    public void onReleaseKey(final Key key) {
        mEmojiPalettesAdapter.addRecentKey(key);
        mEmojiCategory.saveLastTypedCategoryPage();
        final int code = key.getCode();
        if (code == Constants.CODE_OUTPUT_TEXT) {
            mKeyboardActionListener.onTextInput(key.getOutputText());
        } else {
            mKeyboardActionListener.onCodeInput(code, NOT_A_COORDINATE, NOT_A_COORDINATE,
                    false /* isKeyRepeat */);
        }
        mKeyboardActionListener.onReleaseKey(code, false /* withSliding */);
    }

    public void setHardwareAcceleratedDrawingEnabled(final boolean enabled) {
        if (!enabled) return;
        // TODO: Should use LAYER_TYPE_SOFTWARE when hardware acceleration is off?
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    private static void setupAlphabetKey(final TextView alphabetKey, final String label,
                                         final KeyDrawParams params) {
        alphabetKey.setText(label);
        alphabetKey.setTextColor(params.mFunctionalTextColor);
        alphabetKey.setTextSize(TypedValue.COMPLEX_UNIT_PX, params.mLabelSize);
        alphabetKey.setTypeface(params.mTypeface);
    }

    public void startEmojiPalettes(final String switchToAlphaLabel,
                                   final KeyVisualAttributes keyVisualAttr,
                                   final KeyboardIconsSet iconSet) {
        final int deleteIconResId = iconSet.getIconResourceId(KeyboardIconsSet.NAME_DELETE_KEY);
        if (deleteIconResId != 0) {
            mDeleteKey.setImageResource(deleteIconResId);
        }
        final int spacebarResId = iconSet.getIconResourceId(KeyboardIconsSet.NAME_SPACE_KEY);
        if (spacebarResId != 0) {
            // TODO: Remove this workaround to place the spacebar icon.
            mSpacebarIcon.setBackgroundResource(spacebarResId);
        }
        final KeyDrawParams params = new KeyDrawParams();
        params.updateParams(mEmojiLayoutParams.getActionBarHeight(), keyVisualAttr);
        setupAlphabetKey(mAlphabetKeyLeft, switchToAlphaLabel, params);
        setupAlphabetKey(mAlphabetKeyRight, switchToAlphaLabel, params);
        setupAlphabetKey(mCommaKey, ",", params);
        setupAlphabetKey(mDotKey, ".", params);
        mEmojiPager.setAdapter(mEmojiPalettesAdapter);
        mEmojiPager.setCurrentItem(mCurrentPagerPosition);
    }

    public void stopEmojiPalettes() {
        mEmojiPalettesAdapter.releaseCurrentKey(true /* withKeyRegistering */);
        mEmojiPalettesAdapter.flushPendingRecentKeys();
        mEmojiPager.setAdapter(null);
    }

    public void setKeyboardActionListener(final KeyboardActionListener listener) {
        mKeyboardActionListener = listener;
        mDeleteKeyOnTouchListener.setKeyboardActionListener(listener);
    }

    private void updateEmojiCategoryPageIdView() {
        if (mEmojiCategoryPageIndicatorView == null) {
            return;
        }
        mEmojiCategoryPageIndicatorView.setCategoryPageId(
                mEmojiCategory.getCurrentCategoryPageSize(),
                mEmojiCategory.getCurrentCategoryPageId(), 0.0f /* offset */);
    }

    private void setCurrentCategoryId(final int categoryId, final boolean force) {
        final int oldCategoryId = mEmojiCategory.getCurrentCategoryId();
        if (oldCategoryId == categoryId && !force) {
            return;
        }

        if (oldCategoryId == EmojiCategory.ID_RECENTS) {
            // Needs to save pending updates for recent keys when we get out of the recents
            // category because we don't want to move the recent emojis around while the user
            // is in the recents category.
            mEmojiPalettesAdapter.flushPendingRecentKeys();
        }

        mEmojiCategory.setCurrentCategoryId(categoryId);
        final int newTabId = mEmojiCategory.getTabIdFromCategoryId(categoryId);
        final int newCategoryPageId = mEmojiCategory.getPageIdFromCategoryId(categoryId);
        if (force || Objects.requireNonNull(mEmojiCategory.getCategoryIdAndPageIdFromPagePosition(
                mEmojiPager.getCurrentItem())).first != categoryId) {
            mEmojiPager.setCurrentItem(newCategoryPageId, false /* smoothScroll */);
        }
        if (force || mTabHost.getCurrentTab() != newTabId) {
            mTabHost.setCurrentTab(newTabId);
        }
    }

    private static class DeleteKeyOnTouchListener implements OnTouchListener {
        private KeyboardActionListener mKeyboardActionListener =
                KeyboardActionListener.EMPTY_LISTENER;

        public void setKeyboardActionListener(final KeyboardActionListener listener) {
            mKeyboardActionListener = listener;
        }

        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(v);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    final float x = event.getX();
                    final float y = event.getY();
                    if (x < 0.0f || v.getWidth() < x || y < 0.0f || v.getHeight() < y) {
                        // Stop generating key events once the finger moves away from the view area.
                        onTouchCanceled(v);
                    }
                    return true;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    onTouchUp(v);
                    return true;
            }
            return false;
        }

        private void onTouchDown(final View v) {
            mKeyboardActionListener.onPressKey(Constants.CODE_DELETE,
                    0 /* repeatCount */, true /* isSinglePointer */);
            v.setPressed(true /* pressed */);
        }

        private void onTouchUp(final View v) {
            mKeyboardActionListener.onCodeInput(Constants.CODE_DELETE,
                    NOT_A_COORDINATE, NOT_A_COORDINATE, false /* isKeyRepeat */);
            mKeyboardActionListener.onReleaseKey(Constants.CODE_DELETE, false /* withSliding */);
            v.setPressed(false /* pressed */);
        }

        private void onTouchCanceled(final View v) {
            v.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}