package com.upb.taxbilling.view.billtable.events;

import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;

import com.upb.taxbilling.R;
import com.upb.taxbilling.view.billtable.BillRow;

/**
 * Event that executes when the number of a row is clicked.
 * @author Allan Leon
 */
public class RowNumberClickListener implements OnClickListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(View v) {
		BillRow row = (BillRow) v.getParent();
		highLightRow(row);
	}

	/**
	 * Highlights the given row and the table's rows' backgrounds are updated. 
	 * @param row to be highlighted
	 */
	private void highLightRow(BillRow row) {
		row.updateHighlight();
		Resources resource = row.getResources();
		if (row.isHighlighted()) {
			row.setBackgroundColor(resource.getColor(R.color.RowSelectedColor));
		} else {
			row.setBackgroundColor(resource.getColor(R.color.RowNormalColor));
		}
	}
}
