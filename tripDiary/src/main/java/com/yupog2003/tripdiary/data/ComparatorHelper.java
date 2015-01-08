package com.yupog2003.tripdiary.data;

import java.util.Comparator;

public class ComparatorHelper {
	public static final int descending=0;
	public static final int ascending=1;
	public static final int sort_by_cost_POI=0;
	public static final int sort_by_cost_name=1;
	public static final int sort_by_cost_type=2;
	public static final int sort_by_cost_dollar=3;
	
	public static Comparator<CostData> getCostDataComparator(final int sort_by,final boolean ascending){
		return new Comparator<CostData>() {

			public int compare(CostData lhs, CostData rhs) {

				switch(sort_by){
				case sort_by_cost_POI:
					return (ascending?lhs.POI.compareTo(rhs.POI):rhs.POI.compareTo(lhs.POI));
				case sort_by_cost_name:
					return (ascending?lhs.costName.compareTo(rhs.costName):rhs.costName.compareTo(lhs.costName));
				case sort_by_cost_type:
					return (ascending?lhs.costType-rhs.costType:rhs.costType-lhs.costType);
				case sort_by_cost_dollar:
					return (int)(ascending ? lhs.costDollar - rhs.costDollar : rhs.costDollar - lhs.costDollar);
				default:return 0;
				}
			}
		};
	}
	
	
}
