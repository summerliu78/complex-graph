package com.yinker.tinyv.vo;

import java.util.ArrayList;
import java.util.List;

import com.yinker.tinyv.utils.StringUtils;

/**
 * 代表EChart组建需要的JSON格式。
 * @author Zhu Xiuwei
 */
public class EChartJsonJavaV2{
	
	private List<Node> nodes = new ArrayList<Node>();
	private List<Link> links = new ArrayList<Link>();
	private String status;
	private String message;
	public static class Node {
		private String name;
		private int size;
		private String id;
		private String category_name;
		private Attributes attributes;
		public static class Attributes {
			
			public Attributes(){}
			public Attributes(GangMember3rdPartyInfo thirdInfo){
				if(null != thirdInfo){
					this.address = thirdInfo.getId_home();
					this.applay_credit_status = thirdInfo.getReply_status();
					this.applay_credit_time = thirdInfo.getApply_time();
					this.mg_score = thirdInfo.getMiguan();
					this.sex = thirdInfo.getSex();
					this.td_score = thirdInfo.getTongd();
					this.user_id_num = thirdInfo.getId_card();
					this.user_name = thirdInfo.getReal_name();
					this.yuqi_days = thirdInfo.getDue_dates();
				}
			}
			public void fillEmptyFileds(){
				if(StringUtils.isNullOrEmpty(user_name))
					user_name = "-";
				if(StringUtils.isNullOrEmpty(address))
					address = "-";
				if(StringUtils.isNullOrEmpty(sex))
					sex = "-";
				if(StringUtils.isNullOrEmpty(user_id_num))
					user_id_num = "-";
				if(StringUtils.isNullOrEmpty(td_score))
					td_score = "-";
				if(StringUtils.isNullOrEmpty(mg_score))
					mg_score = "-";
				if(StringUtils.isNullOrEmpty(applay_credit_time))
					applay_credit_time = "-";
				if(StringUtils.isNullOrEmpty(degree))
					degree = "-";
				if(StringUtils.isNullOrEmpty(yuqi_days))
					yuqi_days = "-";
				if(StringUtils.isNullOrEmpty(applay_credit_status))
					applay_credit_status = "-";
				if(StringUtils.isNullOrEmpty(is_new))
					is_new = "-";
			}
			private String user_name = "-";
			private String address = "-";
			private String sex = "-";
			private String user_id_num = "-";
			private String td_score = "-";
			private String mg_score = "-";
			private String applay_credit_time = "-";
			private String degree = "-";
			private String yuqi_days = "-";
			private String applay_credit_status = "-";	//审批状态
			private String is_new = "-";
			public String getUser_name() {
				return user_name;
			}
			public void setUser_name(String user_name) {
				this.user_name = user_name;
			}
			public String getAddress() {
				return address;
			}
			public void setAddress(String address) {
				this.address = address;
			}
			public String getSex() {
				return sex;
			}
			public void setSex(String sex) {
				this.sex = sex;
			}
			public String getUser_id_num() {
				return user_id_num;
			}
			public void setUser_id_num(String user_id_num) {
				this.user_id_num = user_id_num;
			}
			public String getTd_score() {
				return td_score;
			}
			public void setTd_score(String td_score) {
				this.td_score = td_score;
			}
			public String getMg_score() {
				return mg_score;
			}
			public void setMg_score(String mg_score) {
				this.mg_score = mg_score;
			}
			public String getApplay_credit_time() {
				return applay_credit_time;
			}
			public void setApplay_credit_time(String applay_credit_time) {
				this.applay_credit_time = applay_credit_time;
			}
			public String getDegree() {
				return degree;
			}
			public void setDegree(String degree) {
				this.degree = degree;
			}
			public String getYuqi_days() {
				return yuqi_days;
			}
			public void setYuqi_days(String yuqi_days) {
				this.yuqi_days = yuqi_days;
			}
			public String getApplay_credit_status() {
				return applay_credit_status;
			}
			public void setApplay_credit_status(String applay_credit_status) {
				this.applay_credit_status = applay_credit_status;
			}
			public String getIs_new() {
				return is_new;
			}
			public void setIs_new(String is_new) {
				this.is_new = is_new;
			}
		}
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCategory_name() {
			return category_name;
		}
		public void setCategory_name(String category_name) {
			this.category_name = category_name;
		}
		public Attributes getAttributes() {
			return attributes;
		}
		public void setAttributes(Attributes attributes) {
			this.attributes = attributes;
		}
	}
	public static class Link {
		private String source;
		private String target;
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

}

