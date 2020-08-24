package cn.items.ssm.realm;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.items.ssm.mapper.SysUserMapper;
import cn.items.ssm.po.ActiveUser;
import cn.items.ssm.po.SysPermission;
import cn.items.ssm.po.SysUser;
import cn.items.ssm.service.SysService;

@Service
public class CustomerRealm extends AuthorizingRealm{

	@Autowired
	private SysService sysService;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		
		String username = token.getPrincipal().toString();
		
		// 假设 模仿从数据库查询
		
//		String username_db = "zhangsan";
//		String password_db = "1111";
		//数据库查询
		SysUser user=null;
		List<SysPermission> menuLis=null;
		try {
			user = this.sysService.findSysUserByUserCode(username);
			
			 menuLis = this.sysService.findMenuListByUserId(user.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user==null) {
			return null;
		}
		String username_db=user.getUsername();
		String password_db=user.getPassword();
		String salt_db=user.getSalt();
		
		ActiveUser activeUser=new ActiveUser();
		activeUser.setUserid(user.getId());
		activeUser.setUsercode(user.getUsercode());
		activeUser.setUsername(user.getUsername());
		
		activeUser.setMenus(menuLis);
		
//		if(!username.equals(username_db))
//		{
//			return null; // 返回null，报UnkownAccountException
//		}
//		
		SimpleAuthenticationInfo authenticationInfo = 
				
				new SimpleAuthenticationInfo(activeUser, password_db,ByteSource.Util.bytes(salt_db), "自定义realm");
		return authenticationInfo;
	}
	
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection Principal) {
		
		ActiveUser user=(ActiveUser) Principal.getPrimaryPrincipal();
		List<SysPermission> permissions=null;
		try {
			permissions= this.sysService.findPermissionListByUserId(user.getUserid());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		List<String> list=new ArrayList<String>();
		for (SysPermission permission : permissions) {
			list.add(permission.getPercode());
		}
		
		SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.addStringPermissions(list);
		
		
		return simpleAuthorizationInfo;
	}


}
