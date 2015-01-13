package beIdea.mtwain.besqueet.beidea.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import beIdea.mtwain.besqueet.beidea.R;
import it.gmariotti.cardslib.library.internal.Card;

public class BaseIdeaCard extends Card {

    TextView tvText;
    public String text = "";

    public BaseIdeaCard(Context context) {
        super(context, R.layout.idea_card_layout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        tvText = (TextView) view.findViewById(R.id.tvText);
        tvText.setText(text);
    }
}
