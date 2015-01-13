package beIdea.mtwain.besqueet.beidea.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import beIdea.mtwain.besqueet.beidea.ui.cards.BaseIdeaCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class IdeaCardArrayAdapter extends CardArrayAdapter  {

    Context context;

    public IdeaCardArrayAdapter(Context context, List<Card> cards) {
        super(context, cards);
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void setCardListView(CardListView cardListView) {
        super.setCardListView(cardListView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseIdeaCard c = (BaseIdeaCard) getItem(position);
        Log.d("B", "------------------------" + c.text);
        return super.getView(position, convertView, parent);
    }

    @Override
    public Card getItem(int position) {
        return super.getItem(position);
    }
}
