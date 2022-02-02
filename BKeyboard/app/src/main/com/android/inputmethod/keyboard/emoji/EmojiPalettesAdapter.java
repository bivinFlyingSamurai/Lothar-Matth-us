
package com.android.inputmethod.keyboard.emoji;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.Matthaus.Lothar.keyboard.R;
import com.android.inputmethod.keyboard.Key;
import com.android.inputmethod.keyboard.Keyboard;
import com.android.inputmethod.keyboard.KeyboardView;

import javax.annotation.Nonnull;

final class EmojiPalettesAdapter extends PagerAdapter {
    private static final String TAG = EmojiPalettesAdapter.class.getSimpleName();
    private static final boolean DEBUG_PAGER = false;

    private final EmojiPageKeyboardView.OnKeyEventListener mListener;
    private final DynamicGridKeyboard mRecentsKeyboard;
    private final SparseArray<EmojiPageKeyboardView> mActiveKeyboardViews = new SparseArray<>();
    private final EmojiCategory mEmojiCategory;
    private int mActivePosition = 0;

    SharedPreferences prefs;
    Context mContext;

    public EmojiPalettesAdapter(final EmojiCategory emojiCategory,
                                final EmojiPageKeyboardView.OnKeyEventListener listener, Context context) {
        mEmojiCategory = emojiCategory;
        mListener = listener;
        mRecentsKeyboard = mEmojiCategory.getKeyboard(EmojiCategory.ID_RECENTS, 0);
        mContext = context;
    }

    public void flushPendingRecentKeys() {
        mRecentsKeyboard.flushPendingRecentKeys();
        final KeyboardView recentKeyboardView =
                mActiveKeyboardViews.get(mEmojiCategory.getRecentTabId());
        if (recentKeyboardView != null) {
            recentKeyboardView.invalidateAllKeys();
        }
    }

    public void addRecentKey(final Key key) {
        if (mEmojiCategory.isInRecentTab()) {
            mRecentsKeyboard.addPendingKey(key);
            return;
        }
        mRecentsKeyboard.addKeyFirst(key);
        final KeyboardView recentKeyboardView =
                mActiveKeyboardViews.get(mEmojiCategory.getRecentTabId());
        if (recentKeyboardView != null) {
            recentKeyboardView.invalidateAllKeys();
        }
    }

    public void onPageScrolled() {
        releaseCurrentKey(false /* withKeyRegistering */);
    }

    public void releaseCurrentKey(final boolean withKeyRegistering) {
        // Make sure the delayed key-down event (highlight effect and haptic feedback) will be
        // canceled.
        final EmojiPageKeyboardView currentKeyboardView =
                mActiveKeyboardViews.get(mActivePosition);
        if (currentKeyboardView == null) {
            return;
        }
        currentKeyboardView.releaseCurrentKey(withKeyRegistering);
    }

    @Override
    public int getCount() {
        return mEmojiCategory.getTotalPageCountOfAllCategories();
    }

    @Override
    public void setPrimaryItem(@Nonnull final ViewGroup container, final int position,
                               @Nonnull final Object object) {
        if (mActivePosition == position) {
            return;
        }
        final EmojiPageKeyboardView oldKeyboardView = mActiveKeyboardViews.get(mActivePosition);
        if (oldKeyboardView != null) {
            oldKeyboardView.releaseCurrentKey(false /* withKeyRegistering */);
            oldKeyboardView.deallocateMemory();
        }
        mActivePosition = position;
    }

    @Override
    public Object instantiateItem(@Nonnull final ViewGroup container, final int position) {
        if (DEBUG_PAGER) {
            Log.d(TAG, "instantiate item: " + position);
        }

        final EmojiPageKeyboardView oldKeyboardView = mActiveKeyboardViews.get(position);
        if (oldKeyboardView != null) {
            oldKeyboardView.deallocateMemory();
            // This may be redundant but wanted to be safer..
            mActiveKeyboardViews.remove(position);
        }
        final Keyboard keyboard = mEmojiCategory.getKeyboardFromPagePosition(position);
        final LayoutInflater inflater = LayoutInflater.from(container.getContext());
        final EmojiPageKeyboardView keyboardView = (EmojiPageKeyboardView)inflater.inflate(
                R.layout.emoji_keyboard_page, container, false /* attachToRoot */);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyEventListener(mListener);
        keyboardView.setBackground(null);
        container.addView(keyboardView);
        mActiveKeyboardViews.put(position, keyboardView);
        return keyboardView;
    }

    @Override
    public boolean isViewFromObject(@Nonnull final View view, @Nonnull final Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@Nonnull final ViewGroup container, final int position,
                            @Nonnull final Object object) {
        if (DEBUG_PAGER) {
            Log.d(TAG, "destroy item: " + position + ", " + object.getClass().getSimpleName());
        }
        final EmojiPageKeyboardView keyboardView = mActiveKeyboardViews.get(position);
        if (keyboardView != null) {
            keyboardView.deallocateMemory();
            mActiveKeyboardViews.remove(position);
        }
        if (object instanceof View) {
            container.removeView((View)object);
        } else {
            Log.w(TAG, "Warning!!! Emoji palette may be leaking. " + object);
        }
    }
}
