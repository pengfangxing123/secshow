package cn.com.jrj.vtmatch.basicmatch.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

import cn.com.jrj.vtmatch.basicmatch.matchserviceinterfaces.VTService;
import cn.com.jrj.vtmatch.basicmatch.matchtrade.MatchTradeApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=MatchTradeApplication.class)
public class VTServiceTests {
	
	 @Resource private VTService vtService;



	
	@Test
	public void commission(){
		String uuid = "win_599467";
		JSONObject res = vtService.commission(uuid, new BigDecimal(2.00), new BigDecimal(100), 0, "000001",0);
		System.out.println(JSONObject.toJSONString(res, true));
	}

	/**
	 * 委托撤单
	 */
	@Test
	public void orderCancel(){
		long commissionId=5337175L;

		String uuid = "win_599467";
		JSONObject res = vtService.orderCancel(uuid, commissionId);
		System.out.println(JSONObject.toJSONString(res, true));
	}

	@Test
	public void queryEntrustLimit(){
		String uuid = "win_599467";
		JSONObject res = vtService.queryEntrustLimit(uuid, "000002", 1, new BigDecimal(2.0));
		System.out.println(JSONObject.toJSONString(res, true));
	}

	/**
	 * 查询成交记录(T)
	 * 
	 * @param uuid
	 * @return QueryUserFunds
	 */
	@Test
	public void queryConcludeList(){
		String uuid = "win_599467";
		JSONObject res = vtService.queryConcludeList(uuid, 1, 10);
		System.out.println(JSONObject.toJSONString(res, true));
	}

	/**
	 * 查询委托记录(T)
	 * 
	 * @param uuid
	 * @return queryCommissionList
	 */
	@Test
	public void queryCommissionList(){
		String uuid = "win_599467";
		JSONObject res = vtService.queryCommissionList(uuid, 1, 10);
		System.out.println(JSONObject.toJSONString(res, true));
	}

	/**
	 * 可撤单的委托
	 * 
	 * @param uuid
	 * @return List<Cancel>
	 */
	@Test
	public void queryCancel(){
		String uuid = "win_599467";
		JSONObject res = vtService.queryCancel(uuid);
		System.out.println(JSONObject.toJSONString(res, true));
	}
	

}
