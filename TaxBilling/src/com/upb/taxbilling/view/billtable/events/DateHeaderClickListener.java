package com.upb.taxbilling.view.billtable.events;

import android.view.View;
import android.view.View.OnClickListener;

import com.upb.taxbilling.model.data.Bill;
import com.upb.taxbilling.view.billtable.BillTableFragment;

/**
 * Event that executes when the date header of the bill table is clicked.
 * @author Allan Leon
 */
public class DateHeaderClickListener implements OnClickListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View arg0) {
		try {
			quickSortBillsByDate(1, BillTableFragment.getBills().keySet().size());
			BillTableFragment.updateRowsByList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Sorts the bills by their date using the quick sort algorithm.
	 * @param start where the algorithm starts
	 * @param end where the algorithm ends
	 */
	private void quickSortBillsByDate(int start, int end) {
    	int left = start;
    	int right = end;
    	int pos = left;
    	boolean flag = true;
    	while(flag) {
    		flag = false;
    		while (BillTableFragment.getBills().get(pos).getEmissionDate().getTime()
    				<= BillTableFragment.getBills().get(right).getEmissionDate().getTime()
    				&& pos != right) {
    			right--;
    		}
    		if (pos != right) {
    			Bill aux = BillTableFragment.getBills().get(pos);
    			BillTableFragment.getBills().put(pos, BillTableFragment.getBills().get(right));
    			BillTableFragment.getBills().put(right, aux);
    			pos = right;
    			while (BillTableFragment.getBills().get(pos).getEmissionDate().getTime()
    					>= BillTableFragment.getBills().get(left).getEmissionDate().getTime()
    					&& pos != left) {
    				left++;
    			}
    			if (pos != left) {
    				flag = true;
    				aux = BillTableFragment.getBills().get(pos);
    				BillTableFragment.getBills().put(pos, BillTableFragment.getBills().get(left));
        			BillTableFragment.getBills().put(left, aux);
        			pos = left;
    			}
    		}
    	}
    	if (pos - 1 > start) {
    		quickSortBillsByDate(start, pos - 1);
    	}
    	if (end > pos + 1) {
    		quickSortBillsByDate(pos + 1, end);
    	}
    }
}
