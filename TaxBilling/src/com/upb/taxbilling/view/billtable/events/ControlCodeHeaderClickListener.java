package com.upb.taxbilling.view.billtable.events;

import java.text.Collator;

import android.view.View;
import android.view.View.OnClickListener;

import com.upb.taxbilling.model.data.Bill;
import com.upb.taxbilling.view.billtable.BillTableFragment;

/**
 * Event that executes when the control code header of the bill table is clicked.
 * @author Allan Leon
 */
public class ControlCodeHeaderClickListener implements OnClickListener {

	Collator collator = Collator.getInstance();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View arg0) {
		try {
			quickSortBillsByControlCode(1, BillTableFragment.getBills().keySet().size());
			BillTableFragment.updateRowsByList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Sorts the bills by their control code using the quick sort algorithm.
	 * @param start where the algorithm starts
	 * @param end where the algorithm ends
	 */
	private void quickSortBillsByControlCode(int start, int end) {
    	int left = start;
    	int right = end;
    	int pos = left;
    	boolean flag = true;
    	while(flag) {
    		flag = false;
    		while (collator.compare(BillTableFragment.getBills().get(pos).getControlCode(),
    				BillTableFragment.getBills().get(right).getControlCode()) <=  0
    				&& pos != right) {
    			right--;
    		}
    		if (pos != right) {
    			Bill aux = BillTableFragment.getBills().get(pos);
    			BillTableFragment.getBills().put(pos, BillTableFragment.getBills().get(right));
    			BillTableFragment.getBills().put(right, aux);
    			pos = right;
    			while (collator.compare(BillTableFragment.getBills().get(pos).getControlCode(),
    					BillTableFragment.getBills().get(left).getControlCode()) >= 0
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
    		quickSortBillsByControlCode(start, pos - 1);
    	}
    	if (end > pos + 1) {
    		quickSortBillsByControlCode(pos + 1, end);
    	}
    }
}
