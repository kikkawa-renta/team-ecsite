package jp.co.internous.team2405.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.team2405.model.domain.MstDestination;
import jp.co.internous.team2405.model.domain.MstUser;
import jp.co.internous.team2405.model.form.DestinationForm;
import jp.co.internous.team2405.model.mapper.MstDestinationMapper;
import jp.co.internous.team2405.model.mapper.MstUserMapper;
import jp.co.internous.team2405.model.session.LoginSession;

/**
 * 宛先情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/team2405/destination")
public class DestinationController {
	
	/*
	 * フィールド定義
	 */
	private Gson gson = new Gson();
	
	@Autowired
	private MstDestinationMapper destinationMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	@Autowired
	private MstUserMapper userMapper;
	
	/**
	 * 宛先画面を初期表示する
	 * @param m 画面表示用オブジェクト
	 * @return 宛先画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		
		MstUser user = userMapper.findByUserNameAndPassword(loginSession.getUserName(),loginSession.getPassword());
		m.addAttribute("user", user);
		m.addAttribute("loginSession", loginSession);
		
        return "destination";
		
	}
	
	/**
	 * 宛先情報を削除する
	 * @param destinationId 宛先情報ID
	 * @return true:削除成功、false:削除失敗
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/delete")
	@ResponseBody
	public boolean delete(@RequestBody String destinationId) {
		
		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");
		int result = destinationMapper.logicalDeleteById(Integer.parseInt(id));

		return result > 0;
		
	}
	
	/**
	 * 宛先情報を登録する
	 * @param f 宛先情報のフォーム
	 * @return 宛先情報id
	 */
	@PostMapping("/register")
	@ResponseBody
	public String register(@RequestBody DestinationForm f) {
		
		MstDestination destination = new MstDestination();
		
		destination.setUserId(loginSession.getUserId());
		destination.setFamilyName(f.getFamilyName());
	    destination.setFirstName(f.getFirstName());
	    destination.setTelNumber(f.getTelNumber());
	    destination.setAddress(f.getAddress());
	    
	    destinationMapper.insert(destination);
	    
	    return String.valueOf(destination.getId());
	    
	}
	
}
