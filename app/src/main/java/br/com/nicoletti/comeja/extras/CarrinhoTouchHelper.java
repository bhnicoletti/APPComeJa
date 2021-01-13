package br.com.nicoletti.comeja.extras;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.adapters.CarrinhoAdapter;

/**
 * Created by Nicoletti on 25/05/2016.
 */
public class CarrinhoTouchHelper extends ItemTouchHelper.SimpleCallback {


        private CarrinhoAdapter mCarrinhoAdapter;

        public CarrinhoTouchHelper(CarrinhoAdapter movieAdapter){
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.mCarrinhoAdapter = movieAdapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //TODO: Not implemented here
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //Remove item
            mCarrinhoAdapter.removeListItem(viewHolder.getAdapterPosition());
        }

}
