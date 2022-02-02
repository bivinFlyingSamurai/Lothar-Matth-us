

package com.android.inputmethod.event;

import java.util.ArrayList;

import javax.annotation.Nonnull;

/**
 * A generic interface for combiners. Combiners are objects that transform chains of input events
 * into committable strings and manage feedback to show to the user on the combining state.
 */
public interface Combiner {
    /**
     * Process an event, possibly combining it with the existing state and return the new event.
     *
     * If this event does not result in any new event getting passed down the chain, this method
     * returns null. It may also modify the previous event list if appropriate.
     *
     * @param previousEvents the previous events in this composition.
     * @param event the event to combine with the existing state.
     * @return the resulting event.
     */
    @Nonnull
    Event processEvent(ArrayList<Event> previousEvents, Event event);

    /**
     * Get the feedback that should be shown to the user for the current state of this combiner.
     * @return A CharSequence representing the feedback to show users. It may include styles.
     */
    CharSequence getCombiningStateFeedback();

    /**
     * Reset the state of this combiner, for example when the cursor was moved.
     */
    void reset();
}
