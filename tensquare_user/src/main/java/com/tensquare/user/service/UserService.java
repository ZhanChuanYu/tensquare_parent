package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加,我们把它定义为是管理员通过后台系统添加的。
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}


	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	/**
	 * 发送手机验证码：
	 * 	实际上的操作是把生成的验证码，存入redis缓存，并且写入消息队列
	 * 	真正实现发送到手机的操作是由：短信微服务完成的
	 *
	 * 	验证码要求：6位
	 * 	验证码可支持注册时间：10分钟
	 */
	public void sendSms(String mobile){
		//1.生成验证码
		Random random = new Random();
		Integer code = random.nextInt(999999);
		if(code < 100000){
			code = code+100000;
		}
		System.out.println("生成的验证码是："+code);
		//2.存入redis
		redisTemplate.opsForValue().set("smsCode_"+mobile,code.toString(),10, TimeUnit.MINUTES);
		//3.写入消息队列
		Map<String,String> map = new HashMap<String,String>();
		map.put("mobile",mobile);
		map.put("code",code.toString());
		rabbitTemplate.convertAndSend("sms",map);
	}

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 用户注册：使用者自己提供洗洗注册的
	 * @param user		注册的对象
	 * @param code		表单传过来的验证码
	 */
	public void register(User user ,String code){
//		//1.判断注册人是否输入了验证码
//		if(code == null){
//			throw new RuntimeException("请输入验证码");
//		}
//		//2.获取redis中的验证码
//		String sysCode = (String)redisTemplate.opsForValue().get("smsCode_"+user.getMobile());
//		//3.判断用户的验证码和缓存的是否一致
//		if(!code.equals(sysCode)){
//			//不一致
//			throw new RuntimeException("验证码有误，请重新输入");
//		}
		//4.补全用户注册信息
		user.setId(String.valueOf(idWorker.nextId()));
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期

		//先把明文密码加密，然后再把密文密码给User的password属性赋值
		String bcryptPassword = encoder.encode(user.getPassword());
		user.setPassword(bcryptPassword);

		//5.实现注册
		userDao.save(user);
	}

	/**
	 * 根据手机号和密码登录
	 * @param mobile
	 * @param password
	 * @return
	 */
	public User findByMobileAndPassword(String mobile,String password){
		//1.调用持久层，根据手机号查询用户
		User  user = userDao.findByMobile(mobile);
		//2.判断用户是否存在
		if(user == null){
			throw new RuntimeException("手机号未注册过");
		}
		//3.校验密码
		boolean check = encoder.matches(password,user.getPassword());
		if(!check){
			throw new RuntimeException("密码不匹配");
		}

		return user;
	}

	/**
	 * 更新粉丝数量
	 * @param userid
	 * @param amount
	 */
	@Transactional
	public void incUserFans(String userid,int amount){
		userDao.incUserFans(userid,amount);
	}


	/**
	 * 更新关注数量
	 * @param userid
	 * @param amount
	 */
	@Transactional
	public void incUserFollowCount(String userid,int amount){
		userDao.incUserFollowCount(userid,amount);
	}
}
