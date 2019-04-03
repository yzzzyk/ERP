package com.itcast.erp.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author Administrator
 *
 */
public class MailUtil {
	
//	郵件发送类
	private JavaMailSender javaMailSender;
	
	private String from;//发件人

	/**
	 *发送邮件
	 * @param to 收件人
	 * @param subject  主题
	 * @param text  邮件正文
	 * @throws MessagingException 
	 */
	public void sendMail(String to,String subject,String text) throws MessagingException{
		
		//创建一个邮件
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		//创建一个邮件包装器
		MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
		//设置 发件人
		helper.setFrom(from); 
		//设置 收发件人
		helper.setTo(to);
		//设置主题
		helper.setSubject(subject);
		//设置内容
		helper.setText(text);
		
		//发送邮件
		javaMailSender.send(mimeMessage);
		
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	
}
