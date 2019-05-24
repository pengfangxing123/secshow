/**
 * 
 */
package cn.com.jrj.vtmatch.basicmatch.util;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 
 * @history <PRE>
 * ---------------------------------------------------------  
 * VERSION       DATE            BY       CHANGE/COMMENT  
 * ---------------------------------------------------------  
 * 1.0           2012-6-29    yuanlong.wang     create  
 * ---------------------------------------------------------
 * </PRE>
 * 
 */
@Slf4j
public class ArrayUtils {
	/**
	 * 类似js的join操作
	 * int [] arr=new int[]{1,2,1,1,1,1,1,1,1,1,1,1};
	 * 结果：1,2,1,1,1,1,1,1,1,1,1,1
	 * @param array
	 * @return
	 */
	public static String join(Object array){
		return join(array,",") ;
	}
	/**
	 * 将数组使用指定符号串起来
	 * @param array
	 * @param arraySeparator
	 * @return
	 */
	public static String join(Object array, String arraySeparator) {
		if (array == null)
			return "";
		return new ToStringBuilder(array, new JoinSimpleToStringStyle(
				arraySeparator)).append(array).toString();
	}

	static class JoinSimpleToStringStyle extends ToStringStyle {
		private static final long serialVersionUID = 1L;
		public final static JoinSimpleToStringStyle JOIN_STYLE = new JoinSimpleToStringStyle();

		JoinSimpleToStringStyle() {
			super.setUseClassName(false);
			super.setUseIdentityHashCode(false);
			super.setUseFieldNames(false);
			super.setContentStart("");
			super.setContentEnd("");
			super.setArrayStart("");
			super.setArrayEnd("");
		}

		JoinSimpleToStringStyle(String arraySeparator) {
			this();
			super.setArraySeparator(arraySeparator);
		}

		protected Object readResolve() {
			return JoinSimpleToStringStyle.JOIN_STYLE;
		}
	}
	
	public static List<String> getStockCodes(JSONArray list,String key){
		List<String> stockCodes = new ArrayList<String>();
		list.forEach(obj -> {
			JSONObject item = (JSONObject)obj;
			if(!StringUtils.isBlank(item.getString(key))){
				stockCodes.add(item.getString(key));
			}
		});
		return stockCodes;
	}
	
	public static void main(String[] args) {
		int [] arr=new int[]{1,2,1,1,1,1,1,1,1,1,1,1};
		log.info(ArrayUtils.join(arr, ","));
	}
}
