package com.xmbl.enumeration;

public enum EnumCommentCode {
		// 故事集
		COMMENT_STORYS(1,"1","comment_storys","故事集评论"),
		// 图片
		COMMENT_IMG(2,"2","comment_img","图片评论"),
		// pk关卡
		COMMENT_PK_GUANKA(3,"3","comment_pk_guanka","pk关卡评论"),
		// 关卡集
		COMMENT_GUANKA_JI(4,"4","comment_guanka_ji","关卡集评论"),
		// pk关卡集
		COMMENT_PK_GUANKA_JI(5,"5","comment_pk_guanka_ji","pk关卡集评论"),
		// 主题
		COMMENT_ZHUTI(6,"6","comment_zhuti","主题评论"),
		// 玩家
		COMMENT_WANJIA(7,"7","comment_wanjia","玩家评论"),
		// 问题反馈
		COMMENT_WENTIFANKUI(8,"8","comment_wentifankui","问题反馈评论"),
		// 挑战关卡评论
		COMMENT_FIGHTGUANQIA(9,"9","comment_fightguanqia","挑战关卡评论"),
		// 关卡集战斗中关卡评论
		STAGE_TREE_BATTLE_STAGE(10,"10","stage_tree_battle_stage","关卡集战斗中关卡评论"),
		// 关卡集详情中评论
		STAGE_TREE_BATTLE(11,"11","stage_tree_battle","关卡集详情中评论")
		;
		private EnumCommentCode(int id, String numcode, String wordcode, String desc) {
			this.id = id;
			this.numcode = numcode;
			this.wordcode = wordcode;
			
			this.desc = desc;
		}
		private int id;
		private String numcode;
		private String wordcode;
		private String desc;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getNumcode() {
			return numcode;
		}
		public void setNumcode(String numcode) {
			this.numcode = numcode;
		}
		public String getWordcode() {
			return wordcode;
		}
		public void setWordcode(String wordcode) {
			this.wordcode = wordcode;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
}
