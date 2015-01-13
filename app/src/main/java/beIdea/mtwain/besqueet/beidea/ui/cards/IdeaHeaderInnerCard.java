package beIdea.mtwain.besqueet.beidea.ui.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import beIdea.mtwain.besqueet.beidea.R;
import it.gmariotti.cardslib.library.internal.CardHeader;


public class IdeaHeaderInnerCard extends CardHeader {

    String header = "";

    public IdeaHeaderInnerCard(Context context) {
        super(context, R.layout.idea_header_layout);
    }

    public void setTopHeader(String text){
        header = text;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        parent.setBackgroundResource(R.drawable.header_green);

        TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);

        if (tvHeader!=null){
            tvHeader.setText(header);
        }
    }
}
